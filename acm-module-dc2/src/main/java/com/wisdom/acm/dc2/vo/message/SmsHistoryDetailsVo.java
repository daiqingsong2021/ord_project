package com.wisdom.acm.dc2.vo.message;

import java.util.Date;


import lombok.Data;


@Data
public class SmsHistoryDetailsVo
{

    /**
     *主键
     */
    private Integer id;
    
    /**
     *odr_sms_task主键id
     */
    private Integer taskId;

    /**
     *发送号码
     */
    private String smsSendNumber;
    
    /**
     *接收号码
     */
    private String targetNumber;
    
    /**
     * 发送状态
     */
    private String sendStatus;
    
    private String sendStatusStr;
    
    public String getSendStatusStr() {
        if("0".equals(sendStatus)){
        	sendStatusStr = "失败";
        } else if("1".equals(sendStatus)){
        	sendStatusStr = "成功";
        }else {
        	sendStatusStr = "未知";
        }
        return sendStatusStr;
    }

    /**
     * 接收状态
     */
    private String receiveStatus;
    
    private String receiveStatusStr;
    
    public String getReceiveStatusStr() {
        if("0".equals(receiveStatus)){
        	receiveStatusStr = "失败";
        } else if("1".equals(receiveStatus)){
        	receiveStatusStr = "成功";
        }else {
        	receiveStatusStr = "未知";
        }
        return receiveStatusStr;
    }
    
    /**
     * 发送时间
     */
    private Date smsSendTime;

}
