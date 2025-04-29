package demo.controller;

import demo.service.UserService;
import site.itprohub.javelin.annotations.*;

import demo.data.User;

@RestController
public class MyBeanController {

    private UserService userService;

    @Inject
    public MyBeanController(UserService userService) {
        this.userService = userService; // ✅ 注入UserService实例 
    }

    @PostConstruct
    public void init() {
        System.out.println("UserController constructor called");
    }

    @PreDestroy
    public void destroy() {
        // 清理任务
        System.out.println("Bean is being destroyed");
    }

    @GetMapping("/mybean")
    @AllowAnonymous
    public String myBean() {
        return "Hello World from " + this.getClass().getName() ;
    }

}
