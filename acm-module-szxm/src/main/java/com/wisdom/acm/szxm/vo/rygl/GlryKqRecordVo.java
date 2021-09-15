package com.wisdom.acm.szxm.vo.rygl;

import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.Date;

/**
 * 管理人员考勤Vo
 */
@Data
public class GlryKqRecordVo
{

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
     * 编号
     */
    private String serialId;

    /**
     * 请假人员ID
     */
    private Integer peopleId;

    /**
     * 请假人员名称
     */
    private String peopleName;

    /**
     * 组织机构名称
     */
    private String orgName;

    /**
     * 职务
     */
    private String job;

    /**
     * 请假原因
     */
    private String idCard;

    /**
     * 应出勤天数
     */
    private Integer days;

    /**
     * 请假天数
     */
    private Integer qjdays;

    /**
     * 缺勤天数
     */
    private Integer qqdays;

    /**
     * 实际出勤天数
     */
    private Integer actDays;
}
