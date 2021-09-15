package com.wisdom.acm.szxm.vo.rygl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PeopleVo
{
    /**
     * 主键ID
     */
    private Integer id;


    private Integer projInfoId;

    private Integer projectId;

    private String projectName;

    private Integer sectionId;

    private String sectionCode;

    private String sectionName;

    private String name;

    private GeneralVo typeVo=new GeneralVo();

    /**
     * 职务
     */
    private GeneralVo jobVo=new GeneralVo();

    private GeneralVo sexVo=new GeneralVo();

    private Integer age;

    private String telPhone;

    private String idCard;

    private GeneralVo peoTypeVo=new GeneralVo();

    private BigDecimal totalClassHour;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date bornDate;

    private String orgName;

    private Integer orgId;

    private Integer sysUserId;

    private String sysUserCode;

    private GeneralVo workTypeVo=new GeneralVo();

    private String gzkh;

    private BigDecimal score;

    private GeneralVo statusVo=new GeneralVo();

    private Integer fileCount;
    /**
     * 是否缺勤 1 是 0 否
     */

    private Integer isQq;

    /**
     * 是否请假 1是 0否
     */

    private Integer isQj;

    /**
     * 记录日期 格式如20190708
     */
    private Integer jlRq;
    //考勤状态  在岗，离岗，请假，出场
    private String  kqStatus;


}
