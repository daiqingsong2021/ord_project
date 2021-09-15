package com.wisdom.base.common.vo.plan.project;

import com.wisdom.base.common.vo.*;
import lombok.Data;

import java.util.Date;

@Data
public class PlanProjectVo {

    private Integer id;

    //代码
    private String code;

    //名称
    private String name;

    //EPS
    private GeneralVo parent;

    //责任主体
    private OrgVo org;

    //责任人
    private UserVo user;

    //计划开始时间
    private Date planStartTime;

    //计划完成时间
    private Date planEndTime;

    //项目投资额
    private Double totalBudget;

    //日历
    private CalendarVo calendar;

    //总工期
    private Double totalDrtn;

    //地址
    private String address;

    //项目概算
    private Double projectEstimate;

    //项目预算
    private Double projectBudget;

    //项目目标
    private String projectTarget;

    //项目概况
    private String projectOverview;

    //备注
    private String remark;

    //
    private String scale;

    //创建者
    private UserVo creator;

    //创建时间
    protected Date creatTime;

    /**
     * 密级
     */
    private DictionaryVo secutyLevel;

    //状态
    private DictionaryVo status;
}
