package com.wisdom.acm.hrb.sys.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author zll
 * 2020/10/20/020 10:17
 * Description:<站点管理信息>
 */
@Data
@Table(name="odr_station_foundation")
public class StationFoundationPo extends BasePo {
    /**
     *站点编号
     */
    @Column(name = "station_code")
    private String stationCode;
    /**
     * 站点名称
     */
    @Column(name = "station_name")
    private String stationName;
    /**
     * 所属线路
     */
    @Column(name = "line")
    private String line;
    /**
     * 上行线站点序号
     */
    @Column(name = "station_num")
    private Integer stationNum;
    /**
     * 站点类型（数字字典）：0正线站点，1辅助线站点，2车辆段站点
     */
    @Column(name = "station_type")
    private Integer stationType;

    /**
     * 关联站点
     */
    @Column(name = "relation_station")
    private String relationStation;
    /**
     * 是否换乘站（0否，1是）
     */
    @Column(name = "is_change_station")
    private Integer isChangeStation;
}
