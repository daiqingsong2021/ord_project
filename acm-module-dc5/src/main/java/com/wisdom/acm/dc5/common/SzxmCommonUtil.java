package com.wisdom.acm.dc5.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.dc5.mapper.menu.MenuMapper;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.feign.CommActivitiService;
import com.wisdom.base.common.feign.CommMessageService;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.form.ActivSysMessageAddForm;
import com.wisdom.base.common.form.SysMessageUserForm;
import com.wisdom.base.common.form.WfRuningProcessForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.UserVo;
import com.wisdom.base.common.vo.wf.WfActivityVo;
import com.wisdom.base.common.vo.wf.WfCandidateGroupVo;
import com.wisdom.base.common.vo.wf.WfCandidateUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 苏州项目工具类
 */
@Component
public class SzxmCommonUtil {

    private static final Logger logger = LoggerFactory.getLogger(SzxmCommonUtil.class);

    /**
     * 用户基本信息服务
     */
    @Autowired
    private CommUserService commUserService;

    @Autowired
    private CommMessageService commMessageService;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private  CommActivitiService commActivitiService;

    /**
     * 发送推送消息
     *
     * @param sysMessageAddForm
     */
    public void sendMessageRecv(ActivSysMessageAddForm sysMessageAddForm) {
        //推送系统消息
        this.sendXtMessageRecv(sysMessageAddForm);
    }

    private void sendXtMessageRecv(ActivSysMessageAddForm sysMessageAddForm) {
        if (StringHelper.isNotNullAndEmpty(sysMessageAddForm.getPcContent()))
            sysMessageAddForm.setContent(sysMessageAddForm.getPcContent());
        ApiResult<Integer> apiId =commMessageService.addMessageRecvForActivi(sysMessageAddForm);
        //保存到草稿箱收件人表
        //保存所有收件人
        if (!ObjectUtils.isEmpty(sysMessageAddForm.getRecvUser())) {
            for (Integer userId : sysMessageAddForm.getRecvUser()) {
                SysMessageUserForm sysMessageUserform = new SysMessageUserForm();
                //消息id
                sysMessageUserform.setMessageId(apiId.getData());
                //收件人
                sysMessageUserform.setRecvUser(userId);
                commMessageService.addMesUserForActivi(sysMessageUserform);
            }
        }
    }

    /**
     * 根据模板节点id查找对应流程模板
     * @param form
     * @return
     */
    public  Map<String, String> getActivitiCodeMap(WfRuningProcessForm form) {
        List<WfActivityVo> activities = form.getCandidate().getActivities();
        List<String> activitiIds = Lists.newArrayList();
        if(!ObjectUtils.isEmpty(activities)){
            for (WfActivityVo wfActivityVo:activities) {
                activitiIds.add(wfActivityVo.getId());
            }
        }
        //查询节点表 得到 节点对应模板
        return  commActivitiService.queryActByIds(activitiIds);
    }
    /**
     * 过滤流程候选人
     *
     * @param form      流程表单
     * @return
     */
    public  List<WfActivityVo> filterFlowCandiateUser(WfRuningProcessForm form,Map<String, String> stringMap) {
        Map<String,  List<GeneralVo>> activiti2Users = Maps.newHashMap();
        if (!ObjectUtils.isEmpty(stringMap)) {
            for (Map.Entry<String, String> entry : stringMap.entrySet()) {
                List<GeneralVo> userInfo = null;
                if ("company".equals(entry.getValue()) || "department".equals(entry.getValue())) {
                    //若为当前用户同一公司下或同一部门下
                    String userId = form.getUserId();
                    userInfo = getUserInfo(entry.getValue(), userId);
                }
                activiti2Users.put(entry.getKey(), userInfo);
            }
        }

        return getWfActivityVos(form, activiti2Users);
    }

    /**
     * 筛选过滤下一步审批人
     * @param form
     * @param activiti2Users
     * @return
     */
    private List<WfActivityVo> getWfActivityVos(WfRuningProcessForm form, Map<String, List<GeneralVo>> activiti2Users) {
        // Map<String, DictionaryVo> dictMap = commDictService.getDictMapByTypeCode("permission.allbidding");
        //用户过滤，判断用户是否在所属标段下
        List<WfActivityVo> activityVoList = form.getCandidate().getActivities();
        if (ObjectUtils.isEmpty(activityVoList))
            return activityVoList;
        for (WfActivityVo wfActivityVo : activityVoList) {//遍历每个节点(可能包含分支)
            if (!ObjectUtils.isEmpty(wfActivityVo.getCandidateGroups())) {//如果角色不为空
                List<WfCandidateGroupVo> wfCandidateGroupVoList = wfActivityVo.getCandidateGroups();
                for (WfCandidateGroupVo wfCandidateGroupVo : wfCandidateGroupVoList) {
                    boolean isFilter = true;
                    //判断该角色是否 是在 项目下所有标段权限(分配角色)列表内
                    // for (Map.Entry<String, DictionaryVo> entry : dictMap.entrySet()) {
                    //     if (wfCandidateGroupVo.getCode().trim().equals(entry.getKey().trim())) {//匹配到,不过滤
                    //         isFilter = false;
                    //         break;
                    //     }
                    // }
                    if (isFilter) {
                        //过滤出新的 candidateUsers
                        List<WfCandidateUserVo> candidateUsers = this.filterWfCandiateUserVos(wfCandidateGroupVo.getCandidateUsers(), activiti2Users.get(wfActivityVo.getId()));
                        wfCandidateGroupVo.setCandidateUsers(candidateUsers);
                    }

                }
            } else {//否则走用户权限过滤
                List<WfCandidateUserVo> candidateUsers = this.filterWfCandiateUserVos(wfActivityVo.getCandidateUsers(), null);
                wfActivityVo.setCandidateUsers(candidateUsers);
            }
        }
        return activityVoList;
    }

    private   List<GeneralVo> getUserInfo(String code, String userId) {
        ApiResult generalVo = commUserService.selectUserInfoForAct(userId);
        ApiResult<List<GeneralVo>>  mainOrg = commUserService.selectUserMainOrg(userId);
        if (ObjectUtils.isEmpty(generalVo)) {
            //未查询到该发起者信息
            return null;
        } else{
            //内部用户 若筛选条件为部门  为其自身(与外部用户查询方法一致), 条件为单位 ==》 其上级为单位的 其所有子类
            return commUserService.queryTeamUsersOutUser(mainOrg.getData().get(0).getId()).getData();
        }
    }

    private List<WfCandidateUserVo> filterWfCandiateUserVos(List<WfCandidateUserVo> candidateUsers, List<GeneralVo> userInfo) {
        //查出这个标段下所有项目团队的用户
        List<WfCandidateUserVo> wfCandidateUserVoList = Lists.newArrayList();
        if (ObjectUtils.isEmpty(userInfo)) {
            return candidateUsers;
        }
        List<String> userCodes = ListUtil.toValueList(userInfo, "code", String.class, true);
        for (WfCandidateUserVo wfCandidateUserVo : candidateUsers) {
            if (ListUtil.toStr(userCodes).contains(wfCandidateUserVo.getCode()))
                wfCandidateUserVoList.add(wfCandidateUserVo);
        }
        return wfCandidateUserVoList;
    }

    public String getMenuName(String menuCode){
        return menuMapper.queryMenuNameByCode(menuCode);
    }

    public <T> void approveFlowAndSendMessage(String bizType, List<T> rawPos) {
        List<Integer> initiatorIds = ListUtil.toValueList(rawPos, "initiatorId", Integer.class);
        Map<Integer, UserVo> mapByUserIds = commUserService.getUserVoMapByUserIds(initiatorIds);

        String menuName = this.getMenuName(bizType);
        for (T po : rawPos) {
            Date initTime = null;
            Integer initiatorId = null;
            try {
                initTime = (Date)po.getClass().getMethod("getInitTime").invoke(po);
                initiatorId = (Integer) po.getClass().getMethod("getInitiatorId").invoke(po);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                logger.info("获取属性值错误");
                e.printStackTrace();
            }
            if(ObjectUtils.isEmpty(initTime) || ObjectUtils.isEmpty(initiatorId)){
                logger.info("获取发起时间或发起人错误，无法发送消息。业务模块为：{}", menuName);
                continue;
            }

            ActivSysMessageAddForm sysMessageAddForm = new ActivSysMessageAddForm();
            sysMessageAddForm.setTitle("故障日况--流程审批提醒");
            sysMessageAddForm.setContent("您于" + DateUtil.getDateFormat(initTime,DateUtil.DATE_CHINA_FORMAT) +
                    "发起的:" + menuName + "已审批通过，详情请登录哈尔滨项目日报管理系统查看。");

            List<Integer> recvUsers = Lists.newArrayList();
            if (!ObjectUtils.isEmpty(mapByUserIds) && !ObjectUtils.isEmpty(mapByUserIds.get(initiatorId))) {
                recvUsers.add(mapByUserIds.get(initiatorId).getId());
                sysMessageAddForm.setRecvUser(recvUsers);
                sysMessageAddForm.setClaimDealTime(new Date());
                this.sendMessageRecv(sysMessageAddForm);
            }else {
                logger.info("审批通过推送消息错误，创建人为空");
            }
        }
    }

}
