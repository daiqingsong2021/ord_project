package com.wisdom.acm.szxm.vo.rygl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PeopleEntryDetailVo
{
    /**
     * 主键ID
     */
    private Integer id;

    private Integer enTryId;

    private String name;

    private GeneralVo typeVo=new GeneralVo();

    private GeneralVo jobVo=new GeneralVo();

    private GeneralVo sexVo=new GeneralVo();

    private Integer age;

    private String telPhone;

    private String idCard;

    private GeneralVo peoTypeVo=new GeneralVo();

    private BigDecimal classHour;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date bornDate;

    private Integer projInfoId;

    private Integer projectId;

    private Integer sectionId;

    private String gzkh;

    private BigDecimal score;

    private Integer peopleId;

    /**
     * 进退场类型 0 进场 1退场
     */
    private String entryType;

    /**
     * 人员所属单位
     */
    private String orgName;
}
