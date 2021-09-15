package com.wisdom.acm.sys.rpc.service;

import com.wisdom.acm.sys.vo.FrontUser;
import com.wisdom.acm.sys.vo.MenuTree;
import com.wisdom.acm.sys.service.MenuBiz;
import com.wisdom.acm.sys.service.UserBiz;
import com.wisdom.acm.sys.constant.AdminCommonConstant;
import com.wisdom.acm.sys.po.Element;
import com.wisdom.acm.sys.po.SysMenuPo;
import com.wisdom.base.common.authority.PermissionInfo;
import com.wisdom.base.common.enums.HMACEnum;
import com.wisdom.base.common.util.HMACUtil;
import com.wisdom.base.common.vo.UserInfo;
import com.wisdom.auth.client.jwt.UserAuthUtil;
import com.wisdom.base.common.constant.CommonConstants;
import com.wisdom.base.common.util.TreeUtil;
import org.apache.commons.lang3.StringUtils;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
@Service
public class PermissionService {
    @Autowired
    private Mapper mapper;

    @Autowired
    private UserBiz userBiz;
    @Autowired
    private MenuBiz menuBiz;

    @Autowired
    private UserAuthUtil userAuthUtil;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public UserInfo getUserByUsername(String userName) {
        //UserInfo info = new UserInfo();
        UserInfo user = userBiz.getUserByUsername(userName);
        UserInfo info = mapper.map(user, UserInfo.class);
        //BeanUtils.copyProperties(user, info);
        info.setId(Integer.valueOf(user.getId()));
        return info;
    }

    public UserInfo validate(String userName,String password){
        //UserInfo info = new UserInfo();
        UserInfo user = userBiz.getUserByUsername(userName);
        String pwd = HMACUtil.getHMAC(password, HMACEnum.HMAC_KEY.getType());
        if (!user.getPassword().equals(pwd)){
            return null;
        }
        //if (encoder.matches(password, user.getPassword())) {
        UserInfo info = mapper.map(user, UserInfo.class);
            //BeanUtils.copyProperties(user, info);
        info.setId(Integer.valueOf(user.getId()));
       // }
        return info;
    }

    public List<PermissionInfo> getAllPermission() {
        List<SysMenuPo> sysMenuPos = menuBiz.selectListAll();
        List<PermissionInfo> result = new ArrayList<PermissionInfo>();
        PermissionInfo info = null;
        menu2permission(sysMenuPos, result);
        //List<Element> elements = elementBiz.getAllElementPermissions();
        //element2permission(result, elements);
        return result;
    }

    private void menu2permission(List<SysMenuPo> sysMenuPos, List<PermissionInfo> result) {
        PermissionInfo info;
        for (SysMenuPo sysMenuPo : sysMenuPos) {
            if (StringUtils.isBlank(sysMenuPo.getUrl())) {
                sysMenuPo.setUrl("/" + sysMenuPo.getMenuCode());
            }
            info = new PermissionInfo();
            info.setCode(sysMenuPo.getMenuCode());
            info.setType(AdminCommonConstant.RESOURCE_TYPE_MENU);
            info.setName(AdminCommonConstant.RESOURCE_ACTION_VISIT);
            String uri = sysMenuPo.getUrl();
            if (!uri.startsWith("/")) {
                uri = "/" + uri;
            }
            info.setUri(uri);
            info.setMethod(AdminCommonConstant.RESOURCE_REQUEST_METHOD_GET);
            result.add(info
            );
            info.setMenu(sysMenuPo.getMenuName());
        }
    }

    public List<PermissionInfo> getPermissionByUsername(String userName) {
        UserInfo user = userBiz.getUserByUsername(userName);
        List<SysMenuPo> sysMenuPos = menuBiz.getUserAuthorityMenuByUserId(user.getId());
        List<PermissionInfo> result = new ArrayList<PermissionInfo>();
        PermissionInfo info = null;
        menu2permission(sysMenuPos, result);
        //List<Element> elements = elementBiz.getAuthorityElementByUserId(user.getId() + "");
        //element2permission(result, elements);
        return result;
    }

    private void element2permission(List<PermissionInfo> result, List<Element> elements) {
        PermissionInfo info;
        for (Element element : elements) {
            info = new PermissionInfo();
            info.setCode(element.getCode());
            info.setType(element.getType());
            info.setUri(element.getUri());
            info.setMethod(element.getMethod());
            info.setName(element.getName());
            info.setMenu(element.getMenuId());
            result.add(info);
        }
    }


    private List<MenuTree> getMenuTree(List<SysMenuPo> sysMenuPos, int root) {
        List<MenuTree> trees = new ArrayList<MenuTree>();
        MenuTree node = null;
        for (SysMenuPo sysMenuPo : sysMenuPos) {
            //node = new MenuTree();
            //BeanUtils.copyProperties(menu, node);
            node = mapper.map(sysMenuPo, MenuTree.class);
            trees.add(node);
        }
        return TreeUtil.bulid(trees, root);
    }

    public FrontUser getUserInfo(String token) throws Exception {
        String userName = userAuthUtil.getInfoFromToken(token).getUniqueName();
        if (userName == null) {
            return null;
        }
        UserInfo user = this.getUserByUsername(userName);
        //FrontUser frontUser = new FrontUser();
        FrontUser frontUser = mapper.map(user, FrontUser.class);
        //BeanUtils.copyProperties(user, frontUser);
        List<PermissionInfo> permissionInfos = this.getPermissionByUsername(userName);
        Stream<PermissionInfo> menus = permissionInfos.parallelStream().filter((permission) -> {
            return permission.getType().equals(CommonConstants.RESOURCE_TYPE_MENU);
        });
        frontUser.setMenus(menus.collect(Collectors.toList()));
        Stream<PermissionInfo> elements = permissionInfos.parallelStream().filter((permission) -> {
            return !permission.getType().equals(CommonConstants.RESOURCE_TYPE_MENU);
        });
        frontUser.setElements(elements.collect(Collectors.toList()));
        return frontUser;
    }

    public List<MenuTree> getMenusByUsername(String token) throws Exception {
        String userName = userAuthUtil.getInfoFromToken(token).getUniqueName();
        if (userName == null) {
            return null;
        }
        UserInfo user = userBiz.getUserByUsername(userName);
        List<SysMenuPo> sysMenuPos = menuBiz.getUserAuthorityMenuByUserId(user.getId());
        return getMenuTree(sysMenuPos,AdminCommonConstant.ROOT);
    }
}
