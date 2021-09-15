package com.wisdom.acm.hrb.sys.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zll
 * 2020/10/20/020 13:46
 * Description:<站点信息>
 */
@Data
public class StationFoundationVo {
    private Integer id;
    /**
     *站点编号
     */
    private String stationCode;
    /**
     * 站点名称
     */
    private String stationName;
    /**
     * 所属线路
     */
    private String line;
    /**
     *线路名称
     */
    private String lineName;
    /**
     * 上行线站点序号
     */
    private Integer stationNum;
    /**
     * 站点类型（数字字典）：0正线站点，1辅助线站点，2车辆段站点
     */
    private Integer stationType;
    private String stationTypeName;


    /**
     * 关联站点
     */
    private String relationStationName;
    /**
     * 关联站点
     */
    private String relationStation;
    /**
     * 是否换乘站（0否，1是）
     */
    private Integer isChangeStation;
    private String isChangeStationName;

    /**
     *正线站点关联辅助站点
     */
    private List<StationFoundationVo> stationFoundationRelation =new ArrayList<>();
}
