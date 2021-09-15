package com.wisdom.acm.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.form.SysIPAccRuleAddForm;
import com.wisdom.acm.sys.form.SysIPAccRuleUpdateForm;
import com.wisdom.acm.sys.mapper.IPAccRuleMapper;
import com.wisdom.acm.sys.po.SysIPAccessPo;
import com.wisdom.acm.sys.service.SysIPAccRuleService;
import com.wisdom.acm.sys.vo.SysIPAccessVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


import java.util.List;

@Service
public class SysIPAccRuleServiceImpl extends BaseService<IPAccRuleMapper, SysIPAccessPo> implements SysIPAccRuleService {

    /**
     * 查询所有访问设置
     * @return
     * @param pageSize
     * @param currentPageNum
     */
    @Override
    public PageInfo<SysIPAccessVo> queryIPAccAll(Integer pageSize, Integer currentPageNum) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<SysIPAccessVo> list = mapper.selectIPAccessAll();
        PageInfo<SysIPAccessVo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public List<SysIPAccessVo> queryIPAccByIsEffect() {
        List<SysIPAccessVo> list = mapper.selectIPAccByIsEffect();
        return  list;
    }

    /**
     * 增加设置访问
     * @param sysIPAcc
     * @return
     */
    @Override
    public SysIPAccessPo addIPAccessRule(SysIPAccRuleAddForm sysIPAcc) {
        //判断ip区间是否重复
        List<SysIPAccessVo> list = mapper.selectIPAccessByStartIpAndEndIp(sysIPAcc.getStartIP(),sysIPAcc.getEndIP());
        if(!ObjectUtils.isEmpty(list)){
            throw new BaseException("ip规则已存在");
        }
        SysIPAccessPo sysIPAccessPo = dozerMapper.map(sysIPAcc,SysIPAccessPo.class);
        sysIPAccessPo.setSort(this.selectNextSort());
        super.insert(sysIPAccessPo);
        return sysIPAccessPo;
    }

    /**
     * 更新设置访问
     * @param sysIPAccRuleUpdateForm
     * @return
     */
    @Override
    @AddLog(title = "修改IP规则" ,module = LoggerModuleEnum.SM_TMM)
    public SysIPAccessPo updateIPAccessRule(SysIPAccRuleUpdateForm sysIPAccRuleUpdateForm) {
        SysIPAccessPo sysIPAccessPo = mapper.selectByPrimaryKey(sysIPAccRuleUpdateForm.getId());
        //判断ip区间是否重复
        List<SysIPAccessVo> list = mapper.selectIPAccessByStartIpAndEndIp(sysIPAccRuleUpdateForm.getStartIP(),sysIPAccRuleUpdateForm.getEndIP());
        if(!ObjectUtils.isEmpty(list) && !list.get(0).getId().equals(sysIPAccRuleUpdateForm.getId())){
            throw new BaseException("ip规则已存在");
        }
        // 添加修改日志
        this.addChangeLogger(sysIPAccRuleUpdateForm,sysIPAccessPo);
        dozerMapper.map(sysIPAccRuleUpdateForm,sysIPAccessPo);
        super.updateSelectiveById(sysIPAccessPo);
        return sysIPAccessPo;
    }

    /**
     * 删除设置访问
     * @param ids
     * @return
     */
    @Override
    public void deleteIPAcc(List<Integer> ids) {
        this.deleteByIds(ids);
    }

    /**
     * 查询一条视图
     * @param IPAccId
     * @return
     */
    @Override
    public SysIPAccessVo getIPAcc(Integer IPAccId) {
        SysIPAccessVo IPAccessVo =  mapper.getOne(IPAccId);
        return IPAccessVo;
    }

    @Override
    public String queryIpLoggerByIds(List<Integer> ids) {
        List<SysIPAccessPo> list = super.selectByIds(ids);
        StringBuffer ret = new StringBuffer();
        if (!ObjectUtils.isEmpty(list)) {
            for (SysIPAccessPo sysIPAccessPo : list) {
                ret.append("起始IP:" + sysIPAccessPo.getStartIP() +",结束IP:" + sysIPAccessPo.getEndIP() +";");
            }
        }
        return ret.toString();
    }
}
