package com.wisdom.base.common.vo.log;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.enums.ParamEnum;
import lombok.Data;

@Data
public class LogContentsVo {

    // 修改值
    private String newValue;

    // 原始值
    private String oldValue;

    // 值类型
    private String type;

    // 标题
    private String title;

    //
    private Object typeValue;
}