package com.wisdom.acm.base.vo.classify;

import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

import java.util.List;

@Data
public class BaseClassifyTreeVo extends TreeVo<BaseClassifyTreeVo> {

    /**
     * 业务对象代码
     */
    private String boCode;

    /**
     * 分类码/码值
     */
    private String classifyCode;

    /**
     * 分类码类型
     */
    private Integer classifyType;

    /**
     * 分类码名称
     */
    private String classifyName;

    /**
     * 分类码子节点
     */
    private List<BaseClassifyTreeVo> children;
}
