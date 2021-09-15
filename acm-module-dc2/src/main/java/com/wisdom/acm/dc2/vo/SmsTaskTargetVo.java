package com.wisdom.acm.dc2.vo;

import lombok.Data;

@Data
public class SmsTaskTargetVo {

    //主键id
    private Integer id;

    //任务id
    private Integer taskId;

    //目标电话号码
    private String targetNumber;
}
