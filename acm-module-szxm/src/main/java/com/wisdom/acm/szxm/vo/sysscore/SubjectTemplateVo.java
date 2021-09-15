package com.wisdom.acm.szxm.vo.sysscore;

/**
 * Author：wqd
 * Date：2020-01-02 14:10
 * Description：<描述>
 */

import com.google.common.collect.Lists;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.List;

/**
 * 主观评分模板
 */
@Data
public class SubjectTemplateVo {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 分组名称编码(菜单表编码)
     */
    private String groupCode;

    /**
     * 分组名称
     */
    private String groupTitle;

    /**
     * 业务模块编码（菜单表）
     */
    private String moduleCode;

    /**
     * 业务模块名称
     */
    private String moduleTitle;

    /**
     * 可以评分的角色
     */
    private List<GeneralVo> rolesVo= Lists.newArrayList();
}
