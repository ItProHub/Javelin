package site.itprohub.javelin.web.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import site.itprohub.javelin.base.config.ConfigClient;
import site.itprohub.javelin.clients.serviceClients.MoonClient;
import site.itprohub.javelin.dto.WebUserInfo;
import site.itprohub.javelin.web.security.Auth.AuthenticationManager;
import site.itprohub.javelin.web.security.Auth.LoginTicket;

public class AuthTest {


    @Test
    public void testLogin() {

        ConfigClient.Instance.setClient(MoonClient.Instance);

         WebUserInfo userInfo = new WebUserInfo();
        userInfo.setUserId("1");
        userInfo.setUserCode("test");
        userInfo.setUserName("吕小布");
        userInfo.setUserRole("admin");
        
        String token = AuthenticationManager.getLoginToken(userInfo, 86400);

        LoginTicket ticket = AuthenticationManager.decodeToken(token);

        assertEquals(userInfo.getUserId(), ticket.getUser().getUserId());
        assertEquals(userInfo.getUserCode(), ticket.getUser().getUserCode());
        assertEquals(userInfo.getUserName(), ticket.getUser().getUserName());
        assertEquals(userInfo.getUserRole(), ticket.getUser().getUserRole());


    } 


}
