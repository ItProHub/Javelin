package demo.controller;

import demo.service.EmployeeService;
import site.itprohub.javelin.annotations.*;
import site.itprohub.javelin.annotations.parameter.*;
import demo.data.Employee;

@RestController
public class EmployeeController {

    private EmployeeService employeeService;

    @Inject
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService; 
    }

    @GetMapping("/employees/{id}")
    @AllowAnonymous
    public Employee error(@FromRoute int id) throws Exception {
        return employeeService.getEmployee(id);
    }
}
