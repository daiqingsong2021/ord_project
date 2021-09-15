package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.po.SysPwdRulePo;
import com.wisdom.acm.sys.vo.SysPwdRuleVo;
import com.wisdom.base.common.mapper.CommMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PwdRuleMapper extends CommMapper<SysPwdRulePo> {

    /**
     * 查询所有访问设置
     * @return
     */
    List<SysPwdRuleVo> selectPwdRuleAll();

    /**
     * 查询一条视图
     * @param pwdRuleId
     * @return
     */
    SysPwdRuleVo getOne(Integer pwdRuleId);
}
