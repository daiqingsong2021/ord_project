package com.wisdom.acm.base.vo.currency;

import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BaseCurrencyVo {

    /**
     * id
     */
    private Integer id;

    /**
     * 货币名称
     */
    private String currencyName;

    /**
     * 货币代码
     */
    private String currencyCode;

    /**
     * 货币符号
     */
    private String currencySymbol;

    /**
     * 基础货币
     */

    private Integer currencyBase;

    /**
     * 创建人
     */
    private UserVo creator;

    /**
     * 创建时间
     */
    private Date creatTime;

    /**
     * 备注
     */
    private String remark;

}