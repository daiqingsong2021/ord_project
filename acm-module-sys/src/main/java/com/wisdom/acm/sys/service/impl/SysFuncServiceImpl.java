package com.wisdom.acm.sys.service.impl;

import com.wisdom.acm.sys.form.SysAuthAddForm;
import com.wisdom.acm.sys.form.SysAuthUpdateForm;
import com.wisdom.acm.sys.mapper.FuncMapper;
import com.wisdom.acm.sys.po.SysFuncPo;
import com.wisdom.acm.sys.service.SysFuncService;
import com.wisdom.acm.sys.service.SysRoleAuthService;
import com.wisdom.acm.sys.vo.SysFuncVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.form.BaseForm;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.po.BasePo;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.LogUtil;
import com.wisdom.base.common.vo.log.LogContentsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SysFuncServiceImpl extends BaseService<FuncMapper, SysFuncPo> implements SysFuncService {

    @Autowired
    private SysRoleAuthService sysRoleAuthService;

    /**
     * 修改功能
     *
     * @param sysAuthUpdateForm
     * @return
     */
    @Override
    @AddLog(title = "修改权限配置" ,module = LoggerModuleEnum.SM_MENU)
    public SysFuncVo updateMenuFunc(SysAuthUpdateForm sysAuthUpdateForm) {
        SysFuncPo sysFuncPo = mapper.selectFuncPoById(sysAuthUpdateForm.getId());
        //判断功能代码是否重复
        Example example = new Example(SysFuncPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("funcCode", sysAuthUpdateForm.getFuncCode());
        List<SysFuncPo> list = this.mapper.selectByExample(example);
        if (!ObjectUtils.isEmpty(list)) {
            if (!list.get(0).getId().equals(sysAuthUpdateForm.getId())) {
                throw new BaseException("功能代码不能重复");
            }
        }

        // 添加修改日志
        this.addChangeLogger(sysAuthUpdateForm,sysFuncPo);

        dozerMapper.map(sysAuthUpdateForm, sysFuncPo);
        mapper.updateByPrimaryKey(sysFuncPo);
        SysFuncVo sysFuncVo = mapper.selectFuncVoOneById(sysFuncPo.getId());
        return sysFuncVo;
    }

    /**
     * 增加功能
     *
     * @param sysAddAuthForm
     * @return
     */
    @Override
    public SysFuncVo addMenuFunc(SysAuthAddForm sysAddAuthForm) {
        //判断功能代码是否重复
        Example example = new Example(SysFuncPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("funcCode", sysAddAuthForm.getFuncCode());
        criteria.andEqualTo("menuId", sysAddAuthForm.getMenuId());
        List<SysFuncPo> list = this.mapper.selectByExample(example);
        if (!ObjectUtils.isEmpty(list)) {
            throw new BaseException("功能代码不能重复");
        }
        SysFuncPo sysFuncPo = dozerMapper.map(sysAddAuthForm, SysFuncPo.class);
        super.insert(sysFuncPo);
        SysFuncVo sysFuncVo = mapper.selectFuncVoOneById(sysFuncPo.getId());
        return sysFuncVo;
    }

    /**
     * 删除功能
     *
     * @param funcIds
     */
    @Override
    public void deleteMenuFunc(List<Integer> funcIds) {
        List<SysFuncVo> funcs = mapper.selectFuncByIds(funcIds);
        List<String> funcCodes = ListUtil.toValueList(funcs, "funcCode", String.class);
        //删除权限功能关系
        sysRoleAuthService.deleteRoleAuthByResCode(funcCodes, "func");
        super.deleteByIds(funcIds);
    }

    /**
     * 删除权限配置（根据menuId）
     *
     * @param menuIds
     */
    @Override
    public void deleteMenuFuncByMenuId(List<Integer> menuIds) {
        if (!ObjectUtils.isEmpty(menuIds)) {
            mapper.deleteFuncByMenuIds(menuIds);
        }
    }

    /**
     * 获取功能权限列表
     *
     * @param menuId
     * @return
     */
    @Override
    public List<SysFuncVo> queryFuncVoById(Integer menuId) {
        List<SysFuncVo> list = mapper.selectFuncVoById(menuId);
        return list;
    }

    @Override
    public List<SysFuncVo> getFuncInfo(Integer funcId) {
        List<SysFuncVo> list = mapper.selectFuncVoByFuncId(funcId);
        return list;
    }

    @Override
    public List<SysFuncPo> queryFuncByResCodes(List<String> funcCodes) {
        return selectByFuncCodes(funcCodes);
    }

    @Override public List<SysFuncPo> selectByFuncCodes(List<String> funcCodes)
    {
        List<SysFuncPo> list = new ArrayList<>();
        list= mapper.selectByFuncCodes(funcCodes);
        if(ObjectUtils.isEmpty(list))
            return new ArrayList<>();
        else return  list;
    }
}
