package site.itprohub.javelin.data.multidb;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import site.itprohub.javelin.data.DatabaseType;
import site.itprohub.javelin.data.command.BaseCommand;
import site.itprohub.javelin.data.command.CPQuery;
import site.itprohub.javelin.data.context.DbContext;
import site.itprohub.javelin.data.paging.Page2Query;
import site.itprohub.javelin.data.paging.PageInfo;

public abstract class BaseClientProvider {

    public abstract DatabaseType getDatabaseType();  // 可返回 mysql, postgres, oracle, sqlserver 等

    /**
     * 将字段或表名包装为数据库兼容的形式，例如 MySQL: `name`, SQLServer: [name]
     */
    public String getObjectFullName(String name) {
        return name;  // 默认不处理，子类覆盖
    }

    /**
     * 返回 SQL 语句中使用的参数占位符，比如：@name / :name / ?
     */
    public String getParameterPlaceholder(String name, DbContext context) {
        return "@" + name;
    }

    /**
     * 判断是否为唯一索引冲突
     */
    public boolean isDuplicateInsertException(Exception ex) {
        return false; // 子类根据 SQLException 错误码判断
    }

    /**
     * 构造分页 SQL
     */
    public CPQuery setPagedQuery(CPQuery query, int skip, int take) {
        return query; // 子类应当实现特定方言
    }

    /**
     * 获取分页的双查询结构（如：先 count，再分页 select）
     */
    public Page2Query getPagedCommand(BaseCommand query, PageInfo pageInfo) {
        throw new UnsupportedOperationException("未实现分页查询");
    }

    /**
     * 设置自动生成主键后返回的 SQL 查询
     */
    public CPQuery getNewIdQuery(CPQuery insertQuery, Object entity) {
        throw new UnsupportedOperationException("当前数据库不支持返回主键");
    }

    /**
     * 切换当前连接数据库（如：use xxxx）
     */
    public void changeDatabase(DbContext dbContext, String dbName) throws SQLException {
        dbContext.getConnection().setCatalog(dbName);
    }

    /**
     * SQL 执行前的准备逻辑
     */
    public void prepareCommand(PreparedStatement stmt, DbContext dbContext) {
        // 默认不处理
    }

    /**
     * 生成有序 GUID（供主键使用）
     */
    public UUID newSeqGuid() {
        return UUID.randomUUID();
    }
}

