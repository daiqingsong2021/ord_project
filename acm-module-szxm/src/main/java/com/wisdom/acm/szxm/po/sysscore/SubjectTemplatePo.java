package com.wisdom.acm.szxm.po.sysscore;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Author：wqd
 * Date：2019-12-30 16:04
 * Description：<描述>
 */

/**
 * 系统评分____主观模板表
 */
@Table(name = "szxm_sys_subjective_template")
@Data
public class SubjectTemplatePo extends BasePo {
    /**
     * 分组名称编码(菜单表编码)
     */
    @Column(name = "group_code")
    private String groupCode;

    /**
     * 分组名称
     */
    @Column(name = "group_title")
    private String groupTitle;

    /**
     * 业务模块编码（菜单表）
     */
    @Column(name = "module_code")
    private String moduleCode;

    /**
     * 业务模块名称
     */
    @Column(name = "module_title")
    private String moduleTitle;

    /**
     * 评分人员角色
     */
    @Column(name = "raters")
    private String raters;
}
