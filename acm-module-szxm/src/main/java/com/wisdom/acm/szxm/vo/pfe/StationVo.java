package com.wisdom.acm.szxm.vo.pfe;

import com.wisdom.base.common.vo.DictionaryVo;
import lombok.Data;

/**
 * 站点
 */
@Data
public class StationVo {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 项目ID
     */
    private Integer projectId;
    /**
     * 区站名称
     */
    private String name;
    /**
     * 编号
     */
    private String code;
    /**
     * 类型
     */
    private DictionaryVo stationType;

    /**
     * 备注
     */
    private String remark;
}
