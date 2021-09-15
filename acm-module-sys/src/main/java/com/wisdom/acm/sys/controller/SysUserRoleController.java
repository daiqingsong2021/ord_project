package com.wisdom.acm.sys.controller;


import com.wisdom.acm.sys.service.SysUserOrgRoleService;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.RoleUserVo;
import com.wisdom.base.common.vo.UserRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 */
@Controller
@RequestMapping("userrole")
@RestController
public class SysUserRoleController {

    @Autowired
    private SysUserOrgRoleService sysUserOrgRoleService;

    /**
     * 根据用户ID查找用户
     *
     * @param userIds
     * @return
     */
    @RequestMapping(value = "/userrolevo/maps", method = RequestMethod.POST)
    ApiResult<Map<Integer, List<UserRoleVo>>> getUserRoleVoMapByIds(@RequestBody List<Integer> userIds){
        Map<Integer, UserRoleVo> map = sysUserOrgRoleService.queryRoleVoMapByUserIds(userIds);
        return ApiResult.success(map);
    };

    /**
     * 根据角色ID查找用户角色
     *
     * @param roleIds
     * @return
     */
    @RequestMapping(value = "/roleuservo/maps", method = RequestMethod.POST)
    ApiResult<Map<Integer, List<RoleUserVo>>> getRoleUserVoMapByIds(@RequestBody List<Integer> roleIds){
        Map<Integer, RoleUserVo> map = sysUserOrgRoleService.queryUserVoMapByRoleIds(roleIds);
        return ApiResult.success(map);
    };
}
