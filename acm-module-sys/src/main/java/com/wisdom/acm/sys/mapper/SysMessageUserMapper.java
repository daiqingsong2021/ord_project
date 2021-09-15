package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.form.SysMessageUserForm;
import com.wisdom.acm.sys.po.SysMessageUserPo;
import com.wisdom.acm.sys.vo.SysMessageRecvUserVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMessageUserMapper extends CommMapper<SysMessageUserPo> {

    /**
     * msgid+recvid  获取msg_user_id
     * @param messageId
     * @param userId
     * @return
     */
    List<SysMessageRecvUserVo> selectMessageUserIdByMsgIdAndRecvId(@Param("messageId") Integer messageId,@Param("userId") Integer userId);

    /**
     * msgid+recvid  获取msg_user_ids
     * @param messageIds
     * @param userId
     * @return
     */
    List<SysMessageRecvUserVo> selectMessageUserIdsByMsgIdAndRecvId(@Param("messageIds")List<Integer> messageIds, @Param("userId") Integer userId);

    /**
     * 增加 wsd_sys_message_user
     * @param sysMessageUserPo
     */
    void addSysMessageUser(SysMessageUserPo sysMessageUserPo);
}

