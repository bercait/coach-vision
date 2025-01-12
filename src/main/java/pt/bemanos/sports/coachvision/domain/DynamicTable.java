package pt.bemanos.sports.coachvision.domain;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Properties;

import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class DynamicTable {
    @Id
    private String name;
    private String cypherMatch;
    private String cypherReturn;
    private String cypherCall;
    @Properties
    private Map<String, String> properties = new HashMap<>();

    public DynamicTable() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCypherMatch() {
        return cypherMatch;
    }

    public void setCypherMatch(String cypherMatch) {
        this.cypherMatch = cypherMatch;
    }

    public String getCypherReturn() {
        return cypherReturn;
    }

    public void setCypherReturn(String cypherReturn) {
        this.cypherReturn = cypherReturn;
    }

    public String getCypherCall() {
        return cypherCall;
    }

    public void setCypherCall(String cypherCall) {
        this.cypherCall = cypherCall;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
