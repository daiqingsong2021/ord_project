package com.wisdom.acm.sys.service;

import com.wisdom.acm.sys.form.SysPwdRuleAddForm;
import com.wisdom.acm.sys.form.SysPwdRuleUpdateForm;
import com.wisdom.acm.sys.po.SysPwdRulePo;
import com.wisdom.acm.sys.vo.SysPwdRuleVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface SysPwdRuleService extends CommService<SysPwdRulePo> {

    /**
     * 查询密码设置
     * @return
     */
    SysPwdRuleVo queryPwdRuleAll();

    /**
     * 增加密码设置
     * @param sysPwdRuleAddForm
     * @return
     */
    SysPwdRulePo insertPwdRule(SysPwdRuleAddForm sysPwdRuleAddForm);

    /**
     * 更新密码设置
     * @param sysPwdRuleUpdateForm
     * @return
     */
    SysPwdRulePo updatePwdRule(SysPwdRuleUpdateForm sysPwdRuleUpdateForm);

    /**
     * 删除密码设置
     * @param ids
     * @return
     */
    void deletePwdRule(List<Integer> ids);

    /**
     * 获取一条密码访问设置Vo
     * @param pwdRuleId
     * @return
     */
    SysPwdRuleVo getPwdRule(Integer pwdRuleId);
}
