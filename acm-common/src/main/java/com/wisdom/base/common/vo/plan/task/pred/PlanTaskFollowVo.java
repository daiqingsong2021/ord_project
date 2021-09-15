package com.wisdom.base.common.vo.plan.task.pred;

import com.wisdom.base.common.vo.OrgVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

@Data
public class PlanTaskFollowVo {
    private Integer id;
    private Integer taskId;
    private Integer followTaskId;
    private String defineName;
    private String taskCode;
    private String taskName;
    private UserVo user;
    private OrgVo org;
    private String planStartTime;
    private String planEndTime;
    private String relationType;
    private Double lagNum;
}
