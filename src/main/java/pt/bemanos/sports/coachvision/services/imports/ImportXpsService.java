package pt.bemanos.sports.coachvision.services.imports;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.jexl3.*;
import org.apache.commons.jexl3.introspection.JexlPermissions;
import pt.bemanos.sports.coachvision.database.repositories.GameRepository;
import pt.bemanos.sports.coachvision.database.repositories.PlayerRepository;
import pt.bemanos.sports.coachvision.database.repositories.TeamRepository;
import pt.bemanos.sports.coachvision.domain.*;
import pt.bemanos.sports.coachvision.domain.events.Assist;
import pt.bemanos.sports.coachvision.domain.events.Goal;
import pt.bemanos.sports.coachvision.domain.events.Save;
import pt.bemanos.sports.coachvision.domain.events.Throw;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ImportXpsService {

    private final PlayerRepository playerRepository = new PlayerRepository();
    private final TeamRepository teamRepository = new TeamRepository();
    private final GameRepository gameRepository = new GameRepository();

    private final String nullable = "NINGUÉM";

    List<CSVRecord> csvRecords;
    List<JexlExpression> rules = new ArrayList<>();
    List<JexlScript> values = new ArrayList<>();

    public ImportXpsService(String path) throws IOException {
        csvRecords = this.parseCsv(path);

        JexlBuilder.setDefaultPermissions(JexlPermissions.UNRESTRICTED);
        JexlEngine jexl = new JexlBuilder().create();

        rules.add(jexl.createExpression("!line.get(14).equalsIgnoreCase('" + nullable + "')"));
        rules.add(jexl.createExpression("line.get(18).contains('Golo') || line.get(18).contains('[Falhado]/')"));
        rules.add(jexl.createExpression("line.get(18).contains('Golo')"));
        rules.add(jexl.createExpression("line.get(18).contains('[Falhado]/[Defesa]')"));

        values.add(jexl.createScript("""
                player = playerRepository.findOrCreate(team.getName(), line.get(14));
                assist.setActor(player);
                return assist;
                """));
        values.add(jexl.createScript("""
                player = playerRepository.findOrCreate(team.getName(), line.get(6));
                shoot.setActor(player);
                return shoot;
                """));
        values.add(jexl.createScript("""
                goalkeeper = playerRepository.findOrCreate(opponent.getName(), line.get(27));
                goal.setGoalkeeper(goalkeeper);
                return goal;
                """));
        values.add(jexl.createScript("""
                goalkeeper = playerRepository.findOrCreate(opponent.getName(), line.get(27));
                save.setGoalkeeper(goalkeeper);
                return save;
                """));
    }

    public List<String> getMatches() {
        return csvRecords.parallelStream()
                .map(record -> record.get(0))
                .distinct()
                .toList();
    }

    public Map<String, List<String>> getTeams() {
        Map<String, List<String>> map = new HashMap<>();

        getMatches().forEach(game -> {
            List<String> teams = csvRecords.parallelStream()
                    .filter(record -> record.get(0).equalsIgnoreCase(game))
                    .map(record -> record.get(5))
                    .distinct()
                    .toList();

            map.put(game, teams);
        });

        return map;
    }

    public List<String> getTeams(String matchId) {
        return csvRecords.parallelStream()
                .filter(record -> record.get(0).equalsIgnoreCase(matchId))
                .map(record -> record.get(5))
                .distinct()
                .toList();
    }

    public List<Player> getPlayersOfTeam(String gameId, String team) {
        List<Integer> teamPlayersId = List.of(6, 14, 28);
        List<Integer> oppositePlayersId = List.of(15, 16, 17, 19, 27);

        List<String> players = new ArrayList<>();
        List<CSVRecord> teamRecords = csvRecords.parallelStream()
                .filter(record -> record.get(0).equalsIgnoreCase(gameId))
                .filter(record -> record.get(5).equalsIgnoreCase(team))
                .toList();

        List<CSVRecord> oppositeRecords = csvRecords.parallelStream()
                .filter(record -> record.get(0).equalsIgnoreCase(gameId))
                .filter(record -> !record.get(5).equalsIgnoreCase(team))
                .toList();

        for (int i : teamPlayersId) {
            players.addAll(teamRecords.parallelStream()
                    .map(record -> record.get(i))
                    .filter(player -> !player.equalsIgnoreCase(nullable))
                    .distinct()
                    .toList()
            );
        }

        for (int j : oppositePlayersId) {
            players.addAll(oppositeRecords.parallelStream()
                    .map(record -> record.get(j))
                    .filter(player -> !player.equalsIgnoreCase(nullable))
                    .distinct()
                    .toList()
            );
        }

        return players.parallelStream()
                .distinct()
                .sorted()
                .map(name -> playerRepository.findOrCreate(team, name))
                .toList();
    }

    public void importEvents() {
        for (String matchId : getMatches()) {
            Game game = new Game();

            List<String> teams = getTeams(matchId);
            Team home = teamRepository.findOrCreate(teams.get(0));
            Team away = teamRepository.findOrCreate(teams.get(1));

            game.setHomeTeam(home);
            game.setAwayTeam(away);

            game.setHomePlayers(getPlayersOfTeam(matchId, home.getName()));
            game.setAwayPlayers(getPlayersOfTeam(matchId, away.getName()));

            gameRepository.save(game);

            AtomicReference<Attack> previousAttack = new AtomicReference<>();
            csvRecords.forEach(line -> {
                Event previousEvent = null;
                Attack attack = new Attack();
                JexlContext context1 = new ImportXpsServiceOld.StreamContext();
                context1.set("team", (line.get(5).equalsIgnoreCase(home.getName())) ? home : away);
                context1.set("opponent", (line.get(5).equalsIgnoreCase(home.getName())) ? away : home);
                context1.set("player", new Player());
                context1.set("goalkeeper", new Player());
                context1.set("shoot", new Throw());
                context1.set("goal", new Goal());
                context1.set("save", new Save());
                context1.set("assist", new Assist());
                context1.set("playerRepository", playerRepository);
                context1.set("line", line);

                for (int i = 0; i < rules.size(); i++) {
                    if ((boolean) rules.get(i).evaluate(context1)) {
                        Event event = (Event) values.get(i).execute(context1);

                        if (previousEvent != null) {
                            previousEvent.setNextEvent(event);
                        } else {
                            attack.setNextEvent(event);
                        }
                        previousEvent = event;
                    }
                }

                if (previousAttack.get() == null) {
                    game.setNextAttack(attack);
                } else {
                    previousAttack.get().setNextAttack(attack);
                }

                previousAttack.set(attack);
            });

            gameRepository.save(game);
        }

    }

    private List<CSVRecord> parseCsv(String filePath) throws IOException {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVParser parser = new CSVParser(
                     reader,
                     CSVFormat.TDF
                             .builder()
                             .setHeader()
                             .setSkipHeaderRecord(true)
                             .setIgnoreHeaderCase(true)
                             .setTrim(true)
                             .build()
             )) {
            return parser.getRecords();
        }
    }
}
