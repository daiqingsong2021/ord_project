package com.wisdom.acm.processing.vo.fault;

import lombok.Data;

@Data
public class FaultDailyProblemVo {
    private Integer id;//编号
    private String problemDesc; //问题描述
    private String problemStatus;//问题状态
    private String problemReason;//问题原因
    private String problemRemark;//问题备注
}
