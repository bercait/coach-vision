package pt.bemanos.sports.coachvision.database.repositories;

import pt.bemanos.sports.coachvision.database.CrudRepository;
import pt.bemanos.sports.coachvision.domain.Event;

public class EventRepository extends CrudRepository<Event, Long> {
    @Override
    public Class<Event> getEntityType() {
        return Event.class;
    }
}
