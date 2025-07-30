package site.itprohub.javelin.data.command;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import site.itprohub.javelin.data.DbConnManager;
import site.itprohub.javelin.data.context.DbContext;
import site.itprohub.javelin.data.entity.Employee;
import site.itprohub.javelin.data.multidb.DatabaseClients;
import site.itprohub.javelin.data.multidb.DbClientFactory;
import site.itprohub.javelin.data.multidb.MysqlClientProvider;

public class BaseCommandTest {

    private DbContext dbContext;
    private BaseCommand baseCommand;


    static class TestCommand extends BaseCommand {
        public TestCommand(DbContext ctx) {
            super(ctx);
        } 
    }

    @BeforeEach
    public void setUp() throws Exception {
        DbClientFactory.registerProvider(DatabaseClients.MYSQL, MysqlClientProvider.INSTANCE);
        
        dbContext = DbConnManager.createAppDb("test");

        baseCommand = new TestCommand(dbContext); 
    }

    @Test
    public void testToList() throws Exception {
       baseCommand.init("SELECT * FROM employees", List.of());

       List<Employee> result = baseCommand.toList(Employee.class);

       assert result.size() > 0;
    }

}
