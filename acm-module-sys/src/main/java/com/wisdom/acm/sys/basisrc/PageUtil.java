package com.wisdom.acm.sys.basisrc;

import java.util.ArrayList;
import java.util.List;

public class PageUtil {

    /**
     * 分页list集合
     * @param pageSize
     * @param currentPageNum
     * @param list
     * @return
     */
    public static List pageList(int pageSize, int currentPageNum, List list) {
        List<Object> retList = new ArrayList<>();
        int start = (currentPageNum - 1) * pageSize;
        int end = start + pageSize;
        for (int i = start; i < end; i++) {
            if (i < list.size()) {
                retList.add(list.get(i));
            }
        }
        return retList;
    }
}
