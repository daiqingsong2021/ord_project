package com.wisdom.acm.szxm.vo.rygl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.Date;

/**
 * 请假VO对象
 */
@Data
public class HolidayVo
{
    /**
     * 主键ID
     */
    private Integer id;

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
     * 开始时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date startTime;

    /**
     * 完成时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date endTime;

    /**
     * 请假天数
     */
    private Integer days;

    /**
     * 请假类别 szxm.rygl.holitype
     */
    private GeneralVo typeVo=new GeneralVo();

    /**
     * 人员职务
     */
    private String ryZw;

    /**
     * 请假原因
     */
    private String reason;

    /**
     * 请假期间工作负责人
     */
    private String changeName;

    /**
     * 请假期间工作负责人id
     */
    private Integer agentId;
    /**
     * 流程状态
     */
    private GeneralVo statusVo=new GeneralVo();


}
