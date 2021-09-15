package com.wisdom.acm.szxm.vo.rygl;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DepartmentSectionVo {

    /**
     * 主键ID
     */
    private Integer id;

    private Integer sort;

    /**
     * 项目ID
     */
    private Integer projectId;

    private String projectName;
    /**
     * 标段ID
     */
    private Integer sectionId;

    /**
     * 标段编码
     */
    private String sectionCode;

    /**
     * 标段名称
     */
    private String sectionName;


    /**
     * 组织机构ID
     */
    private String orgId;
    /**
     * 组织机构code
     */
    private String orgCode;
    /**
     * 组织机构名称
     */
    private String orgName;


    private List<ParticipateUnitVo> participateUnitVo=new ArrayList<ParticipateUnitVo>();





}
