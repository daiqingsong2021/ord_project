package com.wisdom.acm.processing.vo.fault;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class FaultDailyProblemDealVo {

    private Timestamp recordTime;//跟进时间
    private String dealDetail;//跟进描述
}
