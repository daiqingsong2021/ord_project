package com.wisdom.acm.sys.service;

import com.wisdom.acm.sys.form.SysMenuAddForm;
import com.wisdom.acm.sys.form.SysMenuUpdateForm;
import com.wisdom.acm.sys.form.SysSearchMenuForm;
import com.wisdom.acm.sys.po.SysMenuPo;
import com.wisdom.acm.sys.vo.SysAuthMenuVo;
import com.wisdom.acm.sys.vo.SysMenuLableVo;
import com.wisdom.acm.sys.vo.SysMenuVo;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.SelectVo;

import java.util.List;
import java.util.Map;

public interface SysMenuService extends CommService<SysMenuPo> {
    /**
     * 查询menu名称  for activiti
     * @param menuCode
     */
    String queryMenuNameByCode(String menuCode);

    /**
     * 获取菜单列表
     * @return
     */
    public List<SysMenuVo> queryMenuAll();

    /**
     * 查询主观评分模板 分组
     * @return
     */
    List<SysMenuVo> selectSubjectTemplateGroup();

    /**
     * 查询主观评分模板 业务模块
     * @return
     */
    List<SysMenuVo> selectSubjectTemplateMenu(String menuCode);

    List<SelectVo> queryMenu(Integer menuId);


    SelectVo selectMenuById(Integer menuId);

    List<SysAuthMenuVo> selectParentMenuByMenuId(Integer menuId);


    List<SelectVo> queryMenuByIds(List<Integer> menuIds);

    /**
     * 增加菜单
     * @param menu
     */
    public SysMenuPo addMenu( SysMenuAddForm menu);

    /**
     * 修改菜单信息
     * @param menu
     */
    public SysMenuPo updateMenu(SysMenuUpdateForm menu);

    /**
     * 删除菜单
     * @param menuIds
     */
    public void deleteMenu(List<Integer> menuIds);

    /**
     * 获取菜单MenuVo
     * @param menuId
     * @return
     */
    SysMenuVo getMenuVoById(Integer menuId);

    SysMenuVo getMenuVoByCode(String menuCode);

    /**
     * 获取菜单列表
     * @param searchMap
     * @return
     */
    List<SysMenuVo> queryMenuTreeBySearch(SysSearchMenuForm searchMap);


    /**
     * 获取菜单页签
     * @param map
     * @return
     */
    Map<String, List<SysMenuLableVo>> queryMenuTabs(String map);

    /**
     * 获取菜单清单
     * @return
     */
    List<SysAuthMenuVo> queryAuthMenuVoList();

    /**
     * 根据菜单code获取菜单
     * @param list
     * @return
     */
    List<SysMenuVo> selectMenuVoByMenuCodes(List<String> list);

    String queryMenuPos(List<Integer> ids);
}
