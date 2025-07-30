package site.itprohub.javelin.data.command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import site.itprohub.javelin.data.context.DbContext;
import site.itprohub.javelin.data.paging.PageResult;

public abstract class BaseCommand {
    public DbContext dbContext;

    public StringBuilder sbSql;

    public List<Object> params = new ArrayList<>();

    public BaseCommand(DbContext dbContext) {
        this.dbContext = dbContext;
    }

    public void init(String sql, List<Object> params) {
        this.sbSql = new StringBuilder(sql);
        this.params = params; 
    }

    private <T> T execute(StatementCallback<T> action) {
        try {   
            dbContext.openConnection();
            return action.execute();
        } catch (Exception e) {
            throw new RuntimeException("执行 execute 失败: " + action.getClass() + ", error: " + e.getMessage(), e); 
        }
        
    }

    public <T> List<T> toList(Class<T> clazz) {
        return execute(() -> {
            try (PreparedStatement ps = dbContext.getConnection().prepareStatement(sbSql.toString())) {
                setParams(ps);
                ResultSet rs = ps.executeQuery();
                List<T> result = new ArrayList<>(); 
                while (rs.next()) {
                    result.add(BeanPropertyRowMapper.mapRow(rs, clazz)); 
                }
                return result;
            } catch(Exception e) {
                throw new RuntimeException("执行 ToList 失败: " + sbSql.toString() + ", error: " + e.getMessage(), e); 
            }
        });
    }


    public int executeNonQuery() {
        return execute(() -> {
            try (PreparedStatement ps = dbContext.getConnection().prepareStatement(sbSql.toString())) {
                setParams(ps);
                return ps.executeUpdate();
            } catch(Exception e) {
                throw new RuntimeException("执行 executeNonQuery 失败: " + sbSql.toString() + ", error: " + e.getMessage(), e); 
            }
        });
    }

    public <T> T executeScalar(Class<T> clazz) {
        return execute(() -> {
            try ( PreparedStatement ps = dbContext.getConnection().prepareStatement(sbSql.toString())) {
                setParams(ps);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return (T) rs.getObject(1);
                }
                return null; 
            } catch ( Exception e) {
                throw new RuntimeException("执行 executeScalar 失败: " + sbSql.toString() + ", error: " + e.getMessage(), e);
            }
        });
    }

    public <T> T toScalarList(Class<T> clazz) {
        return execute(() -> {
            try ( PreparedStatement ps = dbContext.getConnection().prepareStatement(sbSql.toString())) {
                setParams(ps);
                ResultSet rs = ps.executeQuery();
                List<Object> result = new ArrayList<>();
                while (rs.next()) {
                    result.add((T) rs.getObject(1));
                }
                return null; 
            } catch ( Exception e) {
                throw new RuntimeException("执行 executeScalar 失败: " + sbSql.toString() + ", error: " + e.getMessage(), e);
            }
        });
    }

    public <T> T toSingle(Class<T> clazz) {
        return execute(() -> {
            try (PreparedStatement ps = dbContext.getConnection().prepareStatement(sbSql.toString())) {
                setParams(ps);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return BeanPropertyRowMapper.mapRow(rs, clazz); 
                }
                return null;
            } catch(Exception e) {
                throw new RuntimeException("执行 toSingle 失败: " + sbSql + ", error: " + e.getMessage(), e); 
            }
        });
    }

    public <T> PageResult<T> toPageList(Class<T> clazz, int pageIndex, int pageSize) {
        return execute(() -> {
            int offset = pageIndex * pageSize;
            // 构造分页sql
            String pageSql = sbSql.toString() + " LIMIT ?, ?";

            String countSql = " SELECT COUNT(*) FROM (" + sbSql.toString() + ") AS total_count";

            try (
                PreparedStatement dataPs = dbContext.getConnection().prepareStatement(pageSql);
                PreparedStatement countPs = dbContext.getConnection().prepareStatement(countSql)
            ) {
                setParams(dataPs);
                setParams(countPs);

                // 追加分页参数
                dataPs.setInt(params.size() + 1, offset);
                dataPs.setInt(params.size() + 2, pageSize);

                // 查询总数
                ResultSet countRs = countPs.executeQuery();
                int total = countRs.next() ? countRs.getInt(1) : 0;

                // 查询列表
                ResultSet rs = dataPs.executeQuery();
                List<T> result = new ArrayList<>(); 
                while (rs.next()) {
                result.add(BeanPropertyRowMapper.mapRow(rs, clazz));
                }

                return new PageResult<T>(total, result);
            } catch(Exception e) {
                throw new RuntimeException("执行 toPageList 失败: " + sbSql.toString() + ", error: " + e.getMessage(), e); 
            }
        });
    }



    private void setParams(PreparedStatement ps) throws Exception {
        if (params == null)
            return;
            
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
    }


    public PreparedStatement toPreparedStatement() throws SQLException {
        return execute(() -> {
            PreparedStatement ps = dbContext.getConnection().prepareStatement(sbSql.toString());
            setParams(ps);
            return ps;
        });
    }


}
