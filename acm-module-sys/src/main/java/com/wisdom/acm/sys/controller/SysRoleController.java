package com.wisdom.acm.sys.controller;


import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.form.SysRoleAddForm;
import com.wisdom.acm.sys.form.SysRoleUpdateForm;
import com.wisdom.acm.sys.form.SysSearchRoleForm;
import com.wisdom.acm.sys.po.SysRolePo;
import com.wisdom.acm.sys.service.SysRoleService;
import com.wisdom.acm.sys.service.SysUserOrgRoleService;
import com.wisdom.acm.sys.vo.SysRoleVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 */
@Controller
@RequestMapping("role")
@RestController
public class SysRoleController extends BaseController {
    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysUserOrgRoleService sysUserOrgRoleService;

    /**
     * 根据用户ID和组织ID查找用户
     * @param orgId
     * @param userId
     * @return
     */
    @RequestMapping(value = "/{orgId}/{userId}/info", method = RequestMethod.GET)
    ApiResult<List<RoleVo>> queryRoleList(@PathVariable("orgId") Integer orgId, @PathVariable("userId") Integer userId){
        List<RoleVo> roleVos = new ArrayList<>();
        List<SysRoleVo> list = sysUserOrgRoleService.queryRoleListByOrgIdAndUserId(orgId, userId);
        if (!ObjectUtils.isEmpty(list)){
            list.forEach(vo ->{
                roleVos.add(new RoleVo(vo.getId(),vo.getRoleCode(),vo.getRoleName()));
            });
        }
        return ApiResult.success(roleVos);
    }

    /**
     * 增加角色
     *
     * @param SysRoleAddForm
     * @return
     */
    @AddLog(title = "增加角色",module = LoggerModuleEnum.SM_ROLE)
    @PostMapping(value = "/add")
    public ApiResult addRole(@RequestBody @Valid SysRoleAddForm SysRoleAddForm) {
        SysRolePo sysRolePo = roleService.addRole(SysRoleAddForm);
        SysRoleVo sysRoleVo = roleService.getRoleInfo(sysRolePo.getId());
        return ApiResult.success(sysRoleVo);
    }


    /**
     * 获取角色列表
     *
     * @return
     */
    @GetMapping(value = "/list")
    public ApiResult queryAllRole() {
        List<SysRoleVo> roleList = roleService.queryRoles();
        return ApiResult.success(roleList);
    }

    /**
     * 获取角色信息
     *
     * @param roleId
     * @return
     */
    @GetMapping(value = "/{roleId}/info")
    public ApiResult getRoleInfo(@PathVariable("roleId")Integer roleId) {
        if (ObjectUtils.isEmpty(roleId)) {
           ApiResult.result(666,"roleId不能为空");
        }
        SysRoleVo role = roleService.getRoleInfo(roleId);
        return ApiResult.success(role);
    }


    /**
     * 搜索角色信息
     *
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search/{pageSize}/{currentPageNum}")
    public ApiResult queryRoleBySearch( SysSearchRoleForm searchMap,@PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum) {
        PageInfo<SysRoleVo> roles = roleService.queryRoleBySearch(searchMap,pageSize, currentPageNum);
        return new TableResultResponse(roles);
    }

    /**
     * 修改角色信息
     *
     * @param SysRoleUpdateForm
     * @return
     */
    @PutMapping(value = "/update")
    public ApiResult updateRole(@RequestBody @Valid SysRoleUpdateForm SysRoleUpdateForm) {
        SysRolePo sysRolePo = roleService.updateRole(SysRoleUpdateForm);
        SysRoleVo sysRoleVo = roleService.getRoleInfo(sysRolePo.getId());
        return ApiResult.success(sysRoleVo);
    }


    /**
     * 删除角色
     *
     * @param
     * @return
     */
    @DeleteMapping(value = "/delete")
    @AddLog(title = "删除角色",module = LoggerModuleEnum.SM_ROLE)
    public ApiResult deleteRole(@RequestBody List<Integer> ids) {
        String roleNames = roleService.queryNamesByIds(ids,"roleName");
        this.setAcmLogger(new AcmLogger("批量删除角色，角色名称如下："+roleNames));
        roleService.deleteRole(ids);
        return ApiResult.success();
    }

    @PostMapping("/sysrolevo/maps")
    public ApiResult<Map<Integer, RoleVo>> getUserVoMapByUsers(@RequestBody List<Integer> roleIds) {
        Map<Integer, RoleVo> userVoMap = new HashMap<Integer, RoleVo>();
        List<SysRoleVo> roles = roleService.queryRolesByIds(roleIds);
        if(!ObjectUtils.isEmpty(roles)){
            for (SysRoleVo role : roles){
                userVoMap.put(role.getId(), new RoleVo(role.getId(), role.getRoleCode(), role.getRoleName()));
            }
        }
        return ApiResult.success(userVoMap);
    }

}
