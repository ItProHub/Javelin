package demo.controller;

import demo.service.UserService;
import site.itprohub.javelin.annotations.*;

@RestController
public class HelloController {

    private UserService userService;

    @Inject
    public HelloController(UserService userService) {
        this.userService = userService; // ✅ 注入UserService实例 
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World from " + userService.getUserById(1) ;
    }
}
