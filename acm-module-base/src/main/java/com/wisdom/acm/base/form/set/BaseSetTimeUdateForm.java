package com.wisdom.acm.base.form.set;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.enums.ParamEnum;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

@Data
public class BaseSetTimeUdateForm extends BaseForm {
    // 工时单位
    @LogParam(title = "工时单位",type = ParamEnum.DICT, dictType = "plan.project.timeunit")
    private String timeUnit;
    // 工期单位
    @LogParam(title = "工期单位",type = ParamEnum.DICT, dictType = "plan.project.drtnunit")
    private String drtnUnit;
    // 时间格式
    @LogParam(title = "日期格式",type = ParamEnum.DICT, dictType = "base.date.formate")
    private String dateFormat;
}
