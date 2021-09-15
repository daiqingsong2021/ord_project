package com.wisdom.acm.szxm.service.rygl.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.DateUtil;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.common.SzxmEnumsUtil;
import com.wisdom.acm.szxm.common.officeUtils.DocTransformUtil;
import com.wisdom.acm.szxm.common.officeUtils.WordUtil;
import com.wisdom.acm.szxm.form.rygl.HolidayAddForm;
import com.wisdom.acm.szxm.form.rygl.HolidayUpdateForm;
import com.wisdom.acm.szxm.mapper.rygl.HolidayMapper;
import com.wisdom.acm.szxm.po.rygl.HolidayPo;
import com.wisdom.acm.szxm.po.rygl.KqConfigPo;
import com.wisdom.acm.szxm.po.rygl.PeoplePo;
import com.wisdom.acm.szxm.po.rygl.ProjInfoPo;
import com.wisdom.acm.szxm.service.rygl.HolidayService;
import com.wisdom.acm.szxm.service.rygl.KqConfigService;
import com.wisdom.acm.szxm.service.rygl.PeopleService;
import com.wisdom.acm.szxm.service.rygl.ProjInfoService;
import com.wisdom.acm.szxm.vo.rygl.HolidayVo;
import com.wisdom.acm.szxm.vo.rygl.LwryHolidayVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.feign.CommOrgService;
import com.wisdom.base.common.feign.CommProjectTeamService;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.feign.plan.project.CommPlanProjectService;
import com.wisdom.base.common.feign.sys.CommRoleService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.ResourceUtil;
import com.wisdom.base.common.util.calc.calendar.CalendarUtil;
import com.wisdom.base.common.util.calc.calendar.PmCalendar;
import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.ProjectTeamVo;
import com.wisdom.base.common.vo.RoleVo;
import com.wisdom.base.common.vo.UserInfo;
import com.wisdom.base.common.vo.plan.project.PlanProjectVo;
import com.wisdom.webservice.contract.ContractServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
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
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HolidayServiceImpl extends BaseService<HolidayMapper, HolidayPo> implements HolidayService {

    @Autowired
    private CommProjectTeamService commProjectTeamService;

    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private DocTransformUtil docTransformUtil;

    @Autowired
    private KqConfigService kqConfigService;

    @Autowired
    private CommPlanProjectService commPlanProjectService;


    @Autowired
    private CommDictService commDictService;

    @Autowired
    private CommUserService commUserService;

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private CommRoleService commRoleService;

    @Autowired
    private CommOrgService commOrgService;

    @Autowired
    private ProjInfoService projInfoService;

    @Autowired
    private HttpServletRequest request;

    @Override
    public PageInfo<HolidayVo> selectHolidayList(Map<String, Object> mapWhere, List<String> sectionList,
                                                 Integer pageSize, Integer currentPageNum) {
        mapWhere.put("sectionList", sectionList);
        mapWhere.put("searcher", StringHelper.formattString(String.valueOf(mapWhere.get("searcher"))));
        PageHelper.startPage(currentPageNum, pageSize);
        List<HolidayVo> holidayVoList = mapper.selectHolidayList(mapWhere);
        PageInfo<HolidayVo> pageInfo = new PageInfo<HolidayVo>(holidayVoList);

        if (!ObjectUtils.isEmpty(pageInfo.getList())) {
            PlanProjectVo projectVo = commPlanProjectService.getProject(Integer.valueOf(String.valueOf(mapWhere.get("projectId"))));
            Map<String, DictionaryVo> holiDictMap = commDictService.getDictMapByTypeCode("szxm.rygl.holitype");
            Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());
            for (HolidayVo holidayVo : pageInfo.getList()) {
                holidayVo.getTypeVo().setName(szxmCommonUtil.getDictionaryName(holiDictMap, holidayVo.getTypeVo().getCode()));
                ProjectTeamVo sectionVo = sectionMap.get(holidayVo.getSectionId());
                holidayVo.setSectionCode(sectionVo.getCode());
                holidayVo.setSectionName(sectionVo.getName());
                holidayVo.setProjectName(projectVo.getName());

                //设置流程
                holidayVo.getStatusVo().setName(SzxmEnumsUtil.StatusEnum.valueOf(holidayVo.getStatusVo().getCode()).getName());
            }
        }
        return pageInfo;
    }


    @Override
    public List<HolidayVo> selectFlowHolidayList(Map<String, Object> mapWhere, List<String> sectionList) {
        if (StringHelper.isNotNullAndEmpty(String.valueOf(mapWhere.get("ids")))) {//如果主键Ids 不为空
            String[] idsArray = String.valueOf(mapWhere.get("ids")).split(",");
            List<String> ids = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("ids", ids);
        } else {
            mapWhere.put("sectionList", sectionList);
            mapWhere.put("searcher", StringHelper.formattString(String.valueOf(mapWhere.get("searcher"))));
        }
        List<HolidayVo> holidayVoList = mapper.selectHolidayList(mapWhere);
        if (!ObjectUtils.isEmpty(holidayVoList)) {
            PlanProjectVo projectVo = commPlanProjectService.getProject(holidayVoList.get(0).getProjectId());
            Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());
            Map<String, DictionaryVo> holiDictMap = commDictService.getDictMapByTypeCode("szxm.rygl.holitype");
            for (HolidayVo holidayVo : holidayVoList) {
                holidayVo.getTypeVo().setName(szxmCommonUtil.getDictionaryName(holiDictMap, holidayVo.getTypeVo().getCode()));
                ProjectTeamVo sectionVo = sectionMap.get(holidayVo.getSectionId());
                holidayVo.setSectionCode(sectionVo.getCode());
                holidayVo.setSectionName(sectionVo.getName());
                holidayVo.setProjectName(projectVo.getName());

                //设置流程
                holidayVo.getStatusVo().setName(SzxmEnumsUtil.StatusEnum.valueOf(holidayVo.getStatusVo().getCode()).getName());
            }
        }
        return holidayVoList;
    }

    @Override
    public List<HolidayVo> selectInOneDate(Map<String, Object> holiQueryMap) {
        return mapper.selectInOneDate(holiQueryMap);
    }

    @Override
    public void approveHolidayFlow(String bizType, List<Integer> ids) {
        //1 把业务数据的状态变更为已批准.
        HolidayPo updatePo = new HolidayPo();
        updatePo.setStatus(SzxmEnumsUtil.StatusEnum.APPROVED.getCode());
        this.updateSelectiveByIds(updatePo, ids);
        //更改文件状态


        //推送通知
        List<HolidayPo> holidayPos = super.selectByIds(ids);
        szxmCommonUtil.approveFlowAndSendMessage(bizType, holidayPos);
    }

    @Override
    public String getHolidayWord(Integer id) {
        HolidayPo holidayPo = mapper.selectByPrimaryKey(id);
        PeoplePo peoplePo = peopleService.selectById(holidayPo.getPeopleId());
        PeoplePo jqPerson = peopleService.selectById(holidayPo.getAgentId());
        ProjInfoPo projInfoPo = projInfoService.selectById(peoplePo.getProjInfoId());
        String orgType = projInfoPo.getOrgType();

        InputStream fileInputStrem = null;
        // 单位类型3 为 监理单位 对应监理部请假  2 为 施工单位 对应 经理部请假
        if ("3".equals(orgType)) {
            fileInputStrem = ResourceUtil.findResoureFile("template/jianlbryqj.docx");
        } else if ("2".equals(orgType)) {
            fileInputStrem = ResourceUtil.findResoureFile("template/jinglbryqj.docx");
        }
        if (null == fileInputStrem) {
            throw new BaseException("未匹配上文档模板");
        }

        ProjectTeamVo projectTeamVo = commProjectTeamService.getProjectTeamById(holidayPo.getSectionId());
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

        Map<String, Object> params = getStringObjectMap(holidayPo, jqPerson, projectTeamVo, hth, cbdw);

        File newFile = null;
        try {
            WordUtil wordUtil = new WordUtil();
            //生成临时目录
            String tempFlolderStr = System.getProperty("user.dir") + "/temporary";
            File tempFlolder = new File(tempFlolderStr);
            if (!tempFlolder.isDirectory()) {
                tempFlolder.mkdirs();
            }
            String fileName = "/qjsq" + System.currentTimeMillis() + ".docx";
            String outFileName = tempFlolderStr + fileName;
            wordUtil.generateWord(fileInputStrem, params, outFileName);

            //临时生成文件
            newFile = new File(outFileName);
            FileInputStream fileInputStream = new FileInputStream(outFileName);
            String pdfName = tempFlolder + "/qjsq" + System.currentTimeMillis() + ".pdf";
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
    public int queryHolidayCount(String sectionId) {
        Integer count = mapper.queryHolidayCount(sectionId);
        return ObjectUtils.isEmpty(count) ? 0 : count;
    }

    /**
     * 生成请假审批单所需数据
     *
     * @param holidayPo     holidayPo
     * @param jqPerson      工作期间负责人信息
     * @param projectTeamVo projectTeamVo
     * @param hth           合同号
     * @param cbdw          施工单位
     * @return
     */
    private Map<String, Object> getStringObjectMap(HolidayPo holidayPo, PeoplePo jqPerson, ProjectTeamVo projectTeamVo, String hth, String cbdw) {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> styleMap = new HashMap<String, Object>();
        styleMap.put("underline", UnderlinePatterns.SINGLE);

        params.put("cbdw", cbdw);
        params.put("cbdw_style", styleMap);

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
        params.put("jldw_style", styleMap);
        params.put("hth", hth);
        params.put("hth_style", styleMap);
        params.put("bh", StringHelper.formattString(holidayPo.getSerialId()));
        params.put("bh_style", styleMap);
        params.put("jsdw", "苏州地铁集团");
        params.put("jsdw_style", styleMap);
        params.put("reason", holidayPo.getReason());
        params.put("reason_style", styleMap);
        params.put("startday", DateUtil.getDateFormat(holidayPo.getStartTime()));
        params.put("startday_style", styleMap);
        params.put("endday", DateUtil.getDateFormat(holidayPo.getEndTime()));
        params.put("endday_style", styleMap);
        params.put("other", jqPerson.getName());
        params.put("other_style", styleMap);
        params.put("person", holidayPo.getPeopleName());
        params.put("person_style", styleMap);
        params.put("job", holidayPo.getRyZw());
        params.put("job_style", styleMap);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(holidayPo.getCreatTime());
        // 如果数据为int 类型 可能无法写入文档
        params.put("year", calendar.get(Calendar.YEAR) + "");
        params.put("year_style", styleMap);
        params.put("month", calendar.get(Calendar.MONTH) + "");
        params.put("month_style", styleMap);
        params.put("day", calendar.get(Calendar.DATE) + "");
        params.put("day_style", styleMap);
        return params;
    }

    @Override
    public HolidayVo addHoliday(HolidayAddForm holidayAddForm) {
        UserInfo userInfo = commUserService.getLoginUser();
        Integer bbmId = commOrgService.getUserOrgInfo(userInfo.getId()).getId();//当前登录用户的部门ID
        List<RoleVo> roleVos = commRoleService.queryRoleList(bbmId, userInfo.getId());
        String roleCode = ListUtil.listToNames(roleVos, "code");
        //由于劳务人员的请假申请是统一由考勤管理员提交的，所以肯定是管理员
        if (roleCode.indexOf("YZ_KQGLY") <= -1) {//如果不是考勤管理员
            if(StringUtils.equalsIgnoreCase(holidayAddForm.getPeopleType(),"lwry")){
                throw new BaseException("该人员属于劳务人员，无法为劳务人员请假，联系部门考勤管理员！");
            }
            //此时peopleId 传过来的是 系统当前登录用户ID.判断该用户所对应的的人员（手机号匹配）是否在该标段下进场，如果否，抛出错误
            Example example = new Example(PeoplePo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("telPhone", userInfo.getPhone());//手机号验证
            criteria.andEqualTo("sectionId", holidayAddForm.getSectionId());//标段ID
            criteria.andEqualTo("status", "1");//进场的人员
            PeoplePo exisPeoplePo = peopleService.selectOneByExample(example);
            if (ObjectUtils.isEmpty(exisPeoplePo)) {//如果未查到记录，说明未找到，不匹配
                throw new BaseException("该人员在该标段下未进场，无法请假！");
            }
            holidayAddForm.setPeopleId(exisPeoplePo.getId());//重设为人员的ID

            if (StringHelper.isNullAndEmpty(holidayAddForm.getRyZw())) {
                String jobName = szxmCommonUtil.getDictionaryName("base.position.type", exisPeoplePo.getJob());
                holidayAddForm.setRyZw(jobName);
            }
        }

        //判断同一请假人请假的开始时间，完成时间是否有重叠，重叠的话不允许增加
        Map<String, Object> mapWhere = Maps.newHashMap();
        mapWhere = Maps.newHashMap();
        mapWhere.put("peopleId", holidayAddForm.getPeopleId());
        mapWhere.put("startTime", DateUtil.getDateFormat(holidayAddForm.getStartTime()));
        mapWhere.put("endTime", DateUtil.getDateFormat(holidayAddForm.getEndTime()));
        List<HolidayVo> holidayVoList = mapper.queryHisTory(mapWhere);
        if (!ObjectUtils.isEmpty(holidayVoList) && holidayVoList.size() > 0) {
            throw new BaseException("请假天数与之前请假有重合，无法添加");
        }
        HolidayPo holidayPo = dozerMapper.map(holidayAddForm, HolidayPo.class);
        holidayPo.setStatus(SzxmEnumsUtil.StatusEnum.INIT.toString());
        holidayPo.setSerialId("QJ" + DateUtil.getDateFormat(new Date(), "yyyyMMddHHmmss"));
        if(StringUtils.isBlank(holidayAddForm.getPeopleType())){
            holidayPo.setRyType("glry");
        }else{
            holidayPo.setRyType(StringUtils.lowerCase(holidayAddForm.getPeopleType()));
            holidayPo.setLastUpdUser(userInfo.getId());
            holidayPo.setInitiatorId(userInfo.getId());
            holidayPo.setInitiator(userInfo.getActuName());
            holidayPo.setInitTime(new Date());
        }
        super.insert(holidayPo);
        return selectByHolidayId(holidayPo.getId());
    }

    @Override
    public void deleteHoliday(List<Integer> ids) {
        this.deleteByIds(ids);
    }

    @Override
    public HolidayVo updateHoliday(HolidayUpdateForm holidayUpdateForm) {
        HolidayPo updateConfigPo = dozerMapper.map(holidayUpdateForm, HolidayPo.class);
        //修改的时候应该也判断当前修改的时候是否与之前的数据重复
        Map<String, Object> mapWhere = new HashMap<>();
        mapWhere.put("peopleId", holidayUpdateForm.getPeopleId());
        mapWhere.put("startTime", DateUtil.getDateFormat(holidayUpdateForm.getStartTime()));
        mapWhere.put("endTime", DateUtil.getDateFormat(holidayUpdateForm.getEndTime()));
        List<HolidayVo> holidayVoList = mapper.queryHisTory(mapWhere);
        //修改时应该去除掉自己的那条记录
        holidayVoList = holidayVoList.stream().filter(item->item.getId().intValue() != holidayUpdateForm.getId().intValue()).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(holidayVoList) && holidayVoList.size() > 0) {
            throw new BaseException("请假天数与之前请假有重合，无法添加");
        }
        super.updateSelectiveById(updateConfigPo);//根据ID更新po，值为null的不更新，只更新不为null的值
        return selectByHolidayId(updateConfigPo.getId());
    }

    @Override
    public HolidayVo selectByHolidayId(Integer id) {
        Map<String, Object> mapWhere = Maps.newHashMap();
        mapWhere.put("id", id);
        List<HolidayVo> holidayVoList = mapper.selectHolidayList(mapWhere);
        HolidayVo holidayVo = holidayVoList.get(0);
        ProjectTeamVo sectionVo = commProjectTeamService.getProjectTeamById(holidayVo.getSectionId());
        holidayVo.setSectionCode(sectionVo.getCode());
        holidayVo.setSectionName(sectionVo.getName());
        PlanProjectVo projectVo = commPlanProjectService.getProject(holidayVo.getProjectId());
        holidayVo.setProjectName(projectVo.getName());

        holidayVo.getTypeVo().setName(szxmCommonUtil.getDictionaryName("szxm.rygl.holitype", holidayVo.getTypeVo().getCode()));
        holidayVo.getStatusVo().setName(SzxmEnumsUtil.StatusEnum.valueOf(holidayVo.getStatusVo().getCode()).getName());
        return holidayVo;
    }

    /**
     * 根据项目ID,标段ID，开始时间，完成时间 查询标段所属日历的下的天数,如查不到则去默认配置
     *
     * @param projectId
     * @param sectionId
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public int getDaysBySIdAndTime(Integer projectId, Integer sectionId, Date startTime, Date endTime) {
        Example example = new Example(KqConfigPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId);
        criteria.andEqualTo("sectionId", sectionId);
        criteria.andEqualTo("type", "1");
        KqConfigPo SectionKqConfig = kqConfigService.selectOneByExample(example);
        Integer calenderId;
        if (ObjectUtils.isEmpty(SectionKqConfig)) {//如果查不到取全局日历
            example.clear();
            criteria = example.createCriteria();
            criteria.andEqualTo("projectId", projectId);
            criteria.andEqualTo("type", "0");
            KqConfigPo allKqConfig = kqConfigService.selectOneByExample(example);
            if (ObjectUtils.isEmpty(allKqConfig)) {//全局日历取不到 则取默认日历ID
                calenderId = Integer.valueOf(szxmCommonUtil.getDefaultPmCalendar().getId());
            } else calenderId = allKqConfig.getCalenderId();
        } else {
            calenderId = SectionKqConfig.getCalenderId();
        }
        //根据日历ID，判断是否工作日，是days+1
        int days = 0;
        PmCalendar pmCalendar = szxmCommonUtil.getPmCalendar(calenderId);

        List<Date> dateList = DateUtil.getEveryDay(startTime, endTime);
        for (Date date : dateList) {
            boolean isWorkDay = CalendarUtil.isWorkDate(date, pmCalendar);//判断是否工作日
            if (isWorkDay) days++;
        }
        return days;
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
    public List<HolidayVo> queryLwryHolidayRecord(Map<String, Object> mapWhere) {
        return mapper.queryLwryHolidayRecord(mapWhere);
    }

    @Override
    public PageInfo<LwryHolidayVo> selectSectionLwryHolidayList(Map<String, Object> mapWhere, Integer pageSize,
                                                                Integer currentPageNum) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<LwryHolidayVo> holidayVoList = mapper.selectSectionLwryHolidayList(mapWhere);
        PageInfo<LwryHolidayVo> pageInfo = new PageInfo<LwryHolidayVo>(holidayVoList);

        if (!ObjectUtils.isEmpty(pageInfo.getList())) {
            PlanProjectVo projectVo = commPlanProjectService.getProject(Integer.valueOf(String.valueOf(mapWhere.get("projectId"))));
            Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());
            for (LwryHolidayVo holidayVo : pageInfo.getList()) {
                ProjectTeamVo sectionVo = sectionMap.get(holidayVo.getSectionId());
                holidayVo.setSectionCode(sectionVo.getCode());
                holidayVo.setSectionName(sectionVo.getName());
                holidayVo.setProjectName(projectVo.getName());
                holidayVo.setStatusDesc(StringUtils.equalsIgnoreCase(holidayVo.getStatus(),"APPROVED")?"已完成":"新建");
            }
        }
        return pageInfo;
    }
}
