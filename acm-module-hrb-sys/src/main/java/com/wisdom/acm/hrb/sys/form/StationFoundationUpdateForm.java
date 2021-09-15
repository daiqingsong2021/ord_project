package com.wisdom.acm.hrb.sys.form;

import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

/**
 * @author zll
 * 2020/10/20/020 11:13
 * Description:<站点管理信息>
 */
@Data
public class StationFoundationUpdateForm extends BaseForm {
    /**
     * 主键ID
     */
    private String id;
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
     * 上行线站点序号
     */
    private Integer stationNum;
    /**
     * 站点类型（数字字典）：0正线站点，1辅助线站点，2车辆段站点
     */
    private Integer stationType;
    /**
     * 关联站点
     */
    private String relationStation;
    /**
     * 是否换乘站（0否，1是）
     */
    private Integer isChangeStation;
}
