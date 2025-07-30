package site.itprohub.javelin.data.entity;


public interface IEntity<T> {
    T findById(Object id);

    int insert(T entity);

    int update(T entity);

    int delete(Object id);
}
