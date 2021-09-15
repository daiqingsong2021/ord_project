package com.wisdom.acm.hrb.sys.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.exception.BaseException;
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
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private HrbSysService hrbSysService;

    public String getOperateTimes(String line) throws ParseException {
        LineFoundationVo lineFoundationVo=hrbSysService.queryLineFoundationList().stream().filter(e->StringUtils.equalsIgnoreCase(e.getLine(),line)).collect(Collectors.toList()).get(0);
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,-24);
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        Date date=df.parse(DateUtil.getDateFormat(lineFoundationVo.getOperationTime()));
        long timeMillion=DateUtil.getDateFormat(dateFormat.format(calendar.getTime())).getTime()-date.getTime();
        return String.valueOf((timeMillion/(24l*60*60*1000)));
    }

    /**
     * 定时器配置读取doc的ip地址
     * zll
     * @return
     */
    public String getDocUrl(){
        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("application.yml");
        return prop.getProperty("doc.odrUrl");
    }

    /**
     * 生成pdf路径出错引发的报错，需要重新生成数据
     * zll
     * @return
     */
    public String getDocPdfError(){
        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("application.yml");
        return prop.getProperty("doc.PdfError");
    }

    /**
     * 获取天气的url地址
     * zll
     * @return
     */
    public String getWeatherUrl(){
        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("application.yml");
        return prop.getProperty("weather.url");
    }

    /**
     * 生成预览的PDF给前端
     * @param response 响应
     * @param pdfPath  本地生成的PDF地址
     */
    public  void preViewPdf(HttpServletResponse response, String pdfPath) {
        if (StringHelper.isNullAndEmpty(pdfPath)) {
            throw new BaseException("生成预览审批单pdf出错!");
        }

        File newFile = null;
        FileInputStream fileInputStream;
        try {
            //临时生成文件
            newFile = new File(pdfPath);
            fileInputStream = new FileInputStream(pdfPath);
            // 设置响应头和客户端保存文件名
            String fileName = URLEncoder.encode(newFile.getName(), "UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;Filename=" + fileName);
            IOUtils.copy(fileInputStream, response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
            if (null != fileInputStream) {
                fileInputStream.close();
            }
        } catch (Exception e) {
            logger.info("导出预览审批单出错, 原因为{}", e);
            throw new BaseException("导出预览审批单出错!");
        } finally {
            if (!ObjectUtils.isEmpty(newFile)) {
                newFile.delete();
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
        List<Integer> rewiewers = ListUtil.toValueList(rawPos, "reviewer", Integer.class);//审批人，非发起人
        Map<Integer, UserVo> mapByUserIds = commUserService.getUserVoMapByUserIds(rewiewers);
        String menuName = getMenuName(bizType);
        for (T po : rawPos) {
            Date initTime = null;
            Integer initiatorId = null;
            try {
                initTime = (Date)po.getClass().getMethod("getReportName").invoke(po);
                initiatorId = (Integer) po.getClass().getMethod("getReviewer").invoke(po);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                logger.info("获取属性值错误");
                e.printStackTrace();
            }
            if(ObjectUtils.isEmpty(initTime) || ObjectUtils.isEmpty(initiatorId)){
                logger.info("获取表单创建时间或审批人错误，无法发送消息。业务模块为：{}", menuName);
                continue;
            }
            ActivSysMessageAddForm sysMessageAddForm = new ActivSysMessageAddForm();
            sysMessageAddForm.setTitle("哈尔滨项目日报管理系统--流程审批提醒");
            sysMessageAddForm.setContent("您于" + DateUtil.getDateFormat(initTime,DateUtil.DATE_CHINA_FORMAT) +
                    "创建:" + menuName + "表单已审批通过，详情请登录哈尔滨项目日报管理系统查看。");

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

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) throws IOException {
//        // TODO Auto-generated method stub
////        System.out.println(System.getProperty("user.dir"));
////        List<Integer>one=Lists.newArrayList();
////        one.add(1);
////        one.add(2);
////        one.add(3);
////        List<Integer>two=Lists.newArrayList();
////        two.add(3);
////        two.add(2);
////        two.add(1);
////        System.out.println(ListUtils.isEqualList(one,two));
////        System.out.println(one.containsAll(two));



//        InputStream fileInputStrem= ResourceUtil.findResoureFile("template/yyrbmb.docx");
//        //先把word表格写进去
//        XWPFDocument document= new XWPFDocument(fileInputStrem);
//        //逻辑：由于使用过一次写word则fileInputStrem再次使用的时候会关闭流，则只能从新生成一个新的word
//        //生成临时目录
//        String tempFlolderStr = System.getProperty("user.dir") + "/temporary";
//        System.out.println("user.dir : "+System.getProperty("user.dir"));
//        File tempFlolder = new File(tempFlolderStr);
//        if (!tempFlolder.isDirectory())
//        {
//            tempFlolder.mkdirs();
//        }
//        String fileName = "/运营日报" +"1号线  "+ System.currentTimeMillis() + ".docx";
//        String outFileName = tempFlolderStr + fileName;
//        //临时生成文件
//        File newFile1 = new File(outFileName);
//        FileOutputStream output = new FileOutputStream(outFileName);
//        document.write(output);
//        //关闭流
//        fileInputStrem.close();
//        output.close();




//        String file = "/系统生成运营日报" +"1号线"+ "%20" +new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".docx";
//        file=file.substring(1);
//        System.out.println(file);




//            List<Integer>stationNums=Lists.newArrayList();
//            List<Integer> sun=Lists.newArrayList(25,22);
//            sun.add(0);
//            for(int i=0;i<=sun.size();i++) {
//                if(sun.get(i)==sun.get(i+1)+1){
//                    stationNums.add(sun.get(i));
//                }else{
//                    stationNums.add(sun.get(i));
//                    System.out.println(stationNums);
//                    break;
//                }
//            }



//        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
//        Calendar calendar=Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY,-24);
//        String yesterdayDate=dateFormat.format(calendar.getTime());
//        System.out.println(yesterdayDate);
    }
}
