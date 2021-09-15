package com.wisdom.base.common.vo.plan.define;

import com.wisdom.base.common.vo.*;
import lombok.Data;

import java.util.Date;

@Data
public class PlanDefineTreeVo extends TreeVo<PlanDefineTreeVo> {

    private Integer id;

    //代码
    private String code;

    //名称
    private String name;

    private Integer projectId;

    //计划类型
    private DictionaryVo planType;

    //编制状态
    private DictionaryVo status;

    //备注
    private String remark;

    //计划开始时间
    private Date planStartTime;

    //计划完成时间
    private Date planEndTime;

    //计划工期
    private Double planDrtn;

    //权重
    private Integer estWt;

    //主控计划
    private Integer isMainPlan;

    //组织
    private OrgVo org;

    // 类型
    private String type;

    //用户
    private UserVo user;

    //创建时间
    private Date creatTime;

    //创建人
    private UserVo creator;

    //日历id
    private CalendarVo calendar;

    //标段
    private ProjectTeamVo section;
}
