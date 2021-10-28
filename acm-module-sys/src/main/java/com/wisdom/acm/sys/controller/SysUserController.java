package com.wisdom.acm.sys.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.form.*;
import com.wisdom.acm.sys.po.SysUserPo;
import com.wisdom.acm.sys.service.*;
import com.wisdom.acm.sys.vo.*;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.SelectVo;
import com.wisdom.base.common.vo.UserInfo;
import com.wisdom.base.common.vo.UserVo;
import com.wisdom.base.common.vo.sys.UserOrgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "user")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService userServce;

    @Autowired
    private SysUserOrgService sysUserOrgService;

    @Autowired
    private SysUserIptService sysUserIptService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SysUserOrgRoleService sysUserOrgRoleService;

    /**
     * 给流程使用，在common
     * @param id
     * @return
     */
    @GetMapping(value = "/for/actTeamUsersOutUser")
    public ApiResult<List<GeneralVo>> queryTeamUsersOutUser(@RequestParam("id") Integer id) {
        List<GeneralVo> ge = sysUserOrgService.queryTeamUsersOutUser(id);
        return ApiResult.success(ge);
    }

    /**
     * 给流程使用，在common
     * @param userId
     * @return
     */
    @GetMapping(value = "/for/activiti")
    public ApiResult<GeneralVo> selectUserInfoForAct(@RequestParam("userId") String userId) {
        GeneralVo ge = userServce.selectUserInfoForAct(userId);
        return ApiResult.success(ge);
    }

    /**
     * 给流程使用，在common
     * @param userId
     * @return
     */
    @GetMapping(value = "/for/activitiMainOrg")
    public ApiResult<List<GeneralVo>> selectUserMainOrg(@RequestParam("userId") String userId) {
        List<GeneralVo> ge = sysUserOrgService.selectUserMainOrg(userId);
        return ApiResult.success(ge);
    }

    /**
     * 搜索用户列表
     *
     * @param sysUserSearchForm
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/list/{pageSize}/{currentPageNum}")
    public ApiResult queryUsers(SysUserSearchForm sysUserSearchForm, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) {
        PageInfo<SysUserVo> pageInfo = userServce.queryUserList(sysUserSearchForm, pageSize, currentPageNum);
        return new TableResultResponse(pageInfo);
    }

    /**
     * 搜索用户列表（根据orgId）
     *
     * @param sysOrgUserSearchForm
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/select/{orgId}/orgusers/{pageSize}/{currentPageNum}")
    public ApiResult queryUsersByOrg(SysOrgUserSearchForm sysOrgUserSearchForm, @PathVariable("orgId") Integer orgId, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) {
        PageInfo<SysUserVo> pageInfo = sysUserOrgService.queryUsersByOrgId(sysOrgUserSearchForm, pageSize, currentPageNum, orgId);
        return new TableResultResponse(pageInfo);
    }

    /**
     * 搜索用户列表（根据iptId）
     *
     * @param sysIptUserSearchForm
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/select/{iptId}/iptusers/{pageSize}/{currentPageNum}")
    public ApiResult queryUsersByIptId(SysIptUserSearchForm sysIptUserSearchForm, @PathVariable("iptId") Integer iptId, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) {
        PageInfo<SysIptUserVo> pageInfo = sysUserIptService.queryUsersByIptId(sysIptUserSearchForm, pageSize, currentPageNum, iptId);
        return new TableResultResponse(pageInfo);
    }


    /**
     * 角色管理用户列表（根据roleId）
     *
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/select/{roleId}/roleusers/{pageSize}/{currentPageNum}")
    public ApiResult queryUsersByRoleId(@PathVariable("roleId") Integer roleId, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) {
        PageInfo<SysUserVo> pageInfo = sysUserOrgRoleService.queryUsersByRoleId(roleId, pageSize, currentPageNum);
        return new TableResultResponse(pageInfo);
    }

    /**
     * 获取组织用户(全局)
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/{orgId}/select/list")
    public ApiResult queryGlobalUsersByOrgId(@PathVariable("orgId") Integer orgId) {
        List<SelectVo> list = userServce.queryUserSelectVosByOrgId(orgId);
        return ApiResult.success(list);
    }

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    @GetMapping(value = "/{userId}/info")
    public ApiResult getUserInfo(@PathVariable(value = "userId") Integer userId) {
        SysUserInfoVo sysUserInfoVo = userServce.getUserInfo(userId);
        return ApiResult.success(sysUserInfoVo);
    }

    /**
     * 增加用户
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/add")
    @AddLog(title = "增加用户" , module = LoggerModuleEnum.SM_USER)
    public ApiResult addUser(@RequestBody @Valid SysUserAddFrom user) {
        SysUserPo sysUserPo = userServce.addUser(user);
        SysUserVo sysUserVo = userServce.queryUserById(sysUserPo.getId());
        return ApiResult.success(sysUserVo);
    }

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @PutMapping(value = "/update")
    public ApiResult updateUser(@RequestBody @Valid SysUserUpdateFrom user) {
        SysUserPo sysUserPo = userServce.updateUser(user);
        SysUserVo sysUserVo = userServce.queryUserById(sysUserPo.getId());
        return ApiResult.success(sysUserVo);
    }

    /**
     * 批量增加用户
     *
     * @param userList
     * @return
     */
    @PostMapping(value = "/addBatch")
    @AddLog(title = "批量增加用户" , module = LoggerModuleEnum.SM_USER)
    public ApiResult addBatchUser(@RequestBody List<SysUserAddBatchFrom> userList) {
        String names = ListUtil.listToNames(userList, "actuName");
        this.setAcmLogger(new AcmLogger("批量增加用户,批量增加用户，名称为：" + names + ""));
        List<Integer> userIds = userServce.addUserBatch(userList);
        return ApiResult.success();
    }


    /**
     * 删除用户
     *
     * @param userIds
     * @return
     */
    @DeleteMapping(value = "/delete")
    @AddLog(title = "删除用户" , module = LoggerModuleEnum.SM_USER)
    public ApiResult deleteUser(@RequestBody List<Integer> userIds) {
        String names = userServce.queryUserNamesByIds(userIds);
        this.setAcmLogger(new AcmLogger("批量删除用户，用户名称如下："+names));
        userServce.deleteUser(userIds);
        return ApiResult.success();
    }


    /**
     * 根据用户信息获取
     *
     * @param userName
     * @return
     */
    @GetMapping(value = "/name/{userName}/info")
    public ApiResult<UserInfo> getUserInfoByName(@PathVariable(value = "userName") String userName) {
        UserInfo userInfo = userServce.getUserInfoByName(userName);
        return ApiResult.success(userInfo);
    }

    @GetMapping(value = "/code/{userCode}/info")
    public ApiResult<UserInfo> getUserInfoByCode(@PathVariable(value = "userCode") String userCode)
    {
        UserInfo userInfo = userServce.getUserInfoByCode(userCode);
        return ApiResult.success(userInfo);
    }

    /**
     * 用户登录验证
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    public @ResponseBody
    ApiResult<UserInfo> validate(@RequestBody Map<String, String> body) {
        //获取用户登录ip地址
        String userHost = body.get("userHost");
        UserInfo userInfo = userServce.validate(body.get("userName"), body.get("password"), userHost);
        return ApiResult.success(userInfo);
    }
    /**
     * 统一用户登录验证
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/validate/sso", method = RequestMethod.POST)
    public @ResponseBody
    ApiResult<UserInfo> ssoValidate(@RequestBody Map<String, String> body) {
        //获取用户登录ip地址
        String userHost = body.get("userHost");
        UserInfo userInfo = userServce.getUserInfoByCode(body.get("uid"));
        if(userInfo == null) {
            SysUserAddFrom form = new SysUserAddFrom();
            form.setUserName(body.get("userName"));
            form.setPassword("123456");
            form.setActuName(body.get("name"));
            form.setSex(1);
            form.setPassword(body.get("phone_number"));
            form.setStaffStatus(1);
            List<Integer> list = new ArrayList<Integer>();
            list.add(1080483);
            list.add(1080479);
            list.add(1080488);
            form.setRoles(list);
            form.setOrgId(1664414);
            form.setUserCode(body.get("uid"));
            SysUserPo sysUserPo = userServce.addUser(form);
            UserInfo userInfos = userServce.getUserInfoByCode(body.get("uid"));
            return ApiResult.success(userInfos);
        }else {
            return ApiResult.success(userInfo);
        }
    }
    /**
     * 用户单点登录验证
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/validate/sn", method = RequestMethod.POST)
    public @ResponseBody
    ApiResult<UserInfo> snValidate(@RequestBody Map<String, String> body) {
        //获取用户登录ip地址
        String userHost = body.get("userHost");
        UserInfo userInfo = userServce.snValidate(body.get("userName"), body.get("password"), userHost);
        return ApiResult.success(userInfo);
    }

    /**
     * 修改密码
     *
     * @return
     */
    @PutMapping(value = "validate/password")
    @AddLog(title = "修改密码" , module = LoggerModuleEnum.SM_USER)
    public ApiResult updatePassWord(@RequestBody SysUpdatePassWordForm sysUpdatePassWordForm) throws UnsupportedEncodingException {
        String userName = request.getHeader("userName");
        String actuName = URLDecoder.decode(request.getHeader("actuName"), "UTF-8");
        this.setAcmLogger(new AcmLogger("\""+actuName+"\"修改了密码"));
        userServce.updatePassWord(sysUpdatePassWordForm, userName);
        return ApiResult.success();
    }

    /**
     * 修改密码-筑享云同步用户
     *
     * @return
     */
    @PutMapping(value = "validate/password/zxy")
    @AddLog(title = "修改密码" , module = LoggerModuleEnum.SM_USER)
    public ApiResult updatePassWordForZxy(@RequestBody Map<String,String> map) throws UnsupportedEncodingException {
        SysUpdatePassWordForm sysUpdatePassWordForm = new SysUpdatePassWordForm();
        sysUpdatePassWordForm.setOldPassWord(map.get("oldPassWord"));
        sysUpdatePassWordForm.setNewPassWord(map.get("newPassWord"));
        String userName = map.get("userName");
        String actuName = userName;
        this.setAcmLogger(new AcmLogger("\""+actuName+"\"修改了密码"));
        System.out.println(userName+":修改了密码！"+"===>>"+map.get("newPassWord"));
        userServce.updatePassWord(sysUpdatePassWordForm, userName);
        return ApiResult.success();
    }

    /**
     * 重置密码
     *
     * @param userIds
     * @return
     */
    @PostMapping(value = "reset/password")
    @AddLog(title = "重置密码" , module = LoggerModuleEnum.SM_USER)
    public ApiResult resetPassword(@RequestBody List<Integer> userIds) {
        String userNames = userServce.queryUserNamesByIds(userIds);
        this.setAcmLogger(new AcmLogger("重置密码用户名称：" + userNames));
        userServce.resetPassword(userIds);
        return ApiResult.success();
    }

    /**
     * 锁定用户
     *
     * @param userIds
     * @return
     */
    @PostMapping(value = "lock")
    @AddLog(title = "锁定用户" , module = LoggerModuleEnum.SM_USER)
    public ApiResult lockUser(@RequestBody List<Integer> userIds) {
        String userNames = userServce.queryUserNamesByIds(userIds);
        this.setAcmLogger(new AcmLogger("锁定用户为：" + userNames));
        userServce.lockUser(userIds);
        return ApiResult.success();
    }

    /**
     * 解锁用户
     *
     * @param userIds
     * @return
     */
    @PostMapping(value = "unlock")
    @AddLog(title = "解锁用户" , module = LoggerModuleEnum.SM_USER)
    public ApiResult unlockUser(@RequestBody List<Integer> userIds) {
        String userNames = userServce.queryUserNamesByIds(userIds);
        this.setAcmLogger(new AcmLogger("解锁用户为：" + userNames));
        userServce.unlockUser(userIds);
        return ApiResult.success();
    }

    /**
     * 根据多个用户id获取用户对象
     *
     * @param userIds
     * @return
     */
    @PostMapping("/uservo/maps")
    public ApiResult getUserVoMapByUsers(@RequestBody List<Integer> userIds) {
        Map<Integer, UserVo> userVoMap = userServce.queryUserVoByUserIds(userIds);
        return ApiResult.success(userVoMap);
    }

    /**
     * 获取个人信息以及相应项目信息
     *
     * @param userId
     * @return
     */
    @GetMapping(value = "/{userId}/project/info")
    public ApiResult getUserProjectInfo(@PathVariable("userId") Integer userId) {
        SysUserInfoVo sysUserInfoVo = userServce.getUserProjectInfo(userId);
        return ApiResult.success(sysUserInfoVo);
    }

    /**
     * 获取所有用户
     *
     * @return
     */
    @GetMapping(value = "/all")
    public ApiResult queryAllUser() {
        List<SysAllUserVo> sysAllUserVos = userServce.queryAllUserList();
        return ApiResult.success(sysAllUserVos);
    }

    /**
     * 用户密级查询
     * @param searchForm
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @PostMapping(value = "/{pageSize}/{currentPageNum}/level/list")
    public ApiResult queryUserLevelList(@RequestBody UserLevelSearchForm searchForm, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum){
        PageInfo<UserLevelVo> pageInfo = userServce.queryUserLevelList(searchForm, pageSize, currentPageNum);
        return new TableResultResponse(pageInfo);
    }

    @PutMapping(value = "/level/update")
    public ApiResult updateUserLevel(@RequestBody UserLevelUpdateForm updateForm){
        SysUserPo sysUserPo = userServce.updateUserLevel(updateForm);
        return ApiResult.success(sysUserPo);
    }

    /**
     * 获取所有用户
     *
     * @return
     */
    @PostMapping(value = "/userpo/list")
    public ApiResult querySysAllUserPo(@RequestBody List<Integer> userIds) {
        List<UserVo> sysAllUserVos = userServce.querySysUserPoByIds(userIds);
        return ApiResult.success(sysAllUserVos);
    }

    /**
     * 获取所有用户
     *
     * @return
     */
    @GetMapping(value = "/userorg/{projectId}/list")
    public ApiResult queryUserOrgVos(@PathVariable(value = "projectId") Integer projectId) {
        List<UserOrgVo> userOrgVos = userServce.queryUserOrgByProjectId(projectId);
        return ApiResult.success(userOrgVos);
    }


}
