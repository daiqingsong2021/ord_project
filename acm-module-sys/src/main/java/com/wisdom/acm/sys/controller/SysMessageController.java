package com.wisdom.acm.sys.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.form.*;
import com.wisdom.acm.sys.po.SysMessageRecvPo;
import com.wisdom.acm.sys.service.SysMessageRecvService;
import com.wisdom.acm.sys.service.SysMessageUserService;
import com.wisdom.acm.sys.vo.*;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.form.IdTypeForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class SysMessageController {

    @Autowired
    private SysMessageRecvService messageRecvService;

    @Autowired
    private SysMessageUserService messageUserService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private CommUserService userService;

    /**
     * @param sysMessageUserForm    for activiti
     * @return
     */
    @PutMapping("/message/addMesUserForActivi")
    public ApiResult addMesUserForActivi(@RequestBody SysMessageUserForm sysMessageUserForm){
        messageUserService.addSysMessageUser(sysMessageUserForm);
        return ApiResult.success();
    }

    /**
     * @param sysMessageAddForm    for activiti
     * @return
     */
    @PutMapping("/message/addMesRecvForActivi")
    public ApiResult<Integer> addMessageRecvForActivi(@RequestBody SysMessageAddForm sysMessageAddForm){
        Integer id =messageRecvService.addMessageRecvForActivi(sysMessageAddForm);
        return ApiResult.success(id);
    }

    /**
     * 收件箱
     * @param pageSize
     * @param currentPageNum
     * @param title
     * @return
     */
    @GetMapping(value = {"/message/recv/{pageSize}/{currentPageNum}/{title}","/message/recv/{pageSize}/{currentPageNum}"})
    public ApiResult queryMsgRecvList(@PathVariable("pageSize") int pageSize, @PathVariable("currentPageNum") int currentPageNum,
                                      @PathVariable(value = "title",required = false) String title){
        Integer userId = Integer.valueOf(request.getHeader("userId"));
        PageInfo<SysMessageRecvVo> retList =  messageRecvService.queryMsgRecvList(userId,pageSize,currentPageNum,title);
        return new TableResultResponse(retList);
    }

    /**
     * 发件箱
     * @param pageSize
     * @param currentPageNum
     * @param title
     * @return
     */
    @GetMapping(value = {"/message/send/{pageSize}/{currentPageNum}/{title}","/message/send/{pageSize}/{currentPageNum}"})
    public ApiResult queryMsgSendList(@PathVariable("pageSize") int pageSize, @PathVariable("currentPageNum") int currentPageNum,
                                      @PathVariable(value = "title",required = false) String title){
        Integer userId = Integer.valueOf(request.getHeader("userId"));
        PageInfo<SysMessageSendVo> retList =  messageRecvService.queryMsgSendList(userId,pageSize,currentPageNum,title);
        return new TableResultResponse(retList);
    }

    /**
     * 重要消息列表
     * @param pageSize
     * @param currentPageNum
     * @param title
     * @return
     */
    @GetMapping(value = {"/message/collect/{pageSize}/{currentPageNum}/{title}","/message/collect/{pageSize}/{currentPageNum}"})
    public ApiResult queryMsgCollectList(@PathVariable("pageSize") int pageSize, @PathVariable("currentPageNum") int currentPageNum,
                                         @PathVariable(value = "title",required = false) String title){
        Integer userId = Integer.valueOf(request.getHeader("userId"));
        PageInfo<SysMessageCollectVo> retList =  messageRecvService.queryMsgCollectList(userId,pageSize,currentPageNum,title);
        return new TableResultResponse(retList);
    }

    /**
     * 已删除消息列表
     * @param pageSize
     * @param currentPageNum
     * @param title
     * @return
     */
    @GetMapping(value = {"/message/deleted/{pageSize}/{currentPageNum}/{title}","/message/deleted/{pageSize}/{currentPageNum}"})
    public ApiResult queryMsgDeletedList(@PathVariable("pageSize") int pageSize, @PathVariable("currentPageNum") int currentPageNum,
                                         @PathVariable(value = "title",required = false) String title){
        Integer userId = Integer.valueOf(request.getHeader("userId"));
        PageInfo<SysMessageDeletedVo> retList =  messageRecvService.queryMsgDeletedList(userId,pageSize,currentPageNum,title);
        return new TableResultResponse(retList);
    }

    /**
     * 草稿箱消息列表
     * @param pageSize
     * @param currentPageNum
     * @param title
     * @return
     */
    @GetMapping(value = {"/message/drafts/{pageSize}/{currentPageNum}/{title}","/message/drafts/{pageSize}/{currentPageNum}"})
    public ApiResult queryMsgDraftsList(@PathVariable("pageSize") int pageSize, @PathVariable("currentPageNum") int currentPageNum,
                                        @PathVariable(value = "title",required = false) String title){
        Integer userId = Integer.valueOf(request.getHeader("userId"));
        PageInfo<SysMessageDraftsVo> retList =  messageRecvService.queryMsgDraftsList(userId, pageSize,currentPageNum,title);
        return new TableResultResponse(retList);
    }

    /**
     * 获取邮件信息
     * @param messageId
     * @return
     */
    @GetMapping("/message/{messageId}/view")
    public ApiResult getMsgView(@PathVariable("messageId") Integer messageId){

        return ApiResult.success(messageRecvService.getMsgView(messageId));
    }

    /**
     * 设置为已读
     * @param ids
     * @return
     */
    @PutMapping("/message/read")
    public ApiResult updateMessageUserReadStatus(@RequestBody List<Integer> ids){
        Integer userId = Integer.valueOf(request.getHeader("userId"));

        messageUserService.updateMessageUserReadStatus(ids);
        return ApiResult.success();
    }

    /**
     * 发件箱/收件箱  收藏
     * @param id
     * @param mailType
     * @return
     */
    @PutMapping("/message/{id}/{mailType}/collect")
    public ApiResult updateMessageCollectToOne(@PathVariable("id") Integer id,@PathVariable("mailType")String mailType){
        Integer userId = Integer.valueOf(request.getHeader("userId"));

        //mailType  发件箱sendMessage   收件箱recvMessage
        if(mailType.equals("sendMessage")){
            //收藏发件箱
            SysMessageRecvPo messagePo = messageRecvService.updateMessageCollectToOne(id);
        }
        if(mailType.equals("recvMessage")){
            //收藏收件箱   获取当前登陆用户userid
            messageUserService.updateMessageCollectToOne(id);
        }
        return ApiResult.success();
    }

    /**
     * 发件箱/收件箱 取消收藏
     * @param id
     * @param mailType
     * @return
     */
    @PutMapping("/message/{id}/{mailType}/cancleCollect")
    public ApiResult updateMessageCollectToZero(@PathVariable("id") Integer id,@PathVariable("mailType")String mailType){
        Integer userId = Integer.valueOf(request.getHeader("userId"));

        //mailType  发件箱sendMessage   收件箱recvMessage
        if(mailType.equals("sendMessage")){
            //取消收藏发件箱
            SysMessageRecvPo messagePo = messageRecvService.updateMessageCollectToZero(id);
        }
        if(mailType.equals("recvMessage")){
            //取消收藏收件箱   获取当前登陆用户userid
            messageUserService.updateMessageCollectToZero(id);
        }
        return ApiResult.success();
    }

    /**
     * 删除收件箱（修改del=1）
     * @param ids
     * @return
     */
    @PutMapping("/message/recv/delete")
    public ApiResult updateMessageUserDelToOne(@RequestBody List<Integer> ids){
        Integer userId = Integer.valueOf(request.getHeader("userId"));

        messageUserService.updateMessageUserDelToOne(ids);
        return ApiResult.success();
    }

    /**
     * 删除发件箱（修改del=1）
     * @param messageIds
     * @return
     */
    @PutMapping("/message/send/delete")
    public ApiResult updateMessageDelToOne(@RequestBody List<Integer> messageIds){
        messageRecvService.updateMessageDelToOne(messageIds);
        return ApiResult.success();
    }

    /**
     *  转发消息/新增/回复
     * @param sysMessageAddForm
     * @return
     */
    @PostMapping("/message/write")
    public ApiResult sendMessageRecv(@RequestBody SysMessageAddForm sysMessageAddForm){
        //保存到草稿箱
        messageRecvService.addMessageRecv(sysMessageAddForm);
        return ApiResult.success();
    }

    /**
     *  保存为草稿箱
     * @param sysMessageDraftsAddForm
     * @return
     */
    @PostMapping("/message/drafts/add")
    public ApiResult addMessageDrafts(@RequestBody SysMessageDraftsAddForm sysMessageDraftsAddForm){
        //保存到草稿箱
        SysMessageRecvPo sysMessageRecvPo = messageRecvService.addMessageDrafts(sysMessageDraftsAddForm);
        return ApiResult.success();
    }

    /**
     *  编辑草稿
     * @param sysMessageDraftsUpdateForm
     * @return
     */
    @PutMapping("/message/drafts/update")
    public ApiResult editMessageDrafts(@RequestBody SysMessageDraftsUpdateForm sysMessageDraftsUpdateForm){
        //编辑草稿
        messageRecvService.updateMessageDrafts(sysMessageDraftsUpdateForm);
        return ApiResult.success();
    }

    /**
     *  移入到删除
     * @param messageIds
     * @return
     */
    @PutMapping("/message/drafts/delete")
    public ApiResult updateDel(@RequestBody List<Integer> messageIds){
        messageRecvService.updateDel(messageIds);
        return ApiResult.success();
    }

    /**
     * 发件箱/收件箱 还原已删除
     * @param idTypeFormList
     * @return
     */
    @PutMapping("/message/cancleDel")
    public ApiResult cancleDel(@RequestBody List<IdTypeForm> idTypeFormList){
        messageRecvService.updateMessageDelToZero(idTypeFormList);
        return ApiResult.success();
    }

    /**
     *  清空已删除
     * @param ids
     * @return
     */
    @DeleteMapping("/message/delete")
    public ApiResult deleteMessage(@RequestBody List<Integer> ids){
        messageRecvService.deleteMessage(ids);
        return ApiResult.success();
    }

    /**
     *  我的消息（首页）
     * @param myMessageSearchForm
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @PostMapping("/message/home/{pageSize}/{currentPageNum}/list")
    public ApiResult queryMyMessageList(@RequestBody MyMessageSearchForm myMessageSearchForm,@PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum){
        Integer userId = Integer.valueOf(request.getHeader("userId"));
        PageInfo<MyMessageVo> myMessageVoVoList = messageRecvService.queryMyMessageList(myMessageSearchForm,userId,pageSize,currentPageNum);
        return new TableResultResponse(myMessageVoVoList);
    }


    /**
     *  我的消息（右上角图标下拉展示）
     * @return
     */
    @PostMapping("/message/mine/newest/list")
    public ApiResult queryMyMessageList(){
        Integer userId = Integer.valueOf(request.getHeader("userId"));
        MyNewestMsgVo myNewestMsgVo = messageRecvService.queryMyMessage(userId);
        return ApiResult.success(myNewestMsgVo);
    }



    /**
     * 查询我的未读消息数和已删除数
     * @return
     */
    @GetMapping("/message/num")
    public ApiResult getMessageNum(){
        Integer userId = Integer.valueOf(request.getHeader("userId"));
        SysMessageNumVo sysMessageNumVo = messageRecvService.getMessageNum(userId);
        return ApiResult.success(sysMessageNumVo);
    }


}
