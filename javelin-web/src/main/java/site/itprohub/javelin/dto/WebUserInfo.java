package site.itprohub.javelin.dto;

public class WebUserInfo extends BaseUserInfo {
    public String tenantId;

    public WebUserInfo()
    {
        super();
    }
    
    public String getTenantId()
    {
        return tenantId;
    }

    public void setTenantId(String tenantId)
    {
        this.tenantId = tenantId;
    }

}
