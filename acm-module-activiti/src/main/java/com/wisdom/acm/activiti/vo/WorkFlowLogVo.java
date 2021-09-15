package com.wisdom.acm.activiti.vo;

import lombok.Data;

import java.util.Date;

@Data
public class WorkFlowLogVo {

    private Integer id;

    private String operate;

    private String wsdCreator;

    private String nextUserId;

    private Date actEndTime;

    private Date procEndTime;



}
