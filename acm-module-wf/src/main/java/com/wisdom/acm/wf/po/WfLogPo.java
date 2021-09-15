package com.wisdom.acm.wf.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "wsd_wf_log")
public class WfLogPo extends BasePo {

    //内容
    @Column(name = "content_")
    private String content;

    //操作类型
    @Column(name = "opt_type")
    private String optType;

    //待处理人
    @Column(name = "handle_user")
    private String handleUser;

    //节点
    @Column(name = "activity")
    private String activity;

    //流程实例ID
    @Column(name = "proc_inst_id")
    private String procInstId;

    //处理人
    @Column(name = "user_")
    private String user;

    //停留时间
    @Column(name = "stay_time")
    private Date stayTime;

    //开始时间
    @Column(name = "start_time")
    private Date startTime;

    //工作项节点id
    @Column(name = "activity_id")
    private String activityId;

}
