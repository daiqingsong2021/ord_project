package com.wisdom.base.common.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.List;

public class PageUtil {

    public static <T> PageInfo<T> getPageInfo(List<T> list, int pageSize, int pageNum){

        Page<T> page = new Page<>();
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        page.addAll(list);
        page.getEndRow();
        page.setTotal(list.size());

        PageInfo<T> pageInfo = new PageInfo<>(page);

        List<T> pageList = new ArrayList<>();

        int endRow = (pageNum)*pageSize;
        endRow = endRow > list.size() ? list.size() : endRow;
        int startRow = (pageNum-1)*pageSize;
        for(int i = startRow; i < endRow; i++){
            pageList.add(list.get(i));
        }
        pageInfo.setList(pageList);

        return pageInfo;
    }

}
