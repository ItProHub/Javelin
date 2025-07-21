package site.itprohub.javelin.log;

import java.util.List;

import site.itprohub.javelin.base.BasePipelineContext;
import site.itprohub.javelin.log.models.*;

public class OprLogScope {

    private List<StepItem> steps;

    public OprLog oprlog;

    public static OprLogScope start(BasePipelineContext pipelineContext) {
        OprLogScope scope = new OprLogScope();

        scope.oprlog = OprLog.create(pipelineContext);

        return scope;
    }

    public int setException(Exception ex) {
        return oprlog.setException(ex); 
    }


    public int saveOprLog(BasePipelineContext pipelineContext) {
        LogHelper.Write(this.oprlog);
        return 1;
    }

}
