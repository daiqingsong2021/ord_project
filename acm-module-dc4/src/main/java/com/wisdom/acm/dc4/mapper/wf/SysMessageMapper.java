package com.wisdom.acm.dc4.mapper.wf;

import com.wisdom.acm.dc4.po.SysMessageRecvPo;
import com.wisdom.acm.dc4.po.SysMessageUserPo;
import com.wisdom.base.common.vo.GeneralVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 考勤记录RecordMapper
 */
@Repository
public interface SysMessageMapper
{

    /**
     * 增加wsd_sys_message
     * @param sysMessageRecvPo
     */
    void addSysMessage(SysMessageRecvPo sysMessageRecvPo);

    /**
     * 增加 wsd_sys_message_user
     * @param sysMessageUserPo
     */
    void addSysMessageUser(SysMessageUserPo sysMessageUserPo);

    /**
     * 查询用户在某项目团队下的角色
     * @param projectId
     * @param userId
     * @return
     */
    List<GeneralVo> queryTeamRoles(@Param("projectId") Integer projectId, @Param("userId") Integer userId);

    /**
     * 查询标段下的用户
     * 改标段关联的业主代表 和标段所在单位的项目团队成员 和 该标段关联的监理单位的团队成员(如果是施工标的话)
     * @param sectionId
     * @return
     */
    List<GeneralVo> queryTeamUsers(@Param("sectionId") Integer sectionId);
    /**
     * 查询项目下的用户
     * 改标段关联的业主代表 和标段所在单位的项目团队成员 和 该标段关联的监理单位的团队成员(如果是施工标的话)
     * @param projectId
     * @return
     */
    List<GeneralVo> queryProjectUsers(@Param("projectId") Integer projectId);

    /**
     * 查询外部用户同一公司/部门下的用户
     * @param id
     * @return
     */
    List<GeneralVo> queryTeamUsersOutUser(@Param("id") Integer id);

    /**
     * 查询内部用户同一公司下的用户
     * @param id
     * @return
     */
    List<GeneralVo> queryCompanyInnerUsers(@Param("id") Integer id);

    /**
     * 查询内部用户所属公司
     * @param id
     * @return
     */
    List<GeneralVo> selectCompany(@Param("id") Integer id);

    /**
     * 查询用户信息 根据usercode判断内部、外部成员
     * @param userId
     * @return
     */
    GeneralVo selectUserInfo(@Param("userId") String userId);

    /**
     * 查找用户所在部门/组织
     * @param userId
     * @return
     */
    List<GeneralVo> selectUserMainOrg(@Param("userId") String userId);
}
