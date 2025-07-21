package site.itprohub.javelin.base;

import java.util.Date;
import java.util.UUID;

public abstract class BasePipelineContext {
    // 操作id，唯一不重复
    public String processId;

    public Date startTime;

    public Date endTime;

    public Exception lastException;

    public BasePipelineContext() {
        this.startTime = new Date();
        this.processId = UUID.randomUUID().toString();
    }

}
