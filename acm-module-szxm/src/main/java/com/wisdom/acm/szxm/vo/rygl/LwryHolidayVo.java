package com.wisdom.acm.szxm.vo.rygl;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 劳务人员考勤Vo
 */
@Data
public class LwryHolidayVo {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 编号
     */
    private String serialId;
    /**
     * 项目ID
     */
    private Integer projectId;
    /**
     * 项目名称
     */
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
     * 请假人员ID
     */
    private Integer peopleId;

    /**
     * 请假人员名称
     */
    private String peopleName;

    /**
     * 组织机构名称（所属作业队）
     */
    private String workOrgName;
    /**
     * 组织机构名称（所属外包公司）
     */
    private String companyOrgName;

    /**
     * 请假开始时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date startTime;

    /**
     * 请假结束时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date endTime;

    /**
     * 请假天数
     */
    private Integer days;

    /**
     * 请假状态
     */
    private String status;
    /**
     * 请假状态
     */
    private String statusDesc;

    /**
     * 发起人
     */
    private String provideName;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date createTime;
}
