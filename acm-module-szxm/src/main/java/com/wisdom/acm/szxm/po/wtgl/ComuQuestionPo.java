package com.wisdom.acm.szxm.po.wtgl;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "wsd_comu_question")
public class ComuQuestionPo extends BasePo {

    /**
     * 所属项目id
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 标段ID
     */
    @Column(name = "section_id")
    private Integer sectionId;

    /**
     * 所属关联对象id
     */
    @Column(name = "biz_id")
    private Integer bizId;

    /**
     * 所属关联对象类型
     */
    @Column(name = "biz_type")
    private String bizType;

    /**
     * 标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 问题类型
     */
    @Column(name = "type")
    private String type;

    /**
     * 优先级
     */
    @Column(name = "priority")
    private String priority;

    /**
     * 责任部门id
     */
    @Column(name = "org_id")
    private Integer orgId;

    /**
     * 责任人Id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 问题描述
     */
    @Column(name = "REMARK")
    private String remark;

    /**
     * 处理要求
     */
    @Column(name = "HANDLE")
    private String handle;

    /**
     * 要求处理日期
     */
    @Column(name = "HANDLE_TIME")
    private Date handleTime;

    /**
     * 状态('EDIT'  '编制中' ,'APPROVAL' '审批中' ,'RELEASE' '已发布' , 'SOLVE' '已解决'  ,'CLOSED'  '已关闭')
     */
    @Column(name = "STATUS")
    private String status;

    //解决日期
    @Column(name = "end_time")
    private Date endTime;

}
