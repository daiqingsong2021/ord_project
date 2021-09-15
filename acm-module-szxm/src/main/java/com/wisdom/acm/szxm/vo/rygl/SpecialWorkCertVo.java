package com.wisdom.acm.szxm.vo.rygl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.Date;

@Data
public class SpecialWorkCertVo
{
    /**
     * 主键ID
     */
    private Integer id;

    private Integer projectId;

    private Integer sectionId;

    private Integer certGlId;

    private Integer specialWorkerId;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date certExpirationTime;

    private String certNum;

    private String certName;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date warnDate;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date certFirstPublishTime;

    private String certVerifyUrl;

    private Integer warnPeriod;

    private GeneralVo warnStatusVo=new GeneralVo();

    private Integer fileCount;

    private Integer peopleId;

    private String peopleName;

    private String orgName;
}
