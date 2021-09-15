package com.wisdom.acm.sys.controller;

import com.wisdom.acm.sys.po.SysRolePo;
import com.wisdom.acm.sys.po.SysUserPo;
import com.wisdom.acm.sys.service.SysRoleAuthService;
import com.wisdom.acm.sys.service.SysRoleService;
import com.wisdom.acm.sys.service.SysUserOrgRoleService;
import com.wisdom.acm.sys.service.SysUserService;
import com.wisdom.acm.sys.vo.*;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.feign.LoggerService;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "auth")
public class SysRoleAuthController extends BaseController {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    private SysRoleAuthService authService;

    @Autowired
    private SysUserOrgRoleService sysUserOrgRoleService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private CommUserService userService;

    /**
     * 获取用户模块权限
     * @param menuCode
     * @return
     */
    @GetMapping(value = "/user/{menuCode}/auth/list")
    public ApiResult getUserAuth(@PathVariable(name = "menuCode") String menuCode) {
        UserInfo userInfo = userService.getLoginUser();
        Integer userId = userInfo.getId();
        List<GeneralVo> auths = authService.getUserAuth(menuCode,userId);
        return ApiResult.success(auths);
    }

    /**
     * 获取权限列表
     *
     * @param roleId
     * @return
     */
    @GetMapping(value = "/{roleId}/list")
    public ApiResult queryAuthByRoleId(@PathVariable("roleId") Integer roleId) {
        List<SysAuthMenuVo> list = authService.queryAuthAllByRoleId(roleId);
        return ApiResult.success(list);
    }

    /**
     * 角色授权
     *
     * @return
     */
    @PutMapping(value = "/{roleId}/update/auth")
    @AddLog(title = "修改权限" , module = LoggerModuleEnum.SM_ROLE)
    public ApiResult updateRoleFunc(@PathVariable("roleId") Integer roleId, @RequestBody List<SysRoleAuthUpdateVo> auths) {
        String logger = authService.updateRoleAuths(roleId, auths);
        SysRolePo sysRolePo = sysRoleService.queryRolePoById(roleId);
        if (!logger.equals(""))
            this.setAcmLogger(new AcmLogger("修改\"" + sysRolePo.getRoleName() + "\"角色权限：" + logger));
//            loggerService.addAcmLoggerInfo(module, "修改权限", "修改\"" + sysRolePo.getRoleName() + "\"角色权限：" + logger);
        return ApiResult.success();
    }

    /**
     * 根据权限代码，用户ID查询拥有权限的所在OrgId集合
     *
     * @return
     */
    @PostMapping(value = "/{funcCode}/{userId}/orgId/list")
    public ApiResult queryOrgIdsByUserIdAndFuncCode(@PathVariable(name = "funcCode") String funcCode, @PathVariable(name = "userId") Integer userId) {
        List<Integer> orgIds = authService.queryOrgIdsByUserIdAndFuncCode(funcCode, userId);
        return ApiResult.success(orgIds);
    }

    /**
     * 根据权限代码，用户ID查询拥有权限的所在OrgId集合
     *
     * @return
     */
    @GetMapping(value = "/user/{userId}/org/auth/map")
    public ApiResult queryOrgFuncsByUserId(@PathVariable(name = "userId") Integer userId) {
        Map<Integer, List<String>> funcMaps = authService.getOrgFuncsByUserId(userId);
        return ApiResult.success(funcMaps);
    }

    /**
     * 分配用户角色
     *
     * @param vos
     * @return
     */
    @PostMapping(value = "assign/user/role")
    @AddLog(title = "分配用户角色" , module = LoggerModuleEnum.SM_ROLE)
    public ApiResult assignUserRoles(@RequestBody List<SysUserOrgRoleVo> vos) {
        if (!ObjectUtils.isEmpty(vos)) {
            //日志处理
            SysRolePo sysRolePo = sysRoleService.queryRolePoById(vos.get(0).getRoleId());
            List<Integer> userIds = new ArrayList<>();
            for (SysUserOrgRoleVo sysUserOrgRoleVo : vos) {
                userIds.add(sysUserOrgRoleVo.getUserId());
            }
            String userNames = sysUserService.queryUserNamesByIds(userIds);
            this.setAcmLogger(new AcmLogger("分配\"" + sysRolePo.getRoleName() + "\"角色用户：" + userNames));
//            loggerService.addAcmLoggerInfo(module, "分配用户", "分配\"" + sysRolePo.getRoleName() + "\"角色用户：" + userNames);
            sysUserOrgRoleService.addUserOrgRole(vos);
        }
        return ApiResult.success();
    }

    /**
     * 删除用户角色关系
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/user/role/delete")
    @AddLog(title = "删除用户角色" , module = LoggerModuleEnum.SM_ROLE)
    public ApiResult deleteUserRoles(@RequestBody List<Integer> ids) {
        String logger = sysUserOrgRoleService.queryUserRoleDeleteLogger(ids);
        this.setAcmLogger(new AcmLogger(logger));
//        loggerService.addAcmLoggerInfo(module,"删除角色用户分配",logger);
        sysUserOrgRoleService.deleteUserRole(ids);
        return ApiResult.success();
    }

}
