package site.itprohub.javelin.web.security.Auth;

public class NPrincipal {
    public LoginTicket ticket;

    public String token;

    public LoginTicketSource source;

    public NPrincipal(LoginTicket ticket, LoginTicketSource source, String token) {
        this.ticket = ticket;
        this.token = token;
        this.source = source;

    }
}
