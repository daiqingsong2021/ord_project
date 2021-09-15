package com.wisdom.acm.sys.service.impl;

import com.wisdom.acm.sys.form.SysMessageUserForm;
import com.wisdom.acm.sys.mapper.SysMessageUserMapper;
import com.wisdom.acm.sys.po.SysMessageUserPo;
import com.wisdom.acm.sys.service.SysMessageUserService;
import com.wisdom.acm.sys.vo.SysMessageRecvUserVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.LeafService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SysMessageUserServiceImpl extends BaseService<SysMessageUserMapper, SysMessageUserPo> implements SysMessageUserService {
    @Autowired
    private LeafService leafService;
    @Override
    public void updateMessageUserReadStatus(List<Integer> ids) {
        if (!ObjectUtils.isEmpty(ids)){
            List<SysMessageUserPo> sysMessageUserPoList = this.selectByIds(ids);
            if (!ObjectUtils.isEmpty(sysMessageUserPoList)){
                //设置为已读
                for (SysMessageUserPo userPo:sysMessageUserPoList) {
                    userPo.setRealStatus(1);
                    this.updateById(userPo);
                }
            }
        }
    }

    @Override
    public void updateMessageCollectToOne(Integer id) {
        SysMessageUserPo userPo = this.selectById(id);
        userPo.setCollect(1);
        this.updateById(userPo);
    }

    @Override
    public void updateMessageCollectToZero(Integer id) {
        SysMessageUserPo userPo = this.selectById(id);
        userPo.setCollect(0);
        this.updateById(userPo);
    }

    @Override
    public void updateMessageDelToZero(Integer id) {
        //根据messageId,userId  获取  message_user_id
        SysMessageUserPo sysMessageUserPo = this.selectById(id);
        sysMessageUserPo.setDel(0);
        this.updateById(sysMessageUserPo);
    }

    @Override
    public void updateMessageUserDelToOne(List<Integer> ids) {
        if (!ObjectUtils.isEmpty(ids)){
            List<SysMessageUserPo> sysMessageUserPoList = this.selectByIds(ids);
            if (!ObjectUtils.isEmpty(sysMessageUserPoList)){
                for (SysMessageUserPo userPo : sysMessageUserPoList) {
                    userPo.setDel(1);
                    this.updateById(userPo);
                }
            }
        }
    }

    /**
     *  增加到用户表
     * @param messageId
     * @param recvUserIds
     * @param copyUserIds
     * @return
     */
    @Override
    public void addMessageUser(Integer messageId, List<Integer> recvUserIds, List<Integer> copyUserIds) {
        //保存所有收件人
        if(!ObjectUtils.isEmpty(recvUserIds)){
            for (Integer recvUser : recvUserIds){
                SysMessageUserPo sysMessageUserPo = new SysMessageUserPo();
                //消息id
                sysMessageUserPo.setMessageId(messageId);
                //收件人
                sysMessageUserPo.setRecvUser(recvUser);
                //收件时间
                sysMessageUserPo.setRecvTime(new Date());
                //收件人类型(1代表收件人)
                sysMessageUserPo.setType(1);
                //未删除
                sysMessageUserPo.setDel(0);
                //未收藏
                sysMessageUserPo.setCollect(0);
                //未读
                sysMessageUserPo.setRealStatus(0);
                //保存
                this.insert(sysMessageUserPo);
            }
        }
        //保存所有抄送人
        if (!ObjectUtils.isEmpty(copyUserIds)){
            for (Integer copyUser : copyUserIds){
                SysMessageUserPo sysMessageUserPo = new SysMessageUserPo();
                //消息id
                sysMessageUserPo.setMessageId(messageId);
                //收件人
                sysMessageUserPo.setRecvUser(copyUser);
                //收件时间
                sysMessageUserPo.setRecvTime(new Date());
                //收件人类型(2代表抄送人)
                sysMessageUserPo.setType(2);
                //未删除
                sysMessageUserPo.setDel(0);
                //未收藏
                sysMessageUserPo.setCollect(0);
                //未读
                sysMessageUserPo.setRealStatus(0);
                //保存
                this.insert(sysMessageUserPo);
            }
        }
    }

    /**
     *  增加到草稿箱用户表
     * @param messageId
     * @param recvUserIds
     * @param copyUserIds
     * @return
     */
    @Override
    public void addMessageDraftsUser(Integer messageId, List<Integer> recvUserIds, List<Integer> copyUserIds) {
        //保存所有收件人
        if (!ObjectUtils.isEmpty(recvUserIds)) {
            for (Integer recvUser : recvUserIds) {
                SysMessageUserPo sysMessageUserPo = new SysMessageUserPo();
                //消息id
                sysMessageUserPo.setMessageId(messageId);
                //收件人
                sysMessageUserPo.setRecvUser(recvUser);
                //收件人类型(1代表收件人)
                sysMessageUserPo.setType(1);
                //未读
                sysMessageUserPo.setRealStatus(0);
                //保存
                this.insert(sysMessageUserPo);
            }
        }
        //保存所有抄送人
        if (!ObjectUtils.isEmpty(copyUserIds)) {
            for (Integer copyUser : copyUserIds){
                SysMessageUserPo sysMessageUserPo = new SysMessageUserPo();
                //消息id
                sysMessageUserPo.setMessageId(messageId);
                //收件人
                sysMessageUserPo.setRecvUser(copyUser);
                //收件人类型(2代表抄送人)
                sysMessageUserPo.setType(2);
                //未读
                sysMessageUserPo.setRealStatus(0);
                //保存
                this.insert(sysMessageUserPo);
            }
        }
    }
    /**
     *  根据消息表id删除关联收件人信息
     * @param messageId
     */
    @Override
    public void deleteMessageUser(Integer messageId) {
        Example example = new Example(SysMessageUserPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("messageId", messageId);
        //获取消息关联的所有的收件人信息
        List<SysMessageUserPo> list = this.mapper.selectByExample(example);
        //获取收件人表id集合
        List<Integer> ids = ListUtil.toIdList(list);
        this.deleteByIds(ids);
    }

    /**
     * 增加 wsd_sys_message_user    for activiti
     * @param sysMessageUserForm
     */
    @Override
    public void addSysMessageUser(SysMessageUserForm sysMessageUserForm) {
        SysMessageUserPo sysMessageUserPo=dozerMapper.map(sysMessageUserForm,SysMessageUserPo.class);
        //收件时间
        sysMessageUserPo.setRecvTime(new Date());
        //收件人类型(1代表收件人)
        sysMessageUserPo.setType(1);
        //未删除
        sysMessageUserPo.setDel(0);
        //未收藏
        sysMessageUserPo.setCollect(0);
        //未读
        sysMessageUserPo.setRealStatus(0);
        sysMessageUserPo.setId(leafService.getId());
        mapper.addSysMessageUser(sysMessageUserPo);
    }
}
