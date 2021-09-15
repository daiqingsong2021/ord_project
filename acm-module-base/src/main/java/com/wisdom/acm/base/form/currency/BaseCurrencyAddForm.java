package com.wisdom.acm.base.form.currency;

import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class BaseCurrencyAddForm extends BaseForm {

    @NotBlank(message = "货币代码不能为空")
    private String currencyCode;

    @NotBlank(message = "货币名称不能为空")
    private String currencyName;

    private String currencySymbol;

    public String getLogContent(){
        return "新增货币设置，货币名称："+ this.getCurrencyName();
    }


}