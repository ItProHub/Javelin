package site.itprohub.javelin.rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import site.itprohub.javelin.http.Pipeline.NHttpContext;
import site.itprohub.javelin.utils.EnvUtils;

public class DefaultRootHandler {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void handle(NHttpContext context) throws Exception { // 处理请求的方法，抛出异常
        String text = "This is "+ EnvUtils.ApplicationName +", It's worked! \n" +
        "Server time: " + LocalDateTime.now().format(formatter);

        context.response.write(text);
    }
}
