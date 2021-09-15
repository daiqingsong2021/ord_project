package com.wisdom.acm.base.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "wsd_base_currency")
@Data
public class BaseCurrencyPo extends BasePo {

    @Column(name = "CURRENCY_NAME")
    private String currencyName;

    @Column(name = "CURRENCY_CODE")
    private String currencyCode;

    @Column(name = "CURRENCY_SYMBOL")
    private String currencySymbol;

    @Column(name = "BASE_EXCH_RATE")
    private BigDecimal baseExchRate;

    @Column(name = "CURRENCY_BASE")
    private Integer currencyBase;

    @Column(name = "remark")
    private String remark;

}