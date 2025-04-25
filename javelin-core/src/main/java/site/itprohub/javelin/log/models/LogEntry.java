package site.itprohub.javelin.log.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LogEntry {
    public String oprId;
    public String oprType;
    public String oprName;
    public Date startTime;

    public int status;

    public long duration;

    //#region 业务相关数据
    public String url;

    public String requestMethod;

    public String userId;

    public String userCode;

    public String controller;

    public String action;
    

    //#endregion

    //#region 异常相关数据
    public int hasError;

    public String errorMessage;
    //#endregion


    public LogEntry() {
        this.startTime = new Date();
    }    
}
