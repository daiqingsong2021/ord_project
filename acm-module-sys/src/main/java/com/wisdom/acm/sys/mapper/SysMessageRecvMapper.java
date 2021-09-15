package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.form.MyMessageSearchForm;
import com.wisdom.acm.sys.po.SysMessageRecvPo;
import com.wisdom.acm.sys.vo.*;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMessageRecvMapper extends CommMapper<SysMessageRecvPo> {
    /**
     * 增加wsd_sys_message  for activiti
     * @param sysMessageRecvPo
     */
    void addMessageRecvForActivi(SysMessageRecvPo sysMessageRecvPo);
    /**
     * 收件箱
     * @param title
     * @return
     */
    List<SysMessageRecvVo> selectMsgRecvList(@Param("userId")Integer userId,@Param("title") String title);

    /**
     * 发件箱
     * @param title
     * @return
     */
    List<SysMessageSendVo> selectMsgSendList(@Param("userId")Integer userId,@Param("title") String title);

    /**
     * 重要消息列表
     * @param title
     * @return
     */
    List<SysMessageCollectVo> selectMsgCollectList(@Param("userId") Integer userId,@Param("title") String title);

    /**
     * 已删除消息列表
     * @param title
     * @return
     */
    List<SysMessageDeletedVo> selectMsgDeletedList(@Param("userId") Integer userId,@Param("title") String title);

    /**
     * 获取邮件信息
     * @param messageId
     * @return
     */
    SysMessageViewVo selectMsgView(@Param("messageId") Integer messageId);

    /**
     *  获取草稿箱列表
     * @param title
     * @return
     */
    List<SysMessageDraftsVo> selectMsgDraftsList(@Param("userId") Integer userId,@Param("title") String title);

    /**
     * 获取发件箱 收件人信息  根据msgId
     * @param msgIds
     * @return
     */
    List<SysMessageRecvUserVo> selectRecvUsersByMsgIds(@Param("msgIds") List<Integer> msgIds);
    /**
     * 获取发件箱 收件人信息  根据msgId
     * @param msgId
     * @return
     */
    List<SysMessageRecvUserVo> selectRecvUsersByMsgId(@Param("msgId") Integer msgId);


    /**
     * 获取邮件对应的文件  mesIDs
     * @param msgId
     * @return
     */
    List<DocForMessageVo> selectDocVosByMsgIds(@Param("msgId") Integer msgId);

    List<MyMessageVo> selectMyMessageList(@Param("userId") Integer userId,@Param("searchMap") MyMessageSearchForm searchMap);

    /**
     * 查询收件箱未读消息数
     * @param userId
     * @return
     */
    Integer selectUnreadNum(@Param("userId") Integer userId);

    /**
     * 查询删除消息数
     * @param userId
     * @return
     */
    Integer selectDeletedNum(@Param("userId") Integer userId);


    /**
     * 查询未读消息数
     * @param userId
     * @return
     */
    List<MyNewestMessageVo> selectMyRecvMessageList(@Param("userId") Integer userId);
}
