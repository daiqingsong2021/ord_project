package com.wisdom.acm.hrb.sys.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * @author zll
 * 2020/10/21/021 11:37
 * Description:<区间间距>
 */
@Data
public class StationStationFoundationVo {
    private Integer id;
    /**
     * 序号
     */
    private Integer sum;
    /**
     *站点上
     */
    private String stationUp;
    private String stationUpName;
    /**
     *站点下
     */
    private String stationDown;
    private String stationDownName;
    /**
     *上行间距
     */
    private BigDecimal stationUpDistance=new BigDecimal("0");
    /**
     *下行间距
     */
    private BigDecimal stationDownDistance=new BigDecimal("0");
    /**
     * '是否为新线里程（0否，1是，2不区分）
     */
    private Integer isNewLine;
    private String isNewLineName;
    /**
     * 间距类型（0正线区域站点，1正线区域站点-辅助线/车辆段站点）
     */
    private Integer distanceType;
    private String distanceTypeName;
    /**
     *线路名称（数据字典）
     */
    private String line;
    /**
     *线路名称
     */
    private String lineName;


    //总里程
    private BigDecimal stationMileage=new BigDecimal("0");
    //新线总里程
    private BigDecimal newLineStationMileage=new BigDecimal("0");
    //既有线总里程
    private BigDecimal oldLineStationMileage=new BigDecimal("0");



}
