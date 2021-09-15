package com.wisdom.acm.dc1.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *月累计线路票卡客运量，按日汇总vo
 */
@Data
public class TrafficLineTicketMonthlyVo{
    private Integer id;
    private Integer sortNum;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date creatTime;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date recordTime;
    private String line;
    //线路分期
    private String linePeriod;

    //票卡类型：0：单程票；1：城市通；2：工作票；3：计次纪念票；4：储值票。
    private BigDecimal ticketType;

    //客运量
    private BigDecimal trafficVolume;

    //总客运量
    private BigDecimal totalTrafficVolume;

    //票卡客运量占比
    private BigDecimal rate;
}
