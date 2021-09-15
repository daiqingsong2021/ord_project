package com.wisdom.base.common.vo.plan.task;


import lombok.Data;

import java.util.Set;

/**
 * 任务权限
 *
 * @author Liucs
 */
@Data
public class PlanTaskAuthVo {

    private Integer id;

    //所属项目
    private Integer projectId;

    //所属计划
    private Integer defineId;

    //父节点
    private Integer parentId;

    //责任主体
    private Integer orgId;

    //责任人
    private Integer userId;

    // 状态
    private String status;

    private Integer feedbackStatus;

    //
    private Integer isFeedback;

    // 权限集合
    private Set<String> auths;

}
