package pt.bemanos.sports.coachvision.services;

import org.junit.jupiter.api.Test;
import pt.bemanos.sports.coachvision.services.imports.ImportXpsService;

import java.io.IOException;

class ImportXpsServiceTest {

    @Test
    void validate() throws IOException {
        ImportXpsService service = new ImportXpsService("/Users/tiagobernardes/Downloads/SPSUL x GARRETT.csv");

        service.importEvents();
    }
}