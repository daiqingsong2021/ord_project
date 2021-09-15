package com.wisdom.acm.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.enums.ClaimDealTypeEnum;
import com.wisdom.acm.sys.enums.MessageTypeEnums;
import com.wisdom.acm.sys.form.*;
import com.wisdom.acm.sys.mapper.SysMessageRecvMapper;
import com.wisdom.acm.sys.po.SysMessageRecvPo;
import com.wisdom.acm.sys.po.SysMessageRelationPo;
import com.wisdom.acm.sys.service.SysMessageRecvService;
import com.wisdom.acm.sys.service.SysMessageRelationService;
import com.wisdom.acm.sys.service.SysMessageUserService;
import com.wisdom.acm.sys.vo.*;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommFileService;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.feign.LeafService;
import com.wisdom.base.common.form.IdTypeForm;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SysMessageRecvServiceImpl extends BaseService<SysMessageRecvMapper, SysMessageRecvPo> implements SysMessageRecvService {

    @Autowired
    private CommFileService commFileService;

    @Autowired
    private CommUserService commUserService;

    @Autowired
    private SysMessageUserService messageUserService;

    @Autowired
    private SysMessageRelationService messageRelationService;

    @Autowired
    private LeafService leafService;

    @Override
    public Integer addMessageRecvForActivi(SysMessageAddForm sysMessageAddForm) {
        SysMessageRecvPo sysMessageRecvPo =dozerMapper.map(sysMessageAddForm,SysMessageRecvPo.class);
        //发件时间
        sysMessageRecvPo.setSendTime(new Date());
        //设置del值为0，普通消息
        sysMessageRecvPo.setDel(0);
        //设置消息类型
        sysMessageRecvPo.setType("SYSTEMNOTICE");
        //默认为未收藏
        sysMessageRecvPo.setCollect(0);
        sysMessageRecvPo.setOptType(0);
        sysMessageRecvPo.setParentId(0);
        sysMessageRecvPo.setSource(1);
        //插入wsd_sys_message
        Integer messageId = leafService.getId();
        sysMessageRecvPo.setId(messageId);
        mapper.addMessageRecvForActivi(sysMessageRecvPo);
        return messageId;
    }

    @Override
    public PageInfo<SysMessageRecvVo> queryMsgRecvList(Integer userId,int pageSize, int currentPageNum, String title) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<SysMessageRecvVo> list =  mapper.selectMsgRecvList(userId,title);

        for (SysMessageRecvVo sysMessageRecvVo:list) {
            //消息类型
            if(!ObjectUtils.isEmpty(sysMessageRecvVo.getType())){
                sysMessageRecvVo.getType().setName(MessageTypeEnums.getMessageByCode(sysMessageRecvVo.getType().getId()));
            }
            //需要回复类型
            if(!ObjectUtils.isEmpty(sysMessageRecvVo.getClaimDealType())){
                sysMessageRecvVo.getClaimDealType().setName((ClaimDealTypeEnum.getMessageByCode(sysMessageRecvVo.getClaimDealType().getId())));
            }
        }
        PageInfo<SysMessageRecvVo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public PageInfo<SysMessageSendVo> queryMsgSendList(Integer userId,int pageSize, int currentPageNum, String title) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<SysMessageSendVo> list =  mapper.selectMsgSendList(userId,title);

        if(!ObjectUtils.isEmpty(list)){
            //ids
            List<Integer> setMsgIds = new ArrayList<>();
            for(int i = 0; i < list.size(); i++){
                if(!setMsgIds.contains(list.get(i).getId())){
                    setMsgIds.add(list.get(i).getId());
                }
            }
            List<SysMessageRecvUserVo> recvUserVoList = mapper.selectRecvUsersByMsgIds(setMsgIds);

            for (int i = 0; i < setMsgIds.size(); i++) {
                List<UserVo> forSendVo = new ArrayList<>();
                for(int j = 0; j < recvUserVoList.size(); j++){
                    if(recvUserVoList.get(j).getMessageId().equals(setMsgIds.get(i))){
                        forSendVo.add(recvUserVoList.get(j).getRecvUser());
                    }
                }
                list.get(i).setRecvUser(forSendVo);

                //消息类型
                if(!ObjectUtils.isEmpty(list.get(i).getType())){
                    list.get(i).getType().setName(MessageTypeEnums.getMessageByCode(list.get(i).getType().getId()));
                }
                //需要回复类型
                if(!ObjectUtils.isEmpty(list.get(i).getClaimDealType())){
                    list.get(i).getClaimDealType().setName((ClaimDealTypeEnum.getMessageByCode(list.get(i).getClaimDealType().getId())));
                }
            }
        }

        PageInfo<SysMessageSendVo> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    @Override
    public PageInfo<SysMessageCollectVo> queryMsgCollectList(Integer userId, int pageSize, int currentPageNum, String title) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<SysMessageCollectVo> list =  mapper.selectMsgCollectList(userId,title);
        for (SysMessageCollectVo sysMessageCollectVo:list) {
            //消息类型
            if(!ObjectUtils.isEmpty(sysMessageCollectVo.getType())){
                sysMessageCollectVo.getType().setName(MessageTypeEnums.getMessageByCode(sysMessageCollectVo.getType().getId()));
            }
            //需要回复类型
            if(!ObjectUtils.isEmpty(sysMessageCollectVo.getClaimDealType())){
                sysMessageCollectVo.getClaimDealType().setName((ClaimDealTypeEnum.getMessageByCode(sysMessageCollectVo.getClaimDealType().getId())));
            }
        }
        PageInfo<SysMessageCollectVo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public PageInfo<SysMessageDeletedVo> queryMsgDeletedList(Integer userId, int pageSize, int currentPageNum, String title) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<SysMessageDeletedVo> list =  mapper.selectMsgDeletedList(userId, title);
        for (SysMessageDeletedVo sysMessageDeletedVo:list) {
            //消息类型
            if(!ObjectUtils.isEmpty(sysMessageDeletedVo.getType())){
                sysMessageDeletedVo.getType().setName(MessageTypeEnums.getMessageByCode(sysMessageDeletedVo.getType().getId()));
            }
            //需要回复类型
            if(!ObjectUtils.isEmpty(sysMessageDeletedVo.getClaimDealType())){
                sysMessageDeletedVo.getClaimDealType().setName((ClaimDealTypeEnum.getMessageByCode(sysMessageDeletedVo.getClaimDealType().getId())));
            }
        }
        PageInfo<SysMessageDeletedVo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public SysMessageViewVo getMsgView(Integer messageId) {
        SysMessageViewVo sysMessageViewVo = mapper.selectMsgView(messageId);
        if(ObjectUtils.isEmpty(sysMessageViewVo)){
            throw new BaseException("该条消息不存在!");
        }
        //获取收件人集合
        List<SysMessageRecvUserVo> sysMessageRecvUserVoList = this.mapper.selectRecvUsersByMsgId(messageId);
        if(!ObjectUtils.isEmpty(sysMessageRecvUserVoList)){
            List<UserVo> recvUserList = ListUtil.toValueList(sysMessageRecvUserVoList,"recvUser",UserVo.class);
            sysMessageViewVo.setRecvUser(recvUserList);
        }

        List<SysMessageRelationPo> sysMessageRelationPos = messageRelationService.querySysMessageRelationPos(messageId);
        List<Integer> fileIds = ListUtil.toValueList(sysMessageRelationPos,"bizId",Integer.class);
        if(!ObjectUtils.isEmpty(fileIds)){
            sysMessageViewVo.setFileIds(fileIds);
        }
        //需要回复类型
        if(!ObjectUtils.isEmpty(sysMessageViewVo.getClaimDealType())){
            sysMessageViewVo.getClaimDealType().setName((ClaimDealTypeEnum.getMessageByCode(sysMessageViewVo.getClaimDealType().getId())));
        }
        return sysMessageViewVo;
    }


    @Override
    public SysMessageRecvPo updateMessageCollectToOne(Integer messageId) {
        SysMessageRecvPo messagePo = this.selectById(messageId);
        messagePo.setCollect(1);
        this.updateById(messagePo);
        return messagePo;
    }

    @Override
    public SysMessageRecvPo updateMessageCollectToZero(Integer messageId) {
        SysMessageRecvPo messagePo = this.selectById(messageId);
        if (!Objects.isNull(messagePo)){
            messagePo.setCollect(0);
            this.updateById(messagePo);
        }
        return messagePo;
    }

    /**
     * 还原已删除
     * @param idTypeFormList
     * @return
     */
    @Override
    public void updateMessageDelToZero(List<IdTypeForm> idTypeFormList) {
        for (IdTypeForm idTypeForm : idTypeFormList){
            if("sendMessage".equals(idTypeForm.getType())){
                SysMessageRecvPo messagePo = this.selectById(idTypeForm.getId());
                messagePo.setDel(0);
                this.updateById(messagePo);
            }else if ("recvMessage".equals(idTypeForm.getType())){
                messageUserService.updateMessageDelToZero(idTypeForm.getId());
            }
        }
    }

    @Override
    public void updateMessageDelToOne(List<Integer> messageIds) {
        Example example = new Example(SysMessageRecvPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id",messageIds);
        List<SysMessageRecvPo> msgPoList = this.selectByExample(example);
        for (SysMessageRecvPo msgPo:msgPoList) {
            msgPo.setDel(1);
            this.updateById(msgPo);
        }
    }


    @Override
    public PageInfo<SysMessageDraftsVo> queryMsgDraftsList(Integer userId, int pageSize, int currentPageNum, String title) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<SysMessageDraftsVo> list =  mapper.selectMsgDraftsList(userId, title);
        if(!ObjectUtils.isEmpty(list)){
            //ids
            List<Integer> setMsgIds = ListUtil.toIdList(list);
            List<SysMessageRecvUserVo> recvUserVoList = mapper.selectRecvUsersByMsgIds(setMsgIds);

            for (int i = 0; i < setMsgIds.size(); i++) {
                List<UserVo> forSendVo = new ArrayList<>();
                for(int j = 0; j < recvUserVoList.size(); j++){
                    if(recvUserVoList.get(j).getMessageId().equals(setMsgIds.get(i))){
                        forSendVo.add(recvUserVoList.get(j).getRecvUser());
                    }
                }
                list.get(i).setRecvUser(forSendVo);
            }
            for (SysMessageDraftsVo sysMessageDraftsVo:list) {
                //消息类型
                if(!ObjectUtils.isEmpty(sysMessageDraftsVo.getType())){
                    sysMessageDraftsVo.getType().setName(MessageTypeEnums.getMessageByCode(sysMessageDraftsVo.getType().getId()));
                }
                //需要回复类型
                if(!ObjectUtils.isEmpty(sysMessageDraftsVo.getClaimDealType())){
                    sysMessageDraftsVo.getClaimDealType().setName((ClaimDealTypeEnum.getMessageByCode(sysMessageDraftsVo.getClaimDealType().getId())));
                }
            }
        }
        PageInfo<SysMessageDraftsVo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * 转发消息/新增/回复
     * @param sysMessageAddForm
     * @return
     */
    @Override
    public SysMessageRecvPo addMessageRecv(SysMessageAddForm sysMessageAddForm) {
        SysMessageRecvPo sysMessageRecvPo = dozerMapper.map(sysMessageAddForm,SysMessageRecvPo.class);
        if(ObjectUtils.isEmpty(sysMessageAddForm.getSource())){

            Integer userId = !ObjectUtils.isEmpty(commUserService.getLoginUser()) ? commUserService.getLoginUser().getId() : 0;
            //发件人
            sysMessageRecvPo.setSendUser(userId);
            //消息来源默认为用户（0用户/1系统）
            sysMessageRecvPo.setSource(0);
        }else{
            sysMessageRecvPo.setSource(sysMessageAddForm.getSource());
        }
        //发件时间
        sysMessageRecvPo.setSendTime(new Date());
        //设置del值为0，普通消息
        sysMessageRecvPo.setDel(0);
        //设置消息类型(type为空代表普通，否则为分发)
        if(Objects.isNull(sysMessageAddForm.getType())){
            sysMessageRecvPo.setType(MessageTypeEnums.COMMUNICATE.getCode());
        }else {
            sysMessageRecvPo.setType(MessageTypeEnums.DOC_OUTGIVING.getCode());
        }
        //默认为未收藏
        sysMessageRecvPo.setCollect(0);
        //保存
        this.insert(sysMessageRecvPo);
        //保存到草稿箱收件人表
        messageUserService.addMessageUser(sysMessageRecvPo.getId(),sysMessageAddForm.getRecvUser(),sysMessageAddForm.getCopyUser());
        //保存到附件中间表
        messageRelationService.addMessageRelation(sysMessageRecvPo.getId(),"file",sysMessageAddForm.getFileIds());

        return sysMessageRecvPo;
    }

    /**
     *  保存到草稿箱
     * @param sysMessageDraftsAddForm
     * @return
     */
    @Override
    public SysMessageRecvPo addMessageDrafts(SysMessageDraftsAddForm sysMessageDraftsAddForm) {

        Integer userId = !ObjectUtils.isEmpty(commUserService.getLoginUser()) ? commUserService.getLoginUser().getId() : 0;
        SysMessageRecvPo sysMessageRecvPo = dozerMapper.map(sysMessageDraftsAddForm,SysMessageRecvPo.class);

        //发件人
        sysMessageRecvPo.setSendUser(userId);
        //设置del值为2，代表保存到草稿箱
        sysMessageRecvPo.setDel(2);
        //消息来源默认为用户（0用户/1系统）
        sysMessageRecvPo.setSource(0);
        //设置消息类型
        sysMessageRecvPo.setType(MessageTypeEnums.COMMUNICATE.getCode());
        //默认为未收藏
        sysMessageRecvPo.setCollect(0);
        //保存
        this.insert(sysMessageRecvPo);
        //保存到草稿箱收件人表
        messageUserService.addMessageDraftsUser(sysMessageRecvPo.getId(),sysMessageDraftsAddForm.getRecvUser(),sysMessageDraftsAddForm.getCopyUser());
        //保存到附件中间表
        messageRelationService.addMessageRelation(sysMessageRecvPo.getId(),"file",sysMessageDraftsAddForm.getFileIds());

        return sysMessageRecvPo;
    }

    /**
     * 编辑草稿
     * @param sysMessageDraftsUpdateForm
     */
    @Override
    public void updateMessageDrafts(SysMessageDraftsUpdateForm sysMessageDraftsUpdateForm) {
        //获取当前编辑的草稿
        SysMessageRecvPo sysMessageRecvPo = this.selectById(sysMessageDraftsUpdateForm.getId());

        if(sysMessageRecvPo == null){
            throw new BaseException("修改的草稿不存在!");
        }
        // 修改数据marge到sysMessageRecvPo
        this.dozerMapper.map(sysMessageDraftsUpdateForm,sysMessageRecvPo);
        // 修改数据
        this.updateById(sysMessageRecvPo);

        //编辑草稿收件人
        messageUserService.deleteMessageUser(sysMessageDraftsUpdateForm.getId());
        messageUserService.addMessageDraftsUser(sysMessageDraftsUpdateForm.getId(),sysMessageDraftsUpdateForm.getRecvUser(),sysMessageDraftsUpdateForm.getCopyUser());
        //编辑附件
        messageRelationService.deleteMessageRelation(sysMessageDraftsUpdateForm.getId());
        messageRelationService.addMessageRelation(sysMessageDraftsUpdateForm.getId(),"file",sysMessageDraftsUpdateForm.getFileIds());

    }

    /**
     *  删除草稿
     * @param messageIds
     */
    @Override
    public void updateDel(List<Integer> messageIds) {
        for (Integer messageId : messageIds){
            ////获取当前编辑的草稿
            SysMessageRecvPo sysMessageRecvPo = this.selectById(messageId);
            //设置为删除(1代表删除)
            sysMessageRecvPo.setDel(1);
            //保存修改
            this.updateById(sysMessageRecvPo);
        }
    }

    /**
     * 清除已删除
     * @param ids
     */
    @Override
    public void deleteMessage(List<Integer> ids) {
        //删除消息
        this.deleteByIds(ids);
        for (Integer id : ids){
            //删除所有收件人
            messageUserService.deleteMessageUser(id);
            //删除所有文件关联
            messageRelationService.deleteMessageRelation(id);
        }
    }

    /**
     * 我的消息(首页)
     * @param myMessageSearchForm
     * @param userId
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @Override
    public PageInfo<MyMessageVo> queryMyMessageList(MyMessageSearchForm myMessageSearchForm, Integer userId, int pageSize, int currentPageNum) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<MyMessageVo> myMessageVoList = this.mapper.selectMyMessageList(userId,myMessageSearchForm);
        if (!ObjectUtils.isEmpty(myMessageVoList)){
            for (MyMessageVo myMessageVo : myMessageVoList){
                //消息类型
                if(!ObjectUtils.isEmpty(myMessageVo.getType())){
                    myMessageVo.getType().setName(MessageTypeEnums.getMessageByCode(myMessageVo.getType().getId()));
                }
                //需要回复类型
                if(!ObjectUtils.isEmpty(myMessageVo.getClaimDealType())){
                    myMessageVo.getClaimDealType().setName((ClaimDealTypeEnum.getMessageByCode(myMessageVo.getClaimDealType().getId())));
                }
            }
        }
        PageInfo<MyMessageVo> pageInfo = new PageInfo<MyMessageVo>(myMessageVoList);
        return pageInfo;
    }

    /**
     * 统计前7条我的消息
     * @param userId
     * @return
     */
    @Override
    public MyNewestMsgVo queryMyMessage(Integer userId) {
        MyNewestMsgVo myNewestMsgVo = new MyNewestMsgVo();
        List<MyNewestMessageVo> myMessageVoList = this.mapper.selectMyRecvMessageList(userId);
        myNewestMsgVo.setSize(myMessageVoList.size());
        //
        List<MyNewestMessageVo> returnMessageVoList = new ArrayList<MyNewestMessageVo>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!ObjectUtils.isEmpty(myMessageVoList)){
            for (int i = 0;i < myMessageVoList.size(); i++){
                //取前7条我的消息
                if (i<7){
                    //消息类型
                    if(!ObjectUtils.isEmpty(myMessageVoList.get(i).getType())){
                        myMessageVoList.get(i).getType().setName(MessageTypeEnums.getMessageByCode(myMessageVoList.get(i).getType().getId()));
                    }
                    //需要回复类型
                    if(!ObjectUtils.isEmpty(myMessageVoList.get(i).getClaimDealType())){
                        myMessageVoList.get(i).getClaimDealType().setName((ClaimDealTypeEnum.getMessageByCode(myMessageVoList.get(i).getClaimDealType().getId())));
                    }
                    //相隔时间
                    myMessageVoList.get(i).setDuration(this.getDatePoor(myMessageVoList.get(i).getRecvTime(), new Date()));
                    returnMessageVoList.add(myMessageVoList.get(i));

                }
            }
        }
       myNewestMsgVo.setDetailVos(returnMessageVoList);
       return myNewestMsgVo;
    }


    /**
     * 格式化时间
     * @param endDate
     * @param nowDate
     * @return
     */
    public String getDatePoor(Date endDate, Date nowDate) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = nowDate.getTime() - endDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;

        if(day != 0){
            return day + "天";
        }
        else if(hour!=0){
            return hour + "小时";
        }else{
            if(min <= 1){
                return "刚刚";
            }else{
                return min + "分钟";
            }

        }

    }


    /**
     * 未读消息数和删除数
     * @param userId
     * @return
     */
    @Override
    public SysMessageNumVo getMessageNum(Integer userId) {
        Integer unreadNum = this.mapper.selectUnreadNum(userId);
        Integer deletedNum = this.mapper.selectDeletedNum(userId);
        SysMessageNumVo sysMessageNumVo = new SysMessageNumVo();

        sysMessageNumVo.setUnreadNum(unreadNum);
        sysMessageNumVo.setDeletedNum(deletedNum);
        return sysMessageNumVo;
    }
}
