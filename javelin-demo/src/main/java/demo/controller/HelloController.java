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

    @HttpGet
    @Route("/hello")
    public String hello() {
        return "Hello World from " + userService.getUserById(1) ;
    }

    @HttpGet
    @Route("/query")
    public String query(@FromQuery String name) {
        return "Hello World from " + name ;
    }

    @HttpGet
    @Route("/get/{id}")
    @AllowAnonymous
    public String get(@FromRoute int id) {
        return " This is a GET request. " + userService.getUserById(id);
    }

    @HttpPost
    @Route("/post")
    public String post(@FromBody User user) {
        return " User: " + user.getUsername();
    }

    @HttpGet
    @Route("/user/get/{id}")
    public User GetUser(@FromRoute int id) {
        return new User("查鲜花的小牛粪", 18);
    }

    @HttpGet
    @Route("/error")
    @AllowAnonymous
    public void error() throws Exception {
        throw new Exception("测试异常");
    }
}
