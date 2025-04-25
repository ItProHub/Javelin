package site.itprohub.javelin.log;

import java.util.Date;
import java.util.UUID;

import site.itprohub.javelin.http.Pipeline.HttpPipelineContext;
import site.itprohub.javelin.utils.StatusCodeUtils;

public class OprLog {
    public String oprId;

    public String oprType;

    public String oprName;

    public int status;

    public int hasError;

    public String errType;

    public String errMsg;

    public Date startTime;

    public Date endTime;


    // 部署环境相关
    public String appName;


    // 链路字段
    public String rootId;

    //#region 业务相关
    public String httpMethod;

    public String url;

    public String UserAgent;
    
    public String userId;

    public String userName;

    public String module;

    public String controller;

    public String action;
    //#endregion


    static OprLog create(HttpPipelineContext pipelineContext) {
        OprLog log = new OprLog();
        
        return log;
    }


    public void setBaseInfo(HttpPipelineContext pipelineContext) {
        if ( pipelineContext.routeDefinition == null ) {
            this.startTime = new Date();
            this.oprId =  UUID.randomUUID().toString();
        } else {
            this.startTime = pipelineContext.startTime;
            this.oprId = pipelineContext.processId;
        }
        this.status = 200;
        this.appName = "javelin";
    }

    public int setException(Exception ex) {
       if (ex != null) {
            status = getErrorCode(ex);

            hasError = StatusCodeUtils.isServerError(status) ? 1 : 0;
            errType = ex.getClass().getName();
            errMsg = ex.getMessage();

            return 1;
           
       }
       return 0;
    }


    public static int getErrorCode(Exception ex) {
        if( ex == null )
            return 200;

        if ( ex instanceof IllegalArgumentException  ) {
            return 400;
        }

        return 500;
    }
}
