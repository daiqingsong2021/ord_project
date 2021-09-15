package com.wisdom.base.common.vo;

import lombok.Data;

/**
 * 标段-树型数据VO
 */
@Data
public class SectionTreeVo extends TreeVo<SectionTreeVo>{
    /**
     * 代码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 标段类型编码
     */
    private String typeCode;

    /**
     * 标段类型名称
     */
    private String TypeName;

    /**
     * 项目ID
     */
    private Integer projectId;

    /**
     * 类型
     */
    private String type;
    /**
     * 专业
     */
    private String professional;
    /**
     * 单位分类
     */
    private String orgClassification;


}
