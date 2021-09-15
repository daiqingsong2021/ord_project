package com.wisdom.acm.dc2.po.message;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import com.wisdom.base.common.po.BasePo;

import lombok.Data;


@Table(name = "odr_sms_history")
@Data
public class SmsHistoryDetailsPo extends BasePo
{

    
    /**
     *odr_sms_task主键id
     */
    @Column(name = "task_id")
    private Integer taskId;

    /**
     *发送号码
     */
    @Column(name = "sms_send_number")
    private String smsSendNumber;
    
    /**
     *接收号码
     */
    @Column(name = "target_number")
    private String targetNumber;
    
    /**
     * 发送状态
     */
    @Column(name = "send_status")
    private String sendStatus;

    /**
     * 接收状态
     */
    @Column(name = "receive_status")
    private String receiveStatus;
    
    /**
     * 发送时间
     */
    @Column(name = "sms_send_time")
    private Date smsSendTime;

}
