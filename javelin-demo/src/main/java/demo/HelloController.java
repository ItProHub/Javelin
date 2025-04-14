package demo;

import site.itprohub.javelin.annotations.GetMapping;
import site.itprohub.javelin.annotations.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}
