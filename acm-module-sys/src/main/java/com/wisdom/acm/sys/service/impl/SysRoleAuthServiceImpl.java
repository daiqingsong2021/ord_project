package com.wisdom.acm.sys.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.sys.mapper.AuthMapper;
import com.wisdom.acm.sys.po.*;
import com.wisdom.acm.sys.service.*;
import com.wisdom.acm.sys.vo.SysAuthFuncVo;
import com.wisdom.acm.sys.vo.SysAuthMenuVo;
import com.wisdom.acm.sys.vo.SysRoleAuthUpdateVo;
import com.wisdom.acm.sys.vo.TmmMenusList;
import com.wisdom.base.common.feign.LeafService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.TreeUtil;
import com.wisdom.base.common.vo.GeneralVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleAuthServiceImpl extends BaseService<AuthMapper, SysRoleAuthPo> implements SysRoleAuthService {

    @Autowired
    private SysFuncService funcService;

    @Autowired
    private SysUserOrgRoleService userOrgRoleService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private TmmMenusList tmmMenusList;

    @Autowired
    private LeafService leafService;


    /**
     * 获取功能列表（标记已分配的权限）
     *
     * @param roleId
     * @return
     */
//    @Cache(key = "acm:roleAuth:auths")
    @Override
    public List<SysAuthMenuVo> queryAuthAllByRoleId(Integer roleId) {

        //获取全部菜单
        List<SysAuthMenuVo> allList = sysMenuService.queryAuthMenuVoList();
        //获取全部非系统菜单
        List<SysAuthMenuVo> alList = this.queryNotSystemMenus(allList);

        //获取全部功能清单
        List<SysFuncPo> funcList = funcService.selectListAll();
        //根据菜单id转换功能清单
        Map<Integer, List<SysFuncPo>> funcMap = ListUtil.bulidTreeListMap(funcList, "menuId", Integer.class);
        //获取已分配的权限
        List<SysRoleAuthPo> authList = mapper.selectAuthByRoleId(roleId);

        List<String> menuCodeList = new ArrayList<>();
        List<String> funcCodeList = new ArrayList<>();

        //将符合权限的菜单功能添加到集合
        for (SysRoleAuthPo auth : authList) {
            if ("menu".equals(auth.getResType())) {
                menuCodeList.add(auth.getResCode());
            } else if ("func".equals(auth.getResType())) {
                funcCodeList.add(auth.getResCode());
            }
        }

        List<SysAuthMenuVo> retList = this.markAuthMenuCheck(alList, funcMap, menuCodeList, funcCodeList);
        return TreeUtil.bulid(retList, 0);
    }

    /**
     * 获取菜单集合中的非系统菜单列表
     */
    public List<SysAuthMenuVo> queryNotSystemMenus(List<SysAuthMenuVo> sysAuthMenuVos) {

        List<SysAuthMenuVo> returnList = new ArrayList<SysAuthMenuVo>();
        //获取所有的系统菜单
        List<String> systemMenus = tmmMenusList.getSystem();
        //获取所有的内置菜单
        List<String> builtInMenus = tmmMenusList.getBuilt_in();
        //逐个判断并赋值
        for (SysAuthMenuVo sysAuthMenuVo : sysAuthMenuVos) {
            if (!systemMenus.contains(sysAuthMenuVo.getMenuCode())) {
                returnList.add(sysAuthMenuVo);
            }
        }
        return returnList;
    }


    /**
     * 设置功能菜单已分配标记
     *
     * @param alList
     * @param funcMap
     * @param menuCodeList
     * @param funcCodeList
     * @return
     */
    private List<SysAuthMenuVo> markAuthMenuCheck(List<SysAuthMenuVo> alList, Map<Integer, List<SysFuncPo>> funcMap, List<String> menuCodeList, List<String> funcCodeList) {
        //遍历菜单
        for (SysAuthMenuVo authMenu : alList) {
            //当前菜单下功能集合
            List<SysFuncPo> funcs = funcMap.get(authMenu.getId());
            if (!ObjectUtils.isEmpty(funcs)) {
                List<SysAuthFuncVo> flist = new ArrayList<>();
                for (SysFuncPo func : funcs) {
                    SysAuthFuncVo authFuncVo = new SysAuthFuncVo();
                    authFuncVo.setId(func.getId());
                    authFuncVo.setFuncCode(func.getFuncCode());
                    authFuncVo.setFuncName(func.getFuncName());
                    //判断是否具有功能权限
                    if (funcCodeList.contains(func.getFuncCode())) {
                        authFuncVo.setCheck(1);
                    } else {
                        authFuncVo.setCheck(0);
                    }
                    flist.add(authFuncVo);
                }
                authMenu.setFuncList(flist);
            }
            //判断是否具有菜单权限
            if (menuCodeList.contains(authMenu.getMenuCode())) {
                authMenu.setCheck(1);
            } else {
                authMenu.setCheck(0);
            }
        }
        return alList;
    }

    /**
     * 保存修改权限
     *
     * @param auths
     */
    @Override
    public String updateRoleAuths(Integer roleId, List<SysRoleAuthUpdateVo> auths) {
        //已分配权限
        List<SysRoleAuthPo> haveAuthList = mapper.selectAuthByRoleId(roleId);

        //拼接已分配的权限成string（code和type）
        List<String> existsList = new ArrayList<>();
        //已分配的权限id
        Map<String, Integer> haveAuthMap = new HashMap<>();
        if (!ObjectUtils.isEmpty(haveAuthList)) {
            for (SysRoleAuthPo ra : haveAuthList) {
                existsList.add(ra.getResCode() + ";" + ra.getResType());
                haveAuthMap.put(ra.getResCode() + ";" + ra.getResType(), ra.getId());
            }
        }

        //拼接修改的权限
        List<String> newList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(auths)) {
            for (SysRoleAuthUpdateVo rv : auths) {
                newList.add(rv.getResCode() + ";" + rv.getResType());
            }
        }
        //取已分配和修改的交集
        List<String> intrstlist = ListUtil.intersection(existsList, newList);

        //获取新增的权限
        List<String> addList = ListUtil.difference(newList, intrstlist);

        //获取删除的权限
        List<String> deleteList = ListUtil.difference(existsList, intrstlist);

        if (!ObjectUtils.isEmpty(deleteList))
            existsList.removeAll(deleteList);
        if (!ObjectUtils.isEmpty(addList))
            existsList.addAll(addList);
        List<SysRoleAuthUpdateVo> addVoList = new ArrayList<>();
        SysRoleAuthUpdateVo sysRoleAuthVo = null;

        //将新增的权限拼装成vo对象
        for (String str : addList) {
            sysRoleAuthVo = new SysRoleAuthUpdateVo();
            String resCode = str.substring(0, str.indexOf(";"));
            String resType = str.substring(str.indexOf(";") + 1);
            sysRoleAuthVo.setResCode(resCode);
            sysRoleAuthVo.setResType(resType);
            sysRoleAuthVo.setId(leafService.getId());
            addVoList.add(sysRoleAuthVo);
        }

        List<Integer> ids = new ArrayList<>();
        //需要删除的权限
        if (!ObjectUtils.isEmpty(deleteList)) {
            for (String str : deleteList) {
                ids.add(haveAuthMap.get(str));
            }

            Map<String,Object> idsMap= Maps.newHashMap();
            List<Integer> roleAuthIds= Lists.newArrayList();
            for(int i=0; i<ids.size(); i++)
            {//0-199 200-399
                if(i%1000==0)
                {
                    roleAuthIds=Lists.newArrayList();
                    idsMap.put(String.valueOf(Integer.valueOf(i/1000)),roleAuthIds);
                }
                roleAuthIds.add(ids.get(i));
            }
            //删除权限
            mapper.deleteByRoleIds(idsMap);
        }
        //增加权限
        if (!ObjectUtils.isEmpty(addVoList)) {
            mapper.insertRoleAuth(addVoList, roleId);
        }

        //获取修改日志内容
        String logger = this.queryUpdateAuthLogger(deleteList, existsList);
        return logger;
    }


    /**
     * 获取修改权限日志内容
     *
     * @param deleteList
     * @param existsList
     * @return
     */
    private String queryUpdateAuthLogger(List<String> deleteList, List<String> existsList) {
        //分配后权限
        List<String> existMenuCode = new ArrayList<>();  //菜单权限
        List<String> existFuncCode = new ArrayList<>();  //按钮权限
        if (!ObjectUtils.isEmpty(existsList)) {
            SysRoleAuthUpdateVo sysRoleAuthVo = null;
            List<SysRoleAuthUpdateVo> existVoList = new ArrayList<>();
            //将新增的权限拼装成vo对象
            for (String str : existsList) {
                sysRoleAuthVo = new SysRoleAuthUpdateVo();
                String resCode = str.substring(0, str.indexOf(";"));
                String resType = str.substring(str.indexOf(";") + 1);
                sysRoleAuthVo.setResCode(resCode);
                sysRoleAuthVo.setResType(resType);
                existVoList.add(sysRoleAuthVo);
            }
            if (!ObjectUtils.isEmpty(existVoList)) {
                for (SysRoleAuthUpdateVo sysRoleAuthUpdateVo : existVoList) {
                    if (sysRoleAuthUpdateVo.getResType().equals("menu")) {
                        existMenuCode.add(sysRoleAuthUpdateVo.getResCode());
                    } else if (sysRoleAuthUpdateVo.getResType().equals("func")) {
                        existFuncCode.add(sysRoleAuthUpdateVo.getResCode());
                    }
                }
            }
        }
        List<String> deleteMenuCode = new ArrayList<>();  //删除的菜单权限
        List<String> deleteFuncCode = new ArrayList<>();  //删除的按钮权限
        if (!ObjectUtils.isEmpty(deleteList)) {
            SysRoleAuthUpdateVo sysRoleAuthVo = null;
            List<SysRoleAuthUpdateVo> deleteVoList = new ArrayList<>();
            //将新增的权限拼装成vo对象
            for (String str : deleteList) {
                sysRoleAuthVo = new SysRoleAuthUpdateVo();
                String resCode = str.substring(0, str.indexOf(";"));
                String resType = str.substring(str.indexOf(";") + 1);
                sysRoleAuthVo.setResCode(resCode);
                sysRoleAuthVo.setResType(resType);
                deleteVoList.add(sysRoleAuthVo);
            }
            for (SysRoleAuthUpdateVo sysRoleAuthUpdateVo : deleteVoList) {
                if (sysRoleAuthUpdateVo.getResType().equals("menu")) {
                    deleteMenuCode.add(sysRoleAuthUpdateVo.getResCode());
                } else if (sysRoleAuthUpdateVo.getResType().equals("func")) {
                    deleteFuncCode.add(sysRoleAuthUpdateVo.getResCode());
                }
            }
        }
        List<String> menuCodes = new ArrayList<>();
        List<String> funcCodes = new ArrayList<>();
        if (!ObjectUtils.isEmpty(deleteMenuCode)) {
            menuCodes.addAll(deleteMenuCode);
        }
        if (!ObjectUtils.isEmpty(existMenuCode)) {
            menuCodes.addAll(existMenuCode);
        }
        if (!ObjectUtils.isEmpty(deleteFuncCode)) {
            funcCodes.addAll(deleteFuncCode);
        }
        if (!ObjectUtils.isEmpty(existFuncCode)) {
            funcCodes.addAll(existFuncCode);
        }
        List<SysMenuPo> menuPos = new ArrayList<>();
        List<SysFuncPo> funcPos = new ArrayList<>();
        if (!ObjectUtils.isEmpty(menuCodes))
            menuPos = this.queryMenuPosByCode(menuCodes); //获取菜单po
        Map<String, List<SysMenuPo>> menuMapPo = ListUtil.bulidTreeListMap(menuPos, "menuCode", String.class);
        if (!ObjectUtils.isEmpty(funcCodes))
            funcPos = this.queryFuncPosByCode(funcCodes);  //获取按钮po
        Map<Integer, List<SysFuncPo>> funcMapPo = ListUtil.bulidTreeListMap(funcPos, "menuId", Integer.class);

        List<SysMenuPo> existMenuPos = new ArrayList<>(); //菜单po
        List<SysMenuPo> deleteMenuPos = new ArrayList<>();//删除的菜单po
        if (!ObjectUtils.isEmpty(menuPos)) {
            for (SysMenuPo sysMenuPo : menuPos) {
                if (!ObjectUtils.isEmpty(existMenuCode)) {
                    if (existMenuCode.contains(sysMenuPo.getMenuCode())) {
                        existMenuPos.add(sysMenuPo);
                    }
                }
                if (!ObjectUtils.isEmpty(deleteMenuCode)) {
                    if (deleteMenuCode.contains(sysMenuPo.getMenuCode())) {
                        deleteMenuPos.add(sysMenuPo);
                    }
                }
            }
        }
        List<SysFuncPo> existFuncPos = new ArrayList<>(); //权限po
        if (!ObjectUtils.isEmpty(funcPos)) {
            for (SysFuncPo sysFuncPo : funcPos) {
                if (!ObjectUtils.isEmpty(existFuncCode)) {
                    if (existFuncCode.contains(sysFuncPo.getFuncCode())) {
                        existFuncPos.add(sysFuncPo);
                    }
                }
            }
        }
        Map<Integer, List<SysFuncPo>> funcExistMapPo = ListUtil.bulidTreeListMap(existFuncPos, "menuId", Integer.class);

        StringBuffer retExistNames = new StringBuffer();
        //拼接字符串
        if (!ObjectUtils.isEmpty(existMenuPos)) {
            retExistNames.append("分配菜单（");
            int j = 0;
            for (SysMenuPo sysMenuPo : existMenuPos) {
                retExistNames.append(sysMenuPo.getMenuName());
                if (!ObjectUtils.isEmpty(funcExistMapPo.get(sysMenuPo.getId()))) {
                    int i = 0;
                    for (SysFuncPo sysFuncPo : funcExistMapPo.get(sysMenuPo.getId())) {
                        if (i == 0) {
                            retExistNames.append("[" + sysFuncPo.getFuncName());
                            i++;
                        } else {
                            retExistNames.append("，" + sysFuncPo.getFuncName());
                            i++;
                        }
                    }
                    if (i == funcExistMapPo.get(sysMenuPo.getId()).size()) {
                        retExistNames.append("]，");
                    }
                } else {
                    if (j == existMenuPos.size() -1) {
                        retExistNames.append("");
                    }else {
                        retExistNames.append("，");
                    }
                }
                j++;
            }
            retExistNames.append("），");
        }
        StringBuffer deleteMenus = new StringBuffer();
        String deleteMenuNames = "";
        if (!ObjectUtils.isEmpty(deleteMenuPos)) {
            deleteMenus.append("删除菜单（");
            for (SysMenuPo sysMenuPo : deleteMenuPos) {
                deleteMenus.append(sysMenuPo.getMenuName() + ",");
            }
            deleteMenuNames = deleteMenus.toString().substring(0, deleteMenus.toString().length() - 1) + "）";
        }
        String retName = retExistNames + deleteMenuNames;
        return retName;
    }

    private List<SysMenuPo> queryMenuPosByIds(List<Integer> addNoFuncIds) {
        Example example = new Example(SysMenuPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", addNoFuncIds);
        return sysMenuService.selectByExample(example);
    }

    private List<SysFuncPo> queryFuncPosByCode(List<String> addFuncCode) {
        return funcService.selectByFuncCodes(addFuncCode);
//        Example example = new Example(SysFuncPo.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andIn("funcCode", addFuncCode);
//        return funcService.selectByExample(example);
    }

    private List<SysMenuPo> queryMenuPosByCode(List<String> addMenuCode) {
        Example example = new Example(SysMenuPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("menuCode", addMenuCode);
        return sysMenuService.selectByExample(example);
    }


    /**
     * 获取权限菜单
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> queryAuthByUserId(Integer userId) {
        List<String> retIds = mapper.selectAuthByUserId(userId);
        return retIds;
    }

    @Override
    public List<Integer> queryOrgIdsByUserIdAndFuncCode(String funcCode, Integer userId) {
        return this.mapper.selectOrgIdsByUserIdAndFuncCode(funcCode, userId);
    }

    /**
     * 删除角色权限（根据roleId）
     *
     * @param roleIds
     */
    @Override
    public void deleteRoleAuthByRoles(List<Integer> roleIds) {
        if (!ObjectUtils.isEmpty(roleIds)) {
            mapper.deleteRoleAuthByRoleIds(roleIds);
        }
    }

    /**
     * 删除角色权限（根据Code）
     *
     * @param resCodes
     */
    @Override
    public void deleteRoleAuthByResCode(List<String> resCodes, String resType) {
        if (!ObjectUtils.isEmpty(resCodes)) {
            mapper.deleteRoleAuthByMenuCodes(resCodes, resType);
        }
    }

    @Override
    public List<SysRoleAuthPo> queryAuthFuncByRoleIds(List<Integer> roleIds) {
        List<SysRoleAuthPo> list = new ArrayList<>();
        if (!ObjectUtils.isEmpty(roleIds)) {
            Example example = new Example(SysRoleAuthPo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("roleId", roleIds);
            criteria.andEqualTo("resType", "func");
            list = this.mapper.selectByExample(example);
        }
        return list;
    }

    @Override
    public List<GeneralVo> getUserAuth(String menuCode, Integer userId){
        List<GeneralVo> auths = new ArrayList<>();
        // 根据用户ID查询用户所在组织及权限集合
        List<SysUserOrgRolePo> userOrgRolePos = userOrgRoleService.queryUserOrgRolePosByUserId(userId);
        // 角色ID集合
        List<Integer> roleIds = ListUtil.toValueList(userOrgRolePos, "roleId", Integer.class, true);
        if (!ObjectUtils.isEmpty(roleIds)){
            auths = this.mapper.selectAuths(menuCode, roleIds);
        }
        return auths;
    }

    @Override
    public Map<Integer, List<String>> getOrgFuncsByUserId(Integer userId) {

        Map<Integer, List<String>> retmap = new HashMap<>();
        // 根据用户ID查询用户所在组织及权限集合
        List<SysUserOrgRolePo> userOrgRolePos = userOrgRoleService.queryUserOrgRolePosByUserId(userId);
        // 角色ID集合
        List<Integer> roleIds = ListUtil.toValueList(userOrgRolePos, "roleId", Integer.class, true);
        // 根据角色ID查询所有的权限
        List<SysRoleAuthPo> roleAuthPos = this.queryAuthFuncByRoleIds(roleIds);
        // 角色ID对应的权限集合
        Map<Integer, List<SysRoleAuthPo>> roleAuthMap = ListUtil.bulidTreeListMap(roleAuthPos, "roleId", Integer.class);

        if (!ObjectUtils.isEmpty(userOrgRolePos)) {
            for (SysUserOrgRolePo uor : userOrgRolePos) {
                // 组织机构ID
                Integer roleId = uor.getRoleId();
                // 角色ID
                List<String> authlist = ListUtil.toValueList(roleAuthMap.get(roleId), "resCode", String.class);
                if (ObjectUtils.isEmpty(authlist)) {
                    authlist = new ArrayList<>();
                }
                if (retmap.get(uor.getOrgId()) != null) {
                    retmap.put(uor.getOrgId(), ListUtil.distinct(ListUtil.union(authlist, retmap.get(uor.getOrgId()))));
                } else {
                    retmap.put(uor.getOrgId(), authlist);
                }
            }
        }
        return retmap;
    }
}
