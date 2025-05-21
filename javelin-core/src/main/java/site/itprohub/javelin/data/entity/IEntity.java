package site.itprohub.javelin.data.entity;


public interface IEntity<T> {
    T findById(int id);

    int insert(T entity);

    int update(T entity);

    int delete(int id);
}
