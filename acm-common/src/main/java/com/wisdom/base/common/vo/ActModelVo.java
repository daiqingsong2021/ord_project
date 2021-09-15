package com.wisdom.base.common.vo;

import lombok.Data;

@Data
public class ActModelVo extends TreeVo{

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
