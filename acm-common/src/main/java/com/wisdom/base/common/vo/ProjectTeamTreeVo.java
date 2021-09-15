package com.wisdom.base.common.vo;

import lombok.Data;

/**
 * 团队-标段
 */
@Data
public class ProjectTeamTreeVo extends TreeVo<ProjectTeamTreeVo>{

    /**
     * 团队代码
     */
    private String teamCode;
    /**
     * 团队名称
     */
    private String teamName;
    /**
     * project
     */
    private String bizType;
    /**
     * 所属项目ID
     */
    private Integer bizId;

    /**
     * 扩展字段1
     */
    private String extendedColumn1;
}
