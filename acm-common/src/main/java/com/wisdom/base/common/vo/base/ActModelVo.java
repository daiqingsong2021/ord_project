package com.wisdom.base.common.vo.base;

import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

@Data
public class ActModelVo extends TreeVo {

    /**
     * 模版名称
     */
    private String name;

    /**
     * 代码
     */
    private String key;

    /**
     * 类型
     */
    private String type;

    /**
     * 发布状态
     */
    private String status;

    private String metaInfo;
}
