package com.wisdom.acm.base.vo.custom;

import com.wisdom.base.common.vo.CustomValueVo;
import lombok.Data;

@Data
public class BaseCustomValueVo extends BaseCustomFieldVo {

    /**
     * 自定义值
     */
    private CustomValueVo customValue;
}
