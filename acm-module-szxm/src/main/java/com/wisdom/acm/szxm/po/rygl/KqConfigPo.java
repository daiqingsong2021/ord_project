package com.wisdom.acm.szxm.po.rygl;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "szxm_rygl_attendconfig")
@Data
public class KqConfigPo extends BasePo
{

    /**
     * 项目ID
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 标段ID
     */
    @Column(name = "section_id")
    private Integer sectionId;

    /**
     * 项目基础信息ID 外键
     */
    @Column(name = "calender_id")
    private Integer calenderId;

    /**
     * 考勤类型 0 全局 1 标段
     */
    @Column(name = "type")
    private String type;

    /**
     * 管理员是否考勤 0 否 1是
     */
    @Column(name = "mangerkq")
    private String mangerkq;

    /**
     * 劳务人员是否考勤 0 否 1是
     */
    @Column(name = "workerkq")
    private String workerkq;

}
