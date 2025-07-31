package demo.controller;

import demo.service.UserService;
import site.itprohub.javelin.annotations.*;
import site.itprohub.javelin.annotations.parameter.*;
import site.itprohub.javelin.data.DbConnManager;
import site.itprohub.javelin.data.context.DbContext;
import demo.data.Employee;

@RestController
public class UserController {

    private UserService userService;

    @Inject
    public UserController(UserService userService) {
        this.userService = userService; 
    }

    @HttpPost
    @Route("/user/add")
    @AllowAnonymous
    public long create(@FromBody Employee employee) throws Exception {
        try(DbContext db = DbConnManager.createAppDb("test")) {
            return db.Entity().insert(employee);
        }
    }

}
