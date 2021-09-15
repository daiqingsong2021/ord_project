package com.wisdom.acm.sys.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.form.*;
import com.wisdom.acm.sys.po.SysUserPo;
import com.wisdom.acm.sys.vo.*;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.SelectVo;
import com.wisdom.base.common.vo.UserInfo;
import com.wisdom.base.common.vo.UserVo;
import com.wisdom.base.common.vo.sys.UserOrgVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysUserService extends CommService<SysUserPo> {
    /**
     * for activiti
     * @param userId
     * @return
     */
    GeneralVo selectUserInfoForAct(String userId);

    /**
     * 查询用户
     *
     * @param searchMap
     * @return
     */
    PageInfo<SysUserVo> queryUserList(SysUserSearchForm searchMap, Integer pageSize, Integer currentPageNum);

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    SysUserInfoVo getUserInfo(Integer userId);

    /**
     * 获取用户信息 -- 仅查询该用户所属公司或部门
     *
     * @param userId
     * @return
     */
    SysUserInfoVo getUserDept(Integer userId);


    SysUserPo updateUser(SysUserUpdateFrom sysUserUpdateVo);

    SysUserPo updateUserLastLoginTime(SysUserPo sysUserPo);

    /**
     * 从uuv同步数据 避免事务
     * @param sysUserUpdateFrom
     * @return
     */
    SysUserPo xiuGaiUser(SysUserUpdateFrom sysUserUpdateFrom);

    /**
     * 删除用户
     *
     * @param userIds
     * @return
     */
    void deleteUser(List<Integer> userIds);

    /**
     * 批量增加用户
     *
     * @param userList
     */
    List<Integer> addUserBatch(List<SysUserAddBatchFrom> userList);

    /**
     * 获取组织用户
     *
     * @param orgId
     * @return
     */
    List<SysUserVo> selectUsersByOrgId(Integer orgId);


    /**
     * 增加用户
     *
     * @param user
     */
    SysUserPo addUser(SysUserAddFrom user);

    /**
     * 从uuv增加用户 避免事务
     *
     * @param user
     */
    SysUserPo xinZengUser(SysUserAddFrom user);

    /**
     * 获取组织用户分页
     *
     * @param id
     * @param pageSize
     * @param curragenum
     * @return
     */
    PageInfo<SysUserVo> queryUsersByOrgIdPage(Integer id, Integer pageSize, Integer curragenum);

    PageInfo<SysIptUserVo> queryUsersByIptIdPage(Integer iptId, Integer pageSize, Integer currentPageNum);

    /**
     * 根据用户账号查询用户信息
     *
     * @param userName
     * @return
     */
    UserInfo getUserInfoByName(String userName);

    SysUserPo getUserPoByName(String userName);

    /**
     * 根据多个用户id获取用户信息
     * @param userIds
     * @return
     */
    List<SysUserVo> queryUsersByIds(List<Integer> userIds);

    /**
     * 获取用户列表vo
     * @param id
     * @return
     */
    SysUserVo queryUserById(Integer id);

    /**
     * 根据多个用户id获取用户对象
     * @param userIds
     * @return
     */
    Map<Integer, UserVo> queryUserVoByUserIds(List<Integer> userIds);

    /**
     * 用户登录验证
     * @param userName
     * @param password
     * @param hostIp
     * @return
     */
    UserInfo validate(String userName, String password, String hostIp);

    /**
     * 用户单点登录验证
     * @param userName
     * @param password
     * @param hostIp
     * @return
     */
    UserInfo snValidate(String userName, String password, String hostIp);

    /**
     * 修改密码
     * @param sysUpdatePassWordForm
     * @param userName
     */
    void updatePassWord(SysUpdatePassWordForm sysUpdatePassWordForm, String userName);

    /**
     * 重置密码
     * @param userIds
     */
    void resetPassword(List<Integer> userIds);

    /**
     * 锁定用户
     * @param userIds
     */
    void lockUser(List<Integer> userIds);

    /**
     * 解锁用户
     * @param userIds
     */
    void unlockUser(List<Integer> userIds);

    /**
     * 获取组织用户（全局）
     * @param orgId
     * @return
     */
    List<SelectVo> queryUserSelectVosByOrgId(Integer orgId);

    SysUserInfoVo getUserProjectInfo(Integer userId);

    List<SysAllUserVo> queryAllUserList();

    /**
     * 查找所有用户信息 包括admin
     * @return
     */
    List<SysUserVo> queryAllUser();

    void updUserPo(SysUserPo userPo);

    String queryUserNamesByIds(List<Integer> userIds);

    PageInfo<UserLevelVo> queryUserLevelList(UserLevelSearchForm searchForm, Integer pageSize, Integer currentPageNum);

    SysUserPo updateUserLevel(UserLevelUpdateForm updateForm);

    List<UserVo> querySysUserPoByIds(List<Integer> ids);

    void updateUserByUserIdAndUpOrDown(Integer id,String upOrDown);

    UserInfo getUserInfoByCode(String userCode);

    SysUserPo selectUserByUserName(String userName);

    /**
     * 根据项目ID查询所有的组织用户集合
     *
     * @param projectId
     * @return
     */
    List<UserOrgVo> queryUserOrgByProjectId(Integer projectId);
}
