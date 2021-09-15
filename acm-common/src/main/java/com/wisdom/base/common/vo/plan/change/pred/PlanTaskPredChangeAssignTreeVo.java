package com.wisdom.base.common.vo.plan.change.pred;

import com.wisdom.base.common.vo.OrgVo;
import com.wisdom.base.common.vo.TreeVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.Date;

/**
 * @Author: szc
 * @Date: 2019/3/12 9:55
 * @Version 1.0
 */
@Data
public class PlanTaskPredChangeAssignTreeVo  extends TreeVo<PlanTaskPredChangeAssignTreeVo> {

    // 变更ID
    private Integer changeId;
    // 类型
    private String type;
    //作业类型
    private Integer taskType;
    // 名称
    private String name;
    // 代码
    private String code;
    // 计划开始时间
    private Date planStartTime;
    // 计划完成时间
    private Date planEndTime;
    // 责任主体
    private OrgVo org;
    // 责任人
    private UserVo user;
    // 是否可选
    private Integer check;

}
