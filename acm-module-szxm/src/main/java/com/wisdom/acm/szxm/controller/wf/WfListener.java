package com.wisdom.acm.szxm.controller.wf;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wisdom.acm.szxm.common.DateUtil;
import com.wisdom.acm.szxm.common.PropertiesLoaderUtils;
import com.wisdom.acm.szxm.service.app.EwxService;
import com.wisdom.acm.szxm.service.wf.WfService;
import com.wisdom.base.common.controller.WFListenerController;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.form.SysMessageAddForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.UserVo;
import com.wisdom.base.common.vo.wf.WfCandidateUserVo;
import com.wisdom.base.common.vo.wf.WfProcessInstVo;
import com.wisdom.base.common.vo.wf.WfTaskListenerVo;
import com.wisdom.webservice.schedule.ScheduleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * 流程监听类
 * 需要执行脚本
 * insert into wsd_base_set (ID, BO_CODE, BS_KEY, BS_VALUE, LAST_UPD_TIME, LAST_UPD_USER, CREAT_TIME, CREATOR, LAST_UPD_IP, WSDVER, SORT_NUM)
 * values (100, 'wf', 'wfListenerConfigure', 'http://192.168.2.65:8765/api/szxm/wfListener', to_date('10-08-2019 20:30:00', 'dd-mm-yyyy hh24:mi:ss'), 1, to_date('10-08-2019 20:30:00', 'dd-mm-yyyy hh24:mi:ss'), 1, '127.0.0.1', 0, null);
 */
@RestController
@RequestMapping("wfListener")
public class WfListener extends WFListenerController
{

    private static final Logger logger = LoggerFactory.getLogger(WfListener.class);

    @Autowired
    private WfService wfService;

    @Autowired
    private CommUserService commUserService;

    @Autowired
    private EwxService ewxService;

    /**
     * 创建工作项事件后
     * @param task
     * @return
     */
    @Override
    public ApiResult createTask(@RequestBody WfTaskListenerVo task) {
        if(ObjectUtils.isEmpty(task.getCandidateUsers()))//如果任务处理人为空 直接跳过
            return ApiResult.success();
        //东软初始化配置
        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("application.yml");
        String neruServiceUrl=prop.getProperty("neru.serviceUrl");
        String neruAppId=prop.getProperty("neru.appId");
        String neruSecretKey=prop.getProperty("neru.secretKey");
        String systemIP=prop.getProperty("systemIP");
        prop = new PropertiesLoaderUtils("config.properties");
        String IASID=prop.getProperty("IASID");
        //业务系统ID 1表示OA，2表示合同,38表示智慧工程项目系统。
        Integer systemID=Integer.valueOf(IASID);
        //流程实例名称
        String procName=task.getProcInstName();
        //缓急 1:普通 2:急件 3:特急
        Integer impoLevel=1;
        //步骤名称
        String alias=task.getTaskName();
        //发送人名称
        String passerName=task.getSender().getName();
        //接收时间 task创建时间
        String receTime= DateUtil.getDateFormat(task.getCreateTime(),DateUtil.DATETIME_DEFAULT_FORMAT);
        //接收人ID
        String loginID="";
       //待办推送URL
        String systemURL=systemIP+"?taskId="+task.getId()+"&procInstId="+task.getProcInstId()+"&type=flow";
       //流程实例ID
        String procID=task.getProcInstId();
        procID="szxmJsumtGcjs_"+procID;//防止其他系统与其冲突
        // 任务ID
        Integer taskID=Integer.valueOf(task.getId());
        //流程定义
        String appName=task.getProcDefName();
        //流程创建人
        String creatorName="";
        List<Integer> userIds= Lists.newArrayList();
        Integer creator=wfService.getProcCreatorByProcInstId(task.getProcInstId());
        if(!ObjectUtils.isEmpty(creator))
        {
            if(!userIds.contains(creator))
               userIds.add(creator);
        }
        //接收人
        List<WfCandidateUserVo> wfCandidateUserVoList= task.getCandidateUsers();
        for(WfCandidateUserVo wfCandidateUserVo:wfCandidateUserVoList)
        {
            if(!userIds.contains(Integer.valueOf(wfCandidateUserVo.getId())))
                userIds.add(Integer.valueOf(wfCandidateUserVo.getId()));
        }
        Map<Integer, UserVo> userVoMap=commUserService.getUserVoMapByUserIds(userIds);
        if(!ObjectUtils.isEmpty(creator))
             creatorName=userVoMap.get(creator).getName();//初始化流程创建人

        List<UserVo> recvUsers=Lists.newArrayList();
        for(WfCandidateUserVo wfCandidateUserVo:wfCandidateUserVoList)
        {
            UserVo userVo=userVoMap.get(Integer.valueOf(wfCandidateUserVo.getId()));
            //推送品高待办
            loginID=userVo.getCode();
            try
            {
                boolean isSuccess =ScheduleUtils.getInstance().saveApplicationInfo(systemID, procName, impoLevel, alias,passerName, receTime,
                        loginID, systemURL, procID, taskID, appName, creatorName);
                if(!isSuccess)
                {
                    logger.error("调用品高待办推送接口失败");
                }
            }
            catch (Exception e)
            {
                logger.error("品高待办推送接口服务异常"+e.getMessage());
            }
            //企业微信待办用
            recvUsers.add(userVo);
            //推送东软移动端接口  构造消息JSon（品高已推）
//            Map<String, String> paramsMap = Maps.newHashMap();
//            paramsMap.put("sender", neruAppId);
//            paramsMap.put("senderName", "统一待办");
//            paramsMap.put("token", getNeruSoftToken(neruServiceUrl,neruAppId,neruSecretKey));
//
//            JSONObject message = new JSONObject();
//            message.put("recipient",loginID);
//            JSONObject msg = new JSONObject();
//            msg.put("msg","智慧工程项目系统");
//            msg.put("title","来自"+passerName+"，标题为\""+procName+"\"的流程待办需要您来处理,请及时到工作台-工程管理-在线审批页面处理。");
//            msg.put("creatorName",creatorName);
//            msg.put("recevieTime",receTime);
//            msg.put("url",neruServiceUrl+"/forward?appUrl=");
//            msg.put("redirectUrl","https://mobileoffice.sz-mtr.com:8106");
//            msg.put("avatar","");
//            msg.put("type","text");
//            msg.put("fun","CO");
//            message.put("message",msg.toString());
//
//            paramsMap.put("message", message.toString());

            //推送东软移动端待办
            //     ewxService.sendNeuSoftMessage(paramsMap);
        }
        //推送企业微信移动端待办
        SysMessageAddForm sysMessageAddForm=new SysMessageAddForm();
        sysMessageAddForm.setTitle("智慧工程项目系统流程处理提醒");
        sysMessageAddForm.setContent("来自"+passerName+"，标题为\""+procName+"\"的流程待办需要您来处理，请及时到工作台-工程管理-在线审批页面处理。");
        sysMessageAddForm.setRecvUser(recvUsers);
        ewxService.sendEwxMessage(sysMessageAddForm);
        return ApiResult.success();
    }

    /**
     * 完成工作项事件后
     * @param task
     * @return
     */
    @Override
    public ApiResult completeTask(@RequestBody WfTaskListenerVo task) {
        //System.out.println("完成工作项事件completeTask=" + task);
        try
        {
            PropertiesLoaderUtils prop = new PropertiesLoaderUtils("config.properties");
            String IASID=prop.getProperty("IASID");

            String procID=task.getProcInstId();
            procID="szxmJsumtGcjs_"+procID;//防止其他系统与其冲突
            boolean isSuccess =ScheduleUtils.getInstance().deleteApplicationInfoByTaskID(Integer.valueOf(IASID),procID,Integer.valueOf(task.getId()));
        }
        catch (Exception e)
        {
            logger.error("品高删除待办某一节点接口异常"+e.getMessage());
        }
        return ApiResult.success();
    }

    /**
     * 完成流程事件后
     * @param procInst
     * @return
     */
    @Override
    public ApiResult completeWorkFlow(@RequestBody WfProcessInstVo procInst) {
        try
        {
            PropertiesLoaderUtils prop = new PropertiesLoaderUtils("config.properties");
            String IASID=prop.getProperty("IASID");

            String procID=procInst.getProcInstId();
            procID="szxmJsumtGcjs_"+procID;//防止其他系统与其冲突

            boolean isSuccess =ScheduleUtils.getInstance().delApplicationInfo(Integer.valueOf(IASID),procID);
        }
        catch (Exception e)
        {
            logger.error("品高删除待办待办接口异常"+e.getMessage());
        }
        return ApiResult.success();
    }

    /**
     * 终止流程事件后
     * @param procInst
     * @return
     */
    @Override
    public ApiResult terminateWorkFlow(@RequestBody WfProcessInstVo procInst) {
        try
        {
            PropertiesLoaderUtils prop = new PropertiesLoaderUtils("config.properties");
            String IASID=prop.getProperty("IASID");

            String procID=procInst.getProcInstId();
            procID="szxmJsumtGcjs_"+procID;//防止其他系统与其冲突

            boolean isSuccess =ScheduleUtils.getInstance().delApplicationInfo(Integer.valueOf(IASID),procID);
        }
        catch (Exception e)
        {
            logger.error("品高删除待办待办接口异常"+e.getMessage());
        }
        return ApiResult.success();
    }
    /**
     * 获取东软token
     * @param neruServiceUrl
     * @param neruAppId
     * @param neruSecretKey
     * @return
     */
    private String getNeruSoftToken(String neruServiceUrl,String neruAppId,String neruSecretKey)
    {
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<String>
                responseEntity = restTemplate.getForEntity(neruServiceUrl+"/gettoken?appid="+neruAppId+"&secret="+neruSecretKey,String.class);
        JSONObject jsonObject=JSONObject.parseObject(responseEntity.getBody());
        Map<String,Object>  returnMap=jsonObject.toJavaObject(Map.class);
        String accessToken="";//获取到的凭证，最长为512字节
        if("0.0".equals(String.valueOf(returnMap.get("errcode"))) || "0".equals(String.valueOf(returnMap.get("errcode"))))
        {
            accessToken=String.valueOf(returnMap.get("access_token"));
        }
        else
        {
           logger.error("调用东软公司接口获取token信息错误："+String.valueOf(returnMap.get("errmsg")));
        }
        return accessToken;
    }

    public  static void main(String args[])
    {
        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("config.properties");
        String IASID=prop.getProperty("IASID");
        System.out.println(IASID);
    }
}
