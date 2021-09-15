package com.wisdom.acm.hrb.sys.form;

import com.wisdom.base.common.form.BaseForm;
import lombok.Data;
import java.math.BigDecimal;

/**
 * @author zll
 * 2020/10/20/020 11:14
 * Description:<站点区间>
 */
@Data
public class StationStationFoundationAddForm extends BaseForm {
    /**
     * 序号
     */
    private Integer sum;
    /**
     *线路名称（数据字典）
     */
    private String line;
    /**
     *站点上
     */
    private String stationUp;
    /**
     *站点下
     */
    private String stationDown;
    /**
     *上行间距
     */
    private BigDecimal stationUpDistance;
    /**
     *下行间距
     */
    private BigDecimal stationDownDistance;
    /**
     * '是否为新线里程（0否，1是）
     */
    private Integer isNewLine;
    /**
     * 间距类型（0正线区域站点，1正线区域站点-辅助线/车辆段站点）
     */
    private Integer distanceType;
}
