package com.wisdom.acm.base.vo.classify;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BaseClassifyAssignVo {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 业务对象
     */
    private String boCode;

    /**
     * 业务对象主键C
     */
    private Integer bizId;

    /**
     * 分类码
     */
    private BaseClassifyTreeVo classifyType;

    /**
     * 码值
     */
    private BaseClassifyTreeVo classify;
}
