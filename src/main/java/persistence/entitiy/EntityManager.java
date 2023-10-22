package persistence.entitiy;

public interface EntityManager {

    <T> T find(Class<T> clazz, Long Id);

    Object persist(Object entity);

    void remove(Object entity);
}