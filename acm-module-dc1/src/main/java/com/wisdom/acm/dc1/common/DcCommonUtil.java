package com.wisdom.acm.dc1.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.feign.*;
import com.wisdom.base.common.form.ActivSysMessageAddForm;
import com.wisdom.base.common.form.SysMessageUserForm;
import com.wisdom.base.common.form.WfRuningProcessForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.TreeUtil;
import com.wisdom.base.common.util.calc.calendar.PcCalendar;
import com.wisdom.base.common.util.calc.calendar.PmCalendar;
import com.wisdom.base.common.vo.*;
import com.wisdom.base.common.vo.wf.WfActivityVo;
import com.wisdom.base.common.vo.wf.WfCandidateGroupVo;
import com.wisdom.base.common.vo.wf.WfCandidateUserVo;
import com.wisdom.webservice.DateUtil;
import org.apache.commons.lang.StringUtils;
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
 * @author zll
 * 2020/8/4/004 15:23
 * Description:<描述>
 */
@Component
public class DcCommonUtil {
    private static final Logger logger = LoggerFactory.getLogger(DcCommonUtil.class);

    @Autowired
    private  CommUserService commUserService;

    @Autowired
    private  CommActivitiService commActivitiService;

    @Autowired
    private CommDictService commDictService;

    @Autowired
    private CommCalendarService commCalendarService;

    @Autowired
    private  CommMessageService commMessageService;

    @Autowired
    private  CommSysMenuService commSysMenuService;

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
     * 根据名称获取数据字典Code
     *
     * @param dictionaryType 字典类型
     * @param dictionaryName 字典名称
     * @return
     */
    public String getDictionaryCode(String dictionaryType, String dictionaryName) {
        String dictionaryCode = "";
        Map<String, DictionaryVo> dictMap = commDictService.getDictMapByTypeCode(dictionaryType);
        if (!ObjectUtils.isEmpty(dictMap) && StringHelper.isNotNullAndEmpty(dictionaryName)) {
            for (Map.Entry<String, DictionaryVo> entry : dictMap.entrySet()) {
                DictionaryVo value = entry.getValue();
                if (value.getName().equals(dictionaryName))
                    return entry.getKey();
            }
        }
        return dictionaryCode;
    }

    public String getDictionaryCode(Map<String, DictionaryVo> dictMap, String dictionaryName) {
        String dictionaryCode = "";
        if (!ObjectUtils.isEmpty(dictMap) && !StringUtils.isEmpty(dictionaryName)) {
            for (Map.Entry<String, DictionaryVo> entry : dictMap.entrySet()) {
                DictionaryVo value = entry.getValue();
                if (value.getName().equals(dictionaryName))
                    return entry.getKey();
            }
        }
        return dictionaryCode;
    }

    /**
     * 查询数据字典说明名称
     *
     * @param dictionaryType 字典类型
     * @param dictionaryCode 字典编码
     * @return
     */
    public String getDictionaryName(String dictionaryType, String dictionaryCode) {
        String dictionaryName = "";
        Map<String, DictionaryVo> dictMap = commDictService.getDictMapByTypeCode(dictionaryType);
        if (!ObjectUtils.isEmpty(dictMap) && !StringUtils.isEmpty(dictionaryCode)) {
            dictionaryName = String.valueOf(dictMap.get(dictionaryCode).getName());
        }
        return dictionaryName;
    }

    /**
     * 根据数据字典 Map 查询code对应的name
     *
     * @param dictMap
     * @param dictionaryCode
     * @return
     */
    public String getDictionaryName(Map<String, DictionaryVo> dictMap, String dictionaryCode) {
        String dictionaryName = "";
        if (!ObjectUtils.isEmpty(dictMap) && !StringUtils.isEmpty(dictionaryCode)) {
            dictionaryName = String.valueOf(dictMap.get(dictionaryCode).getName());
        }
        return dictionaryName;
    }


    /**
     * 获取日历
     *
     * @param calendarId 日历ID
     * @return
     */
    public PmCalendar getPmCalendar(Integer calendarId) {
        ApiResult<CalendarVo> calendarInfo = commCalendarService.getCalendarInfo(calendarId);
        if (!ObjectUtils.isEmpty(calendarInfo.getData()))
            return new PcCalendar(calendarInfo.getData());
        return null;
    }

    public PmCalendar getDefaultPmCalendar() {
        ApiResult<CalendarVo> calendarInfo = commCalendarService.getCalendarDefaultInfo();
        if (!ObjectUtils.isEmpty(calendarInfo.getData()))
            return new PcCalendar(calendarInfo.getData());
        return null;
    }

    /**
     * 根据列表产生树
     *
     * @param treeNodes
     * @param <T>
     * @return
     */
    public <T extends TreeVo> List<T> generateTree(List<T> treeNodes) {
        List<T> retList = Lists.newArrayList();
        if (!ObjectUtils.isEmpty(treeNodes)) {
            List<Integer> idList = ListUtil.toIdList(treeNodes);
            List<T> rootNodes = Lists.newArrayList();//根节点初始化
            for (T treeNode : treeNodes) {
                if (!idList.contains(treeNode.getParentId())) {
                    //如不包含，则说明，该节点为根节点
                    rootNodes.add(treeNode);
                }
            }
            for (T rootNode : rootNodes) {//遍历根节点，在根节点下拼装树形数据
                //List<T> tree =TreeUtil.bulid(treeNodes, rootNode.getId());
                //寻找子节点
                Map<Integer, List<T>> childrenMap = TreeUtil.toChildrenMap(treeNodes);
                List<T> childrenList = this.bulidChildren(childrenMap, rootNode.getId());
                rootNode.setChildren(childrenList);
                retList.add(rootNode);
            }
        }
        return retList;
    }

    /**
     * 递归计算子节点
     *
     * @param childrenMap
     * @param parentId
     * @param <T>
     * @return
     */
    private  <T extends TreeVo> List<T> bulidChildren(Map<Integer, List<T>> childrenMap, Integer parentId) {
        List<T> list = childrenMap.get(parentId);
        if (!ObjectUtils.isEmpty(list)) {
            for (T t : list) {
                // 递归查询子节点
                List<T> children = bulidChildren(childrenMap, t.getId());
                t.setChildren(null);
                if (!ObjectUtils.isEmpty(children)) {
                    t.setChildren(children);
                }
            }
        }

        return list;
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

    /**
     * 筛选过滤下一步审批人
     * @param form
     * @param activiti2Users
     * @return
     */
    private  List<WfActivityVo> getWfActivityVos(WfRuningProcessForm form, Map<String, List<GeneralVo>> activiti2Users) {
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
                    if (isFilter) {
                        //过滤出新的 candidateUsers
                        List<WfCandidateUserVo> candidateUsers = filterWfCandiateUserVos(wfCandidateGroupVo.getCandidateUsers(), activiti2Users.get(wfActivityVo.getId()));
                        wfCandidateGroupVo.setCandidateUsers(candidateUsers);
                    }

                }
            } else {//否则走用户权限过滤
                List<WfCandidateUserVo> candidateUsers = filterWfCandiateUserVos(wfActivityVo.getCandidateUsers(), null);
                wfActivityVo.setCandidateUsers(candidateUsers);
            }
        }
        return activityVoList;
    }

    private  List<WfCandidateUserVo> filterWfCandiateUserVos(List<WfCandidateUserVo> candidateUsers, List<GeneralVo> userInfo) {
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

    public <T> void approveFlowAndSendMessage(String bizType, List<T> rawPos) {
        List<Integer> initiatorIds = ListUtil.toValueList(rawPos, "initiatorId", Integer.class);
        Map<Integer, UserVo> mapByUserIds = commUserService.getUserVoMapByUserIds(initiatorIds);
        String menuName = getMenuName(bizType);
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
            sysMessageAddForm.setTitle("客运日况-流程审批提醒");
            sysMessageAddForm.setContent("您于" + DateUtil.getDateFormat(initTime,DateUtil.DATE_CHINA_FORMAT) +
                    "发起的:" + menuName + "已审批通过，详情请登录哈尔滨运营日报管理系统查看。");

            List<Integer> recvUsers = Lists.newArrayList();
            if (!ObjectUtils.isEmpty(mapByUserIds) && !ObjectUtils.isEmpty(mapByUserIds.get(initiatorId))) {
                recvUsers.add(mapByUserIds.get(initiatorId).getId());
                sysMessageAddForm.setRecvUser(recvUsers);
                sysMessageAddForm.setClaimDealTime(new Date());
                sendXtMessageRecv(sysMessageAddForm);
            }else {
                logger.info("审批通过推送消息错误，创建人为空");
            }
        }
    }

    private String getMenuName(String menuCode){
        return commSysMenuService.queryMenuNameByCode(menuCode).getData().toString();
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
}
