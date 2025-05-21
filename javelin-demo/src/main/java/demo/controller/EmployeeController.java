package demo.controller;

import demo.service.EmployeeService;
import site.itprohub.javelin.annotations.*;
import site.itprohub.javelin.annotations.parameter.*;
import site.itprohub.javelin.data.DbConnManager;
import site.itprohub.javelin.data.DbContext;

import java.util.List;

import demo.data.Employee;

@RestController
public class EmployeeController {

    private EmployeeService employeeService;

    @Inject
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService; 
    }

    @PostMapping("/employee/add")
    @AllowAnonymous
    public int create(@FromBody Employee employee) throws Exception {
        try(DbContext db = DbConnManager.createAppDb("test")) {
            return db.Entity().create(Employee.class).insert(employee);
        }
    }

    @PostMapping("/employee/update")
    @AllowAnonymous
    public int update(@FromBody Employee employee) throws Exception {
        try(DbContext db = DbConnManager.createAppDb("test")) {
            return db.Entity().create(Employee.class).update(employee);
        }
    }

    @GetMapping("/employee/{id}")
    @AllowAnonymous
    public Employee error(@FromRoute int id) throws Exception {
        return employeeService.getEmployee(id);
    }

    @GetMapping("/employee/entity/{id}")
    @AllowAnonymous
    public Employee getById(@FromRoute int id) throws Exception {
        return employeeService.getEmployeeEntity(id);
    }

    @GetMapping("/employee/delete/{id}")
    @AllowAnonymous
    public int delete(@FromRoute int id) throws Exception {
        try(DbContext db = DbConnManager.createAppDb("test")) {
            return db.Entity().create(Employee.class).delete(id);
        }
    }

    @GetMapping("/employee/list")
    @AllowAnonymous
    public List<Employee> list() throws Exception {
        try(DbContext db = DbConnManager.createAppDb("test")) {
            return db.Entity().create(Employee.class).where("salary > ?", 1000).toList();
        }
    }
}
