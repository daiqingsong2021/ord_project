package com.wisdom.acm.sys.service.impl;

import com.wisdom.acm.sys.mapper.UserMapper;
import com.wisdom.acm.sys.mapper.UserOrgRoleMapper;
import com.wisdom.acm.sys.po.SysFuncPo;
import com.wisdom.acm.sys.po.SysRoleAuthPo;
import com.wisdom.acm.sys.po.SysUserPo;
import com.wisdom.acm.sys.service.*;
import com.wisdom.acm.sys.vo.*;
import com.wisdom.acm.sys.vo.app.AppUserInfoVo;
import com.wisdom.base.common.feign.LeafService;
import com.wisdom.base.common.util.DateUtil;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.GeneralVo;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
public class SysLoginServiceImpl implements SysLoginService {

    @Autowired
    private Mapper dozarMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SysRoleAuthService sysRoleAuthService;

    @Autowired
    private SysMenuService sysMenuService;


    @Autowired
    private TmmAdminAuthList tmmAdminAuthList;

    @Autowired
    private SysFavoritesService sysFavoritesService;

    @Autowired
    private SysUserOrgRoleService sysUserOrgRoleService;

    @Autowired
    private SysFuncService sysFuncService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SystemVo systemVo;

    @Autowired
    private SysPwdRuleService sysPwdRuleService;

    @Autowired
    private LeafService leafService;
    @Autowired
    private SysMenuLevelVo sysMenuLevelVo;
    @Autowired
    private SysOrgService orgService;
    @Autowired
    private UserOrgRoleMapper userOrgRoleMapper;

    /**
     * 获取用户登录信息
     *
     * @param userName
     * @return
     */
//    @Cache(key = "acm:users:userName{1}")
    @Override
    public SysUserLoginVo mgetUserLoginInfo(String userName) {
        //获取用户基本信息
        SysUserPo userInfo = userMapper.selectUserByUserName(userName);
        // SysUserPo userInfo = sysUserService.getUserPoByName(userName);
        //获取用户角色信息
        List<Integer> ids = new ArrayList<>();
        ids.add(userInfo.getId());
        List<SysUserOrgRoleVo> roles = this.queryUserRoleByUserIdAndRoleId(ids, null);
        Map<Integer, List<SysUserOrgRoleVo>> roleMap = ListUtil.bulidTreeListMap(roles, "userId", Integer.class);
        List<SysRoleVo> roleVos1 = null;
        if (!ObjectUtils.isEmpty(roleMap.get(userInfo.getId()))) {
            roleVos1 = new ArrayList<>();
            for (SysUserOrgRoleVo sysUserOrgRoleVo : roleMap.get(userInfo.getId())) {
                SysRoleVo sysRoleVo1 = new SysRoleVo();
                sysRoleVo1.setId(sysUserOrgRoleVo.getRoleId());
                sysRoleVo1.setRoleCode(sysUserOrgRoleVo.getRoleCode());
                sysRoleVo1.setRoleName(sysUserOrgRoleVo.getRoleName());
                roleVos1.add(sysRoleVo1);
            }
        }
        //获取密码规则设置
        SysPwdRuleVo sysPwdRuleVo = sysPwdRuleService.queryPwdRuleAll();
        //判断密码修改周期
        Integer isUpdatePwd = this.validateUpdatePwdCycly(userInfo, sysPwdRuleVo);
        if (isUpdatePwd == 0) {
            //外部用户第一次登录需修改默认密码
            if (!ObjectUtils.isEmpty(userInfo.getUserCode()) && userInfo.getUserCode().contains("UVU") && ObjectUtils.isEmpty(userInfo.getLastloginTime())) {
                isUpdatePwd = 1;
            }else{
                sysUserService.updateUserLastLoginTime(userInfo);
            }
        }else{
            //内部用户密码不受修改期限
            if (!ObjectUtils.isEmpty(userInfo.getUserCode()) && !userInfo.getUserCode().contains("UVU")) {
                isUpdatePwd = 0;
            }
        }
        //获取三员管理开启状态
        Integer isOpen = systemVo.getEnable();
        System.out.println("==============================>" + tmmAdminAuthList);
        //获取三级菜单开启状态
        Integer isThreeMenu = sysMenuLevelVo.getEnable();
        //获取用户按钮权限
        List<String> funcCodes = this.queryAuthByUserRole(userInfo);
        //获取用户菜单权限
        Map<String, List<SysMenuVo>> sysLoginMenuVos = this.validateTmmAuth(userInfo, isOpen);
        //获取用户收藏夹菜单代码
        List<String> favortCodes = sysFavoritesService.queryFavoritesContentByUserIdAndBizType(userInfo.getId(), "menu");
        SysUserLoginVo sysUserLoginVo = new SysUserLoginVo();
        dozarMapper.map(userInfo, sysUserLoginVo);
        sysUserLoginVo.setLoginMenu(sysLoginMenuVos);
        sysUserLoginVo.setIsOpen(isOpen);
        sysUserLoginVo.setFuncCodes(funcCodes);
        sysUserLoginVo.setFavortCodes(favortCodes);
        sysUserLoginVo.setIsUpdatePwd(isUpdatePwd);
        sysUserLoginVo.setIsThreeMenu(isThreeMenu);
        sysUserLoginVo.setRoles(roleVos1);
        if(!ObjectUtils.isEmpty(userInfo.getUserCode()) && userInfo.getUserCode().contains("UVU")){
            sysUserLoginVo.setInnerPeople(Boolean.FALSE);
        }else{
            sysUserLoginVo.setInnerPeople(Boolean.TRUE);
        }
        return sysUserLoginVo;
    }

    private List<SysUserOrgRoleVo> queryUserRoleByUserIdAndRoleId(List<Integer> ids, Integer roleId) {
        List<SysUserOrgRoleVo> list = userOrgRoleMapper.selectUserRoleByUserIdAndRoleId(ids, roleId);
        return list;
    }


    /**
     * 获取用户登录信息(APP使用)
     *
     * @param userName
     * @return
     */
    @Override
    public AppUserInfoVo getUserAppLoginInfo(String userName, String token) {
        //获取用户基本信息
        SysUserPo userInfo = userMapper.selectUserByUserName(userName);
        AppUserInfoVo appUserInfoVo = new AppUserInfoVo();
        appUserInfoVo.setId(userInfo.getId());
        appUserInfoVo.setName(userInfo.getActuName());
        appUserInfoVo.setSex(userInfo.getSex().intValue() == 1 ? "男" : "女");
        appUserInfoVo.setPhone(userInfo.getPhone());
        appUserInfoVo.setEmail(userInfo.getEmail());
//        List<GeneralVo> orgs = this.userMapper.selectOrgsByUserId(userInfo.getId());
        //获取主部门
        SysOrgInfoVo sysOrgInfoVo = this.orgService.querySysUserOrgPoByUserId(userInfo.getId());
        GeneralVo org = new GeneralVo();
        org.setId(sysOrgInfoVo.getId());
        org.setCode(sysOrgInfoVo.getOrgCode());
        org.setName(sysOrgInfoVo.getOrgName());
        appUserInfoVo.setOrg(org);
        appUserInfoVo.setAuthorization(token);              // token
        return appUserInfoVo;
    }


    /**
     * 判断密码修改周期
     *
     * @param userPo
     * @param sysPwdRuleVo
     */
    private Integer validateUpdatePwdCycly(SysUserPo userPo, SysPwdRuleVo sysPwdRuleVo) {
        int time = DateUtil.getDaysChaByTwoTime(userPo.getUpdatePwdTime(), new Date());
        if (sysPwdRuleVo != null && sysPwdRuleVo.getCycle() != null && time >= sysPwdRuleVo.getCycle()) {
//            userPo.setStatus(0);
//            sysUserService.updUserPo(userPo);
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 获取用户按钮权限
     *
     * @param userInfo
     */
    private List<String> queryAuthByUserRole(SysUserPo userInfo) {
        //获取用户角色id
        List<Integer> roleIds = sysUserOrgRoleService.queryRoleIdByUserId(userInfo.getId());
        //根据角色获取按钮权限关系
        List<SysRoleAuthPo> funcList = sysRoleAuthService.queryAuthFuncByRoleIds(roleIds);
        List<String> funcCodes = new ArrayList<>();
        if (!ObjectUtils.isEmpty(funcList)) {
            for (SysRoleAuthPo sysRoleAuthPo : funcList) {
                funcCodes.add(sysRoleAuthPo.getResCode());
            }
        }
        //根据关系获取按钮权限code
        List<SysFuncPo> funcs = sysFuncService.queryFuncByResCodes(funcCodes);
        List<String> retCodes = new ArrayList<>();
        if (!ObjectUtils.isEmpty(funcs)) {
            funcs.forEach(sysFuncPo -> {
                retCodes.add(sysFuncPo.getFuncCode());
            });
        }

        //根据菜单id转换功能清单
//        Map<Integer, List<SysFuncPo>> funcMap = ListUtil.bulidTreeListMap(funcs, "menuId", Integer.class);
        return retCodes;
    }

    private Map<String, List<SysMenuVo>> validateTmmAuth(SysUserPo userInfo, Integer isOpen) {
        Map<String, List<SysMenuVo>> retMap = new HashMap<>();
        //获取全部菜单列表
        List<SysMenuVo> menuTree = sysMenuService.queryMenuAll();
        List<SysMenuVo> menuTree2 = new ArrayList<>();
        SysMenuVo sysMenu = null;
        for (SysMenuVo sysMenuVo : menuTree) {
            sysMenu = new SysMenuVo();
            dozarMapper.map(sysMenuVo, sysMenu);
            menuTree2.add(sysMenu);
        }
        //获取权限菜单code
        List<String> authCodes = this.queryMenuCodeByAuth(userInfo, isOpen);
        //是否开启三级菜单
        Integer isThreeMenu = sysMenuLevelVo.getEnable();

        if (isThreeMenu == 0) {
            //获取权限菜单列表(菜单栏)
            List<SysMenuVo> rightMenuTree = new ArrayList<>();
            rightMenuTree = this.queryAuthMenuTree(menuTree, authCodes);
            retMap.put("left", rightMenuTree);
            //获取权限菜单列表（九宫格）
            List<SysMenuVo> leftMenuTree = new ArrayList<>();
            leftMenuTree = this.queryAuthJiuMenuTree(menuTree2, authCodes);
            retMap.put("right", leftMenuTree);

            return retMap;
        } else {
            //获取所有菜单列表(菜单栏),
            List<SysMenuVo> retRightMenuTree = new ArrayList<>();
            List<SysMenuVo> retLeftMenuTree = new ArrayList<>();
            retRightMenuTree = this.queryAuthMenuTree(menuTree, authCodes);
            retLeftMenuTree = this.queryAuthJiuMenuTree(menuTree2, authCodes);
            retRightMenuTree.addAll(retLeftMenuTree);
            //设置三级菜单
            this.queryAuthMenuThreeTree(retRightMenuTree);
            retMap.put("left", retRightMenuTree);

            return retMap;
        }
    }

    private List<String> queryFavoritesMenuCodes(List<SysFavoritesVo> favoritesVos) {
        List<String> codes = new ArrayList<>();
        favoritesVos.forEach(favoriteseVo -> {
            //codes.add(favoriteseVo.getMenuCode());
        });
        return codes;
    }

    private List<String> queryMenuCodeByAuth(SysUserPo userInfo, Integer isOpen) {
        String type = userInfo.getUserType();
        List<String> authCodes = new ArrayList<>();
        //配置文件中三员权限
        Map<String, List<String>> authMap = tmmAdminAuthList.getMap();
        if (isOpen == 1 && !type.equals("1")) {
            authCodes = authMap.get(type);
        } else if (isOpen == 0 && type.equals("5")) {
            authCodes = authMap.get(type);
        } else if (type.equals("1")) {
            authCodes = sysRoleAuthService.queryAuthByUserId(userInfo.getId());
        }
        return authCodes;
    }

    /**
     * 递归查询权限菜单
     *
     * @param menuTree
     * @param authCodes
     * @return
     */
    private List<SysMenuVo> queryAuthMenuTree(List<SysMenuVo> menuTree, List<String> authCodes) {
        List<SysMenuVo> retList = new ArrayList<>();

        //判断当前id是否包含在查询范围内
        if (!ObjectUtils.isEmpty(menuTree)) {
            for (SysMenuVo sysMenuVo : menuTree) {
                if (sysMenuVo.getMenuType().getId().equals(2) && authCodes.contains(sysMenuVo.getMenuCode())) {
                    sysMenuVo.setChildren(null);
                    if (ObjectUtils.isEmpty(sysMenuVo.getMenuLocal()))
                        retList.add(sysMenuVo);
                    continue;
                } else if (sysMenuVo.getMenuType().getId().equals(1)) {
                    List<SysMenuVo> clist = this.queryAuthMenuTree(sysMenuVo.getChildren(), authCodes);
                    sysMenuVo.setChildren(null);
                    if (!ObjectUtils.isEmpty(clist)) {
                        sysMenuVo.setChildren(clist);
                        if (ObjectUtils.isEmpty(sysMenuVo.getMenuLocal()))
                            retList.add(sysMenuVo);
                    }
                }
            }
        }
        return retList;
    }

    private void queryAuthMenuThreeTree(List<SysMenuVo> retList) {
        if (!ObjectUtils.isEmpty(retList)) {
            for (SysMenuVo sysMenuVo : retList) {
                List<SysMenuVo> list = sysMenuVo.getChildren();
                Map<String, List<SysMenuVo>> map = ListUtil.bulidTreeListMap(list, "groupName", String.class);
                List<String> groupNames = ListUtil.toValueList(list, "groupName", String.class);
                List<String> retGroupNames = ListUtil.distinct(groupNames);
                retGroupNames.remove(null);
                List<SysMenuVo> childrens = new ArrayList<>();

                if (!ObjectUtils.isEmpty(retGroupNames)) {
                    for (String groupName : retGroupNames) {
                        SysMenuVo groupVo = new SysMenuVo();
                        groupVo.setGroupName(groupName);
                        groupVo.setId(leafService.getId());
                        groupVo.setChildren(map.get(groupName));
                        childrens.add(groupVo);
                    }

                    sysMenuVo.setChildren(childrens);
                }
            }
        }
    }

    private List<SysMenuVo> queryAuthJiuMenuTree(List<SysMenuVo> menuTree, List<String> authCodes) {
        List<SysMenuVo> retList = new ArrayList<>();

        //判断当前id是否包含在查询范围内
        if (!ObjectUtils.isEmpty(menuTree)) {
            for (SysMenuVo sysMenuVo : menuTree) {
                if (sysMenuVo.getMenuType().getId().equals(2) && authCodes.contains(sysMenuVo.getMenuCode())) {
                    sysMenuVo.setChildren(null);
                    if (!ObjectUtils.isEmpty(sysMenuVo.getMenuLocal()) && sysMenuVo.getMenuLocal() == 1)
                        retList.add(sysMenuVo);
                    continue;
                } else if (sysMenuVo.getMenuType().getId().equals(1)) {
                    List<SysMenuVo> clist = this.queryAuthJiuMenuTree(sysMenuVo.getChildren(), authCodes);
                    sysMenuVo.setChildren(null);
                    if (!ObjectUtils.isEmpty(clist)) {
                        sysMenuVo.setChildren(clist);
                        if (!ObjectUtils.isEmpty(sysMenuVo.getMenuLocal()) && sysMenuVo.getMenuLocal() == 1)
                            retList.add(sysMenuVo);
                    }
                }
            }
        }
        return retList;
    }

}
