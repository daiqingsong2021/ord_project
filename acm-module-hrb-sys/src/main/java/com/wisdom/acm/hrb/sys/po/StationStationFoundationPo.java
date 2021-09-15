package com.wisdom.acm.hrb.sys.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author zll
 * 2020/10/20/020 10:25
 * Description:<站点区间管理表>
 */
@Data
@Table(name="odr_station_station_foundation")
public class StationStationFoundationPo extends BasePo {
    /**
     * 序号
     */
    @Column(name = "sum")
    private Integer sum;
    /**
     *站点上
     */
    @Column(name = "station_up")
    private String stationUp;
    /**
     *站点下
     */
    @Column(name = "station_down")
    private String stationDown;
    /**
     *上行间距
     */
    @Column(name = "station_up_distance")
    private BigDecimal stationUpDistance;
    /**
     *下行间距
     */
    @Column(name = "station_down_distance")
    private BigDecimal stationDownDistance;
    /**
     * '是否为新线里程（0否，1是）
     */
    @Column(name = "is_new_line")
    private Integer isNewLine;
    /**
     * 间距类型（0正线区域站点，1正线区域站点-辅助线/车辆段站点）
     */
    @Column(name = "distance_type")
    private Integer distanceType;
    /**
     *线路名称（数据字典）
     */
    @Column(name = "line")
    private String line;
}
