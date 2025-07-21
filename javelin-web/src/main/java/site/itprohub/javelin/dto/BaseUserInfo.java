package site.itprohub.javelin.dto;

/**
 * 用户信息接口
 */
public abstract class BaseUserInfo {

    private String userId;
    private String userCode;
    private String userName;
    private String userRole;    

    public BaseUserInfo() {}

    public String getUserId()
    {
        return userId;
    }

    public String getUserCode()
    {
        return userCode;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getUserRole()
    {
        return userRole;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setUserCode(String userCode)
    {
        this.userCode = userCode;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setUserRole(String userRole)
    {
        this.userRole = userRole;
    }

    public void validate() {
        if (userId.isBlank()) {
            throw new IllegalArgumentException("userId不能为空");
        }

        if (userName.isBlank()) {
            throw new IllegalArgumentException("userName不能为空");
        }
    }
}

