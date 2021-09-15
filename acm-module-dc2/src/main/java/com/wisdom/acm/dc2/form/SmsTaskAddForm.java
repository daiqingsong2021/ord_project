package com.wisdom.acm.dc2.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.acm.dc2.po.SmsTaskTargetPo;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class SmsTaskAddForm {

    //短信通道（NW-内，WW-外)
    @NotBlank(message = "短信通道为空")
    private String sendChannel;

    //线路（'-无，1-1号线，3-3号线)
    private String line;

    //短信内容
    @NotBlank(message = "短信内容为空")
    private String messageContent;

    //发送目标类型（GROUP-群组，HAND_CHOOSE-手动)
    @NotBlank(message = "发送目标类型为空")
    private String sendPerson;

    //发送目标群组编号拼接
    private String smsTargetGroup;

    //发送时间类型（1-立即，2-定时)
    @NotNull(message = "发送时间类型为空")
    private Integer sendTimeWay;

    //发送时间
    @NotNull(message = "发送时间")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    //发送方电话号码
    private String smsSendNumber;

    List<SmsTaskTargetPo> mobiles;
}
