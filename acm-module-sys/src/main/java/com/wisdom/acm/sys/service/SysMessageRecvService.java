package com.wisdom.acm.sys.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.form.*;
import com.wisdom.acm.sys.po.SysMessageRecvPo;
import com.wisdom.acm.sys.vo.*;
import com.wisdom.base.common.form.IdTypeForm;
import java.util.List;

public interface SysMessageRecvService {
    /**
     *  for activiti
     * @param sysMessageAddForm
     */
    Integer addMessageRecvForActivi(SysMessageAddForm sysMessageAddForm);

    /**
     * 收件箱列表
     * @param pageSize
     * @param currentPageNum
     * @param title
     * @return
     */
    PageInfo<SysMessageRecvVo> queryMsgRecvList(Integer userId,int pageSize, int currentPageNum, String title);

    /**
     * 发件箱列表
     * @param pageSize
     * @param currentPageNum
     * @param title
     * @return
     */
    PageInfo<SysMessageSendVo> queryMsgSendList(Integer userId,int pageSize, int currentPageNum, String title);

    /**
     * 重要消息列表
     * @param pageSize
     * @param currentPageNum
     * @param title
     * @return
     */
    PageInfo<SysMessageCollectVo> queryMsgCollectList(Integer userId,int pageSize, int currentPageNum, String title);

    /**
     * 已删除消息列表
     * @param pageSize
     * @param currentPageNum
     * @param title
     * @return
     */
    PageInfo<SysMessageDeletedVo> queryMsgDeletedList(Integer userId, int pageSize, int currentPageNum, String title);

    /**
     * 获取邮件信息
     * @param messageId
     * @return
     */
    SysMessageViewVo getMsgView(Integer messageId);

    /**
     * 发件箱收藏
     * @param messageId
     * @return
     */
    SysMessageRecvPo updateMessageCollectToOne(Integer messageId);

    /**
     * 发件箱取消收藏
     * @param messageId
     * @return
     */
    SysMessageRecvPo updateMessageCollectToZero(Integer messageId);

    /**
     * 发件箱删除
     * @param messageIds
     */
    void updateMessageDelToOne(List<Integer> messageIds);

    /**
     * 还原已删除
     * @param idTypeFormList
     * @return
     */
    void updateMessageDelToZero(List<IdTypeForm> idTypeFormList);

    /**
     * 草稿箱列表
     * @param pageSize
     * @param currentPageNum
     * @param title
     * @return
     */
    PageInfo<SysMessageDraftsVo> queryMsgDraftsList(Integer userId, int pageSize, int currentPageNum, String title);


    /**
     * 转发消息/新增/回复
     * @param sysMessageAddForm
     * @return
     */
    SysMessageRecvPo addMessageRecv(SysMessageAddForm sysMessageAddForm);

    /**
     *  保存到草稿箱
     * @param sysMessageDraftsAddForm
     * @return
     */
    SysMessageRecvPo addMessageDrafts(SysMessageDraftsAddForm sysMessageDraftsAddForm);

    /**
     *  编辑草稿箱
     * @param sysMessageDraftsUpdateForm
     */
    void updateMessageDrafts(SysMessageDraftsUpdateForm sysMessageDraftsUpdateForm);

    /**
     *  删除草稿
     * @param messageIds
     */
    void updateDel(List<Integer> messageIds);

    /**
     * 清除已删除
     * @param ids
     */
    void deleteMessage(List<Integer> ids);


    PageInfo<MyMessageVo> queryMyMessageList(MyMessageSearchForm myMessageSearchForm,Integer userId, int pageSize, int currentPageNum);

    //统计前7条我的消息
    MyNewestMsgVo queryMyMessage(Integer userId);

    //统计消息未读数和删除数
    SysMessageNumVo getMessageNum(Integer userId);
}
