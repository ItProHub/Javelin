package site.itprohub.javelin.data.command;

import site.itprohub.javelin.data.context.DbContext;

public class CPQuery extends BaseCommand {

    public CPQuery(DbContext dbContext) {
        super(dbContext);
    }        

    //#region 拼接操作
    public CPQuery append(String sql) {
        sbSql.append(sql);
        return this;
    }
    //#endregion


}
