package pt.bemanos.sports.coachvision.services.imports;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportXpsService {

    private final String nullable = "NINGUÉM";

    List<CSVRecord> csvRecords;

    public ImportXpsService(String path) throws IOException {
        csvRecords = this.parseCsv(path);
    }

    public List<String> getMatches() {
        return csvRecords.parallelStream()
                .map(record -> record.get(0))
                .distinct()
                .toList();
    }

    public Map<String, List<String>> getTeams() {
        Map<String, List<String>> map = new HashMap<>();

        List<String> games = csvRecords.parallelStream()
                .map(record -> record.get(0))
                .distinct()
                .toList();

        games.forEach(game -> {
            List<String> teams = csvRecords.parallelStream()
                    .filter(record -> record.get(0).equalsIgnoreCase(game))
                    .map(record -> record.get(5))
                    .distinct()
                    .toList();

            map.put(game, teams);
        });

        return map;
    }

    public List<String> getPlayersOfTeam(String gameId, String team) {
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

        return players.parallelStream().distinct().sorted().toList();
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
