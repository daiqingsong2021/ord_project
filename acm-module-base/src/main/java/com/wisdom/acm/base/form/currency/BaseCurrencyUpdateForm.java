package com.wisdom.acm.base.form.currency;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class BaseCurrencyUpdateForm extends BaseForm {

    @NotNull(message = "货币主键不能为空")
    private Integer id;

    @NotBlank(message = "货币代码不能为空")
    @LogParam(title = "货币代码")
    private String currencyCode;

    @NotBlank(message = "货币名称不能为空")
    @LogParam(title = "货币名称")
    private String currencyName;

    @LogParam(title = "货币符号")
    private String currencySymbol;

    @LogParam(title = "备注")
    private String remark;

}