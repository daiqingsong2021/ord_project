package com.wisdom.acm.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.wisdom.acm.sys.form.SysFavoritesDeleteForm;
import com.wisdom.acm.sys.form.SysMenuAddForm;
import com.wisdom.acm.sys.form.SysMenuUpdateForm;
import com.wisdom.acm.sys.form.SysSearchMenuForm;
import com.wisdom.acm.sys.mapper.MenuMapper;
import com.wisdom.acm.sys.po.SysMenuPo;
import com.wisdom.acm.sys.service.SysFavoritesService;
import com.wisdom.acm.sys.service.SysFuncService;
import com.wisdom.acm.sys.service.SysMenuService;
import com.wisdom.acm.sys.service.SysRoleAuthService;
import com.wisdom.acm.sys.vo.SysAuthMenuVo;
import com.wisdom.acm.sys.vo.SysMenuGroupVo;
import com.wisdom.acm.sys.vo.SysMenuLableVo;
import com.wisdom.acm.sys.vo.SysMenuVo;
import com.wisdom.acm.sys.vo.TmmMenusList;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.LogUtil;
import com.wisdom.base.common.util.TreeUtil;
import com.wisdom.base.common.vo.SelectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysMenuServiceImpl extends BaseService<MenuMapper, SysMenuPo> implements SysMenuService {

    @Autowired
    private SysRoleAuthService sysRoleAuthService;

    @Autowired
    private SysFuncService sysFuncService;

    @Autowired
    private SysFavoritesService sysFavoritesService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TmmMenusList tmmMenusList;

    /**
     * 查询menu名称  for activiti
     * @param menuCode
     */
    @Override
    public String queryMenuNameByCode(String menuCode) {
        return mapper.queryMenuNameByCode(menuCode);
    }

    /**
     * 获取菜单列表
     *
     * @return
     */
    //@Cache(key = "acm:menu:userId{1}")
//    @Cache(key = "acm:menus:all")
    @Override
    public List<SysMenuVo> queryMenuAll() {
        List<SysMenuVo> menuList = mapper.selectMenuVoList();
        //
        List<SysMenuVo> list = this.judgeSystemOrBuiltIn(menuList);
        List<SysMenuVo> menuTree = TreeUtil.bulid(list, 0);
        return menuTree;
    }

    @Override
    public List<SysMenuVo> selectSubjectTemplateGroup() {
        return mapper.selectSubjectTemplateGroup();
    }

    @Override
    public List<SysMenuVo> selectSubjectTemplateMenu(String menuCode) {
        return mapper.selectSubjectTemplateMenu(menuCode);
    }

    @Override
    public List<SelectVo> queryMenu(Integer menuId) {
        PageHelper.orderBy("sort_num asc");
        List<SysMenuVo> menuList = new ArrayList<>();
        if(ObjectUtils.isEmpty(menuId) || menuId.intValue() == 0){
            menuList  = mapper.selectMenuVoMenuList();
        }else{
            menuList = mapper.selectMenuVoMenuListByMenuId(menuId);
        }
        List<SysMenuVo> list = this.removeSystemMenus(menuList);
        List<SelectVo> retList = new ArrayList<>();
        for (SysMenuVo sysMenuVo : list) {
            SelectVo selectVo = new SelectVo();
            selectVo.setId(sysMenuVo.getId());
            selectVo.setParentId(sysMenuVo.getParentId());
            selectVo.setTitle(sysMenuVo.getMenuName());
            selectVo.setValue(sysMenuVo.getId());
            retList.add(selectVo);
        }
        if(retList.size() > 1){
            retList = TreeUtil.bulid(retList, 0);
        }
        return retList;
    }

    @Override
    public SelectVo selectMenuById(Integer menuId) {
        SysMenuPo sysMenuPo = this.selectById(menuId);

        SelectVo selectVo = new SelectVo();
        selectVo.setValue(menuId);
        selectVo.setTitle(sysMenuPo.getMenuName());
        return selectVo;
    }

    /**
     * 递归获取父节点
     * @param menuId
     * @return
     */
    @Override
    public List<SysAuthMenuVo> selectParentMenuByMenuId(Integer menuId) {
        return mapper.selectParentMenuByMenuId(menuId);
    }

    @Override
    public List<SelectVo> queryMenuByIds(List<Integer> menuIds){
        List<SysMenuPo> sysMenuPoList = this.selectByIds(menuIds);

        List<SelectVo> selectVoList = new ArrayList<>();
        if(!ObjectUtils.isEmpty(sysMenuPoList)){
            for (SysMenuPo sysMenuPo : sysMenuPoList) {

                SelectVo selectVo = new SelectVo();
                selectVo.setValue(sysMenuPo.getId());
                selectVo.setTitle(sysMenuPo.getMenuName());

                selectVoList.add(selectVo);
            }

        }
        return selectVoList;
    }
    /**
     * 判断菜单集合中哪些是内置，哪些是系统
     */
    public List<SysMenuVo> judgeSystemOrBuiltIn(List<SysMenuVo> menuList){
        //获取所有的系统菜单
        List<String> systemMenus = tmmMenusList.getSystem();
        //获取所有的内置菜单
        List<String> builtInMenus = tmmMenusList.getBuilt_in();
        //逐个判断并赋值
        for (SysMenuVo sysMenuVo : menuList){
            if (systemMenus.contains(sysMenuVo.getMenuCode())){
                sysMenuVo.setSystem(0);
            }else{
                sysMenuVo.setSystem(1);
            }
            if (builtInMenus.contains(sysMenuVo.getMenuCode())){
                sysMenuVo.setBuilt_in(0);
            }else{
                sysMenuVo.setBuilt_in(1);
            }
        }
        return menuList;
    }

    /**
     * 去掉系统菜单
     * @param menuList
     * @return
     */
    public List<SysMenuVo> removeSystemMenus(List<SysMenuVo> menuList){
        List<SysMenuVo> returnList = new ArrayList<>();

        //获取所有的系统菜单
        List<String> systemMenus = tmmMenusList.getSystem();
        //获取所有的内置菜单
        List<String> builtInMenus = tmmMenusList.getBuilt_in();
        for (SysMenuVo sysMenuVo : menuList){
            if (systemMenus.contains(sysMenuVo.getMenuCode())){
                continue;
            }
            if (builtInMenus.contains(sysMenuVo.getMenuCode())){
                sysMenuVo.setBuilt_in(0);
            }else{
                sysMenuVo.setBuilt_in(1);
            }
            returnList.add(sysMenuVo);
        }
        return returnList;
    }

    /**
     * 查询菜单信息
     *
     * @param menuId
     * @return
     */
    @Override
    public SysMenuVo getMenuVoById(Integer menuId) {
        SysMenuVo sysMenuVo = mapper.selectMenuVoById(menuId);
        return sysMenuVo;
    }

    /**
     * 查询菜单信息
     *
     * @param menuCode
     * @return
     */
    @Override
    public SysMenuVo getMenuVoByCode(String menuCode) {
        SysMenuVo sysMenuVo = mapper.selectMenuVoByCode(menuCode);
        return sysMenuVo;
    }

    /**
     * 搜索菜单列表
     *
     * @param searchMap
     * @return
     */
    @Override
    public List<SysMenuVo> queryMenuTreeBySearch(SysSearchMenuForm searchMap) {
        List<SysMenuVo> menuTree = this.queryMenuAll();
        if (searchMap.getSearcher() == null || searchMap.getSearcher() == "") {
            return menuTree;
        } else {
            List<Integer> retIds = mapper.selectMenuBySearch(searchMap);
            boolean auth = false;
            List<SysMenuVo> retMemuTree = this.querySearchMenuTree(retIds, menuTree, auth, null);
            return retMemuTree;
        }
    }

    /**
     * 增加菜单
     *
     * @param menu
     */
    @Override
    public SysMenuPo addMenu(SysMenuAddForm menu) {

        //判断菜单代码是否重复
        List<SysMenuPo> list = this.getMenuPoByCode(menu.getMenuCode(), menu.getParentId());
        if (!ObjectUtils.isEmpty(list)) {
            throw new BaseException("菜单简码不能重复");
        }
        if (ObjectUtils.isEmpty(menu.getSort())) {
            menu.setSort(this.getNextSortByParentId(menu.getParentId()));
        }
        SysMenuPo sysMenuPo = dozerMapper.map(menu, SysMenuPo.class);
        sysMenuPo.setGroup(1);
        super.insert(sysMenuPo);
        return sysMenuPo;
    }

    public int getNextSortByParentId(Integer parentId) {
        Example example = new Example(SysMenuPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId", parentId);
        return this.selectCountByExample(example);
    }

    /**
     * 修改菜单信息
     *
     * @param sysMenuUpdateForm
     */
    @Override
    @AddLog(title = "修改菜单", module = LoggerModuleEnum.SM_MENU)
    public SysMenuPo updateMenu(SysMenuUpdateForm sysMenuUpdateForm) {

        //判断修改的数据是否存在
        SysMenuPo sysMenuPo = this.selectById(sysMenuUpdateForm.getId());

        if (ObjectUtils.isEmpty(sysMenuPo)) {
            throw new BaseException("该菜单不存在");
        }
        // 添加修改日志
        this.addChangeLogger(sysMenuUpdateForm,sysMenuPo);
        //判断菜单代码是否重复
        List<SysMenuPo> list = this.getMenuPoByCode(sysMenuUpdateForm.getMenuCode(), sysMenuUpdateForm.getParentId());
        if (!ObjectUtils.isEmpty(list) && !list.get(0).getId().equals(sysMenuUpdateForm.getId())) {
            throw new BaseException("菜单简码不能重复");
        }

        dozerMapper.map(sysMenuUpdateForm, sysMenuPo);
        super.updateSelectiveById(sysMenuPo);
        return sysMenuPo;
    }

    private void addMenuUpdateLog(SysMenuUpdateForm sysMenuUpdateForm, SysMenuPo sysMenuPo) {

        StringBuffer logstr = new StringBuffer();
        if(sysMenuPo != null){
            LogUtil.getEditLog("菜单名称",sysMenuPo.getMenuName(),sysMenuUpdateForm.getMenuName());
        }
    }

    public List<SysMenuPo> getMenuPoByCode(String code, Integer parentId) {
        Example example = new Example(SysMenuPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("menuCode", code);
        criteria.andEqualTo("parentId", parentId);
        List<SysMenuPo> list = this.mapper.selectByExample(example);
        return ObjectUtils.isEmpty(list) ? null : list;
    }

    public List<SysMenuPo> getMenuByName(List<String> menuNames) {
        Example example = new Example(SysMenuPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("menuName", menuNames);
        List<SysMenuPo> list = this.mapper.selectByExample(example);
        return ObjectUtils.isEmpty(list) ? null : list;
    }


    /**
     * 删除菜单
     *
     * @param menuIds
     */
    @Override
    public void deleteMenu(List<Integer> menuIds) {
        List<String> menuCodes = ListUtil.toValueList(this.queryChildrenAndMePos(menuIds), "menuCode", String.class);
        // 当前登陆用户
        Integer userId = Integer.valueOf(request.getHeader("userId"));
        SysFavoritesDeleteForm favoritesDeleteForm = new SysFavoritesDeleteForm();
        favoritesDeleteForm.setBizType("menu");
        favoritesDeleteForm.setBizs(menuCodes);
        //删除收藏夹菜单
        sysFavoritesService.deleteFavorites(favoritesDeleteForm,userId);
        //删除权限菜单关系
        sysRoleAuthService.deleteRoleAuthByResCode(menuCodes, "menu");
        //删除菜单功能
        sysFuncService.deleteMenuFuncByMenuId(menuIds);
        super.deleteChildrenAndMe(menuIds);
    }


    /**
     * 递归查询符合条件的menu结果集
     *
     * @param retIds
     * @param menuAllTree
     * @param auth
     * @param codes
     * @return
     */
    private List<SysMenuVo> querySearchMenuTree(List<Integer> retIds, List<SysMenuVo> menuAllTree, boolean auth, List<String> codes) {
        List<SysMenuVo> retList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(menuAllTree)) {
            //判断当前id是否包含在查询范围内
            for (SysMenuVo sysMenuVo : menuAllTree) {
                boolean thisAuth = auth;
                if (!thisAuth && retIds.contains(sysMenuVo.getId())) {
                    thisAuth = true;
                }
                //递归查询子节点
                List<SysMenuVo> childrenList = this.querySearchMenuTree(retIds, sysMenuVo.getChildren(), thisAuth, codes);

                if (thisAuth || !ObjectUtils.isEmpty(childrenList)) {
                    SysMenuVo retMenuVo = new SysMenuVo();
                    this.dozerMapper.map(sysMenuVo, retMenuVo);
                    if (!ObjectUtils.isEmpty(codes)) {
                        if (codes.contains(retMenuVo.getMenuCode())) {
                            retMenuVo.setIsFavorite(1);
                        } else {
                            retMenuVo.setIsFavorite(0);
                        }
                    }
                    if (!ObjectUtils.isEmpty(childrenList)) {
                        retMenuVo.setChildren(childrenList);
                    }
                    retList.add(retMenuVo);
                }
            }
        }
        return retList;
    }

    private List<SysMenuVo> queryAuthMenuTree(List<String> menuCodes, List<SysMenuVo> menuTree, boolean auth, List<String> codes) {
        List<SysMenuVo> retList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(menuTree)) {
            //判断当前id是否包含在查询范围内
            for (SysMenuVo sysMenuVo : menuTree) {
                boolean thisAuth = auth;
                if (!thisAuth && menuCodes.contains(sysMenuVo.getMenuCode())) {
                    thisAuth = true;
                }
                //递归查询子节点
                List<SysMenuVo> childrenList = this.queryAuthMenuTree(menuCodes, sysMenuVo.getChildren(), thisAuth, codes);

                if (thisAuth || !ObjectUtils.isEmpty(childrenList)) {
                    SysMenuVo retMenuVo = new SysMenuVo();
                    this.dozerMapper.map(sysMenuVo, retMenuVo);
                    if (!ObjectUtils.isEmpty(codes)) {
                        if (codes.contains(retMenuVo.getMenuCode())) {
                            retMenuVo.setIsFavorite(1);
                        } else {
                            retMenuVo.setIsFavorite(0);
                        }
                    }
                    if (!ObjectUtils.isEmpty(childrenList)) {
                        retMenuVo.setChildren(childrenList);
                    }
                    retList.add(retMenuVo);
                }
            }
        }
        return retList;
    }

    /**
     * 获取菜单页签
     *
     * @param menuCode
     * @return
     */
    @Override
    public Map<String, List<SysMenuLableVo>> queryMenuTabs(String menuCode) {
        //获取菜单下的子集
        List<SysMenuLableVo> menus = mapper.selectMenuTabs(menuCode);
        Map<String, SysMenuLableVo> codeMap = ListUtil.listToMap(menus, "menuCode", String.class);
        //返回值
        List<SysMenuGroupVo> retList = new ArrayList<>();
        Map<String,List<SysMenuLableVo>> retMap = new HashMap<>();
        List<SysMenuLableVo> menuLables =  new ArrayList<>();;

        List<String> groupCodes = new ArrayList<>();
        if (!ObjectUtils.isEmpty(menus)) {
            for (SysMenuLableVo sysMenuLableVo : menus) {
                //分组的代码集合
                if (sysMenuLableVo.getMenuType() != null && sysMenuLableVo.getMenuType() == 4) {
                    groupCodes.add(sysMenuLableVo.getMenuCode());
                } else {
                    //直接挂在菜单下的页签
                    menuLables.add(sysMenuLableVo);
                }
            }
            //保存直接挂在菜单下的页签
            if (!ObjectUtils.isEmpty(menuLables)) {
                retMap.put("1",menuLables);
            }
            //处理分组下的页签
            if (!ObjectUtils.isEmpty(groupCodes)) {
                //获取分组下的页签
                List<SysMenuLableVo> groupMenus = mapper.selectGroupMenus(groupCodes);
                if (!ObjectUtils.isEmpty(groupMenus)) {
                    SysMenuGroupVo sysMenuGroupVo = null;
                    //将页签转换成以分组code为key的Map
                    Map<String, List<SysMenuLableVo>> map = ListUtil.bulidTreeListMap(groupMenus, "parentCode", String.class);
                    for (String code : groupCodes) {
                        retMap.put(codeMap.get(code).getShortCode(),map.get(code));
                    }
                }
            }
        }
        return retMap;
    }

    /**
     * 获取菜单清单
     *
     * @return
     */
    @Override
    public List<SysAuthMenuVo> queryAuthMenuVoList() {
        PageHelper.orderBy("sort_num asc");
        List<SysAuthMenuVo> list = mapper.selectAuthMenuVoList();
        return list;
    }

    @Override
    public List<SysMenuVo> selectMenuVoByMenuCodes(List<String> list) {
        List<SysMenuVo> retList = null;
        if (!ObjectUtils.isEmpty(list)) {
            retList = mapper.selectMenuVoByMenuCode(list);
        }
        return retList;
    }

    @Override
    public String queryMenuPos(List<Integer> ids) {
        List<SysMenuPo> sysMenuPos = super.selectByIds(ids);
        String retName = "";
        StringBuffer menuNames = new StringBuffer();
        if (!ObjectUtils.isEmpty(sysMenuPos)){
            for (SysMenuPo sysMenuPo : sysMenuPos){
                menuNames.append(sysMenuPo.getMenuName() + ",");
            }
            String temp = menuNames.toString().substring(0,menuNames.toString().length()-1);
            retName = temp;
        }

        return retName;
    }
}
