package com.wisdom.acm.sys.form;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;


@Data
public class SysIPAccRuleUpdateForm extends BaseForm {

    private Integer id;

    /**
     * 开始ip
     */
    @LogParam(title = "开始ip")
    private String startIP;

    /**
     * 结束ip
     */
    @LogParam(title = "结束ip")
    private String endIP;

    /**
     * 规则设置
     */
    @LogParam(title = "规则设置")
    private Integer accessRule;

    @LogParam(title = "是否生效")
    private Integer isEffect;

    /**
     * 备注
     */
    @LogParam(title = "备注")
    private String remark;
}
