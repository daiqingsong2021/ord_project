package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.form.SysUserAddFrom;
import com.wisdom.acm.sys.form.SysUserSearchForm;
import com.wisdom.acm.sys.form.UserLevelSearchForm;
import com.wisdom.acm.sys.po.SysUserPo;
import com.wisdom.acm.sys.vo.*;
import com.wisdom.base.common.mapper.CommMapper;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.SelectVo;
import com.wisdom.base.common.vo.UserInfo;
import com.wisdom.base.common.vo.UserVo;
import com.wisdom.base.common.vo.sys.UserOrgVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserMapper extends CommMapper<SysUserPo> {

    /**
     * 查询用户信息 根据usercode判断内部、外部成员
     * @param userId
     * @return
     */
    GeneralVo selectUserInfoForAct(@Param("userId") String userId);

    /**
     * 根据类型查询用户组织集合
     *
     * @param bizType
     * @param bizId
     */
    List<UserOrgVo> selectUserOrgsByBiz(@Param("bizType") String bizType, @Param("bizId") Integer bizId);

    /**
     * 查询用户列表
     * @param searchMap
     * @return
     */
    List<SysUserVo> selectUsers(@Param("search") SysUserSearchForm searchMap);

    /**
     * 根据账号获取用户
     * @param userName
     * @return
     */
    SysUserPo selectUserByUserName(@Param("userName") String userName);
    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    SysUserInfoVo selectUserInfo(@Param("userId") Integer userId);

    /**
     * 根据用户，部门删除角色关系
     */
    void deleRoleUserByOrgId(@Param("userId") Integer userId,@Param("orgId") Integer orgId);

     /**
     * 删除角色关系
     */
    void deleRoleUser(@Param("userId") Integer userId);

    /**
     * 删除组织关系
     */
    void deleOrgUser(@Param("userId") Integer userId);


    /**
     * 批量增加用户
     * @param userList
     */
    void insertUserBatch(@Param("userList") List<SysUserAddFrom> userList);

    /**
     * 更新最后登录时间
     * @param sysUserPo
     */
    void updateUserLastLoginTime(@Param("sysUserPo")SysUserPo sysUserPo);

    /**
     * 获取组织用户
     * @param orgId
     * @return
     */
    List<SysUserVo> selectUsersByOrgId(@Param("orgId") Integer orgId);

    /**
     * 获取ipt用户
     * @param iptId
     * @return
     */
    List<SysIptUserVo> selectUsersByIptId(@Param("iptId")Integer iptId);

    /**
     * 根据多个用户id获取用户
     * @param userIds
     * @return
     */
    List<SysUserVo> selectUsersByIds(@Param("userIds") List<Integer> userIds);

    UserInfo getUserInfoByUsername(@Param("userName") String userName,@Param("pwd") String pwd);

    /**
     * 根据账号和密码获取用户
     * @param oldPwd
     * @param userName
     * @return
     */
    SysUserPo selectUserByUsernameAndPwd(@Param("oldpwd") String oldPwd,@Param("userName") String userName);

    List<GeneralVo> selectOrgsByUserId(@Param("userId") Integer userId);

    void updatePasswordByUserIds(@Param("userIds") List<Integer> userIds,@Param("date") Date date,@Param("pwd") String pwd);

    void updateLockUser(@Param("userIds") List<Integer> ids,@Param("date") Date date);

    void updateUnLockUser(@Param("userIds") List<Integer> ids,@Param("date") Date date);

    List<SelectVo> selectUserSelectByOrgId(@Param("orgId") Integer orgId);

    /**
     * 根据用户id集合获取用户对象
     * @param userIds
     * @return
     */
    List<UserVo> selectUserVoByUserIds(@Param("userIds") List<Integer> userIds);

    /**
     * 根据用户id获取项目信息
     * @param userId
     * @return
     */
    List<SysUserProjectInfoVo> getUserProjectInfoVos(@Param("userId") Integer userId);

    GeneralVo selectUserMainOrg(@Param("userId") Integer userId);

    void updateUserPwdErrorNumber();

    List<SysAllUserVo> selectAllUsers();

    List<SysUserVo> queryAllUser();

    SysUserVo selectUserByUserId(@Param("userId") Integer id);

    List<UserLevelVo> selectUserLevelList(@Param("searchForm") UserLevelSearchForm searchForm);
}

