package com.wisdom.base.common.dc.vo.base;

import lombok.Data;

/**
 * @author zll
 * 2020/8/18/018 15:15
 * Description:<描述>
 */
@Data
public class BaseBoVo {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 业务对象代码
     */
    private String boCode;

    /**
     * 业务对象名称
     */
    private String boName;
}
