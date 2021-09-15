package com.wisdom.acm.wf.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "wsd_wf_delegate")
public class WfDelegatePo extends BasePo {

    //委托人
    @Column(name = "assignee")
    private Integer assignee;

    //代理人
    @Column(name = "attorney")
    private Integer attorney;

    //开始时间
    @Column(name = "start_time")
    private Date startTime;

    //结束时间
    @Column(name = "end_time")
    private Date endTime;

    //是否全局
    @Column(name = "global")
    private Integer global;

    //是否启用
    @Column(name = "enable")
    private Integer enable;

    //备注
    @Column(name = "remarks")
    private String remarks;

}
