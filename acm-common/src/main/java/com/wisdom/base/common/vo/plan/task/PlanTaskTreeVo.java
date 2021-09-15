package com.wisdom.base.common.vo.plan.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * task
 *
 * @author Lcs
 * @date 2019-02-13 18:45:35
 */
@Data
public class PlanTaskTreeVo extends TreeVo<PlanTaskTreeVo> {
    //代码
    private String code;

    //名称
    private String name;

    //所属项目
    private Integer projectId;

    //所属计划定义
    private Integer defineId;

    //责任主体
    private OrgVo org;

    //责任人
    private UserVo user;

    //计划开始时间
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date planStartTime;

    //计划完成时间
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date planEndTime;

    //基线开始时间
    private Date blStartTime;

    //基线完成时间
    private Date blEndTime;

    //计划类型
    private DictionaryVo planType;

    //计划级别
    private DictionaryVo planLevel;

    //尚需工期
    private Double remainDrtn;

    //实际工期
    private Double actDrtn;

    //计划工期
    private Double planDrtn;

    //实际开始时间
    private Date actStartTime;

    //实际完成时间
    private Date actEndTime;

    //日历
    private CalendarVo calendar;

    //编制状态
    private StatusVo status;

    //反馈状态
    private GeneralVo feedbackStatus;

    //完成百分比
    private Double completePct;

    //预计完成时间
    private Date maybeEndTime;

    //备注
    private String remark;

    // 节点类型(project,define,task)
    private String nodeType;

    //任务类型(0任务，1开始里程碑，2完成里程碑，3WBS)
    private Integer taskType;

    //总工时
    private Double planQty;

    //工期类型
    private DictionaryVo taskDrtnType;

    //控制账户
    private Integer controlAccount;

    //wbs反馈
    private Integer isFeedback;

    //权重
    private Double estWt;

    //创建日期
    private Date creatTime;

    // 创建人ID
    private UserVo creator;

    private Date releaseTime;

    private UserVo releaseUser;

    // 创建组织机构
    private Integer creatOrgId;

    //权重
    private Integer sortNum;

    //权限
    private Integer auth;

    // 删除标识
    private Integer del;

    // 关键路径
    private Integer critical;

    // WBS PATH
    private String wbsPath;

    // 逻辑关系线，为甘特图展示使用
    private List<PredecessorLinkVo> PredecessorLink;

    // 任务或wbs是否被分配分类码（1:已分配；0:未分配）
    private String isAssign;

    /**
     * 扩展字段
     */
    private String custom01;

    /**
     * 扩展字段
     */
    private String custom02;

    /**
     * 扩展字段
     */
    private String custom03;

    /**
     * 扩展字段
     */
    private String custom04;

    /**
     * 扩展字段
     */
    private String custom05;

    /**
     * 扩展字段
     */
    private String custom06;

    /**
     * 扩展字段
     */
    private String custom07;

    /**
     * 扩展字段
     */
    private String custom08;

    /**
     * 扩展字段
     */
    private String custom09;

    /**
     * 扩展字段
     */
    private String custom10;

}
