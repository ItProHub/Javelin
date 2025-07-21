package site.itprohub.javelin.web;

import site.itprohub.javelin.web.security.Auth.AuthOptions;

public class WebInit {
    public static void init() {
        AuthOptions.init();
    }
}
