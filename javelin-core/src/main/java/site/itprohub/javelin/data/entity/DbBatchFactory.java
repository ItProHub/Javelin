package site.itprohub.javelin.data.entity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import site.itprohub.javelin.data.command.BaseCommand;
import site.itprohub.javelin.data.command.CPQuery;
import site.itprohub.javelin.data.context.DbContext;

public class DbBatchFactory {

    private final DbContext dbContext;

    public DbBatchFactory(DbContext dbContext) {
        if (dbContext == null)
            throw new IllegalArgumentException("DbContext cannot be null.");
        this.dbContext = dbContext;
    }

    public <T> int insert(List<T> list) throws SQLException {
        return insert(list, 100);
    }

    public <T> int insert(List<T> list, int batchSize) throws SQLException {
        return executeRealBatch(list, CurdKind.INSERT, batchSize, "SQL_BatchInsert");
    }

    public <T> int update(List<T> list) throws SQLException {
        return update(list, 100);
    }

    public <T> int update(List<T> list, int batchSize) throws SQLException {
        return executeRealBatch(list, CurdKind.UPDATE, batchSize, "SQL_BatchUpdate");
    }

    public int execute(List<BaseCommand> list, int batchSize) throws SQLException {
        if (list == null || list.isEmpty()) return 0;

        if (batchSize <= 0 || list.size() <= batchSize) {
            return executeBaseCommandBatch(list, "SQL_BatchExecute");
        }

        int total = 0;
        for (int i = 0; i < list.size(); i += batchSize) {
            int end = Math.min(i + batchSize, list.size());
            List<BaseCommand> sublist = list.subList(i, end);
            total += executeBaseCommandBatch(sublist, "SQL_BatchExecute");
        }
        return total;
    }

    private int executeBaseCommandBatch(List<BaseCommand> list, String operationName) throws SQLException {
        int total = 0;
        for (BaseCommand cmd : list) {
            if (cmd == null) continue;
            try (PreparedStatement stmt = cmd.toPreparedStatement()) {
                total += stmt.executeUpdate();
            }
        }
        return total;
    }

    private <T> int executeRealBatch(List<T> list, CurdKind kind, int batchSize, String operationName) throws SQLException {
        if (list == null || list.isEmpty()) return 0;

        if (batchSize <= 0 || list.size() <= batchSize) {
            return executeEntityBatch(list, kind, operationName);
        }

        int total = 0;
        for (int i = 0; i < list.size(); i += batchSize) {
            int end = Math.min(i + batchSize, list.size());
            List<T> sublist = list.subList(i, end);
            total += executeEntityBatch(sublist, kind, operationName);
        }
        return total;
    }

    private <T> int executeEntityBatch(List<T> list, CurdKind kind, String operationName) throws SQLException {
        if (list.isEmpty()) return 0;

        T first = list.get(0);
        String sql;
        List<Object[]> paramList = new ArrayList<>();

        switch (kind) {
            case INSERT -> {
                sql = EntityCudUtils.getInsertSQL(first.getClass(), dbContext);
                for (T entity : list) {
                    paramList.add(Arrays.asList(EntityCudUtils.getInsertParams(entity, dbContext)).toArray());
                }
            }
            case UPDATE -> {
                // 不支持统一批更新语句，因为每个字段可能不同，仍逐条执行
                int total = 0;
                for (T entity : list) {
                    CPQuery query = EntityCudUtils.getUpdateQuery(entity, dbContext);
                    try (PreparedStatement stmt = query.toPreparedStatement()) {
                        total += stmt.executeUpdate();
                    }
                }
                return total;
            }
            default -> throw new IllegalArgumentException("Unsupported CurdKind: " + kind);
        }

        dbContext.openConnection();
        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sql)) {
            for (Object[] params : paramList) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
                stmt.addBatch();
            }
            int[] results = stmt.executeBatch();
            return Arrays.stream(results).sum();
        }
    }
}
