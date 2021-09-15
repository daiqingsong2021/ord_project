package com.wisdom.base.common.vo.plan.task;

import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.OrgVo;
import com.wisdom.base.common.vo.TreeVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.Date;

/**
 * 计划编制协同VO,(发布计划/取消发布/确认计划/取消确认)
 *
 * @author Lcs
 * @date 2019-02-13 18:45:35
 */
@Data
public class PlanTaskCollaborationVo extends TreeVo<PlanTaskCollaborationVo> {

    //代码
    private String code;

    //名称
    private String name;

    //所属计划定义
    private Integer defineId;

    //责任主体
    private OrgVo org;

    //责任人
    private UserVo user;

    //计划开始时间
    private Date planStartTime;

    //计划完成时间
    private Date planEndTime;

    // 节点类型(project,define,task)
    private String nodeType;

    //任务类型(0任务，1开始里程碑，2完成里程碑，3WBS)
    private Integer taskType;

    // 权限
    private Integer check;

    /**
     * 计划类型
     */
    private DictionaryVo planType;
}
