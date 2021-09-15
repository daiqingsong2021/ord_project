package com.wisdom.base.common.msg;

import com.github.pagehelper.PageInfo;
import lombok.Getter;

import java.util.List;

/**
 */
@Getter
public class TableResultResponse<T> extends ApiResult {

    private long total;

    public TableResultResponse(PageInfo<T> page) {
        this.total = page.getTotal();
        this.setData(page.getList());
    }

    public TableResultResponse(long total, List<T> rows) {
        this.total = total;
        this.setData(rows);
    }

}
