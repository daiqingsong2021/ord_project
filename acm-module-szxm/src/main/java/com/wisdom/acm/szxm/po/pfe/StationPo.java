package com.wisdom.acm.szxm.po.pfe;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 站点
 */
@Table(name = "wsd_pfe_station")
@Data
public class StationPo extends BasePo {

    /**
     * 项目ID
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 区站名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 站点代码
     */
    @Column(name = "code")
    private String code;

    /**
     * 类型
     */
    @Column(name = "station_type")
    private String stationType;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String  remark;

    /**
     * 站点状态
     */
    @Column(name = "status")
    private String status;
}
