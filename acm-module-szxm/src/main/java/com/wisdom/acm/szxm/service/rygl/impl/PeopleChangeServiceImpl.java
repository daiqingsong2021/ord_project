package com.wisdom.acm.szxm.service.rygl.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.common.DateUtil;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.common.SzxmEnumsUtil;
import com.wisdom.acm.szxm.common.officeUtils.DocTransformUtil;
import com.wisdom.acm.szxm.common.officeUtils.WordUtil;
import com.wisdom.acm.szxm.form.rygl.PeopleChangeAddForm;
import com.wisdom.acm.szxm.form.rygl.PeopleChangeUpdateForm;
import com.wisdom.acm.szxm.mapper.rygl.PeopleChangeMapper;
import com.wisdom.acm.szxm.po.rygl.PeopleChangePo;
import com.wisdom.acm.szxm.po.rygl.PeoplePo;
import com.wisdom.acm.szxm.service.rygl.PeopleChangeService;
import com.wisdom.acm.szxm.service.rygl.PeopleService;
import com.wisdom.acm.szxm.vo.rygl.PeopleChangeVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.feign.CommProjectTeamService;
import com.wisdom.base.common.feign.doc.CommDocService;
import com.wisdom.base.common.feign.plan.project.CommPlanProjectService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ResourceUtil;
import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.ProjectTeamVo;
import com.wisdom.base.common.vo.plan.project.PlanProjectVo;
import com.wisdom.webservice.contract.ContractServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PeopleChangeServiceImpl extends BaseService<PeopleChangeMapper, PeopleChangePo> implements
        PeopleChangeService {
    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private DocTransformUtil docTransformUtil;

    @Autowired
    private CommDocService commDocService;

    @Autowired
    private CommPlanProjectService commPlanProjectService;

    @Autowired
    private CommProjectTeamService commProjectTeamService;

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private CommDictService commDictService;

    @Autowired
    private HttpServletRequest request;


    @Override
    public PageInfo<PeopleChangeVo> selectPeopleChangeList(Map<String, Object> mapWhere, List<String> sectionList,
                                                           Integer pageSize, Integer currentPageNum) {
        mapWhere.put("sectionList", sectionList);
        mapWhere.put("status", StringHelper.formattString(String.valueOf(mapWhere.get("status"))));
        mapWhere.put("searcher", StringHelper.formattString(String.valueOf(mapWhere.get("searcher"))));
        PageHelper.startPage(currentPageNum, pageSize);
        List<PeopleChangeVo> peopleChangeVoList = mapper.selectPeopleChangeList(mapWhere);
        PageInfo<PeopleChangeVo> pageInfo = new PageInfo<PeopleChangeVo>(peopleChangeVoList);
        PlanProjectVo projectVo = commPlanProjectService.getProject(Integer.valueOf(String.valueOf(mapWhere.get("projectId"))));
        if (!ObjectUtils.isEmpty(pageInfo.getList())) {
            Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());

            for (PeopleChangeVo peopleChangeVo : pageInfo.getList()) {
                ProjectTeamVo sectionVo = sectionMap.get(peopleChangeVo.getSectionId());
                peopleChangeVo.setSectionCode(sectionVo.getCode());
                peopleChangeVo.setSectionName(sectionVo.getName());
                peopleChangeVo.setProjectName(projectVo.getName());
                //设置流程
                peopleChangeVo.getStatusVo().setName(SzxmEnumsUtil.StatusEnum.valueOf(peopleChangeVo.getStatusVo().getCode()).getName());
            }
        }

        return pageInfo;
    }

    @Override
    public List<PeopleChangeVo> selectFlowPeopleChangeList(Map<String, Object> mapWhere, List<String> sectionList) {
        if (StringHelper.isNotNullAndEmpty(String.valueOf(mapWhere.get("ids")))) {//如果主键Ids 不为空
            String[] idsArray = String.valueOf(mapWhere.get("ids")).split(",");
            List<String> ids = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("ids", ids);
        } else {
            mapWhere.put("sectionList", sectionList);
            mapWhere.put("status", StringHelper.formattString(String.valueOf(mapWhere.get("status"))));
            mapWhere.put("searcher", StringHelper.formattString(String.valueOf(mapWhere.get("searcher"))));
        }
        List<PeopleChangeVo> peopleChangeVoList = mapper.selectPeopleChangeList(mapWhere);
        if (!ObjectUtils.isEmpty(peopleChangeVoList)) {
            PlanProjectVo projectVo = commPlanProjectService.getProject(peopleChangeVoList.get(0).getProjectId());
            Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());
            for (PeopleChangeVo peopleChangeVo : peopleChangeVoList) {
                ProjectTeamVo sectionVo = sectionMap.get(peopleChangeVo.getSectionId());
                peopleChangeVo.setSectionCode(sectionVo.getCode());
                peopleChangeVo.setSectionName(sectionVo.getName());
                peopleChangeVo.setProjectName(projectVo.getName());
                peopleChangeVo.getStatusVo().setName(SzxmEnumsUtil.StatusEnum.valueOf(peopleChangeVo.getStatusVo().getCode()).getName());
            }
        }
        return peopleChangeVoList;
    }

    @Override
    public void approvePeopleChangeFlow(String bizType, List<Integer> ids) {
        //1 把业务数据的状态变更为已批准.
        PeopleChangePo updatePo = new PeopleChangePo();
        updatePo.setStatus(SzxmEnumsUtil.StatusEnum.APPROVED.getCode());
        this.updateSelectiveByIds(updatePo, ids);
        //更改文件状态
        String bizIds = "";
        for (Integer id : ids) {
            bizIds += (id + ",");
        }
        bizIds = bizIds.substring(0, bizIds.lastIndexOf(","));
        commDocService.releaseDocByBizTypeAndBizIds("STAFF-CHANGE", bizIds);//发布关联文档

        Map<String, DictionaryVo> psotionDictMap = commDictService.getDictMapByTypeCode("base.position.type");
        for (Integer changeId : ids) {//变更人 改为 被变更人的职务
            PeopleChangePo peopleChangePo = this.selectById(changeId);
            PeoplePo peoplePo = peopleService.selectById(peopleChangePo.getAchangerId());
            peoplePo.setJob(szxmCommonUtil.getDictionaryCode(psotionDictMap, peopleChangePo.getChangeGw()));
            peopleService.updateSelectiveById(peoplePo);
        }

        //推送通知
        List<PeopleChangePo> peopleChangePos = super.selectByIds(ids);
        szxmCommonUtil.approveFlowAndSendMessage(bizType, peopleChangePos);

    }

    @Override
    public PeopleChangeVo addPeopleChange(PeopleChangeAddForm peopleChangeAddForm) {
        PeopleChangePo peopleChangePo = dozerMapper.map(peopleChangeAddForm, PeopleChangePo.class);
        peopleChangePo.setStatus(SzxmEnumsUtil.StatusEnum.INIT.toString());
        peopleChangePo.setCode("BG" + DateUtil.getDateFormat(new Date(), "yyyyMMddHHmmss"));
        this.insert(peopleChangePo);

        PeopleChangeVo peopleChangeVo = dozerMapper.map(peopleChangePo, PeopleChangeVo.class);//po对象转换为Vo对象
        ProjectTeamVo sectionVo = commProjectTeamService.getProjectTeamById(peopleChangePo.getSectionId());
        peopleChangeVo.setSectionCode(sectionVo.getCode());
        peopleChangeVo.setSectionName(sectionVo.getName());
        PlanProjectVo projectVo = commPlanProjectService.getProject(peopleChangePo.getProjectId());
        peopleChangeVo.setProjectName(projectVo.getName());
        peopleChangeVo.getStatusVo().setCode(peopleChangePo.getStatus());
        peopleChangeVo.getStatusVo().setName(SzxmEnumsUtil.StatusEnum.valueOf(peopleChangePo.getStatus()).getName());

        return peopleChangeVo;
    }

    @Override
    public void deletePeopleChange(List<Integer> ids) {
        //删除文件
        String bizIds = "";
        for (Integer id : ids) {
            bizIds += (id + ",");
        }
        bizIds = bizIds.substring(0, bizIds.lastIndexOf(","));
        commDocService.deleteDocByBizTypeAndBizIds("STAFF-CHANGE", bizIds);
        this.deleteByIds(ids);
    }

    @Override
    public PeopleChangeVo updatePeopleChange(PeopleChangeUpdateForm peopleChangeUpdateForm) {
        PeopleChangePo updatePeopleChangePo = dozerMapper.map(peopleChangeUpdateForm, PeopleChangePo.class);
        super.updateSelectiveById(updatePeopleChangePo);//根据ID更新po，值为null的不更新，只更新不为null的值

        PeopleChangePo peopleChangePo = this.selectById(updatePeopleChangePo.getId());//将数据查询出来
        PeopleChangeVo peopleChangeVo = dozerMapper.map(peopleChangePo, PeopleChangeVo.class);//po对象转换为Vo对象
        ProjectTeamVo sectionVo = commProjectTeamService.getProjectTeamById(peopleChangePo.getSectionId());
        peopleChangeVo.setSectionCode(sectionVo.getCode());
        peopleChangeVo.setSectionName(sectionVo.getName());
        PlanProjectVo projectVo = commPlanProjectService.getProject(peopleChangePo.getProjectId());
        peopleChangeVo.setProjectName(projectVo.getName());
        peopleChangeVo.getStatusVo().setCode(peopleChangePo.getStatus());
        peopleChangeVo.getStatusVo().setName(SzxmEnumsUtil.StatusEnum.valueOf(peopleChangePo.getStatus()).getName());

        return peopleChangeVo;
    }

    @Override
    public PeopleChangeVo selectByPeopleChangeId(Integer id) {
        PeopleChangePo peopleChangePo = this.selectById(id);
        PeopleChangeVo peopleChangeVo = dozerMapper.map(peopleChangePo, PeopleChangeVo.class);//po对象转换为Vo对象
        ProjectTeamVo sectionVo = commProjectTeamService.getProjectTeamById(peopleChangePo.getSectionId());
        peopleChangeVo.setSectionCode(sectionVo.getCode());
        peopleChangeVo.setSectionName(sectionVo.getName());
        PlanProjectVo projectVo = commPlanProjectService.getProject(peopleChangePo.getProjectId());
        peopleChangeVo.setProjectName(projectVo.getName());
        peopleChangeVo.getStatusVo().setCode(peopleChangePo.getStatus());
        peopleChangeVo.getStatusVo().setName(SzxmEnumsUtil.StatusEnum.valueOf(peopleChangePo.getStatus()).getName());
        return peopleChangeVo;
    }

    @Override
    public String ylPeopleChangeCheck(Integer id) {
        InputStream fileInputStrem = null;
        PeopleChangePo peopleChangePo = mapper.selectByPrimaryKey(id);
        if (ObjectUtils.isEmpty(peopleChangePo)) {
            throw new BaseException("未查出人员变更信息");
        }
        String changeGw = peopleChangePo.getChangeGw();
        if (StringHelper.isNullAndEmpty(changeGw)) {
            throw new BaseException("变更人员岗位为空");
        }
        if (changeGw.contains("总监")) {
            fileInputStrem = ResourceUtil.findResoureFile("template/zjlgcsbg.docx");
        } else if ("专业监理工程师".equals(changeGw)) {
            fileInputStrem = ResourceUtil.findResoureFile("template/jlgcsbg.docx");
        } else if ("安全员".equals(changeGw)) {
            fileInputStrem = ResourceUtil.findResoureFile("template/xmzzaqy.docx");
        } else if ("项目总工".equals(changeGw)) {
            fileInputStrem = ResourceUtil.findResoureFile("template/xmzgcsbg.docx");
        } else if (changeGw.contains("经理")) {
            fileInputStrem = ResourceUtil.findResoureFile("template/xmjlbg.docx");
        }
        if (null == fileInputStrem) {
            throw new BaseException("未匹配上文档模板");
        }
        Map<String, Object> params = new HashMap<String, Object>();

        ProjectTeamVo projectTeamVo = commProjectTeamService.getProjectTeamById(peopleChangePo.getSectionId());
        String segCode = projectTeamVo.getCode();
        String hth = ContractServiceUtils.getInstance().getContractNo(segCode);
        String cbdw = "";
        if (!ObjectUtils.isEmpty(projectTeamVo.getCuList())) {//如果施工单位不为空
            for (GeneralVo cdbwVo : projectTeamVo.getCuList()) {
                cbdw += cdbwVo.getName() + ",";
            }
        }
        if (StringHelper.isNotNullAndEmpty(cbdw)) {
            cbdw = cbdw.substring(0, cbdw.length() - 1);
        }
        params.put("cbdw", cbdw);

        String jldw = "";
        if (!ObjectUtils.isEmpty(projectTeamVo.getCcuList())) {//如果施工单位不为空
            for (GeneralVo jlbwVo : projectTeamVo.getCcuList()) {
                jldw += jlbwVo.getName() + ",";
            }
        }
        if (StringHelper.isNotNullAndEmpty(jldw)) {
            jldw = jldw.substring(0, jldw.length() - 1);
        }
        params.put("jldw", jldw);
        params.put("hth", hth);
        params.put("bh", StringHelper.formattString(peopleChangePo.getCode()));
        params.put("jsdw", "苏州地铁集团");
        params.put("reason", StringHelper.formattString(peopleChangePo.getChangeReason()));
        params.put("xmmcbdh", projectTeamVo.getCode() + "标");
        params.put("bchanger", StringHelper.formattString(peopleChangePo.getBchanger()));
        params.put("achanger", StringHelper.formattString(peopleChangePo.getAchanger()));

        File newFile = null;
        try {
            WordUtil wordUtil = new WordUtil();
            //生成临时目录
            String tempFlolderStr = System.getProperty("user.dir") + "/temporary";
            File tempFlolder = new File(tempFlolderStr);
            if (!tempFlolder.isDirectory()) {
                tempFlolder.mkdirs();
            }
            String fileName = "/rybg" + System.currentTimeMillis() + ".docx";
            String outFileName = tempFlolderStr + fileName;
            wordUtil.generateWord(fileInputStrem, params, outFileName);

            //临时生成文件
            newFile = new File(outFileName);
            FileInputStream fileInputStream = new FileInputStream(outFileName);
            String pdfName = tempFlolder + "/rybg" + System.currentTimeMillis() + ".pdf";
            docTransformUtil.docToPDF(fileInputStream, pdfName);
            return pdfName;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            newFile.delete();
        }
        return null;
    }

    @Override
    public int queryPeopleChangeCount(String sectionId) {
        Integer count = mapper.queryPeopleChangeCount(sectionId);
        return ObjectUtils.isEmpty(count) ? 0 : count;
    }


    /**
     * 上传文件
     *
     * @param fileSystemResource
     * @return
     */
    public Map<String, Object> uploadFile(FileSystemResource fileSystemResource) {
        String token = request.getHeader("Authorization");//获取认证Token
        String gateWayHost = request.getHeader("gateway_host");//获取网关请求地址

        RestTemplate restTemplate = new RestTemplate();
        // 发送请求
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);
        headers.set("Authorization", token);
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file", fileSystemResource);
        form.add("filename", fileSystemResource.getFilename());
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);
        Map<String, Object> returnMap = restTemplate.postForObject("http://" + gateWayHost + "/api/doc/file/upload", files, Map.class);
        return returnMap;
    }


    @Override
    public PageInfo<PeopleChangeVo> selectSectionPeopleChangeList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {

        PageHelper.startPage(currentPageNum, pageSize);
        List<PeopleChangeVo> peopleChangeVoList = mapper.selectPeopleChangeList(mapWhere);
        PageInfo<PeopleChangeVo> pageInfo = new PageInfo<PeopleChangeVo>(peopleChangeVoList);
        //PlanProjectVo projectVo = commPlanProjectService.getProject(Integer.valueOf(String.valueOf(mapWhere.get("projectId"))));
        if (!ObjectUtils.isEmpty(pageInfo.getList())) {
            Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(Integer.valueOf(mapWhere.get("projectId").toString()));

            for (PeopleChangeVo peopleChangeVo : pageInfo.getList()) {
                ProjectTeamVo sectionVo = sectionMap.get(peopleChangeVo.getSectionId());
                peopleChangeVo.setSectionCode(sectionVo.getCode());
                peopleChangeVo.setSectionName(sectionVo.getName());
              //  peopleChangeVo.setProjectName(projectVo.getName());
                //设置流程
                peopleChangeVo.getStatusVo().setName(SzxmEnumsUtil.StatusEnum.valueOf(peopleChangeVo.getStatusVo().getCode()).getName());
            }
        }

        return pageInfo;
    }
}
