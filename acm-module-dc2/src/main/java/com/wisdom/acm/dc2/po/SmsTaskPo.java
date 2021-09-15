package com.wisdom.acm.dc2.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "odr_sms_task")
public class SmsTaskPo extends BasePo {

    //短信通道（NW-内，WW-外)
    @Column(name = "sms_channel")
    private String sendChannel;

    //线路（'-无，1-1号线，3-3号线)
    @Column(name = "sms_line")
    private String line;

    //短信内容
    @Column(name = "sms_content")
    private String messageContent;

    //发送目标类型（GROUP-群组，HAND_CHOOSE-手动)
    @Column(name = "sms_target_type")
    private String sendPerson;

    //发送目标群组编号拼接
    @Column(name = "sms_target_group")
    private String smsTargetGroup;

    //发送时间类型（1-立即，2-定时)
    @Column(name = "sms_send_type")
    private Integer sendTimeWay;

    //发送时间
    @Column(name = "sms_time")
    private Date sendTime;

    //发送方电话号码
    @Column(name = "sms_send_number")
    private String smsSendNumber;

    //发送状态（0-未发送，1-已发送）
    @Column(name = "sms_send_status")
    private Integer smsSendStatus;
}
