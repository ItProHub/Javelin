package demo.controller;

import site.itprohub.javelin.annotations.AllowAnonymous;
import site.itprohub.javelin.annotations.HttpGet;
import site.itprohub.javelin.annotations.RestController;
import site.itprohub.javelin.annotations.Route;
import site.itprohub.javelin.dto.WebUserInfo;
import site.itprohub.javelin.web.security.Auth.AuthenticationManager;

@RestController
@Route("/auth")
public class AuthController {

    @AllowAnonymous
    @HttpGet
    @Route("/login")
    public String login() {
        // 登录逻辑

        WebUserInfo userInfo = new WebUserInfo();
        userInfo.setUserId("1");
        userInfo.setUserCode("test");
        userInfo.setUserName("吕小布");
        userInfo.setUserRole("admin");
        
        return AuthenticationManager.getLoginToken(userInfo, 86400);
    }

    @HttpGet
    @Route("/validate")
    public int validateLogin() {
        return 1;
    }


    @HttpGet
    @Route("/logout")
    public void logout() {
        AuthenticationManager.logout();
    }
}
