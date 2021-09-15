package com.wisdom.base.common.vo.sys;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class SectionProjectVo {

    private Integer id;

    private Integer parentId;

    private String code;

    private String name;

    private Integer projectId;

    private String projectCode;

    private String projectName;
    /**
     * org/section
     */
    private String orgType;

    /**
     * 专业
     */
    private  String professional;
    /**
     * 类型（标段/组织类型）
     */
    private String typeCode;

}
