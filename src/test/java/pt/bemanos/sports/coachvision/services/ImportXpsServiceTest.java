package pt.bemanos.sports.coachvision.services;

import pt.bemanos.sports.coachvision.services.imports.ImportXpsService;

import java.io.IOException;

class ImportXpsServiceTest {

    void validate() throws IOException {
        ImportXpsService service = new ImportXpsService("/Users/tiagobernardes/Downloads/SPSUL x GARRETT.csv");

        service.importEvents();
    }
}