package pt.bemanos.sports.coachvision.database.repositories;

import pt.bemanos.sports.coachvision.database.CrudRepository;
import pt.bemanos.sports.coachvision.domain.Team;

import java.util.HashMap;
import java.util.Map;

public class TeamRepository extends CrudRepository<Team, Long> {
    @Override
    public Class<Team> getEntityType() {
        return Team.class;
    }

    public Team findByName(String teamName) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("teamName", teamName);

        return session.queryForObject(
                getEntityType(),
                """
                        MATCH (t:Team {name:$teamName})
                        RETURN t
                        """,
                parameters
        );
    }

    public Team findOrCreate(String teamName) {
        Team team = findByName(teamName);
        if (team != null) {
            return team;
        }

        team = new Team();
        team.setName(teamName);
        this.save(team);
        return team;
    }
}
