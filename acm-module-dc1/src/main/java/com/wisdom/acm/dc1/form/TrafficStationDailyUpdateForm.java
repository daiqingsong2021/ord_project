package com.wisdom.acm.dc1.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zll
 * 2020/8/4/004 9:19
 * Description:<站点信息更新>
 */
@Data
public class TrafficStationDailyUpdateForm {
    @NotNull(message = "主键ID不能为空")
    private Integer id;
    @NotBlank(message = "线路不能为空")
    private String line;
    @NotNull(message = "交易日期不能为空")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date recordTime;
    //线路分期
    private String linePeriod;
    //站
    private String station;

    //进站量
    private BigDecimal arrivalVolume;

    //出站量
    private BigDecimal outboundVolume;

    //进站量+出站量
    private BigDecimal totalVolume;
}
