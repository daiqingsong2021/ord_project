package com.wisdom.acm.szxm.vo.rygl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.Date;

@Data
public class PeopleEntryVo
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

    private String sectionTypeCode;

    private String sectionTypeName;

    /**
     * 标段名称
     */
    private String sectionName;

    private String code;

    private GeneralVo typeVo=new GeneralVo();

    private Integer peoNums;

    private GeneralVo peoEntryTypeVo=new GeneralVo();

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date entryTime;

    private Integer projInfoId;

    private String orgName;

    private GeneralVo statusVo=new GeneralVo();

    private String creater;
    private String userName;
    private String userId;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date createTime;

    /**
     * 单位类型
     */
    private GeneralVo orgTypeVo=new GeneralVo();


    /**
     * 单位分类Code 对应的 Name base.org.classification
     */
    private GeneralVo orgCategoryVo=new GeneralVo();
}
