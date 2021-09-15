package com.wisdom.acm.dc1.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zll
 * 2020/7/30/030 21:43
 * Description:<>
 */
@Data
public class TrafficLineTicketDailyVo {
    private Integer id;
    private Integer sortNum;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date creatTime;
    private Integer creator;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date recordTime;
    //'几号线'
    private String line;
    //'几期'
    private String linePeriod;

    /**
     * 也可以是月的，也可以是日的
     */
    //'票卡类型：0：单程票；1：城市通；2：工作票；3：计次纪念票；4：储值票；5：刷脸进站'
    private GeneralVo ticketTypeVo=new GeneralVo();
    private BigDecimal ticketType;

    //'客运量（人次）'
    private BigDecimal trafficVolume;

    //'总客运量（人次）'
    private BigDecimal totalTrafficVolume;

    //'票卡客运量占比'
    private BigDecimal rate;

    private String bl;
    //星期几
    private String week;
    //'线路'
    private GeneralVo lineVo=new GeneralVo();
}
