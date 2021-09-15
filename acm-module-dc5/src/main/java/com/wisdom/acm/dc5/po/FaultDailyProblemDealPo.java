package com.wisdom.acm.dc5.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "odr_fault_daily_problem_deal")
@Data
public class FaultDailyProblemDealPo extends BasePo {

    /**
     *问题关联关系 odr_fault_daily_problem表的主键
     */
    @Column(name = "problem_id")
    private Integer problemId;

    /**
     * 记录日期
     */
    @Column(name = "record_time")
    private Date recordTime;

    /**
     * 处理详情
     */
    @Column(name = "deal_detail")
    private String dealDetail;

}
