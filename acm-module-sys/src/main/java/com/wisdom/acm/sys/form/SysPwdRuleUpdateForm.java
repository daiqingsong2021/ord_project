package com.wisdom.acm.sys.form;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class SysPwdRuleUpdateForm extends BaseForm {

    private Integer id;

    /**
     * 修改周期
     */
    @LogParam(title = "修改周期")
    private Integer cycle;

    /**
     * 密码长度
     */
    @LogParam(title = "密码长度")
    private Integer length;

    /**
     * 错误次数
     */
    @LogParam(title = "错误次数")
    private Integer errorNumber;

    /**
     * 锁定时长（分钟）
     */
    @LogParam(title = "锁定时长")
    private Integer lockTime;
}
