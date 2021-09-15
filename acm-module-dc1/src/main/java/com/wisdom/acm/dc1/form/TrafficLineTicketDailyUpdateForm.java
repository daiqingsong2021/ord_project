package com.wisdom.acm.dc1.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zll
 * 2020/8/4/004 9:28
 * Description:<描述>
 */
@Data
public class TrafficLineTicketDailyUpdateForm {
    @NotNull(message = "主键ID不能为空")
    private Integer id;
    @NotBlank(message = "线路不能为空")
    private String line;
    @NotNull(message = "交易日期不能为空")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date recordTime;
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
