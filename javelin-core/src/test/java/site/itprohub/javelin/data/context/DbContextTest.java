package site.itprohub.javelin.data.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import site.itprohub.javelin.data.DbConnManager;
import site.itprohub.javelin.data.entity.Employee;
import site.itprohub.javelin.data.multidb.DatabaseClients;
import site.itprohub.javelin.data.multidb.DbClientFactory;
import site.itprohub.javelin.data.multidb.MysqlClientProvider;

public class DbContextTest {
    @BeforeAll
    static void setup() {
        DbClientFactory.registerProvider(DatabaseClients.MYSQL, MysqlClientProvider.INSTANCE);
    }

    @Test
    void testEntityCURD() throws SQLException{
        try(DbContext dbContext = DbConnManager.createAppDb("test")){

            // insert
            Employee employee = new Employee();
            employee.setName("to-insert");
            employee.setPosition("to-insert");
            dbContext.Entity().insert(employee);

            Employee insertEmployee = dbContext.Entity(Employee.class)
                .where("name =?", "to-insert")                
                .toSingle();

            // update    
            insertEmployee.setName("to-update");
            dbContext.Entity().update(insertEmployee);

            Employee updateEmployee = dbContext.Entity(Employee.class)
                .where("name =?", "to-update")                
                .toSingle();

            assertEquals("to-update", updateEmployee.getName());

            // delete
            dbContext.Entity(Employee.class).deleteByKey(updateEmployee.getId());

            Employee deleteEmployee = dbContext.Entity(Employee.class).getByKey(updateEmployee.getId());
            assertTrue(deleteEmployee == null);
        }

    }
}
