package com.wisdom.acm.szxm.vo.rygl;

import com.google.common.collect.Lists;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SpecialWorkerVo
{
    /**
     * 主键ID
     */
    private Integer id;

    private Integer projectId;

    private String projectName;

    private Integer sectionId;

    private String sectionCode;

    private String sectionName;

    private String name;

    private String job;

    private String peoType;

    private String telPhone;

    private String orgName;
    /**
     * （多个以逗号,相连）
     */
    private String workType;
    /**
     * 特殊工种显示列表
     */
    private List<GeneralVo> workTypeVoList= Lists.newArrayList();

    /**
     * 流程状态
     */
    private GeneralVo statusVo=new GeneralVo();

    /**
     * 人员状态
     */
    private GeneralVo peoStatusVo=new GeneralVo();

    /**
     * 预警数
     */
    private Integer warnNums;

    /**
     * 过期数
     */
    private Integer dDateNums;

}
