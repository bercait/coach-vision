package pt.bemanos.sports.coachvision.database;

import java.io.Serializable;
import java.util.Collection;

public interface Repository<T, ID extends Serializable> {
    Collection<T> findAll();

    T findById(ID id);

    void save(T entity);

    void delete(T entity);
}
