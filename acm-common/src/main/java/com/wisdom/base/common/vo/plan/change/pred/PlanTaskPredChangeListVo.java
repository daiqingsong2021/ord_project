package com.wisdom.base.common.vo.plan.change.pred;

import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.OrgVo;
import com.wisdom.base.common.vo.TypeVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.Date;

/**
 * @Author: szc
 * @Date: 2019/3/10 15:37
 * @Version 1.0
 */
@Data
public class PlanTaskPredChangeListVo {
    // 主键ID
    private Integer id;
    // 变更ID
    private Integer changeId;
    // 原逻辑关系ID
    private Integer logicId;
    // 变更类型
    private TypeVo changeType;
    // 项目ID
    private Integer projectId;
    // 计划定义对象
    private GeneralVo define;
    // 任务对象（后置任务/前置任务）
    private GeneralVo task;
    // 责任主体
    private OrgVo org;
    // 责任人
    private UserVo user;
    // 原逻辑关系类型
    private String baseRelationType;
    // 新逻辑关系类型
    private String newRelationType;
    // 原延时
    private Double baseLagNum;
    // 新延时
    private Double newLagNum;
    // 计划开始
    private Date planStartTime;
    // 计划完成
    private Date planEndTime;

}
