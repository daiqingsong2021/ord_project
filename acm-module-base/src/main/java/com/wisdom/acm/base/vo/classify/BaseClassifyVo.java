package com.wisdom.acm.base.vo.classify;

import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

import java.util.List;

@Data
public class BaseClassifyVo{

    // 主键
    private Integer id;
    // 父节点
    private Integer parentId;

    // 业务对象代码
    private String boCode;

    // 分类码
    private String classifyCode;

    // 分类码类型
    private Integer classifyType;

    // 分类码说明
    private String classifyName;

}
