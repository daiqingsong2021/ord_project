package com.wisdom.acm.szxm.po.wtgl;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * 问题PO
 */
@Data
@Table(name = "SZXM_SYS_QUESTION")
public class QuestionPo  extends BasePo
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
     * 问题标题
     */
    @Column(name = "TITLE")
    private String title;

    /**
     * 问题类别
     */
    @Column(name = "TYPE")
    private String type;

    /**
     * 问题标题
     */
    @Column(name = "PRIORITY")
    private String priority;

    /**
     * 责任部门
     */
    @Column(name = "ORG_ID")
    private Integer orgId;


    /**
     * 责任人
     */
    @Column(name = "USER_ID")
    private Integer userId;


    /**
     * 当前处理人所属组织ID
     */
    @Column(name = "CURRENT_USER_ORG_ID")
    private Integer currentUserOrgId;


    /**
     * 当前处理人
     */
    @Column(name = "CURRENT_USER_ID")
    private Integer currentUserId;

    /**
     * 要求处理日期
     */
    @Column(name = "HANDLE_TIME")
    private Date handleTime;


    /**
     * 状态(0新建、1待处理、2待审核、3已关闭、4已挂起)
     */
    @Column(name = "STATUS")
    private String status;

    /**
     * 问题上一步处理人所属组织
     */
    @Column(name = "LAST_USER_ORG_ID")
    private Integer lastUserOrgId;


    /**
     * 问题上一步处理人
     */
    @Column(name = "LAST_USER_ID")
    private Integer lastUserId;

    /**
     * 问题上一步状态(0新建、1待处理、2待审核、3已关闭、4挂起)
     */
    @Column(name = "LAST_STATUS")
    private String lastStatus;


    /**
     * 业务ID
     */
    @Column(name = "biz_id")
    private Integer bizId;

    /**
     * 业务类型
     */
    @Column(name = "biz_type")
    private String bizType;

    //解决日期
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 问题说明
     */
    @Column(name = "REMARK")
    private String remark;

    /**
     * 处理要求
     */
    @Column(name = "HANDLE")
    private String handle;

    /**
     * 创建人所属部门ID
     */
    @Column(name = "CREATOR_ORG_ID")
    private Integer createrOrgId;

    /**
     * 站点/区间，逗号相隔
     */
    @Column(name = "station")
    private String station;

}
