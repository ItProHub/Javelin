package demo.controller;

import demo.service.EmployeeService;
import site.itprohub.javelin.annotations.*;
import site.itprohub.javelin.annotations.parameter.*;
import site.itprohub.javelin.data.DbConnManager;
import site.itprohub.javelin.data.context.DbContext;

import java.util.List;

import demo.data.Employee;

@RestController
@Route("")
public class EmployeeController {

    private EmployeeService employeeService;

    @Inject
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService; 
    }

    @HttpPost
    @Route("/employee/add")
    @AllowAnonymous
    public int create(@FromBody Employee employee) throws Exception {
        try(DbContext db = DbConnManager.createAppDb("test")) {
            return db.Entity().create(Employee.class).insert(employee);
        }
    }

    @HttpPost
    @Route("/employee/update")
    @AllowAnonymous
    public int update(@FromBody Employee employee) throws Exception {
        try(DbContext db = DbConnManager.createAppDb("test")) {
            return db.Entity().create(Employee.class).update(employee);
        }
    }

    @HttpGet
    @Route("/employee/{id}")
    @AllowAnonymous
    public Employee error(@FromRoute int id) throws Exception {
        return employeeService.getEmployee(id);
    }

    @HttpGet
    @Route("/employee/entity/{id}")
    @AllowAnonymous
    public Employee getById(@FromRoute int id) throws Exception {
        return employeeService.getEmployeeEntity(id);
    }

    @HttpGet
    @Route("/employee/delete/{id}")
    @AllowAnonymous
    public int delete(@FromRoute int id) throws Exception {
        try(DbContext db = DbConnManager.createAppDb("test")) {
            return db.Entity().create(Employee.class).delete(id);
        }
    }

    @HttpGet
    @Route("/employee/list")
    @AllowAnonymous
    public List<Employee> list() throws Exception {
        try(DbContext db = DbConnManager.createAppDb("test")) {
            return db.Entity().create(Employee.class).where("salary > ?", 1000).toList();
        }
    }
}
