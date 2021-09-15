package com.wisdom.acm.dc4.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.dc4.common.DateUtil;
import com.wisdom.acm.dc4.common.JsonHelper;
import com.wisdom.acm.dc4.common.NeruSoftHttpsUtils;
import com.wisdom.acm.dc4.common.PropertiesLoaderUtils;
import com.wisdom.acm.dc4.common.redisUtils.RedisUtil;
import com.wisdom.acm.dc4.service.EwxService;
import com.wisdom.acm.dc4.vo.app.AppUserInfoVo;
import com.wisdom.acm.dc4.vo.app.KqRecordVo;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommAuthService;
import com.wisdom.base.common.feign.CommOrgService;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.form.SysMessageAddForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EwxServiceImpl implements EwxService
{

    /**
     * logback
     */
    private static final Logger logger = LoggerFactory.getLogger(EwxServiceImpl.class);

    //东软接口配置
    private String neruServiceUrl="";//东软移动接口URL
    private String neruAppId="";//东软移动注册APPID
    private String neruSecretKey="";//东软移动 智慧工程项目系统应用 SecretKey
    private String neruSendmsgWorkUrl="";//东软待办推送接口URL
   //企业微信接口配置
    private String ewxServiceUrl="";//企业微信URL
    private String ewxCorpId="";//企业微信 企业ID
    private String ewxAgentId="";//企业微信 智慧工程项目系统应用 agentId
    private String ewxSecretKey="";//企业微信 智慧工程项目系统应用 SecretKey
    private String ewxKqSecretKey="";//企业微信 打卡应用 SecretKey

    private RestTemplate restTemplate;

    @Autowired
    private CommUserService commUserService;

    @Autowired
    private CommOrgService commOrgService;

    @Autowired
    private CommAuthService commAuthService;

    @Autowired
    private RedisUtil redisUtil;

    @PostConstruct
    public void  init(){
        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("application.yml");
        //企业微信初始化配置
        ewxServiceUrl = prop.getProperty("ewx.serviceUrl");
        ewxCorpId = prop.getProperty("ewx.corpId");
        ewxAgentId = prop.getProperty("ewx.agentId");
        ewxSecretKey = prop.getProperty("ewx.secretKey");
        ewxKqSecretKey = prop.getProperty("ewx.kqSecretKey");

        //东软初始化配置
        neruServiceUrl=prop.getProperty("neru.serviceUrl");
        neruAppId=prop.getProperty("neru.appId");
        neruSecretKey=prop.getProperty("neru.secretKey");
        neruSendmsgWorkUrl=prop.getProperty("neru.sendmsgWorkUrl");
        restTemplate = new RestTemplate();
    }

    /**
     * 企业微信 获取用户code
     * @param code 登录鉴权code
     * @return
     */
    @Override
    public AppUserInfoVo getEwxAppUserInfo(String code)
    {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(ewxServiceUrl+"/gettoken?corpid="+ewxCorpId+"&corpsecret="+ewxSecretKey,String.class);
        Map<String,Object> returnMap= JsonHelper.fromJsonWithGson(responseEntity.getBody(),Map.class);
        String accessToken="";//获取到的凭证，最长为512字节
        String expiresIn="";//凭证的有效时间（秒）
        if("0.0".equals(String.valueOf(returnMap.get("errcode"))) || "0".equals(String.valueOf(returnMap.get("errcode"))))
        {
            accessToken=String.valueOf(returnMap.get("access_token"));
            expiresIn=String.valueOf(returnMap.get("expires_in"));
        }
        else
        {
            throw new BaseException("调用企业微信获取token错误："+String.valueOf(returnMap.get("errmsg")));
        }
        String userName="";
        responseEntity = restTemplate.getForEntity(ewxServiceUrl+"/user/getuserinfo?access_token="+accessToken+"&code="+code,String.class);
        returnMap=JsonHelper.fromJsonWithGson(responseEntity.getBody(),Map.class);
        if("0.0".equals(String.valueOf(returnMap.get("errcode"))) || "0".equals(String.valueOf(returnMap.get("errcode"))))
        {
            userName=String.valueOf(returnMap.get("UserId"));
        }
        else
        {
            throw new BaseException("调用企业微信获取用户信息错误："+String.valueOf(returnMap.get("errmsg")));
        }
        //将获取到的usercode 查询系统用户
        AppUserInfoVo appUserInfoVo=new AppUserInfoVo();
        ApiResult<UserInfo> userInfoApiResult=commUserService.getUserInfoByName(userName);
        UserInfo userInfo=userInfoApiResult.getData();

        if(!ObjectUtils.isEmpty(userInfo))
        {
            appUserInfoVo.setEmail(userInfo.getEmail());
            appUserInfoVo.setId(userInfo.getId());
            appUserInfoVo.setName(userInfo.getActuName());
            appUserInfoVo.setSex(userInfo.getSex());
            appUserInfoVo.setPhone(userInfo.getPhone());
            if(!ObjectUtils.isEmpty(userInfo.getOrgIds()))
            {
                ApiResult<List<SysOrgInfoVo>> sysOrgResult=commOrgService.queryOrgList(userInfo.getOrgIds());
                List<SysOrgInfoVo> orgInfoVos= sysOrgResult.getData();
                if(!ObjectUtils.isEmpty(orgInfoVos))
                {
                    for(SysOrgInfoVo sysOrgInfoVo:orgInfoVos)
                    {
                        GeneralVo orgGeneralVo=new GeneralVo(sysOrgInfoVo.getId(),sysOrgInfoVo.getOrgName());
                        appUserInfoVo.getOrgs().add(orgGeneralVo);
                    }
                }
            }
            //平台鉴权
            JwtAuthenticationRequest userVo=new JwtAuthenticationRequest();
            userVo.setUserName(userInfo.getUserName());
            userVo.setPassword(userInfo.getPassword());
            ApiResult<String>  jwtAuthId=commAuthService.createAuthenticationTokenSn(userVo);

            appUserInfoVo.setAuthorization(jwtAuthId.getData());
        }
        else
        {
            throw new BaseException("获取系统用户信息失败!");
        }

        return appUserInfoVo;
    }

    /**
     * 东软公司 获取用户code
     * @param code 登录鉴权code
     * @return
     */
    @Override
    public AppUserInfoVo getNeusoftAppUserInfo(String code) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(neruServiceUrl+"/gettoken?appid="+neruAppId+"&secret="+neruSecretKey,String.class);
        Map<String,Object> returnMap=JsonHelper.fromJsonWithGson(responseEntity.getBody(),Map.class);
        String accessToken="";//获取到的凭证，最长为512字节
        if("0.0".equals(String.valueOf(returnMap.get("errcode"))) || "0".equals(String.valueOf(returnMap.get("errcode"))))
        {
            accessToken=String.valueOf(returnMap.get("access_token"));
        }
        else
        {
            throw new BaseException("调用东软公司接口获取token信息错误："+String.valueOf(returnMap.get("errmsg")));
        }
        String userName="";
        responseEntity = restTemplate.getForEntity(neruServiceUrl+"/getuserinfo?access_token="+accessToken+"&code="+code,String.class);
        returnMap=JsonHelper.fromJsonWithGson(responseEntity.getBody(),Map.class);
        if("0.0".equals(String.valueOf(returnMap.get("errcode"))) || "0".equals(String.valueOf(returnMap.get("errcode"))))
        {
            userName=String.valueOf(returnMap.get("userid"));
        }
        else
        {
            throw new BaseException("调用东软公司接口获取用户信息错误："+String.valueOf(returnMap.get("errmsg")));
        }
        //将获取到的usercode 查询系统用户
        AppUserInfoVo appUserInfoVo=new AppUserInfoVo();
        ApiResult<UserInfo> userInfoApiResult=commUserService.getUserInfoByName(userName);
        UserInfo userInfo=userInfoApiResult.getData();
        if(!ObjectUtils.isEmpty(userInfo))
        {
            appUserInfoVo.setEmail(userInfo.getEmail());
            appUserInfoVo.setId(userInfo.getId());
            appUserInfoVo.setName(userInfo.getActuName());
            appUserInfoVo.setSex("1".equals(userInfo.getSex())?"男":"女");
            appUserInfoVo.setPhone(userInfo.getPhone());
            if(!ObjectUtils.isEmpty(userInfo.getOrgIds()))
            {
                ApiResult<List<SysOrgInfoVo>> sysOrgResult=commOrgService.queryOrgList(userInfo.getOrgIds());
                List<SysOrgInfoVo> orgInfoVos= sysOrgResult.getData();
                if(!ObjectUtils.isEmpty(orgInfoVos))
                {
                    for(SysOrgInfoVo sysOrgInfoVo:orgInfoVos)
                    {
                        GeneralVo orgGeneralVo=new GeneralVo(sysOrgInfoVo.getId(),sysOrgInfoVo.getOrgName());
                        appUserInfoVo.getOrgs().add(orgGeneralVo);
                    }
                }
            }
            //平台鉴权
            JwtAuthenticationRequest userVo=new JwtAuthenticationRequest();
            userVo.setUserName(userInfo.getUserName());
            userVo.setPassword(userInfo.getPassword());
            ApiResult<String>  jwtAuthId=commAuthService.createAuthenticationTokenSn(userVo);

            appUserInfoVo.setAuthorization(jwtAuthId.getData());
        }
        else
        {
            throw new BaseException("获取系统用户信息失败!");
        }
        return appUserInfoVo;
    }

    @Override public Map<String, Object> getJsApiTicket(String url)
    {
        Map<String,Object> resMap= Maps.newHashMap();
        //1 获取企业微信的ACCESS_TOKEN
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(ewxServiceUrl+"/gettoken?corpid="+ewxCorpId+"&corpsecret="+ewxSecretKey,String.class);
        Map<String,Object> returnMap=JsonHelper.fromJsonWithGson(responseEntity.getBody(),Map.class);
        String accessToken="";//获取到的凭证，最长为512字节
        String expiresIn="";//凭证的有效时间（秒）
        if("0.0".equals(String.valueOf(returnMap.get("errcode"))) || "0".equals(String.valueOf(returnMap.get("errcode"))))
        {
            accessToken=String.valueOf(returnMap.get("access_token"));
            expiresIn=String.valueOf(returnMap.get("expires_in"));
        }
        else
        {
            throw new BaseException("调用企业微信获取token错误："+String.valueOf(returnMap.get("errmsg")));
        }
        //2 先从redis获取，如获取不到，则调用企业微信获取jsapi_ticket
        String jsapiTicket=redisUtil.getStringValue("jsapiTicket");
        if(StringHelper.isNullAndEmpty(jsapiTicket))
        {
            responseEntity = restTemplate.getForEntity(ewxServiceUrl+"/get_jsapi_ticket?access_token="+accessToken,String.class);
            returnMap=JsonHelper.fromJsonWithGson(responseEntity.getBody(),Map.class);
            if("0.0".equals(String.valueOf(returnMap.get("errcode"))) || "0".equals(String.valueOf(returnMap.get("errcode"))))
            {
                jsapiTicket=String.valueOf(returnMap.get("ticket"));
            }
            else
            {
                throw new BaseException("调用企业微信获取企业的jsapi_ticket错误："+String.valueOf(returnMap.get("errmsg")));
            }
            redisUtil.setStringValue("jsapiTicket",jsapiTicket,7200);
        }
        //3 SHA加密
        String timeStamp=String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));
        //解密url
        url=url.replaceAll("%3D","=").replaceAll("%26","&");
        String encodeStr="jsapi_ticket="+jsapiTicket+"&noncestr=Wm3WZYTPz0wzccnW&timestamp="+timeStamp+"&url="+url;
        String sha1Str=DigestUtils.sha1Hex(encodeStr);
        //拼装返回数据给移动端
        resMap.put("url",url);
        resMap.put("signature",sha1Str);
        resMap.put("ticket",jsapiTicket);
        resMap.put("corpid",ewxCorpId);
        resMap.put("timestamp",timeStamp);
        resMap.put("nonceStr","Wm3WZYTPz0wzccnW");
        return resMap;
    }

    /**
     * 移动端消息推送
     * @param sysMessageAddForm
     */
    @Override public void sendEwxMessage(SysMessageAddForm sysMessageAddForm)
    {
        if(ObjectUtils.isEmpty(sysMessageAddForm.getRecvUser()))
            return;
        //1 消息推送企业微信
        //1.1 获取企业微信token
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(ewxServiceUrl+"/gettoken?corpid="+ewxCorpId+"&corpsecret="+ewxSecretKey,String.class);
        Map<String,Object> returnMap=JsonHelper.fromJsonWithGson(responseEntity.getBody(),Map.class);
        String accessToken="";//获取到的凭证，最长为512字节
        String expiresIn="";//凭证的有效时间（秒）
        if("0.0".equals(String.valueOf(returnMap.get("errcode"))) || "0".equals(String.valueOf(returnMap.get("errcode"))))
        {
            accessToken=String.valueOf(returnMap.get("access_token"));
            expiresIn=String.valueOf(returnMap.get("expires_in"));
        }
        else
        {
            logger.error("调用企业微信获取token错误："+String.valueOf(returnMap.get("errmsg")));
        }
        //1.2 推送消息
        Map<String,Object> reqMap=Maps.newHashMap();
        String touSerUsers="";
        for(UserVo recvUser:sysMessageAddForm.getRecvUser())
        {
            String recvUserCode=recvUser.getCode();
            touSerUsers+=(recvUserCode+"|");
        }
        if(StringHelper.isNotNullAndEmpty(touSerUsers))
          touSerUsers=touSerUsers.substring(0,touSerUsers.lastIndexOf("|"));
        reqMap.put("touser",touSerUsers);
        reqMap.put("msgtype","text");
        reqMap.put("agentid",ewxAgentId);
        Map<String,Object> contentMap=Maps.newHashMap();
        contentMap.put("content",sysMessageAddForm.getContent());
        reqMap.put("text",contentMap);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity formEntity = new HttpEntity(JsonHelper.toJsonWithGson(reqMap), headers);
        String jsonString = restTemplate.postForObject(ewxServiceUrl+"/message/send?access_token="+accessToken,formEntity,String.class);
        returnMap=JsonHelper.fromJsonWithGson(jsonString,Map.class);
        if(!"0.0".equals(String.valueOf(returnMap.get("errcode"))) && !"0".equals(String.valueOf(returnMap.get("errcode"))))
        {
            logger.error("调用企业微信推送消息错误："+String.valueOf(returnMap.get("errmsg")));
        }
    }

    /**
     * 获取考勤数据
     * @param reqMap
     * @return
     */
    @Override public List<KqRecordVo> getKqRecord(Map<String, Object> reqMap)
    {
        //1 企业微信获取 打卡应用token
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(ewxServiceUrl+"/gettoken?corpid="+ewxCorpId+"&corpsecret="+ewxKqSecretKey,String.class);
        Map<String,Object> returnMap=JsonHelper.fromJsonWithGson(responseEntity.getBody(),Map.class);
        String accessToken="";//获取到的凭证，最长为512字节
        String expiresIn="";//凭证的有效时间（秒）
        if("0.0".equals(String.valueOf(returnMap.get("errcode"))) || "0".equals(String.valueOf(returnMap.get("errcode"))))
        {
            accessToken=String.valueOf(returnMap.get("access_token"));
            expiresIn=String.valueOf(returnMap.get("expires_in"));
        }
        else
        {
            logger.error("调用企业微信获取打卡应用token错误："+String.valueOf(returnMap.get("errmsg")));
        }
        // 调用获取打卡数据接口
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity formEntity = new HttpEntity(JsonHelper.toJsonWithGson(reqMap), headers);
        String jsonString = restTemplate.postForObject(ewxServiceUrl+"/checkin/getcheckindata?access_token="+accessToken,formEntity,String.class);
        JSONObject jsonObject=JSONObject.parseObject(jsonString);
        returnMap=jsonObject.toJavaObject(Map.class);
        if(!"0.0".equals(String.valueOf(returnMap.get("errcode"))) && !"0".equals(String.valueOf(returnMap.get("errcode"))))
        {
            logger.error("调用企业微信获取打卡数据接口错误："+String.valueOf(returnMap.get("errmsg")));
        }
        // 获取考勤数据
        List<KqRecordVo> kqRecordVoList=Lists.newArrayList();
        List<Map<String,Object>> checkindata=(List<Map<String,Object>>)returnMap.get("checkindata");
        for(Map<String,Object> userInfo:checkindata)
        {
            String exception_type=StringHelper.formattString(String.valueOf(userInfo.get("exception_type")));
            if(exception_type.indexOf("未打卡")<0)
            {//未打卡的不记录
                KqRecordVo kqRecordVo=new KqRecordVo();
                kqRecordVo.setUserCode(String.valueOf(userInfo.get("userid")));
                String checkinTimeStamp=String.valueOf(userInfo.get("checkin_time"));//uninx 时间戳
                Date checkTime= DateUtil.localDateTimeConvertToDate(LocalDateTime.ofEpochSecond(Long.valueOf(checkinTimeStamp),0, ZoneOffset.of("+8")));
                kqRecordVo.setChecktime(DateUtil.formatDate(DateUtil.getDateFormat(checkTime,"yyyy-MM-dd HH:mm"),"yyyy-MM-dd HH:mm"));//转换成带分的
                kqRecordVo.setLocationAddress(String.valueOf(userInfo.get("location_detail")));
                kqRecordVo.setCheckType("1");//微信考勤
                kqRecordVoList.add(kqRecordVo);
            }
        }
        return kqRecordVoList;
    }

    @Override public void sendNeuSoftMessage(Map<String,String> paramsMap)
    {
         try
         {
             Map<String, String> header = new HashMap<String, String>();
             header.put("ContentType","application/x-www-form-urlencoded;charset=UTF-8");
             String jsonString= NeruSoftHttpsUtils.post(neruSendmsgWorkUrl,header,paramsMap,null);
             JSONObject jsonObject=JSONObject.parseObject(jsonString);
             if(!"0.0".equals(String.valueOf(jsonObject.get("code"))) && !"0".equals(String.valueOf(jsonObject.get("code"))))
             {
                 logger.error("调用东软移动端待办推送消息错误："+String.valueOf(jsonObject.get("msg")));
             }
         }
         catch (Exception e)
         {
             logger.error("东软移动端待办推送接口服务异常"+e.getMessage());
         }
    }

    public static void main(String args[])
    {
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://10.10.11.119:8091/co/oapi/gettoken?appid=68ce69533ae8490cacf95e1c1fb3d2cb&secret=f9e9b44a-a7b3-449a-8b9d-1dfa2429ca34",String.class);
        Map<String,Object> returnMap=JsonHelper.fromJsonWithGson(responseEntity.getBody(),Map.class);
        String accessToken="";//获取到的凭证，最长为512字节
        if("0.0".equals(String.valueOf(returnMap.get("errcode"))) || "0".equals(String.valueOf(returnMap.get("errcode"))))
        {
            accessToken=String.valueOf(returnMap.get("access_token"));
        }
        else
        {
            throw new BaseException("调用东软公司接口获取token信息错误："+String.valueOf(returnMap.get("errmsg")));
        }
        String userName="";
        responseEntity = restTemplate.getForEntity("http://10.10.11.119:8091/co/oapi/getuserinfo?access_token="+accessToken+"&code=d4ea0fb0b1fc4a42b08e5ffde0039b78",String.class);
        returnMap=JsonHelper.fromJsonWithGson(responseEntity.getBody(),Map.class);
        if("0.0".equals(String.valueOf(returnMap.get("errcode"))) || "0".equals(String.valueOf(returnMap.get("errcode"))))
        {
            userName=String.valueOf(returnMap.get("userid"));
        }
        else
        {
            throw new BaseException("调用东软公司接口获取用户信息错误："+String.valueOf(returnMap.get("errmsg")));
        }


    }


    public static void getTimeStamp()
    {
        String today = DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");//今天凌晨0点
        String tomorrow = DateUtil.getDateFormat(DateUtil.getDayAfter(new Date(), 1), "yyyy-MM-dd");//明天凌晨0点
        System.out.println(DateUtil.getDateFormat(today));
        System.out.println(DateUtil.getDateFormat(tomorrow));
    }

}
