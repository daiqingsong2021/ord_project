package com.wisdom.acm.szxm.po.wtgl;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 问题处理记录PO
 */
@Data
@Table(name = "SZXM_SYS_QUESTION_RECORD")
public class QuestionRecordPo  extends BasePo
{
    /**
     * 项目ID
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 标段ID
     */
    @Column(name = "section_id")
    private Integer sectionId;

    /**
     * 问题ID
     */
    @Column(name = "question_id")
    private Integer questionId;

    /**
     * 处理说明
     */
    @Column(name = "REMARK")
    private String remark;

    /**
     * 下一节点处理人组织ID
     */
    @Column(name = "ORG_ID")
    private Integer orgId;


    /**
     * 下一节点处理人ID
     */
    @Column(name = "USER_ID")
    private Integer userId;

    /**
     * 状态(0新建、1待处理、2待审核、3已关闭、4已挂起)
     */
    @Column(name = "STATUS")
    private String status;

    /**
     * 动作(0新建、1发布、2处理、3转发、4驳回、5确认、6挂起、7关闭)
     */
    @Column(name = "action")
    private String action;

    @Column(name = "creator_org_id")
    private Integer createrOrgId;

}
