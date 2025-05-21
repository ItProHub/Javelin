package site.itprohub.javelin.data.entity;

import site.itprohub.javelin.data.DbContext;

public class EntityFactory {
    private final DbContext dbContext;

    public EntityFactory(DbContext dbContext) {
        this.dbContext = dbContext;
    }

    public <T> Entity<T> create(Class<T> clazz) {
        Entity entity = new Entity(clazz, this.dbContext);
        return entity;
    }
}
