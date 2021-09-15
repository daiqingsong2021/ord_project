package com.wisdom.acm.szxm.po.rygl;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 分包作业队
 */
@Table(name = "SZXM_RYGL_FBZYD")
@Data
public class FbzydPo extends BasePo
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
     * 分包作业队名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 分包作业队地址
     */
    @Column(name = "address")
    private String address;

    /**
     * 居住人数
     */
    @Column(name = "resident_num")
    private Integer residentNum;

    /**
     * 房屋性质
     */
    @Column(name = "house_character")
    private String houseCharacter;

    /**
     * 房屋面积
     */
    @Column(name = "house_area")
    private String houseArea;
}
