package site.itprohub.javelin.data.command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import site.itprohub.javelin.data.DbContext;

public class CPQuery {
    private DbContext dbContext;

    private String sql;

    private List<Object> params = new ArrayList<>();

    public CPQuery(DbContext dbContext) {
        this.dbContext = dbContext;
    }

    public void init(String sql, List<Object> params) {
        this.sql = sql;
        this.params = params; 
    }


    public int executeNonQuery() {
        try (PreparedStatement ps = dbContext.getConnection().prepareStatement(sql)) {
            setParams(ps);
            return ps.executeUpdate();
        } catch(Exception e) {
            throw new RuntimeException("SQL execute fail: " + sql + ", error: " + e.getMessage(), e); 
        }
    }

    public <T> T executeScalar(Class<T> clazz) {
        return null;
    }

    public <T> T toSingle(Class<T> clazz) {
        try (PreparedStatement ps = dbContext.getConnection().prepareStatement(sql)) {
            setParams(ps);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
               return BeanPropertyRowMapper.mapRow(rs, clazz); 
            }
            return null;
        } catch(Exception e) {
            throw new RuntimeException("SQL execute fail: " + sql + ", error: " + e.getMessage(), e); 
        }
    }

    public <T> List<T> ToList(Class<T> clazz) {
        try (PreparedStatement ps = dbContext.getConnection().prepareStatement(sql)) {
            setParams(ps);
            ResultSet rs = ps.executeQuery();
            List<T> result = new ArrayList<>(); 
            while (rs.next()) {
               result.add(BeanPropertyRowMapper.mapRow(rs, clazz)); 
            }
            return result;
        } catch(Exception e) {
            throw new RuntimeException("SQL execute fail: " + sql + ", error: " + e.getMessage(), e); 
        }
    }

    private void setParams(PreparedStatement ps) throws Exception {
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
    }
}
