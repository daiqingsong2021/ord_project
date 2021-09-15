package com.wisdom.acm.szxm.po.wtgl;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "wsd_comu_quehandle")
public class ComuQueHandlePo extends BasePo {

    /**
     * 问题id
     */
    @Column(name = "question_id")
    private Integer questionId;

    /**
     * 项目id
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 处理时间
     */
    @Column(name = "handle_time")
    private Date handleTime;

    /**
     * 处理人
     */
    @Column(name = "handle_user")
    private String handleUser;

    /**
     * 处理结果说明
     */
    @Column(name = "handle_result")
    private String handleResult;

    /**
     * 处理记录
     */
    @Column(name = "handle_remark")
    private String handleRemark;

    @Column(name = "status")
    private String status;
}
