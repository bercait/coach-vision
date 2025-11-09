package pt.bemanos.sports.coachvision;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax0.license3j.HardwareBinder;
import javax0.license3j.License;
import javax0.license3j.io.LicenseReader;
import pt.bemanos.sports.coachvision.database.DatabaseFactory;
import pt.bemanos.sports.coachvision.database.ServerFactory;
import pt.bemanos.sports.coachvision.database.repositories.DynamicTableRepository;
import pt.bemanos.sports.coachvision.domain.DynamicTable;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * JavaFX App
 */
public class ApplicationFX extends Application {

    BooleanProperty ready = new SimpleBooleanProperty(false);
    StringProperty errorMessage = new SimpleStringProperty("");
    Logger logger = Logger.getLogger(ApplicationFX.class.getName());

//    ImportXpsService importXpsService = new ImportXpsService("/Users/tiagobernardes/Downloads/SPSUL x GARRETT.csv");
//    ImportXpsService importXpsService = new ImportXpsService("/Users/tiagobernardes/Downloads/1ª parte Angola x Visionários.csv");

    public ApplicationFX() throws IOException {
    }

    public static void main(String[] args) {
        System.setProperty("javafx.preloader", SplashScreen.class.getName());
        Logger logger = Logger.getLogger(ApplicationFX.class.getName());
        logger.info("Starting Main ");
        launch();
    }

    @Override
    public void init() {
        try {
            super.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Path path = Path.of(System.getProperty("user.home"), ".coachvision");

        try {
            FileHandler fileHandler = new FileHandler(Path.of(path.toAbsolutePath().toString(), "app.log").toString());
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);

            logger.info("Log file opened");
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        logger.info("Starting Licensing");
        final License license;
        String filePath = Path.of(path.toAbsolutePath().toString(), "license.bin").toString();
        logger.info(filePath);
        try (var reader = new LicenseReader(filePath)) {
            license = reader.read();
        } catch (IOException e) {
            errorMessage.set("License not found");
            logger.warning(e.getMessage());
            return;
        }

        byte[] key = new byte[]{
                (byte) 0x52,
                (byte) 0x53, (byte) 0x41, (byte) 0x2F, (byte) 0x45, (byte) 0x43, (byte) 0x42, (byte) 0x2F, (byte) 0x50,
                (byte) 0x4B, (byte) 0x43, (byte) 0x53, (byte) 0x31, (byte) 0x50, (byte) 0x61, (byte) 0x64, (byte) 0x64,
                (byte) 0x69, (byte) 0x6E, (byte) 0x67, (byte) 0x00, (byte) 0x30, (byte) 0x82, (byte) 0x01, (byte) 0xA2,
                (byte) 0x30, (byte) 0x0D, (byte) 0x06, (byte) 0x09, (byte) 0x2A, (byte) 0x86, (byte) 0x48, (byte) 0x86,
                (byte) 0xF7, (byte) 0x0D, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x05, (byte) 0x00, (byte) 0x03,
                (byte) 0x82, (byte) 0x01, (byte) 0x8F, (byte) 0x00, (byte) 0x30, (byte) 0x82, (byte) 0x01, (byte) 0x8A,
                (byte) 0x02, (byte) 0x82, (byte) 0x01, (byte) 0x81, (byte) 0x00, (byte) 0xD1, (byte) 0x36, (byte) 0x9C,
                (byte) 0x72, (byte) 0xC1, (byte) 0xC4, (byte) 0x15, (byte) 0x36, (byte) 0x5B, (byte) 0x2A, (byte) 0x97,
                (byte) 0x05, (byte) 0x2F, (byte) 0x2E, (byte) 0x80, (byte) 0xBE, (byte) 0x0C, (byte) 0xB5, (byte) 0x26,
                (byte) 0xBA, (byte) 0x77, (byte) 0xE6, (byte) 0xB3, (byte) 0xA6, (byte) 0xE4, (byte) 0x73, (byte) 0xD9,
                (byte) 0xB4, (byte) 0x94, (byte) 0x1A, (byte) 0xE2, (byte) 0xC4, (byte) 0xF2, (byte) 0x1E, (byte) 0x77,
                (byte) 0x36, (byte) 0x8C, (byte) 0x0F, (byte) 0x84, (byte) 0x98, (byte) 0x1C, (byte) 0x8A, (byte) 0xBE,
                (byte) 0x0C, (byte) 0x42, (byte) 0x26, (byte) 0x82, (byte) 0x50, (byte) 0x74, (byte) 0x6C, (byte) 0xE4,
                (byte) 0x74, (byte) 0x65, (byte) 0x13, (byte) 0x68, (byte) 0x1D, (byte) 0x67, (byte) 0x20, (byte) 0x86,
                (byte) 0x7B, (byte) 0x08, (byte) 0xBC, (byte) 0x0B, (byte) 0xA9, (byte) 0x94, (byte) 0x18, (byte) 0xB0,
                (byte) 0x5F, (byte) 0x78, (byte) 0x3F, (byte) 0xB7, (byte) 0x77, (byte) 0x8E, (byte) 0xE4, (byte) 0x7A,
                (byte) 0x4A, (byte) 0xCC, (byte) 0x4F, (byte) 0xFA, (byte) 0xC7, (byte) 0xE3, (byte) 0x4C, (byte) 0x1A,
                (byte) 0xBB, (byte) 0x66, (byte) 0x76, (byte) 0xCD, (byte) 0x63, (byte) 0xA0, (byte) 0xF8, (byte) 0x3C,
                (byte) 0xCD, (byte) 0xC9, (byte) 0xB4, (byte) 0x46, (byte) 0x64, (byte) 0x72, (byte) 0x88, (byte) 0x22,
                (byte) 0xA9, (byte) 0x46, (byte) 0x69, (byte) 0x08, (byte) 0x45, (byte) 0x25, (byte) 0xAC, (byte) 0x50,
                (byte) 0xBA, (byte) 0xEC, (byte) 0xA9, (byte) 0x4B, (byte) 0x27, (byte) 0x5B, (byte) 0xE0, (byte) 0x6C,
                (byte) 0x64, (byte) 0x30, (byte) 0x85, (byte) 0x3E, (byte) 0xEB, (byte) 0xF6, (byte) 0x0C, (byte) 0x01,
                (byte) 0x0B, (byte) 0xC1, (byte) 0x20, (byte) 0xA5, (byte) 0xEE, (byte) 0xFA, (byte) 0x37, (byte) 0xA7,
                (byte) 0xC2, (byte) 0xBB, (byte) 0x3F, (byte) 0xE9, (byte) 0x3B, (byte) 0xE1, (byte) 0x4C, (byte) 0xB6,
                (byte) 0x05, (byte) 0x42, (byte) 0x46, (byte) 0x1B, (byte) 0x2C, (byte) 0x8B, (byte) 0x83, (byte) 0x43,
                (byte) 0x50, (byte) 0x09, (byte) 0x4B, (byte) 0x99, (byte) 0x09, (byte) 0x02, (byte) 0xC5, (byte) 0xBF,
                (byte) 0xB9, (byte) 0x3C, (byte) 0xE6, (byte) 0x3B, (byte) 0x2A, (byte) 0xF6, (byte) 0x3D, (byte) 0x1C,
                (byte) 0x46, (byte) 0xF3, (byte) 0xB4, (byte) 0xCF, (byte) 0x7B, (byte) 0x89, (byte) 0x8B, (byte) 0x6D,
                (byte) 0x73, (byte) 0x4D, (byte) 0x23, (byte) 0x0B, (byte) 0xA2, (byte) 0x2A, (byte) 0x12, (byte) 0x5D,
                (byte) 0x15, (byte) 0x17, (byte) 0xDB, (byte) 0x87, (byte) 0x62, (byte) 0x6F, (byte) 0xDA, (byte) 0xB1,
                (byte) 0xA0, (byte) 0x53, (byte) 0x44, (byte) 0xFE, (byte) 0xC2, (byte) 0x93, (byte) 0x24, (byte) 0xB7,
                (byte) 0x32, (byte) 0xBD, (byte) 0x25, (byte) 0x2C, (byte) 0x5C, (byte) 0xC1, (byte) 0x14, (byte) 0xB0,
                (byte) 0xA9, (byte) 0x48, (byte) 0xDC, (byte) 0xBE, (byte) 0x43, (byte) 0x52, (byte) 0x4D, (byte) 0x05,
                (byte) 0x75, (byte) 0xCB, (byte) 0x97, (byte) 0x2B, (byte) 0x34, (byte) 0x41, (byte) 0x8E, (byte) 0xC8,
                (byte) 0xB3, (byte) 0x86, (byte) 0x80, (byte) 0x7C, (byte) 0xB0, (byte) 0xDE, (byte) 0x8F, (byte) 0xA4,
                (byte) 0xBD, (byte) 0x77, (byte) 0xDA, (byte) 0x6A, (byte) 0x1A, (byte) 0x39, (byte) 0x94, (byte) 0xCC,
                (byte) 0xF4, (byte) 0xF6, (byte) 0x1E, (byte) 0x40, (byte) 0xB0, (byte) 0x0F, (byte) 0x09, (byte) 0x47,
                (byte) 0x73, (byte) 0xEE, (byte) 0x7E, (byte) 0x75, (byte) 0xAE, (byte) 0xBB, (byte) 0x61, (byte) 0x0C,
                (byte) 0xDF, (byte) 0x69, (byte) 0x22, (byte) 0xA4, (byte) 0xB3, (byte) 0xE5, (byte) 0xC7, (byte) 0xCE,
                (byte) 0x20, (byte) 0x2B, (byte) 0xC5, (byte) 0x9D, (byte) 0x47, (byte) 0x90, (byte) 0xBB, (byte) 0x76,
                (byte) 0x9F, (byte) 0xB1, (byte) 0xD4, (byte) 0x17, (byte) 0xC3, (byte) 0x10, (byte) 0xB8, (byte) 0xEB,
                (byte) 0x15, (byte) 0xC7, (byte) 0xD0, (byte) 0xF4, (byte) 0x55, (byte) 0xB6, (byte) 0x2D, (byte) 0x26,
                (byte) 0x18, (byte) 0x3B, (byte) 0xF6, (byte) 0x86, (byte) 0xCE, (byte) 0x1C, (byte) 0x15, (byte) 0xAD,
                (byte) 0x32, (byte) 0xE0, (byte) 0x5D, (byte) 0x26, (byte) 0x61, (byte) 0x72, (byte) 0xE9, (byte) 0xB1,
                (byte) 0xB5, (byte) 0x4C, (byte) 0x33, (byte) 0x5C, (byte) 0xF1, (byte) 0xDC, (byte) 0x36, (byte) 0xC6,
                (byte) 0xB6, (byte) 0x1A, (byte) 0xF7, (byte) 0xBA, (byte) 0xC6, (byte) 0xC6, (byte) 0x4B, (byte) 0xEB,
                (byte) 0x93, (byte) 0xA4, (byte) 0x45, (byte) 0xB1, (byte) 0xBB, (byte) 0x08, (byte) 0x04, (byte) 0xCA,
                (byte) 0x53, (byte) 0xB8, (byte) 0x96, (byte) 0xDE, (byte) 0xCE, (byte) 0x1B, (byte) 0xF3, (byte) 0x4D,
                (byte) 0x88, (byte) 0x06, (byte) 0xE6, (byte) 0xC7, (byte) 0x7D, (byte) 0xA8, (byte) 0x48, (byte) 0x53,
                (byte) 0xE3, (byte) 0x7A, (byte) 0xB4, (byte) 0xD4, (byte) 0xA1, (byte) 0x28, (byte) 0xA5, (byte) 0x52,
                (byte) 0xC2, (byte) 0x92, (byte) 0x0F, (byte) 0x8E, (byte) 0xC4, (byte) 0xBF, (byte) 0x27, (byte) 0x4C,
                (byte) 0xB8, (byte) 0x1E, (byte) 0x6B, (byte) 0xEF, (byte) 0xB7, (byte) 0xE4, (byte) 0x5F, (byte) 0xC9,
                (byte) 0xFE, (byte) 0x0F, (byte) 0x30, (byte) 0x72, (byte) 0x76, (byte) 0x58, (byte) 0xBC, (byte) 0x15,
                (byte) 0x79, (byte) 0x01, (byte) 0xE6, (byte) 0x3F, (byte) 0xB4, (byte) 0xB4, (byte) 0x36, (byte) 0x62,
                (byte) 0xC7, (byte) 0xD8, (byte) 0x60, (byte) 0xC6, (byte) 0x11, (byte) 0x02, (byte) 0x03, (byte) 0x01,
                (byte) 0x00, (byte) 0x01,
        };


        try {
            logger.info(license.getLicenseId().toString());
            logger.info(new HardwareBinder().getMachineId().toString());
            logger.info(Boolean.toString(license.isOK(key)));

            if (!license.isOK(key)) {
                errorMessage.set("License not valid");
                logger.warning("License not valid");
                return;
            }

            if (!license.getLicenseId().equals(new HardwareBinder().getMachineId())) {
                errorMessage.set("License not valid for this machine");
                logger.warning("License not valid for this machine");
                return;
            }

//            if (!license.isExpired()) {
//                errorMessage.set("License is expired");
//                return;
//            }

        } catch (NoSuchAlgorithmException | UnknownHostException | SocketException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException(e);
        }

        logger.info("Neo4j server started with database: " + ServerFactory.getInstance().getGraphDatabase().databaseName());
        // Registers a shutdown hook for the Neo4j instance so that it shuts down nicely when the VM exits
        // (even if you "Ctrl-C" the running application).
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down Neo4j server...");
            DatabaseFactory.getInstance().getSessionFactory().close();
            ServerFactory.getInstance().getManagementService().shutdown();
        }));

//        Player homePlayer = new Player();
//        homePlayer.setName("Jogador Casa");
//        Player awayPlayer = new Player();
//        awayPlayer.setName("Fora Jogador");
//
//        Save save = new Save();
//        save.setGoalkeeper(awayPlayer);
//        Throw throw2 = new Throw();
//        throw2.setActor(homePlayer);
//        throw2.setNextEvent(save);
//        Attack attack2 = new Attack();
//        attack2.setNextEvent(throw2);
//
//        Goal goal = new Goal();
//        goal.setGoalkeeper(awayPlayer);
//        Throw throw1 = new Throw();
//        throw1.setActor(homePlayer);
//        throw1.setNextEvent(goal);
//        Attack attack1 = new Attack();
//        attack1.setNextEvent(throw1);
//        attack1.setNextAttack(attack2);
//
//        Game game = new Game();
//        game.setNextAttack(attack1);
//        game.setHomePlayers(List.of(homePlayer));
//        game.setAwayPlayers(List.of(awayPlayer));
//
//        GameRepository gameRepository = new GameRepository();
//        gameRepository.save(game);

//        Session session = DatabaseFactory.getInstance().getSession();
//        session.query("CREATE (:Team)<-[:HOME_TEAM]-(n0:Game)-[:NEXT_ATTACK]->(n1:Attack)-[:NEXT_ATTACK]->(:Attack)-[:NEXT_EVENT]->(n10:Event:Throw)-[:NEXT_EVENT]->(:Event:Save)-[:GOALKEEPER]->(n6:Player {name: \"Fora Jogador\"}),\n" +
//                "(n1)-[:NEXT_EVENT]->(n3:Event:Throw {position: \"[225,65]\", category: \"breakthrough\", type: \"direct\"})-[:NEXT_EVENT]->(:Event:Goal {location: \"[175, 30]\", position: \"[190, 15]\"})-[:GOALKEEPER]->(n6)<-[:AWAY_PLAYER]-(n0),\n" +
//                "(n3)-[:ACTION]->(n5:Player {name: \"Jogador Casa\"})<-[:HOME_PLAYER]-(n0)-[:AWAY_TEAM]->(:Team),\n" +
//                "(n10)-[:ACTION]->(n5)", new HashMap<>(), false);
//
        //importXpsService.importEvents();

        logger.info("Initializing Dynamic Table");
        DynamicTable dynamicTable = new DynamicTable();
        dynamicTable.setName("player_total");
//        dynamicTable.setCypherMatch("MATCH (g:Game)-[:NEXT_ATTACK]-*(a:Attack)-[:NEXT_EVENT]-+(e:Event)-[]-(p:Player)-[]-(g)-[]-(t:Team) ");
        dynamicTable.setCypherMatch("""
                CALL () {
                MATCH (g:Game)-[:NEXT_ATTACK]-*(a:Attack)-[:NEXT_EVENT]-*(e:Event)-[]-(p:Player)-[:AWAY_PLAYER]-(g)-[:AWAY_TEAM]-(t:Team {name:$teamName})
                RETURN *
                UNION ALL
                MATCH (g:Game)-[:NEXT_ATTACK]-*(a:Attack)-[:NEXT_EVENT]-*(e:Event)-[]-(p:Player)-[:HOME_PLAYER]-(g)-[:HOME_TEAM]-(t:Team {name:$teamName})
                RETURN *
                }
                """);
        dynamicTable.setCypherReturn("""
                RETURN DISTINCT p.name AS Name,
                (Throws + Turnover) AS Actions,
                Goals,
                Throws,
                Turnover,
                pt.bemanos.sports.coachvision.database.procedure.percentage(Goals,Throws) AS PT,
                pt.bemanos.sports.coachvision.database.procedure.percentage(Goals,(Throws + Turnover)) AS PA
                """);
        //dynamicTable.setCypherReturn("RETURN DISTINCT p.name as Name");
        dynamicTable.setCypherCall("CALL (p) ");
        Map<String, String> map = new HashMap<>();
        map.put("Throws", "MATCH (p)<-[:ACTOR]-(t:Throw) RETURN count(t)");
        map.put("Goals", "MATCH (p)<-[:ACTOR]-(t:Throw)-[:NEXT_EVENT]-(g:Goal) RETURN count(g)");
        map.put("Saves", "MATCH (p)<-[:ACTOR]-(t:Throw)-[:NEXT_EVENT]-(s:Save) RETURN count(s)");
//        map.put("Assists", "MATCH (p)<-[:ACTOR]-(a:Assist) RETURN count(a)");
        map.put("Turnover", "MATCH (p)<-[:ACTOR]-(t:Turnover) RETURN count(t)");
//        map.put("Teste", "MATCH (p)<-[:ACTOR]-(t:Teste) RETURN count(t)");
        dynamicTable.setProperties(map);

        DynamicTableRepository dynamicTableRepository = new DynamicTableRepository();
        dynamicTableRepository.save(dynamicTable);

        logger.info("Finished Dynamic Table");


//        session.query("CREATE (:DynamicTable {name: \"player_total\", cypherMatch: \"MATCH (g:Game)-[]-(p:Player)\", cypherReturn: \"RETURN p.name as Name\", cypherCall: \"CALL (p)\", Goals: \"MATCH (p)<-[:ACTION]-(t:Throw)-[:NEXT_EVENT]-(g:Goal) RETURN count(g)\", Throws: \"MATCH (p)<-[:ACTION]-(t:Throw) RETURN count(t)\", Saves: \"MATCH (p)<-[:GOALKEEPER]-(s:Save) RETURN count(s)\"})", new HashMap<>(), false);

        ready.set(true);
    }

    @Override
    public void start(Stage stage) throws IOException {
        try {
            logger.info("Beginning Start Method");
            //Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
            logger.info("Loading Antialiasing");

            var antialiasing = Platform.isSupported(ConditionalFeature.SCENE3D)
                    ? SceneAntialiasing.BALANCED
                    : SceneAntialiasing.DISABLED;

            String fxml = "report_totals.fxml";
            String title = "Coach Vision";
            logger.info("Ready: " + ready.toString());
            if (!ready.get()) {
                fxml = "dialog_request_license.fxml";
                title = errorMessage.get();
            }

            logger.info("Loading Application FXML");

            stage.setTitle(title);
            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationFX.class.getResource("/fxml/" + fxml));
            Pane pane = fxmlLoader.load();

            logger.info("Loaded Application FXML");

            Platform.runLater(() -> {
                try {
                    var scene = new Scene(pane, 800, 600, false, antialiasing);
                    stage.setScene(scene);

                    logger.info("Loaded Application Scene ");

                    stage.show();
                    stage.requestFocus();

                    //ScenicView.show(scene);
                } catch (Exception e) {
                    logger.severe(e.getMessage());
                }
            });
        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        logger.info("Stopping Application FX");

        DatabaseFactory.getInstance().getSessionFactory().close();
        ServerFactory.getInstance().getManagementService().shutdown();
        Platform.exit();
    }

}