package com.wisdom.acm.dc1.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 *月累计线路票卡客运量，按日汇总po
 */
@Table(name = "odr_traffic_line_ticket_monthly")
@Data
public class TrafficLineTicketMonthlyPo extends BasePo {
    @Column(name = "record_time")
    private Date recordTime;
    @Column(name = "line")
    private String line;
    //线路分期
    @Column(name = "line_period")
    private String linePeriod;

    //票卡类型：0：单程票；1：城市通；2：工作票；3：计次纪念票；4：储值票。
    @Column(name = "ticket_type")
    private BigDecimal ticketType;

    //客运量
    @Column(name = "traffic_volume")
    private BigDecimal trafficVolume;

    //总客运量
    @Column(name = "total_traffic_volume")
    private BigDecimal totalTrafficVolume;

    //票卡客运量占比
    @Column(name = "rate")
    private BigDecimal rate;
}
