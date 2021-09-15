package com.wisdom.acm.szxm.po.rygl;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "szxm_rygl_warehouse")
@Data
public class WarnHousePo extends BasePo
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
    @Column(name = "proj_info_id")
    private Integer projInfoId;


    /**
     * 仓库名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 仓库地址
     */
    @Column(name = "address")
    private String address;

}
