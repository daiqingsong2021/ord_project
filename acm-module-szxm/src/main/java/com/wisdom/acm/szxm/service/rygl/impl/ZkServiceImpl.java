package com.wisdom.acm.szxm.service.rygl.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.DateUtil;
import com.wisdom.acm.szxm.common.JsonHelper;
import com.wisdom.acm.szxm.common.PropertiesLoaderUtils;
import com.wisdom.acm.szxm.po.rygl.ProjInfoPo;
import com.wisdom.acm.szxm.service.rygl.ProjInfoService;
import com.wisdom.acm.szxm.service.rygl.ZkService;
import com.wisdom.acm.szxm.vo.rygl.KqRecordVo;
import com.wisdom.acm.szxm.vo.rygl.PeopleVo;
import com.wisdom.acm.szxm.vo.rygl.ProjInfoVo;
import com.wisdom.base.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ZkServiceImpl implements ZkService {
    private String zkServiceUrl = "";
    private String zkServicekey = "";
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ZkServiceImpl.class);
    /**
     * 项目组织信息Service
     */
    @Autowired
    private ProjInfoService projInfoService;


    @PostConstruct
    public void init() {
        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("application.yml");
        zkServiceUrl = prop.getProperty("zkservice.url");
        zkServicekey = prop.getProperty("zkservice.key");
        restTemplate = new RestTemplate();
    }

    /**
     * 批量增加或者更新考勤系统组织机构
     *
     * @param projInfoVoList
     * @return 1 成功 0 失败
     */
    @Override
    public int addOrUpdateZkOrg(List<ProjInfoVo> projInfoVoList) {
        String deptnumber = "", deptname = "", parentnumber = "";

        List<Map<String, Object>> reqJsonList = Lists.newArrayList();
        for (ProjInfoVo projInfoVo : projInfoVoList) {
            Map<String, Object> reqMap = Maps.newHashMap();
            deptnumber = String.valueOf(projInfoVo.getId());
            deptname = String.valueOf(projInfoVo.getOrgName());
            parentnumber = projInfoVo.getParentId() == 0 ? "1" : String.valueOf(projInfoVo.getParentId());
            reqMap.put("deptnumber", String.valueOf(deptnumber));
            reqMap.put("deptname", String.valueOf(deptname));
            reqMap.put("parentnumber", String.valueOf(parentnumber));
            reqJsonList.add(reqMap);
        }
        if (reqJsonList.size() > 200)
            reqJsonList = reqJsonList.subList(0, 200);//请求参数截取

        // 发送请求
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity formEntity = new HttpEntity(JsonHelper.toJsonWithGson(reqJsonList), headers);
        String jsonString = null;
        try {
            jsonString = restTemplate.postForObject(zkServiceUrl + "/department/update/?key=" + zkServicekey, formEntity, String.class);
        } catch (RestClientException e) {
            logger.error("调用中控平台异常：" + e.getMessage());
        }
        Map<String, Object> returnMap = JsonHelper.fromJsonWithGson(jsonString, Map.class);
        if ("0.0".equals(String.valueOf(returnMap.get("ret"))) || "0".equals(String.valueOf(returnMap.get("ret"))))
            return 1;
        else return 0;
    }

    @Override
    public int addOrUpdateZkOrg(ProjInfoVo projInfoVo) {
        List<ProjInfoVo> projInfoVoList = Lists.newArrayList();
        projInfoVoList.add(projInfoVo);
        return this.addOrUpdateZkOrg(projInfoVoList);
    }

    /**
     * 删除考勤系统组织机构
     *
     * @param projInfoVo
     * @return 1 成功 0 失败
     */
    @Override
    public int deleteZkOrg(ProjInfoVo projInfoVo) {
        Example example = new Example(ProjInfoPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projInfoVo.getProjectId());//项目ID
        List<ProjInfoPo> allRecords = projInfoService.selectByExample(example);
        //查询子部门并删除
        List<Integer> delList = this.queryChildDept(projInfoVo.getId(), allRecords);
        for (Integer deptnumber : delList) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            Map<String, Object> reqMap = Maps.newHashMap();
            reqMap.put("deptnumber", String.valueOf(deptnumber));
            HttpEntity formEntity = new HttpEntity(JsonHelper.toJsonWithGson(reqMap), headers);
            String jsonString = null;
            try {
                jsonString = restTemplate.postForObject(zkServiceUrl + "/department/delete/?key=" + zkServicekey, formEntity, String.class);
            } catch (RestClientException e) {
                logger.error("调用中控平台异常：" + e.getMessage());
            }
            Map<String, Object> returnMap = JsonHelper.fromJsonWithGson(jsonString, Map.class);
            if (!"0.0".equals(String.valueOf(returnMap.get("ret"))) && !"0".equals(String.valueOf(returnMap.get("ret"))))
                return 0;//一旦有出错 立马返回
        }

        return 1;
    }

    private List<Integer> queryChildDept(Integer pid, List<ProjInfoPo> allRecords) {
        //查询子节点
        List<Integer> retList = Lists.newArrayList();
        for (ProjInfoPo projInfoPo : allRecords) {
            if (pid.equals(projInfoPo.getParentId())) {
                List<Integer> child = queryChildDept(projInfoPo.getId(), allRecords);
                retList.addAll(child);
                retList.add(projInfoPo.getId());
            }
        }
        retList.add(pid);
        return retList;
    }

    /**
     * 批量增加或者更新考勤系统人员
     *
     * @param peopleVoList
     * @return 1 成功 0 失败
     */
    @Override
    public int addOrUpdateZkPeole(List<PeopleVo> peopleVoList) {

        List<Map<String, Object>> reqJsonList = Lists.newArrayList();
        for (PeopleVo peopleVo : peopleVoList) {
            Map<String, Object> reqMap = Maps.newHashMap();

            reqMap.put("pin", String.valueOf(peopleVo.getId()));
            reqMap.put("name", String.valueOf(peopleVo.getName()));
            reqMap.put("deptnumber", String.valueOf(peopleVo.getProjInfoId()));
            //地址栏存：项目ID_标段ID_人员类型
            //reqMap.put("address",String.valueOf(peopleVo.getProjectId()+"_"+peopleVo.getSectionId()+"_"+peopleVo.getTypeVo().getCode()));
            // reqMap.put("mobile",String.valueOf(peopleVo.getTelPhone()));
            // reqMap.put("identitycard",String.valueOf(peopleVo.getIdCard()));
            reqJsonList.add(reqMap);
        }
        if (reqJsonList.size() > 200)
            reqJsonList = reqJsonList.subList(0, 200);//请求参数截取

        // 发送请求
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity formEntity = new HttpEntity(JsonHelper.toJsonWithGson(reqJsonList), headers);
        String jsonString = null;
        try {
            jsonString = restTemplate.postForObject(zkServiceUrl + "/employee/update/?key=" + zkServicekey, formEntity, String.class);
        } catch (RestClientException e) {
            logger.error("调用中控平台异常：" + e.getMessage());
        }
        Map<String, Object> returnMap = JsonHelper.fromJsonWithGson(jsonString, Map.class);
        if ("0.0".equals(String.valueOf(returnMap.get("ret"))) || "0".equals(String.valueOf(returnMap.get("ret"))))
            return 1;
        else return 0;
    }

    @Override
    public int addOrUpdateZkPeole(PeopleVo peopleVo) {
        List<PeopleVo> peopleVoList = Lists.newArrayList();
        peopleVoList.add(peopleVo);
        return this.addOrUpdateZkPeole(peopleVoList);
    }

    /**
     * 删除考勤系统人员
     *
     * @param peopleId
     * @return 1 成功 0 失败
     */
    @Override
    public int deleteZkPeole(Integer peopleId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        Map<String, Object> reqMap = Maps.newHashMap();
        reqMap.put("pin", String.valueOf(peopleId));
        HttpEntity formEntity = new HttpEntity(JsonHelper.toJsonWithGson(reqMap), headers);
        String jsonString = null;
        try {
            jsonString = restTemplate.postForObject(zkServiceUrl + "/employee/delete/?key=" + zkServicekey, formEntity, String.class);
        } catch (RestClientException e) {
            logger.error("调用中控平台异常：" + e.getMessage());
        }
        Map<String, Object> returnMap = JsonHelper.fromJsonWithGson(jsonString, Map.class);
        if ("0.0".equals(String.valueOf(returnMap.get("ret"))) || "0".equals(String.valueOf(returnMap.get("ret"))))
            return 1;
        return 0;
    }

    /**
     * 根据条件查询考勤系统考勤记录
     *
     * @param queryMap
     * @return
     */
    @Override
    public List<KqRecordVo> getZkRecords(Map<String, String> queryMap) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity formEntity = new HttpEntity(JsonHelper.toJsonWithGson(queryMap), headers);
        // 发送请求
        String jsonString = null;
        try {
            //jsonString = restTemplate.postForObject(zkServiceUrl + "/transaction/get/?key=" + zkServicekey, formEntity, String.class);
            jsonString = restTemplate.postForObject( "http://iepms.sz-mtr.com:8502/api/v2/transaction/get/?key=0bbdr7rnfrrxbofsvrm_0c7iu1tfz5242k0qbr-xrsjy", formEntity, String.class);
        } catch (Exception e) {
            logger.error("调用中控平台异常：" + e.getMessage());
        }

        List<KqRecordVo> zkRecordVos = Lists.newArrayList();
        String id = "";
        String verify = "";
        String checktime = "";
        String sn = "";
        String alias = "";
        String pin = "";
        String ename = "";
        String deptnumber = "";
        String deptname = "";
        String stateno = "";
        String state = "";

        Map<String, Object> returnMap = JsonHelper.fromJsonWithGson(jsonString, Map.class);
        if(ObjectUtils.isEmpty(returnMap)){
            throw new BaseException("调用中控平台异常");
        }
        Map<String, Object> dataMap = (Map<String, Object>) returnMap.get("data");
        if (!ObjectUtils.isEmpty(dataMap) && Float.valueOf(String.valueOf(dataMap.get("count"))) > 0F) {
            List<Map<String, Object>> itemList = (List<Map<String, Object>>) dataMap.get("items");
            for (Map<String, Object> item : itemList) {
                id = String.valueOf(item.get("id"));
                verify = String.valueOf(item.get("verify"));
                checktime = String.valueOf(item.get("checktime"));
                sn = String.valueOf(item.get("sn"));
                alias = String.valueOf(item.get("alias"));
                pin = String.valueOf(item.get("pin"));
                ename = String.valueOf(item.get("ename"));
                deptnumber = String.valueOf(item.get("deptnumber"));
                deptname = String.valueOf(item.get("deptname"));
                stateno = String.valueOf(item.get("stateno"));
                state = String.valueOf(item.get("state"));

                KqRecordVo zkRecordVo = new KqRecordVo();
                zkRecordVo.setId(id);
                zkRecordVo.setVerify(verify);
                zkRecordVo.setChecktime(DateUtil.formatDate(checktime, "yyyy-MM-dd HH:mm"));
                zkRecordVo.setSn(sn);
                zkRecordVo.setAlias(alias);
                zkRecordVo.setPin(pin);
                zkRecordVo.setEname(ename);
                zkRecordVo.setDeptnumber(Integer.valueOf(deptnumber));
                zkRecordVo.setDeptname(deptname);
                zkRecordVo.setStateno(stateno);
                zkRecordVo.setState(state);
                zkRecordVo.setCheckType("0");//中控考勤
                zkRecordVos.add(zkRecordVo);
            }
        }
        return zkRecordVos;
    }

    public static void main(String args[]) {
        RestTemplate restTemplate = new RestTemplate();
        // 发送请求
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);
        headers.set("Authorization", "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJuaXVqdWFuanVhbiIsInVzZXJJZCI6IjEwMzAxNiIsIm5hbWUiOiIxNzIuMTguMC4xIiwiY3VycmVudEFjdHVOYW1lIjoi54mb5aif5aifIiwiY3VycmVudFVzZXJUeXBlIjoiMSIsImV4cCI6MTU2NjY0NDc3OH0.LBi82a_4Mkw4KI7Fb9KAvAmejZnqhSemAR6h0EsWHS3WSDf3-RaNItlu-Sx0MDLQ-ZyfocTJmAKW39rYBStoZ-M3KtDDuC8LvyjL5pbfhtpDM9bYXPrjBjfJ0xkk56r2g9182B47Il5pPmsnkXyhpeN1DmuOu9kXpIT25uH7H_I");
        FileSystemResource fileSystemResource = new FileSystemResource("C:/Users/Administrator/Pictures/40t58PICD7i_1024.jpg");
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file", fileSystemResource);
        form.add("filename", "40t58PICD7i_1024.jpg");
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);

        String s = restTemplate.postForObject("http://192.168.2.65:8765/api/doc/file/upload", files, String.class);
        System.out.println(s);
    }
}
