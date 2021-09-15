package com.wisdom.acm.dc2.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SmsTaskVo {

    //主键
    private Integer id;

    //短信通道（NW-内，WW-外)
    private String sendChannel;

    private String sendChannelStr;

    public String getSendChannelStr() {
        if("NW".equals(sendChannel)){
            sendChannelStr = "内网";
        } else {
            sendChannelStr = "外网";
        }
        return sendChannelStr;
    }

    //线路（'-无，1-1号线，3-3号线)
    private String line;

    //短信内容
    private String messageContent;

    //发送目标类型（GROUP-群组，HAND_CHOOSE-手动)
    private String sendPerson;

    private String sendPersonStr;

    public String getSendPersonStr() {
        if("GROUP".equals(sendPerson)){
            sendPersonStr = "群组";
        } else {
            sendPersonStr = "手动";
        }
        return sendPersonStr;
    }

    //发送目标群组编号拼接
    private String smsTargetGroup;

    //发送时间类型（1-立即，2-定时)
    private Integer sendTimeWay;

    //发送时间
    private Date sendTime;

    //发送方电话号码
    private String smsSendNumber;

    //发送成功数
    private Integer sendNum;
    //接收成功数
    private Integer receiveSuccessNum;

    //发送状态（0-未发送，1-已发送,2-已撤销）
    private Integer smsSendStatus;

    //发送状态中文
    private String smsSendStatusStr;

    public String getSmsSendStatusStr() {
        if(smsSendStatus == 1){
            smsSendStatusStr = "已发送";
        } else if (smsSendStatus == 2){
            smsSendStatusStr = "已撤销";
        } else{
            smsSendStatusStr = "未发送";
        }
        return smsSendStatusStr;
    }
}
