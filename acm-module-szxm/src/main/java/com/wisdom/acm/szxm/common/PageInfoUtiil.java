/**
 * Project Name:tyfw
 * File Name:PageInfoUtiil.java
 * Package Name:com.jsumt.util
 * Date:2017年6月21日上午11:06:23
 * Copyright (c) 2017, wuyf5@asiainfo-linkage.com All Rights Reserved.
 */

package com.wisdom.acm.szxm.common;

import java.util.List;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * ClassName:PageInfoUtiil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年6月21日 上午11:06:23 <br/>
 * 
 * @author wyf
 * @version
 * @param <E>
 * @since JDK 1.6
 * @see
 */
public class PageInfoUtiil<E>
{  
    /**
     * 上次page信息List
     * generatePageList:(这里用一句话描述这个方法的作用). <br/>
     * TODO(这里描述这个方法适用条件 - 可选).<br/>
     * TODO(这里描述这个方法的执行流程 - 可选).<br/>
     * TODO(这里描述这个方法的使用方法 - 可选).<br/>
     * TODO(这里描述这个方法的注意事项 - 可选).<br/>
     *
     * @author wyf
     * @param pageInfo 页面信息
     * @param dataList 数据集合
     * @return
     * @since JDK 1.6
     */
    public Page<E> generatePageList(PageInfo pageInfo, List<E> dataList)
    {
        Page<E> pageList = new Page(pageInfo.getPageNum(), pageInfo.getPageSize());
        int remainCounts = dataList.size() - (pageInfo.getPageNum() - 1) * pageInfo.getPageSize();
        if (remainCounts < pageInfo.getPageSize())
        {

            pageList.addAll(dataList.subList((pageInfo.getPageNum() - 1) * pageInfo.getPageSize(), dataList.size()));
        }
        else
        {
            pageList.addAll(dataList.subList((pageInfo.getPageNum() - 1) * pageInfo.getPageSize(),
                    pageInfo.getPageNum() * pageInfo.getPageSize()));
        }
        pageInfo.setTotal(dataList.size());

        return pageList;

    }

}
