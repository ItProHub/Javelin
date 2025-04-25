package site.itprohub.javelin.log.models;

import java.util.Date;

public class StepItem {
    public String stepId;

    public String stepType;

    public String stepName;

    public Date startTime;

    public Date endTime;

    public long duration;

    public int status;
    
    public int hasError;
}
