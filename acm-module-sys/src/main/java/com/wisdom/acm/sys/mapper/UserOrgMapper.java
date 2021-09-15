package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.form.SysOrgUserSearchForm;
import com.wisdom.acm.sys.po.SysUserOrgPo;
import com.wisdom.acm.sys.vo.SysOrgUserTreeVo;
import com.wisdom.acm.sys.vo.SysOrgUserVo;
import com.wisdom.acm.sys.vo.SysUserVo;
import com.wisdom.base.common.mapper.CommMapper;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.sys.UserOrgVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserOrgMapper extends CommMapper<SysUserOrgPo> {

    /**
     * 查询外部用户同一公司/部门下的用户 for activiti
     * @param id
     * @return
     */
    List<GeneralVo> queryTeamUsersOutUser(@Param("id") Integer id);

    /**
     * 查找用户所在部门/组织 for activiti
     * @param userId
     * @return
     */
    List<GeneralVo> selectUserMainOrg(@Param("userId") String userId);

    /**
     * 获取用户部门信息
     * @param bizType null:全局，ipt:ipt，project:项目团队
     * @param userId 用户ID
     * @return
     */
    List<UserOrgVo> selectListByUserId(@Param("bizType") String bizType, @Param("userId") Integer userId);

    void deleteByOrgId(@Param("orgIds") List<Integer> orgIds);

    List<SysOrgUserVo> selectListByOrgId(@Param("orgId") Integer orgId);

    List<SysOrgUserVo> selectListByOrgIds(@Param("orgIds") List<Integer> orgIds);

    void deleteUserOrgRelationByUserIds(@Param("userIds") List<Integer> userIds);

    List<SysOrgUserTreeVo> selectOrgUsers();

    List<SysUserVo> selectUserByOrgId(@Param("searchForm") SysOrgUserSearchForm sysOrgUserSearchForm, @Param("orgId") Integer orgId);

    void deleteUserOrgRelationByUserIdsAndOrgId(@Param("userIds") List<Integer> userIds,@Param("orgId") Integer orgId);

    int selectUserOrgNextSort(@Param("orgId")  Integer orgId);
}
