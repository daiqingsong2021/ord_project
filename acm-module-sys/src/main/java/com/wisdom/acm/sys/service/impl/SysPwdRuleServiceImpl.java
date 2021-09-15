package com.wisdom.acm.sys.service.impl;

import com.wisdom.acm.sys.form.SysPwdRuleAddForm;
import com.wisdom.acm.sys.form.SysPwdRuleUpdateForm;
import com.wisdom.acm.sys.mapper.PwdRuleMapper;
import com.wisdom.acm.sys.po.SysPwdRulePo;
import com.wisdom.acm.sys.service.SysPwdRuleService;
import com.wisdom.acm.sys.vo.SysPwdRuleVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.LogUtil;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class SysPwdRuleServiceImpl extends BaseService<PwdRuleMapper, SysPwdRulePo> implements SysPwdRuleService {
    @Autowired
    private Mapper dozerMapper;

    /**
     * 查询所有密码设置
     * @return
     */
    @Override
    public SysPwdRuleVo queryPwdRuleAll() {
        List<SysPwdRuleVo> list = mapper.selectPwdRuleAll();
        return ObjectUtils.isEmpty(list) ? null : list.get(0);
    }

    /**
     * 增加密码设置
     * @param sysPwdRuleAddForm
     * @return
     */
    @Override
    public SysPwdRulePo insertPwdRule(SysPwdRuleAddForm sysPwdRuleAddForm) {
        SysPwdRulePo bean = dozerMapper.map(sysPwdRuleAddForm,SysPwdRulePo.class);
        super.insert(bean);
        return bean;
    }

    /**
     * 更新密码设置
     * @param sysPwdRuleUpdateForm
     * @return
     */
    @Override
    @AddLog(title = "修改密码设置" ,module = LoggerModuleEnum.SM_TMM)
    public SysPwdRulePo updatePwdRule(SysPwdRuleUpdateForm sysPwdRuleUpdateForm) {
        SysPwdRulePo sysPwdRulePo = mapper.selectByPrimaryKey(sysPwdRuleUpdateForm.getId());
        if(sysPwdRulePo == null){
            this.addNewLogger(sysPwdRuleUpdateForm);
            sysPwdRulePo = dozerMapper.map(sysPwdRuleUpdateForm,SysPwdRulePo.class);
            this.insert(sysPwdRulePo);
            // 添加修改日志
        }else{
            // 添加修改日志
            this.addChangeLogger(sysPwdRuleUpdateForm,sysPwdRulePo);
            dozerMapper.map(sysPwdRuleUpdateForm,sysPwdRulePo);
            super.updateSelectiveById(sysPwdRulePo);
        }
        return sysPwdRulePo;
    }

    /**
     * 删除密码设置
     * @param ids
     * @return
     */
    @Override
    public void deletePwdRule(List<Integer> ids) {
      this.deleteByIds(ids);
    }

    /**
     * 查询一条视图
     * @param pwdRuleId
     * @return
     */
    @Override
    public SysPwdRuleVo getPwdRule(Integer pwdRuleId) {
        SysPwdRuleVo pwdRuleVo = mapper.getOne(pwdRuleId);
        return pwdRuleVo;
    }
}
