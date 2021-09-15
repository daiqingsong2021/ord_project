package com.wisdom.acm.szxm.service.doc.impl;

import com.alibaba.nacos.client.logger.Logger;
import com.alibaba.nacos.client.logger.LoggerFactory;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.mapper.doc.DocSectionMapper;
import com.wisdom.acm.szxm.mapper.sysscore.SubjectScoreDetailMapper;
import com.wisdom.acm.szxm.po.doc.DocSectionPo;
import com.wisdom.acm.szxm.service.doc.DocSectionService;
import com.wisdom.acm.szxm.service.sysscore.SubjectTemplateService;
import com.wisdom.acm.szxm.vo.sysscore.SubjectScoreItemVo;
import com.wisdom.base.common.enums.StatusEnum;
import com.wisdom.base.common.feign.CommProjectTeamService;
import com.wisdom.base.common.feign.sys.CommSectionService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.PageUtil;
import com.wisdom.base.common.vo.DocRelationInfoVo;
import com.wisdom.base.common.vo.ProjectTeamVo;
import com.wisdom.base.common.vo.doc.DocProjectReleaseVo;
import com.wisdom.base.common.vo.doc.DocProjectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: szc
 * @Date: 2019/7/17 17:12
 * @Version 1.0
 */
@Service
public class DocSectionServiceImpl extends BaseService<DocSectionMapper, DocSectionPo> implements DocSectionService {

    @Autowired
    private CommProjectTeamService commProjectTeamService;
    @Autowired
    private SzxmCommonUtil szxmCommonUtil;
    @Autowired
    private CommSectionService commSectionService;
    @Autowired
    private SubjectTemplateService subjectTemplateService;
    @Autowired
    private SubjectScoreDetailMapper subjectScoreDetailMapper;

    private static final Logger logger = LoggerFactory.getLogger(DocSectionServiceImpl.class);

    /**
     * 增加文档和标段的关联关系
     *
     * @param docProjectVo
     * @param sectionId
     */
    @Override
    public DocProjectVo addDocSectionIdRelation(DocProjectVo docProjectVo, Integer sectionId) {
        if (!ObjectUtils.isEmpty(sectionId)) {
            DocSectionPo docSectionPo = new DocSectionPo();
            docSectionPo.setDocId(docProjectVo.getId());
            docSectionPo.setSectionId(sectionId);
            this.insert(docSectionPo);
        }
        List<Integer> sectionIds = new ArrayList<>();
        sectionIds.add(sectionId);
        //获取标段数据集合
        List<ProjectTeamVo> projectTeamVos = commProjectTeamService.querySectionList(sectionIds);
        if (!ObjectUtils.isEmpty(projectTeamVos)) {
            ProjectTeamVo projectTeamVo = projectTeamVos.get(0);
            docProjectVo.setSection(projectTeamVo);
        }
        return docProjectVo;

    }

    @Override
    public List<DocRelationInfoVo> addDocScore(String bizType, List<DocRelationInfoVo> docRelationInfoVos) {
        Map<String, Integer> returnMap = subjectTemplateService.isInSubjectTemplate(bizType);
        if (0 == returnMap.get("isRated")) {
            //查询主观模板表判断是否需要评分  0  不需要
            return docRelationInfoVos;
        }
        List<Integer> fileIds = ListUtil.toValueList(docRelationInfoVos, "fileId", Integer.class);
        Map<String, Object> mapWhere = Maps.newHashMap();
        mapWhere.put("fileIds", fileIds);
        List<SubjectScoreItemVo> subjectScoreItemVos = subjectScoreDetailMapper.selectSubjectScore(mapWhere);
        Map<Integer, SubjectScoreItemVo> scoreItemVoMap = ListUtil.listToMap(subjectScoreItemVos, "fileId", Integer.class);
        if (ObjectUtils.isEmpty(scoreItemVoMap)) {
            return docRelationInfoVos;
        }
        for (DocRelationInfoVo docRelationInfoVo : docRelationInfoVos) {
            SubjectScoreItemVo subjectScoreItemVo = scoreItemVoMap.get(docRelationInfoVo.getFileId());
            if (!ObjectUtils.isEmpty(subjectScoreItemVo)) {
                docRelationInfoVo.setModuleTitle(subjectScoreItemVo.getModuleTitle());
                docRelationInfoVo.setRater(subjectScoreItemVo.getRater());
                docRelationInfoVo.setScore(subjectScoreItemVo.getScore());
                docRelationInfoVo.setScoreTime(subjectScoreItemVo.getScoreTime());
            }
        }
        return docRelationInfoVos;
    }


    /**
     * 将标段拼接到文档列表
     *
     * @param projectId
     * @param pageSize
     * @param pageNum
     * @param sectionIds
     * @param projectVoPageInfoList
     * @return
     */
    @Override
    public PageInfo<DocProjectVo> filterDocBySectionIds(Integer projectId, Integer pageSize, Integer pageNum, String sectionIds, List<DocProjectVo> projectVoPageInfoList) {
        List<DocProjectVo> resultList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(projectVoPageInfoList)) {
            List<Integer> sectionIdList = new ArrayList<>();
            Map<Integer, DocProjectVo> docProjectVoMap = ListUtil.listToMap(projectVoPageInfoList, "id", Integer.class);
            if (!sectionIds.contains(",") && Integer.valueOf(sectionIds).intValue() == 0) {
                //没有选择标段时，按照当前登陆人的所有标段查询文档
                sectionIdList = commSectionService.querySectionIdListList(projectId);
            } else {
                //查询能看到的所有标段List
                List<String> sectionList = szxmCommonUtil.getSectionList(projectId.toString(), sectionIds);
                for (String str : sectionList) {
                    int i = Integer.valueOf(str);
                    sectionIdList.add(i);
                }
            }
            List<ProjectTeamVo> projectTeamVos = new ArrayList<>();
            if (!ObjectUtils.isEmpty(sectionIdList)) {
                //获取标段数据集合
                projectTeamVos = commProjectTeamService.querySectionList(sectionIdList);
            }
            Map<Integer, ProjectTeamVo> projectTeamVoMap = ListUtil.listToMap(projectTeamVos, "id", Integer.class);
            Stopwatch started = Stopwatch.createStarted();
            //获取指定标段内的文档
            List<DocSectionPo> docSectionPoList = new ArrayList<>();
            if (!ObjectUtils.isEmpty(sectionIdList)) {
                Example example = new Example(DocSectionPo.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andIn("sectionId", sectionIdList);
                docSectionPoList = this.selectByExample(example);
            }
            logger.info("获取指定标段的文档id耗时为：{}秒", started.elapsed(TimeUnit.SECONDS));
            if (!ObjectUtils.isEmpty(docSectionPoList)) {
                List<Integer> docIds = ListUtil.toValueList(docSectionPoList, "docId", Integer.class);
                Map<Integer, DocSectionPo> docSectionPoMap = ListUtil.listToMap(docSectionPoList, "docId", Integer.class);
                for (Map.Entry<Integer, DocProjectVo> entry : docProjectVoMap.entrySet()) {
                    Integer id = entry.getKey();
                    if (docIds.contains(id)) {
                        DocProjectVo docProjectVo = entry.getValue();
                        DocSectionPo docSectionPo = docSectionPoMap.get(id);
                        Integer sectionId = docSectionPo.getSectionId();
                        ProjectTeamVo projectTeamVo = projectTeamVoMap.get(sectionId);
                        docProjectVo.setSection(projectTeamVo);
                        resultList.add(docProjectVo);
                    }
                }
                if (pageSize == -1) {
                    PageInfo<DocProjectVo> pageInfo = new PageInfo<>();
                    pageInfo.setSize(resultList.size());
                    pageInfo.setList(resultList);
                    return pageInfo;
                }
            }
        }
        //PageHelper.startPage(pageNum, pageSize);
        //PageInfo<DocProjectVo> pageInfo = new PageInfo<>(resultList);
        PageInfo<DocProjectVo> pageInfo = PageUtil.getPageInfo(resultList,pageSize,pageNum);
        return pageInfo;
    }

    @Override
    public List<DocProjectReleaseVo> filterDocReleaseBySectionIds(Integer projectId, String sectionIds, List<DocProjectReleaseVo> docProjectReleaseVos) {
        List<DocProjectReleaseVo> resultList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(docProjectReleaseVos)) {
            List<Integer> sectionIdList = new ArrayList<>();
            Map<Integer, DocProjectReleaseVo> docProjectVoMap = ListUtil.listToMap(docProjectReleaseVos, "id", Integer.class);
            boolean flag = false;
            if (!sectionIds.contains(",") && Integer.valueOf(sectionIds).intValue() == 0) {
//              flag = true;
                sectionIdList = commSectionService.querySectionIdListList(projectId);
            } else {
                //查询能看到的所有标段List
                List<String> sectionList = szxmCommonUtil.getSectionList(projectId.toString(), sectionIds);
                for (String str : sectionList) {
                    int i = Integer.valueOf(str);
                    sectionIdList.add(i);
                }
            }
            List<ProjectTeamVo> projectTeamVos = new ArrayList<>();
            if (!ObjectUtils.isEmpty(sectionIdList)) {
                //获取标段数据集合
                projectTeamVos = commProjectTeamService.querySectionList(sectionIdList);
            }
//            else{
//                //获取项目下所有标段集合
//                projectTeamVos = commProjectTeamService.querySectioinListByProjectId(projectId);
//            }
            Map<Integer, ProjectTeamVo> projectTeamVoMap = ListUtil.listToMap(projectTeamVos, "id", Integer.class);
            //获取指定标段内的文档
            List<DocSectionPo> docSectionPoList = new ArrayList<>();
            if (!ObjectUtils.isEmpty(sectionIdList)) {
                Example example = new Example(DocSectionPo.class);
                Example.Criteria criteria = example.createCriteria();
                if (!ObjectUtils.isEmpty(sectionIdList)) {
                    criteria.andIn("sectionId", sectionIdList);
                }
                docSectionPoList = this.selectByExample(example);
            }

            if (!ObjectUtils.isEmpty(docSectionPoList)) {

                List<Integer> docIds = ListUtil.toValueList(docSectionPoList, "docId", Integer.class);

                Map<Integer, DocSectionPo> docSectionPoMap = ListUtil.listToMap(docSectionPoList, "docId", Integer.class);

                for (Map.Entry<Integer, DocProjectReleaseVo> entry : docProjectVoMap.entrySet()) {
                    Integer id = entry.getKey();
                    if (docIds.contains(id)) {
                        DocProjectReleaseVo docProjectVo = entry.getValue();
                        DocSectionPo docSectionPo = docSectionPoMap.get(id);

                        Integer sectionId = docSectionPo.getSectionId();
                        ProjectTeamVo projectTeamVo = projectTeamVoMap.get(sectionId);

                        docProjectVo.setSection(projectTeamVo);
                        resultList.add(docProjectVo);
                    }
                }
            }
        }
        return resultList;
    }

    /**
     * 将标段拼接到文档列表
     *
     * @param projectId
     * @param sectionIds
     * @param docProjectVos
     * @return
     */
    @Override
    public List<DocProjectVo> filterDocProjectVosBySectionIds(Integer projectId, String sectionIds, List<DocProjectVo> docProjectVos) {
        List<DocProjectVo> resultList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(docProjectVos)) {
            List<Integer> sectionIdList = new ArrayList<>();

            Map<Integer, DocProjectVo> docProjectVoMap = ListUtil.listToMap(docProjectVos, "id", Integer.class);

            boolean flag = false;
            if (!sectionIds.contains(",") && Integer.valueOf(sectionIds).intValue() == 0) {
//                flag = true;
                sectionIdList = commSectionService.querySectionIdListList(projectId);
            } else {
                //查询能看到的所有标段List
                List<String> sectionList = szxmCommonUtil.getSectionList(projectId.toString(), sectionIds);
                for (String str : sectionList) {
                    int i = Integer.valueOf(str);
                    sectionIdList.add(i);
                }
            }

            List<ProjectTeamVo> projectTeamVos = new ArrayList<>();
            if (!ObjectUtils.isEmpty(sectionIdList)) {
                //获取标段数据集合
                projectTeamVos = commProjectTeamService.querySectionList(sectionIdList);
            }
            Map<Integer, ProjectTeamVo> projectTeamVoMap = ListUtil.listToMap(projectTeamVos, "id", Integer.class);

            //获取指定标段内的文档
            List<DocSectionPo> docSectionPoList = new ArrayList<>();
            if (!ObjectUtils.isEmpty(sectionIdList)) {
                Example example = new Example(DocSectionPo.class);
                Example.Criteria criteria = example.createCriteria();
                if (!ObjectUtils.isEmpty(sectionIdList)) {
                    criteria.andIn("sectionId", sectionIdList);
                }
                docSectionPoList = this.selectByExample(example);
            }

            if (!ObjectUtils.isEmpty(docSectionPoList)) {

                List<Integer> docIds = ListUtil.toValueList(docSectionPoList, "docId", Integer.class);

                Map<Integer, DocSectionPo> docSectionPoMap = ListUtil.listToMap(docSectionPoList, "docId", Integer.class);

                for (Map.Entry<Integer, DocProjectVo> entry : docProjectVoMap.entrySet()) {
                    Integer id = entry.getKey();
                    if (docIds.contains(id)) {
                        DocProjectVo docProjectVo = entry.getValue();
                        DocSectionPo docSectionPo = docSectionPoMap.get(id);

                        Integer sectionId = docSectionPo.getSectionId();
                        ProjectTeamVo projectTeamVo = projectTeamVoMap.get(sectionId);

                        docProjectVo.setSection(projectTeamVo);
                        if (docProjectVo.getStatus().getId().equals(StatusEnum.RELEASE.getCode())) {
                            resultList.add(docProjectVo);
                        }
                    }
                }
            }
        }
        return resultList;
    }

}
