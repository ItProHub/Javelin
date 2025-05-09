package demo.service;

import demo.data.Employee;
import site.itprohub.javelin.data.DbConnManager;
import site.itprohub.javelin.data.DbContext;
import site.itprohub.javelin.data.command.CPQuery;

public class EmployeeService {
    
    public Employee getEmployee(int id) throws Exception {
        try(DbContext db = DbConnManager.createAppDb("test")) {
           CPQuery query = db.CPQuery().create("SELECT * FROM employees WHERE id = ?", new Object[]{id});
           return query.toSingle(Employee.class); 
        }
   }
}
