package com.wisdom.acm.szxm.mapper.menu;

/**
 * Author：wqd
 * Date：2020-04-15 16:09
 * Description：<描述>
 */

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 根据bizType查询模块名
 */
@Repository
public interface MenuMapper {
    /**
     * 查询menu名称
     * @param menuCode
     */
    String queryMenuNameByCode(@Param("menuCode") String menuCode);

}
