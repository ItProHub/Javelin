package site.itprohub.javelin.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import site.itprohub.javelin.utils.StringExtensions;

/**
 * 用户信息接口
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
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
        if (StringExtensions.isNullOrEmpty(userId)) {
            throw new IllegalArgumentException("userId不能为空");
        }

        if (StringExtensions.isNullOrEmpty(userCode)) {
            throw new IllegalArgumentException("userCode不能为空");
        }
    }
}

