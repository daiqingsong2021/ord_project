package com.wisdom.base.common.vo.plan.task.pred;

import com.wisdom.base.common.vo.StatusVo;
import com.wisdom.base.common.vo.sys.OrgVo;
import com.wisdom.base.common.vo.sys.UserVo;
import lombok.Data;

@Data
public class PlanTaskPredVo {


    //逻辑关系id
    private Integer id;
    //任务id
    private Integer taskId;
    //紧前任务id
    private Integer predTaskId;
    //计划名称
    private String defineName;
    //任务代码
    private String taskCode;
    //任务名称
    private String taskName;
    //责任人
    private UserVo user;
    //责任主体
    private OrgVo org;
    //计划开始时间
    private String planStartTime;
    //计划完成时间
    private String planEndTime;
    //实际开始时间
    private String actStartTime;
    //实际完成时间
    private String actEndTime;
    //关系类型
    private String relationType;
    //延时
    private Double lagNum;
    //完成状态
    private StatusVo status;
    //进展说明
    private String remark;
}
