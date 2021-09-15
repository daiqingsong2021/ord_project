package com.wisdom.acm.hrb.sys.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zll
 * 2020/12/1/001 13:35
 * Description:<描述>
 */
@Data
@Table(name="odr_transport_foundation")
public class TransportFoundationPo extends BasePo {
    @Column(name = "line")
    private String line;
    @Column(name = "init_time")
    private Date initTime;
    @Column(name = "month_volume")
    private BigDecimal monthVolume;
    @Column(name = "month_volume_average")
    private BigDecimal monthVolumeAverage;
    @Column(name = "year_volume")
    private BigDecimal yearVolume;
    @Column(name = "year_volume_average")
    private BigDecimal yearVolumeAverage;
    @Column(name = "open_volume")
    private BigDecimal openVolume;
    @Column(name = "open_volume_average")
    private BigDecimal openVolumeAverage;
    @Column(name = "remark")
    private String remark;
}
