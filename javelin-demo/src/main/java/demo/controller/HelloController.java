package demo.controller;

import demo.service.UserService;
import site.itprohub.javelin.annotations.*;
import site.itprohub.javelin.annotations.parameter.*;

import demo.data.User;

@RestController
public class HelloController {

    private UserService userService;

    @Inject
    public HelloController(UserService userService) {
        this.userService = userService; // ✅ 注入UserService实例 
    }

    @GetMapping("/hello")
    @AllowAnonymous
    public String hello() {
        return "Hello World from " + userService.getUserById(1) ;
    }

    @GetMapping("/query")
    public String query(@FromQuery String name) {
        return "Hello World from " + name ;
    }

    @GetMapping("/get/{id}")
    @AllowAnonymous
    public String get(@FromRoute int id) {
        return " This is a GET request. " + userService.getUserById(id);
    }

    @PostMapping("/post")
    public String post(@FromBody User user) {
        return " User: " + user.getUsername();
    }

    @GetMapping("/user/get/{id}")
    public User GetUser(@FromRoute int id) {
        return new User("查鲜花的小牛粪", 18);
    }

    @GetMapping("/error")
    @AllowAnonymous
    public void error() throws Exception {
        throw new Exception("测试异常");
    }
}
