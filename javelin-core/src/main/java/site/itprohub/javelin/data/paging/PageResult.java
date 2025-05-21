package site.itprohub.javelin.data.paging;

import java.util.List;

public class PageResult<T> {
    public int total;

    public List<T> list;

    public PageResult(int total, List<T> list) {
        this.total = total;
        this.list = list;
    }
}
