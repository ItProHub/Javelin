package site.itprohub.javelin.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import site.itprohub.javelin.annotations.GetMapping;
import site.itprohub.javelin.annotations.RestController;
import site.itprohub.javelin.http.Pipeline.NHttpContext;

public class RouterTest {
    private Router router;

    @BeforeEach
    public void setup() throws Exception {
        router = new Router();

        // 手动添加一个测试路由（模拟 registerRoutes 之后的结果）
        Method method = TestController.class.getDeclaredMethod("hello", NHttpContext.class);
        Field field = Router.class.getDeclaredField("dynamicRoutes");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        var routes = (List<RouteDefinition>) field.get(router);
        routes.add(new RouteDefinition("GET", "/hello", Pattern.compile("/hello"), java.util.List.of(), TestController.class, method));
    }

    @Test
    public void testRouteMatch() throws Exception {
        Method method = TestController.class.getDeclaredMethod("hello", NHttpContext.class);
        RouteDefinition def = new RouteDefinition("GET", "/hello", Pattern.compile("/hello"), java.util.List.of(), TestController.class, method);
        assertTrue(def.pathPattern.matcher("/hello").matches());
    }

    @RestController
    public static class TestController {
        @GetMapping("/hello")
        public void hello(NHttpContext context) {
            // 可加打印或mock验证
        }
    }
}
