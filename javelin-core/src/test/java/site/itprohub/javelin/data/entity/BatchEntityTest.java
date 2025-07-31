package site.itprohub.javelin.data.entity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import site.itprohub.javelin.data.DbConnManager;
import site.itprohub.javelin.data.context.DbContext;
import site.itprohub.javelin.data.multidb.DatabaseClients;
import site.itprohub.javelin.data.multidb.DbClientFactory;
import site.itprohub.javelin.data.multidb.MysqlClientProvider;
import site.itprohub.javelin.data.multidb.SqlServerClientProvider;

public class BatchEntityTest {
    @BeforeEach
    void setup() {
        DbClientFactory.registerProvider(DatabaseClients.MYSQL, MysqlClientProvider.INSTANCE);
        DbClientFactory.registerProvider(DatabaseClients.SQLSERVER, SqlServerClientProvider.INSTANCE);
    }

    @Test
    void testBatchInsert() {
        try(DbContext dbContext = DbConnManager.createAppDb("test")){
            List<Employee> employees = List.of(
                new Employee("test1", "测试", new BigDecimal(10000)),
                new Employee("test2", "测试", new BigDecimal(8000))
            );
            dbContext.Batch().insert(employees);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     @Test
    void testBatchUpdate() {
        try(DbContext dbContext = DbConnManager.createAppDb("test")){

            List<Employee> employees = dbContext.Entity(Employee.class)
                .where("name in ('test1','test2')")                
                .toList();

            employees.forEach(e -> e.setPosition("开发"));
            dbContext.Batch().update(employees);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     @Test
    void testEnd() {
        try(DbContext dbContext = DbConnManager.createAppDb("test")){
            dbContext.CPQuery().create("delete from employees where name = 'test1' or name = 'test2';").executeNonQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
