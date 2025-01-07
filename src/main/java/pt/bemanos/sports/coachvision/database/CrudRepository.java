package pt.bemanos.sports.coachvision.database;

import org.neo4j.ogm.session.Session;

import java.io.Serializable;
import java.util.Collection;

public abstract class CrudRepository<T, ID extends Serializable> implements Repository<T, ID> {
    protected Session session = DatabaseFactory.getInstance().getSession();

    @Override
    public Collection<T> findAll() {
        return this.session.loadAll(this.getEntityType());
    }

    @Override
    public T findById(ID id) {
        return this.session.load(this.getEntityType(), id);
    }

    @Override
    public void save(T entity) {
        this.session.save(entity);
    }

    @Override
    public void delete(T entity) {
        this.session.delete(entity);
    }

    public abstract Class<T> getEntityType();
}
