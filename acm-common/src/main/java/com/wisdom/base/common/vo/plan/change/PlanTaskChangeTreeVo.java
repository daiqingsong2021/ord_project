package com.wisdom.base.common.vo.plan.change;

import com.wisdom.base.common.vo.*;
import com.wisdom.base.common.vo.plan.task.PredecessorLinkVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: szc
 * @Date: 2019/3/5 13:43
 * @Version 1.0
 */
@Data
public class PlanTaskChangeTreeVo extends TreeVo<PlanTaskChangeTreeVo> {

    //任务id
    private Integer taskId;
    //项目id
    private Integer projectId;
    //计划id
    private Integer defineId;
    //变更id
    private Integer changeId;
    //父级id
    private Integer parentId;
    //父级类型
    private String  parentType;
    //名称
    private String name;
    //代码
    private String code;
    //任务类型
    private Integer taskType;
    //节点类型
    private String nodeType;
    //责任主体
    private OrgVo org;
    //责任人
    private UserVo user;
    //日历
    private CalendarVo calendar;
    //计划开始时间
    private Date planStartTime;
    //计划完成时间
    private Date planEndTime;
    //计划工期
    private Double planDrtn;
    //计划工时
    private Double planQty;
    //计划类型
    private DictionaryVo planType;
    //计划级别
    private DictionaryVo planLevel;
    //工期类型
    private DictionaryVo taskDrtnType;
    //控制账户
    private Integer controlAccount;
    // 发布人
    private UserVo aprvUser;
    // 发布时间
    private Date aprvTime;
    //编制状态
    private StatusVo status;
    //创建日期
    private Date creatTime;
    //创建人
    private UserVo creator;
    //变更类型
    private TypeVo changeType;
    //变更次数
    private Integer changeCount;
    //备注
    private String desc;
    //排序
    private Integer sort;
    //
    private Integer check;
    // 逻辑关系线，为甘特图展示使用
    private List<PredecessorLinkVo> PredecessorLink;
}
