package com.wisdom.acm.szxm.service.rygl.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.DateUtil;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.common.UUIDHexGenerator;
import com.wisdom.acm.szxm.common.officeUtils.ExcelError;
import com.wisdom.acm.szxm.common.officeUtils.ExcelUtil;
import com.wisdom.acm.szxm.common.redisUtils.RedisUtil;
import com.wisdom.acm.szxm.form.rygl.PeopleAddForm;
import com.wisdom.acm.szxm.form.rygl.PeopleUpdateForm;
import com.wisdom.acm.szxm.mapper.rygl.PeopleMapper;
import com.wisdom.acm.szxm.po.rygl.PeopleEntryDetailPo;
import com.wisdom.acm.szxm.po.rygl.PeoplePo;
import com.wisdom.acm.szxm.service.rygl.PeopleService;
import com.wisdom.acm.szxm.service.rygl.ZkService;
import com.wisdom.acm.szxm.vo.rygl.LwryHolidayVo;
import com.wisdom.acm.szxm.vo.rygl.PeopleVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.feign.CommFileService;
import com.wisdom.base.common.feign.CommOrgService;
import com.wisdom.base.common.feign.plan.project.CommPlanProjectService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.*;
import com.wisdom.base.common.vo.plan.project.PlanProjectVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Slf4j
public class PeopleServiceImpl extends BaseService<PeopleMapper, PeoplePo> implements PeopleService {
    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private ZkService zkService;

    private static final Logger logger = LoggerFactory.getLogger(PeopleServiceImpl.class);

    @Autowired
    private CommDictService commDictService;

    @Autowired
    private CommOrgService commOrgService;

    @Autowired
    private CommPlanProjectService commPlanProjectService;

    @Autowired
    private CommFileService commFileService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public PageInfo<PeopleVo> selectPeopleList(Map<String, Object> mapWhere, Integer projInfoId, Integer pageSize,
                                               Integer currentPageNum) {
        mapWhere.put("projInfoId", StringHelper.formattString(String.valueOf(projInfoId)));
        mapWhere.put("searcher", StringHelper.formattString(String.valueOf(mapWhere.get("searcher"))));
        PageHelper.startPage(currentPageNum, pageSize);
        List<PeopleVo> peopPleVoList = mapper.selectPeople(mapWhere);
        PageInfo<PeopleVo> pageInfo = new PageInfo<PeopleVo>(peopPleVoList);
        if (!ObjectUtils.isEmpty(pageInfo.getList())) {
            //查询标段编码
            Map<String, DictionaryVo> psotionDictMap = commDictService.getDictMapByTypeCode("base.position.type");
            for (PeopleVo peopleVo : pageInfo.getList()) {
                peopleVo.getJobVo()
                        .setName(szxmCommonUtil.getDictionaryName(psotionDictMap, peopleVo.getJobVo().getCode()));
                //计算年龄
                peopleVo.setAge(szxmCommonUtil.getAge(peopleVo.getBornDate()));
            }
        }


        return pageInfo;
    }

    @Override
    public PeopleVo addPeople(PeopleAddForm peopleAddForm) {
        PeoplePo peoplePo = null;
        Example example = new Example(PeoplePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("idCard", peopleAddForm.getIdCard());
        criteria.andEqualTo("projectId", peopleAddForm.getProjectId());
        peoplePo = this.selectOneByExample(example);
        boolean isInsertZk = false;
        if (!ObjectUtils.isEmpty(peoplePo)) {
            //存在即更新状态为进场
            peoplePo.setStatus("1");
            this.updateById(peoplePo);
        } else {
            isInsertZk = true;
            peoplePo = dozerMapper.map(peopleAddForm, PeoplePo.class);
            peoplePo.setTotalClassHour(BigDecimal.valueOf(0));//总学时 初始化为0
            if (StringHelper.isNullAndEmpty(peoplePo.getType()))
                peoplePo.setType("2");
            if (StringHelper.isNullAndEmpty(peoplePo.getPeoType()))
                peoplePo.setPeoType("1");
            if (StringHelper.isNullAndEmpty(peoplePo.getSex()))
                peoplePo.setSex("1");
            peoplePo.setStatus("1");
            super.insert(peoplePo);
        }
        PeopleVo peopleVo = dozerMapper.map(peoplePo, PeopleVo.class);//po对象转换为Vo对象
        peopleVo.getTypeVo().setCode(peoplePo.getType());
        peopleVo.getTypeVo().setName(szxmCommonUtil.getDictionaryName("szxm.rygl.peopleType", peoplePo.getType()));

        peopleVo.getPeoTypeVo().setCode(peoplePo.getPeoType());
        if ("0".equals(peoplePo.getPeoType()))
            peopleVo.getPeoTypeVo().setName("分包");
        else if ("1".equals(peoplePo.getType()))
            peopleVo.getPeoTypeVo().setName("自有");
        peopleVo.getSexVo().setCode(peoplePo.getSex());
        if ("0".equals(peoplePo.getSex()))
            peopleVo.getSexVo().setName("女");
        else if ("1".equals(peoplePo.getSex()))
            peopleVo.getSexVo().setName("男");
        peopleVo.getJobVo().setCode(StringHelper.formattString(peoplePo.getJob()));
        peopleVo.getJobVo().setName(szxmCommonUtil.getDictionaryName("base.position.type", peoplePo.getJob()));
        //计算年龄
        peopleVo.setAge(szxmCommonUtil.getAge(peoplePo.getBornDate()));

        if (isInsertZk) {//更新中控考勤
            try {
                //更新考勤信息
                zkService.addOrUpdateZkPeole(peopleVo);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return peopleVo;
    }

    @Override
    public List<PeoplePo> addPeopleByEntry(List<PeopleEntryDetailPo> peopleEntryDetailPoList, Integer projectId, Integer projInfoId) {
        //找出项目下的人员
        Example example = new Example(PeoplePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId);
        List<PeoplePo> peoplePoList = this.selectByExample(example);

        List<PeoplePo> unInsertPeoplePos = Lists.newArrayList();
        List<PeoplePo> insertPeoplePos = Lists.newArrayList();

        List<PeoplePo> updatePeoplePos = Lists.newArrayList();
        for (PeopleEntryDetailPo peopleEntryDetailPo : peopleEntryDetailPoList) {
            PeoplePo insertPo = dozerMapper.map(peopleEntryDetailPo, PeoplePo.class);
            insertPo.setStatus("1");//进场状态
            insertPo.setProjInfoId(projInfoId);
            insertPo.setTotalClassHour(peopleEntryDetailPo.getClassHour());

            boolean canInsert = true;
            for (PeoplePo existPeople : peoplePoList) {
                if (existPeople.getIdCard().equals(insertPo.getIdCard())) {//如果找到有相同idCard的人员，就不允许插入，累计更新他的学时，并且激活这个人员
                    canInsert = false;

                    //初始化需要更新的updatePo
                    PeoplePo updatePo = new PeoplePo();
                    dozerMapper.map(existPeople, updatePo);
                    updatePo.setTotalClassHour(existPeople.getTotalClassHour().add(insertPo.getTotalClassHour()));
                    updatePo.setType(insertPo.getType());
                    updatePo.setJob(insertPo.getJob());
                    updatePo.setPeoType(insertPo.getPeoType());
                    updatePo.setGzkh(insertPo.getGzkh());
                    updatePo.setTelPhone(insertPo.getTelPhone());
                    updatePo.setScore(insertPo.getScore());
                    updatePo.setStatus("1");
                    updatePeoplePos.add(updatePo);

                    //初始化existPod的学时，重置为与进退场相等
                    //初始化需要返回的peoplePo
                    PeoplePo returnPpo = new PeoplePo();
                    dozerMapper.map(existPeople, returnPpo);
                    returnPpo.setTotalClassHour(insertPo.getTotalClassHour());
                    returnPpo.setScore(insertPo.getScore());
                    unInsertPeoplePos.add(returnPpo);

                    break;
                }
            }

            if (canInsert) {
                insertPeoplePos.add(insertPo);
            }

        }
        this.insert(insertPeoplePos);

        for (PeoplePo updatePo : updatePeoplePos) {//批量更新人员
            this.updateById(updatePo);
        }
        try {
            List<PeopleVo> peopleVoList = Lists.newArrayList();
            for (PeoplePo peoplePo : insertPeoplePos) {
                peopleVoList.add(dozerMapper.map(peoplePo, PeopleVo.class));
            }
            List<String> names = ListUtil.toValueList(peopleVoList, "name", String.class);
            logger.info("增加或更新考勤系统人员为：{}", names);
            int zkPeole = zkService.addOrUpdateZkPeole(peopleVoList);
            String result = 1 == zkPeole ? "成功" : "失败";
            logger.info("增加或更新考勤系统人员结果：{}", result);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        insertPeoplePos.addAll(unInsertPeoplePos);
        return insertPeoplePos;
    }


    @Override
    public PeopleVo updatePeople(PeopleUpdateForm peopleUpdateForm) {
        Example example = new Example(PeoplePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", peopleUpdateForm.getProjectId());
        criteria.andEqualTo("idCard", peopleUpdateForm.getIdCard());
        criteria.andNotEqualTo("id", peopleUpdateForm.getId());
        PeoplePo existPo = this.selectOneByExample(example);
        if (!ObjectUtils.isEmpty(existPo)) {
            throw new BaseException("已存在身份证号相同人员,无法修改");
        }

        PeoplePo peopleUpdatePo = dozerMapper.map(peopleUpdateForm, PeoplePo.class);
        super.updateSelectiveById(peopleUpdatePo);//根据ID更新po，值为null的不更新，只更新不为null的值
        PeoplePo peoplePo = this.selectById(peopleUpdatePo.getId());//将数据查询出来
        PeopleVo peopleVo = dozerMapper.map(peoplePo, PeopleVo.class);//po对象转换为Vo对象
        peopleVo.getTypeVo().setCode(peoplePo.getType());
        if ("0".equals(peoplePo.getType()))
            peopleVo.getTypeVo().setName("管理人员");
        else if ("1".equals(peoplePo.getType()))
            peopleVo.getTypeVo().setName("特殊工种");
        else if ("2".equals(peoplePo.getType()))
            peopleVo.getTypeVo().setName("普通人员");

        peopleVo.getPeoTypeVo().setCode(peoplePo.getPeoType());
        if ("0".equals(peoplePo.getPeoType()))
            peopleVo.getPeoTypeVo().setName("分包");
        else if ("1".equals(peoplePo.getType()))
            peopleVo.getPeoTypeVo().setName("自有");
        peopleVo.getSexVo().setCode(peoplePo.getSex());
        if ("0".equals(peoplePo.getSex()))
            peopleVo.getSexVo().setName("女");
        else if ("1".equals(peoplePo.getSex()))
            peopleVo.getSexVo().setName("男");
        peopleVo.getJobVo().setCode(StringHelper.formattString(peoplePo.getJob()));
        peopleVo.getJobVo().setName(szxmCommonUtil.getDictionaryName("base.position.type", peoplePo.getJob()));
        //计算年龄
        peopleVo.setAge(szxmCommonUtil.getAge(peoplePo.getBornDate()));
        try {
            //更新考勤信息
            zkService.addOrUpdateZkPeole(peopleVo);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return peopleVo;
    }

    @Override
    public void deletePeople(List<Integer> ids) {
        // 删除特殊工种信息
        mapper.deleteSpeciWorkByPeoId(ids);
        //删除中控考勤系统人员
        try {
            for (Integer id : ids) {
                zkService.deleteZkPeole(id);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        //删除人员关联文件信息
        commFileService.deleteDocFileRelationByBiz(ids, "STAFF-PROJINFO-PERSON");
        //删除人员信息
        this.deleteByIds(ids);
    }

    @Override
    public void deletePeopleByInfoIds(List<Integer> allIds) {
        Example example = new Example(PeoplePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("projInfoId", allIds);
        List<PeoplePo> peoplePoList = this.selectByExample(example);
        List<Integer> delIds = Lists.newArrayList();
        for (PeoplePo peoplePo : peoplePoList) {
            delIds.add(peoplePo.getId());
        }
        if (!ObjectUtils.isEmpty(delIds))
            this.deletePeople(delIds);
    }

    @Override
    public String uploadPeopleFile(MultipartFile multipartFile, Map<String, Object> paramMap) {
        if (multipartFile.isEmpty()) {
            throw new BaseException("文件不能为空!");
        }
        String fileName = multipartFile.getOriginalFilename();//文件名
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);//文件类型
        if (!"xlsx".equals(ext)) {
            throw new BaseException("文件格式不支持!");
        }
        Workbook wb = null;
        try {
            wb = ExcelUtil.getWorkbook(multipartFile);
            Sheet sheet = ExcelUtil.getSheet(wb, 0);//获取第一页的工作簿
            // 取出指定工作簿的内容
            List<Map<String, Object>> dataList = ExcelUtil.getSheetValue(sheet, 1);
            List<Map<String, Object>> columnList = ExcelUtil.getSheetValue(sheet, 0, 1);

            //定义错误日志
            ExcelError excelError = new ExcelError();
            Integer projInfoId = Integer.valueOf(String.valueOf(paramMap.get("projInfoId")));// 进退场主键ID*
            Integer projectId = ObjectUtils.isEmpty(paramMap.get("projectId")) ? null :
                    Integer.valueOf(String.valueOf(paramMap.get("projectId")));// 项目ID
            Integer sectionId = ObjectUtils.isEmpty(paramMap.get("sectionId")) ? null :
                    Integer.valueOf(String.valueOf(paramMap.get("sectionId")));// 标段ID
            String name = "";// 名称*
            String type = "";// 人员分类(szxm.rygl.peopleType)
            String job = "";// 职务
            String sex = "";// 性别 0 女 1男
            String birth = "";// 出生日期
            String telPhone = "";// 联系电话
            String idCard = "";// 身份证号*
            String peoType = "";// 人员类型(1 自有 0 分包)
            List<PeoplePo> insertPos = Lists.newArrayList();
            String pattern = "";
            Map<String, DictionaryVo> psotionDictMap = commDictService.getDictMapByTypeCode("base.position.type");
            Map<String, DictionaryVo> peopleTypeDictMap = commDictService.getDictMapByTypeCode("szxm.rygl.peopleType");
            List<String> idCards = Lists.newArrayList();//身份证缓存 去重用

            Example example = new Example(PeoplePo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("projectId", projectId);
            List<PeoplePo> peoplePoList = this.selectByExample(example);
            List<Integer> updatePeoples = Lists.newArrayList();

            for (int i = 0; i < dataList.size(); i++) {
                Map<String, Object> datamap = dataList.get(i);
                excelError.addRow(Integer.valueOf(String.valueOf(datamap.get("rowIndex"))) + 1);

                name = String.valueOf(datamap.get("0"));
                if (StringHelper.isNullAndEmpty(name))
                    excelError.addError(0, "姓名", "人员姓名不能为空");

                type = String.valueOf(datamap.get("1"));
                type = szxmCommonUtil.getDictionaryCode(peopleTypeDictMap, type);

                job = String.valueOf(datamap.get("2"));
                job = szxmCommonUtil.getDictionaryCode(psotionDictMap, job);

                sex = String.valueOf(datamap.get("3"));
                if ("女".equals(sex))
                    sex = "0";
                else
                    sex = "1";

                birth = String.valueOf(datamap.get("4"));
                if (!StringHelper.isNullAndEmpty(birth)) {
                    boolean isMatch = DateUtil.isValidDate(birth, DateUtil.DATE_DEFAULT_FORMAT);
                    if (!isMatch)
                        excelError.addError(4, "出生日期", "出生日期非法，请确认格式为：yyyy-MM-dd");
                }
                telPhone = String.valueOf(datamap.get("5"));
                if (StringHelper.isNullAndEmpty(telPhone))
                    excelError.addError(5, "联系方式", "联系方式不能为空");
                else {
                    pattern = "^\\d+$";
                    boolean isMatch = Pattern.matches(pattern, String.valueOf(datamap.get("5")));
                    if (!isMatch)
                        excelError.addError(5, "联系方式", "联系方式只能为数字");
                }

                idCard = String.valueOf(datamap.get("6"));
                if (StringHelper.isNullAndEmpty(idCard))
                    excelError.addError(6, "身份证号", "身份证号不能为空");
                else {
                    pattern =
                            "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
                    boolean isMatch = Pattern.matches(pattern, idCard);
                    if (!isMatch)
                        excelError.addError(6, "身份证号", "身份证号格式不对");
                    else if (idCards.contains(idCard))
                        excelError.addError(6, "身份证号", "请确认表格内身份证号是否存在重复");
                    else {
                        idCards.add(idCard);
                        for (PeoplePo existPeople : peoplePoList) {
                            if (existPeople.getIdCard().equals(idCard)) {
                                //如果找到 则更新这个人的 状态
                                updatePeoples.add(existPeople.getId());
                                break;
                            }
                        }
                    }
                }

                peoType = String.valueOf(datamap.get("7"));
                if ("分包".equals(peoType))
                    peoType = "0";
                else
                    peoType = "1";

                PeoplePo peoplePo = new PeoplePo();
                peoplePo.setName(name);
                peoplePo.setJob(job);
                if (StringHelper.isNotNullAndEmpty(birth))
                    peoplePo.setBornDate(DateUtil.formatDate(birth, "yyyy-MM-dd"));
                peoplePo.setTelPhone(telPhone);
                peoplePo.setPeoType(peoType);
                peoplePo.setProjectId(projectId);
                peoplePo.setSectionId(sectionId);
                peoplePo.setSex(sex);
                peoplePo.setType(type);
                peoplePo.setIdCard(idCard);
                peoplePo.setProjInfoId(projInfoId);
                peoplePo.setTotalClassHour(BigDecimal.valueOf(0));
                insertPos.add(peoplePo);
            }

            if (!excelError.isHasError()) {
                this.insert(insertPos);
                PeoplePo updatePo = new PeoplePo();
                updatePo.setStatus("1");
                this.updateSelectiveByIds(updatePo, updatePeoples);
                try {
                    List<PeopleVo> peopleVoList = Lists.newArrayList();
                    for (PeoplePo peoplePo : insertPos) {
                        peopleVoList.add(dozerMapper.map(peoplePo, PeopleVo.class));
                    }
                    zkService.addOrUpdateZkPeole(peopleVoList);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                return "";
            } else {//导出错误Excel
                Map<String, Object> configMap = Maps.newHashMap();
                String errorId = UUIDHexGenerator.generator();
                redisUtil.setxObjectValue(errorId, excelError, 120);//120秒后消亡
                return errorId;
            }
        } catch (Exception e) {
            throw new BaseException("导入错误!");
        }
    }

    @Override
    public List<PeopleVo> selectPeople(Map<String, Object> mapWhere) {
        List<PeopleVo> peopleVoList = mapper.selectPeople(mapWhere);
        Map<String, DictionaryVo> psotionDictMap = commDictService.getDictMapByTypeCode("base.position.type");
        for (PeopleVo peopleVo : peopleVoList) {
            peopleVo.getJobVo()
                    .setName(szxmCommonUtil.getDictionaryName(psotionDictMap, peopleVo.getJobVo().getCode()));
            //计算年龄
            peopleVo.setAge(szxmCommonUtil.getAge(peopleVo.getBornDate()));
        }
        return peopleVoList;
    }

    @Override
    public List<PeopleVo> selectOrgPeople(Map<String, Object> mapWhere) {
        return mapper.selectOrgPeople(mapWhere);
    }

    @Override
    public List<PeopleVo> selectAllKqPeople(Map<String, Object> mapWhere) {
        List<PeopleVo> peopleVoList = mapper.selectAllKqPeople(mapWhere);
        Map<String, DictionaryVo> psotionDictMap = commDictService.getDictMapByTypeCode("base.position.type");
        PlanProjectVo projectVo = null;
        Map<Integer, ProjectTeamVo> sectionMap = null;
        if (!ObjectUtils.isEmpty(mapWhere.get("projectId"))) {
            projectVo = commPlanProjectService.getProject(Integer.valueOf(String.valueOf(mapWhere.get("projectId"))));
            sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());
        }

        for (PeopleVo peopleVo : peopleVoList) {
            peopleVo.getJobVo()
                    .setName(szxmCommonUtil.getDictionaryName(psotionDictMap, peopleVo.getJobVo().getCode()));
            //计算年龄
            peopleVo.setAge(szxmCommonUtil.getAge(peopleVo.getBornDate()));

            if (!ObjectUtils.isEmpty(projectVo)) {
                peopleVo.setProjectId(projectVo.getId());
                peopleVo.setProjectName(projectVo.getName());
            }
            if (!ObjectUtils.isEmpty(sectionMap)) {
                ProjectTeamVo teamVo = sectionMap.get(peopleVo.getSectionId());
                if (!ObjectUtils.isEmpty(teamVo)) {
                    peopleVo.setSectionCode(teamVo.getCode());
                    peopleVo.setSectionName(teamVo.getName());
                }
            }
        }
        return peopleVoList;
    }

    /**
     * 根据身份证查询人员信息  若不传身份证，则查询该项目下所有进场人员
     *
     * @param idCard
     * @return
     */
    @Override
    public List<PeopleVo> selectPeopleByIDCard(String idCard) {
        //找出项目下进场的人员
        Map<String, Object> mapWhere = Maps.newHashMap();
        mapWhere.put("status", "1");
        if (StringHelper.isNotNullAndEmpty(idCard)) {
            mapWhere.put("idCard", idCard);
        }
        return mapper.selectPeopleByIDCard(mapWhere);
    }

    @Override
    public List<PeopleVo> selectAllKqPeopleAllSection(Map<String, Object> mapWhere) {
        List<PeopleVo> peopleVoList = mapper.selectAllKqPeople(mapWhere);
        PlanProjectVo projectVo = commPlanProjectService.getProject(Integer.valueOf(String.valueOf(mapWhere.get("projectId"))));
        Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());
        for (PeopleVo peopleVo : peopleVoList) {
            ProjectTeamVo sectionVo = sectionMap.get(peopleVo.getSectionId());
            if (!ObjectUtils.isEmpty(sectionVo)) {
                peopleVo.setSectionCode(sectionVo.getCode());
                peopleVo.setSectionName(sectionVo.getName());
            }
        }
        return peopleVoList;
    }

    @Override
    public List<PeopleVo> selectAddressBookPeople(Map<String, Object> mapWhere) {
        return mapper.selectAddressBookPeople(mapWhere);
    }

    @Override
    public   PageInfo<PeopleVo> selectSectionPeopleList(Map<String,Object> mapWhere,Integer projInfoId, Integer pageSize, Integer currentPageNum){
        mapWhere.put("projInfoId", StringHelper.formattString(String.valueOf(projInfoId)));
        mapWhere.put("searcher", StringHelper.formattString(String.valueOf(mapWhere.get("searcher"))));
        List<PeopleVo> peopPleVoList = mapper.selectSectionPeople(mapWhere);
        if (!ObjectUtils.isEmpty(peopPleVoList)) {
            for (PeopleVo peopleVo : peopPleVoList) {
                if("0".equals(peopleVo.getStatusVo().getCode()) )
                {
                    peopleVo.setKqStatus("退场");
                }else if("0".equals(peopleVo.getIsQq()))
                {
                    peopleVo.setKqStatus("在岗");
                }
                else if("1".equals(peopleVo.getIsQq()) && "0".equals(peopleVo.getIsQj()))
                {
                    //缺勤 且未请假
                    peopleVo.setKqStatus("离岗");
                }else if("1".equals(peopleVo.getIsQq()) && "1".equals(peopleVo.getIsQj()))
                {
                    peopleVo.setKqStatus("请假");
                }else
                {
                    peopleVo.setKqStatus("考勤异常");
                }

            }
        }
        PageInfo<PeopleVo> pageInfo = new PageInfo<PeopleVo>(peopPleVoList);
        return pageInfo;
    }

    @Override
    public List<LwryHolidayVo> getConstructionCrowdInfo(int sectionId) {
        if(sectionId == 0){
            return Collections.emptyList();
        }
        //根据sectionId获取到当前的组织id(作业队)
        int sysOrgId = mapper.getSysOrgIdBySectionId(sectionId);
        if(sysOrgId == 0){
            return Collections.emptyList();
        }
        //根据作业队查询下面的所有人员
        List<LwryHolidayVo> lwryInfoList = mapper.getLwryInfoByOrgId(sysOrgId);
        //根据人员id获取当前人员所在的部门
        for(LwryHolidayVo lwryHolidayVo : lwryInfoList){
            SysOrgInfoVo sysOrgInfoVo=commOrgService.getUserOrgInfo(lwryHolidayVo.getPeopleId());
            lwryHolidayVo.setCompanyOrgName(sysOrgInfoVo.getOrgName());
        }
        return lwryInfoList;
    }
}
