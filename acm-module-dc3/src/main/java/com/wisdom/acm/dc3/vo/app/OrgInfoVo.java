package com.wisdom.acm.dc3.vo.app;
import lombok.Data;

/**
 * 组织信息
 */
@Data
public class OrgInfoVo
{
    /**
     * 组织ID
     */
    private  Integer orgId;

    /**
     * 项目ID
     */
    private  Integer projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 标段ID
     */
    private Integer sectionId;

    /**
     * 标段号
     */
    private  String sectionCode;

    /**
     * 标段名称
     */
    private String sectionName;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 项目部地址
     */
    private String projUnitAddress;


    /**
     * 联系电话
     */
    private String telPhone;

    /**
     * 分管项目部领导
     */
    private String leader;

    /**
     * 来源 0 人员管理 1 项目团队
     */
    private String  source;
}
