package com.wisdom.acm.sys.service;

import com.wisdom.acm.sys.form.SysMessageUserForm;
import com.wisdom.acm.sys.po.SysMessageUserPo;
import java.util.List;

public interface SysMessageUserService {

    /**
     * 设置为已读
     * @param ids
     * @return
     */
    void updateMessageUserReadStatus(List<Integer> ids);

    /**
     * 收件箱收藏
     * @param id
     * @return
     */
    void updateMessageCollectToOne(Integer id);

    /**
     * 收件箱取消收藏
     * @param id
     * @return
     */
    void updateMessageCollectToZero(Integer id);

    /**
     * 删除（修改del=1）收件箱
     * @param ids
     * @return
     */
    void updateMessageUserDelToOne(List<Integer> ids);

    /**
     * 还原已删除
     * @param id
     */
    void updateMessageDelToZero(Integer id);
    /**
     *  保存到用户表中
     *  @param messageId
     *  @param recvUserIds
     *  @param copyUserIds
     */
    void addMessageUser(Integer messageId,List<Integer> recvUserIds,List<Integer> copyUserIds);

    /**
     *  保存到草稿箱用户表中
     *  @param messageId
     *  @param recvUserIds
     *  @param copyUserIds
     */
    void addMessageDraftsUser(Integer messageId,List<Integer> recvUserIds,List<Integer> copyUserIds);

    /**
     *  根据消息表id删除关联收件人信息
     * @param messageId
     */
    void deleteMessageUser(Integer messageId);

    /**
     * 增加 wsd_sys_message_user
     * @param sysMessageUserForm
     */
    void addSysMessageUser(SysMessageUserForm sysMessageUserForm);
}
