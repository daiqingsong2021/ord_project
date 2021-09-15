package com.wisdom.base.common.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.po.SysUserPo;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 */
@FeignClient(value = "acm-module-sys", configuration = FeignConfiguration.class)
public interface CommUserService {

	/**
	 * 给流程使用，在common
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/user/for/actTeamUsersOutUser")
	public ApiResult<List<GeneralVo>> queryTeamUsersOutUser(@RequestParam("id") Integer id);

	/**
	 * 给流程使用，在common
	 * @param userId
	 * @return
	 */
	@GetMapping(value = "/user/for/activiti")
	public ApiResult<GeneralVo> selectUserInfoForAct(@RequestParam("userId") String userId);

	/**
	 * 给流程使用，在common
	 * @param userId
	 * @return
	 */
	@GetMapping(value = "/user/for/activitiMainOrg")
	public ApiResult<List<GeneralVo>> selectUserMainOrg(@RequestParam("userId") String userId);

	/**
	 * 获取当前登录用户信息
	 *
	 * @return
	 */
	default UserInfo getLoginUser() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		if (request != null) {
			Object userName = null;
			try {
				userName = URLDecoder.decode(request.getHeader("userName"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (!ObjectUtils.isEmpty(userName)) {
				return this.getUserInfoByName(String.valueOf(userName)).getData();
			}
		}
		return null;
	}

	/**
	 * 根据用户ID获取用户信息
	 *
	 * @return
	 */
	default UserVo getUserVoByUserId(Integer userId) {
		// 根据用户ID集合
		Map<Integer, UserVo> userVoMap = this.getUserVoMapByUserIds(ListUtil.toArrayList(userId));
		//
		if (userVoMap != null) {
			return userVoMap.get(userId);
		}
		return null;
	}

	/**
	 * 获取当前登录用户信息
	 *
	 * @return
	 */
	default Map<Integer, UserVo> getUserVoMapByUserIds(List<Integer> userIds) {
		// 根据任务信息
		ApiResult<Map<Integer, UserVo>> apiResult = this.getUserVoMapByUsers(userIds);
		//
		if (apiResult.getStatus() == 200) {
			return apiResult.getData();
		}

		return null;
	}

	/**
	 * @return
	 */
	default List<UserVo> getUserPoListByUserIds(List<Integer> userIds) {
		// 根据任务信息
		ApiResult<List<UserVo>> apiResult = this.querySysAllUserPo(userIds);
		//
		if (apiResult.getStatus() == 200) {
			return apiResult.getData();
		}

		return null;
	}


	/**
	 * 根据用户名读取用户信息
	 *
	 * @param userName
	 * @return
	 */
	@GetMapping(value = "/user/name/{userName}/info")
	public ApiResult<UserInfo> getUserInfoByName(@PathVariable("userName") String userName);

	/**
	 * 根据userCode查询用户信息
	 * @param userCode
	 * @return
	 */
	@GetMapping(value = "/user/code/{userCode}/info")
	public ApiResult<UserInfo> getUserInfoByCode(@PathVariable("userCode") String userCode);

	/**
	 * 根据权限代码，用户ID查询拥有权限的所在OrgId集合
	 *
	 * @param funcCode
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/auth/{funcCode}/{userId}/orgId/list", method = RequestMethod.POST)
	public ApiResult<List<Integer>> queryOrgIdsByUserIdAndFuncCode(@PathVariable("funcCode") String funcCode, @PathVariable("userId") Integer userId);

	/**
	 * 根据组织id获取组织对象
	 *
	 * @param userIds
	 * @return
	 */
	@RequestMapping(value = "/user/uservo/maps", method = RequestMethod.POST)
	ApiResult<Map<Integer, UserVo>> getUserVoMapByUsers(@RequestBody List<Integer> userIds);

	/**
	 * 获取所有用户id与真实姓名集合
	 *
	 * @return
	 */
	@RequestMapping(value = "/user/all", method = RequestMethod.GET)
	ApiResult<List<SysAllUserVo>> queryAllUser();

	/**
	 * 根据角色ID查找
	 *
	 * @param roleIds
	 * @return
	 */
	@RequestMapping(value = "/role/sysrolevo/maps", method = RequestMethod.POST)
	ApiResult<Map<Integer, RoleVo>> getSysRoleVoMapByIds(@RequestBody List<Integer> roleIds);

	/**
	 * 根据用户ID查找用户
	 *
	 * @param userIds
	 * @return
	 */
	@RequestMapping(value = "/userrole/userrolevo/maps", method = RequestMethod.POST)
	ApiResult<Map<Integer, UserRoleVo>> getUserRoleVoMapByIds(@RequestBody List<Integer> userIds);

	/**
	 * 根据角色ID查找用户角色
	 *
	 * @param roleIds
	 * @return
	 */
	@RequestMapping(value = "/userrole/roleuservo/maps", method = RequestMethod.POST)
	ApiResult<Map<Integer, RoleUserVo>> getRoleUserVoMapByIds(@RequestBody List<Integer> roleIds);


	/**
	 * 根据权限代码，用户ID查询拥有权限的所在OrgId集合
	 *
	 * @return
	 */
	default Map<String, List<String>> getOrgFuncsMapByUserId(Integer userId) {

		// 根据任务信息
		ApiResult<Map<String, List<String>>> apiResult = this.getOrgFuncsByUserId(userId);
		//
		if (apiResult.getStatus() == 200) {
			return apiResult.getData();
		}

		return new HashMap<>();
	}

	/**
	 * 根据权限代码，用户ID查询拥有权限的所在OrgId集合
	 *
	 * @return
	 */
	@GetMapping(value = "/auth/user/{userId}/org/auth/map")
	ApiResult<Map<String, List<String>>> getOrgFuncsByUserId(@PathVariable(name = "userId") Integer userId);


	/**
	 * 根据组织id获取组织对象
	 *
	 * @param userIds
	 * @return
	 */
	@RequestMapping(value = "/user/userpo/list", method = RequestMethod.POST)
	ApiResult<List<UserVo>> querySysAllUserPo(@RequestBody List<Integer> userIds);

}
