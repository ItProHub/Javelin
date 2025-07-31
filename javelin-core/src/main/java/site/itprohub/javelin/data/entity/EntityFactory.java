package site.itprohub.javelin.data.entity;

import site.itprohub.javelin.data.context.DbContext;

public class EntityFactory {

    private final DbContext dbContext;

    public EntityFactory(DbContext dbContext) {
        this.dbContext = dbContext;
    }

    public <T> long insert(T entity) {
        return EntityCurdUtils.getInsertQuery(entity, dbContext).executeNonQuery();

    }

    public <T> int update(T entity) {
        return EntityCurdUtils.getUpdateQuery(entity, dbContext).executeNonQuery();
    }    
    
}

