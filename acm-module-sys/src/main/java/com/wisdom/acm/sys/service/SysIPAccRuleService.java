package com.wisdom.acm.sys.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.form.SysIPAccRuleAddForm;
import com.wisdom.acm.sys.form.SysIPAccRuleUpdateForm;
import com.wisdom.acm.sys.po.SysIPAccessPo;
import com.wisdom.acm.sys.vo.SysIPAccessVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface SysIPAccRuleService extends CommService<SysIPAccessPo> {

    /**
     * 查询所有访问设置
     * @return
     * @param pageSize
     * @param currentPageNum
     */
    PageInfo<SysIPAccessVo> queryIPAccAll(Integer pageSize, Integer currentPageNum);

    List<SysIPAccessVo> queryIPAccByIsEffect();

    /**
     * 增加设置访问
     * @param sysIPAcc
     * @return
     */
    SysIPAccessPo addIPAccessRule(SysIPAccRuleAddForm sysIPAcc);

    /**
     * 更新设置访问
     * @param sysIPAccRuleUpdateForm
     * @return
     */
    SysIPAccessPo updateIPAccessRule(SysIPAccRuleUpdateForm sysIPAccRuleUpdateForm);

    /**
     * 删除设置访问
     * @param ids
     * @return
     */
    void deleteIPAcc(List<Integer> ids);

    /**
     * 获取一条访问设置Vo
     * @param IPAccId
     * @return
     */
    SysIPAccessVo getIPAcc(Integer IPAccId);

    String queryIpLoggerByIds(List<Integer> ids);
}
