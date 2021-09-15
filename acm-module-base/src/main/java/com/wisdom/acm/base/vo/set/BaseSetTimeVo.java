package com.wisdom.acm.base.vo.set;

import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

@Data
public class BaseSetTimeVo {
    // 工时单位
    private GeneralVo timeUnit;
    // 工期单位
    private GeneralVo drtnUnit;
    // 时间格式
    private GeneralVo dateFormat;
    // 客运返回运行天数
    private GeneralVo trainFormat;
    //保留位数
    private int precision = 3;
}
