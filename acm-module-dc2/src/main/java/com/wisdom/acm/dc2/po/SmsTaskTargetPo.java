package com.wisdom.acm.dc2.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "odr_sms_task_target")
public class SmsTaskTargetPo extends BasePo {


    //odr_sms_task主键id
    @Column(name = "task_id")
    private Integer taskId;

    //目标电话号码
    @Column(name = "target_number")
    private String targetNumber;
}
