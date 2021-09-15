package com.wisdom.acm.sys.service;

import com.wisdom.acm.sys.form.SysAuthAddForm;
import com.wisdom.acm.sys.form.SysAuthUpdateForm;
import com.wisdom.acm.sys.po.SysFuncPo;
import com.wisdom.acm.sys.vo.SysFuncVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface SysFuncService extends CommService<SysFuncPo> {

    /**
     * 修改权限配置
     * @param sysAuthAddForm
     * @return
     */
    SysFuncVo updateMenuFunc(SysAuthUpdateForm sysAuthAddForm);

    /**
     * 获取权限配置
     * @param menuId
     * @return
     */
    List<SysFuncVo> queryFuncVoById(Integer menuId);


    /**
     * 增加权限配置
     * @param sysAuthAddForm
     * @return
     */
    SysFuncVo addMenuFunc(SysAuthAddForm sysAuthAddForm);

    /**
     * 删除权限配置(根据funcId)
     * @param funcIds
     */
    void deleteMenuFunc(List<Integer> funcIds);

    /**
     * 删除权限配置（根据menuId）
     * @param menuIds
     */
    void deleteMenuFuncByMenuId(List<Integer> menuIds);

    List<SysFuncVo> getFuncInfo(Integer funcId);

    List<SysFuncPo> queryFuncByResCodes(List<String> funcCodes);

    /**
     * 查询FuncCodes查询
     * @param funcCodes
     */
    List<SysFuncPo> selectByFuncCodes(List<String> funcCodes);
}
