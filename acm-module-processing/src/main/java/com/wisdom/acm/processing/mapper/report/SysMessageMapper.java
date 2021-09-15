package com.wisdom.acm.processing.mapper.report;

import com.wisdom.acm.processing.po.report.SysMessageRecvPo;
import com.wisdom.acm.processing.po.report.SysMessageUserPo;
import com.wisdom.base.common.vo.GeneralVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

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
}
