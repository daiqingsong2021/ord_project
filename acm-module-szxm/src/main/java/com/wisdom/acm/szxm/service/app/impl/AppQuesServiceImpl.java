package com.wisdom.acm.szxm.service.app.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.DateUtil;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.mapper.wtgl.QuestionMapper;
import com.wisdom.acm.szxm.po.pfe.StationPo;
import com.wisdom.acm.szxm.po.wtgl.QuestionPo;
import com.wisdom.acm.szxm.service.app.AppQuesService;
import com.wisdom.acm.szxm.service.pfe.StationService;
import com.wisdom.acm.szxm.service.wtgl.QuestionRecordService;
import com.wisdom.acm.szxm.vo.pfe.StationVo;
import com.wisdom.acm.szxm.vo.wtgl.FileBizType;
import com.wisdom.acm.szxm.vo.wtgl.QuestionRecordVo;
import com.wisdom.acm.szxm.vo.wtgl.QuestionVo;
import com.wisdom.base.common.feign.CommFileService;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.doc.DocFileInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
@Slf4j
public class AppQuesServiceImpl extends BaseService<QuestionMapper, QuestionPo> implements AppQuesService
{
    @Autowired
    private CommUserService commUserService;

    @Autowired
    private QuestionRecordService questionRecordService;

    @Autowired
    private CommFileService commFileService;

    @Autowired
    private StationService stationService;

    @Override
    public PageInfo<QuestionVo> selectAppQuestions(Map<String, Object> mapWhere, Integer pageSize,
            Integer currentPageNum)
    {
        List<String> sectionList = null;
        String sectionIds = String.valueOf(ObjectUtils.isEmpty(mapWhere.get("sectionIds"))?"":mapWhere.get("sectionIds"));//获取标段ID集合
        if(StringHelper.isNotNullAndEmpty(sectionIds))
        {
            sectionIds=StringHelper.formattString(sectionIds);
            String[] sectionIdsArray = sectionIds.split(",");
            sectionList = new ArrayList<String>(Arrays.asList(sectionIdsArray));
        }
        mapWhere.put("sectionList",sectionList);
        mapWhere.put("projectId", StringHelper.formattString(mapWhere.get("projectId")));
        mapWhere.put("bizType", StringHelper.formattString(mapWhere.get("bizType")));
        mapWhere.put("title", StringHelper.formattString(mapWhere.get("title")));

        String status = String.valueOf(ObjectUtils.isEmpty(mapWhere.get("status"))?"":mapWhere.get("status"));//获取标段ID集合
        if(StringHelper.isNotNullAndEmpty(status))
        {
            status=StringHelper.formattString(status);
            String[] statusArray = status.split(",");
            List<String> statusList = new ArrayList<String>(Arrays.asList(statusArray));
            mapWhere.remove("status");
            mapWhere.put("statusList", statusList);
        }
        mapWhere.put("type", StringHelper.formattString(mapWhere.get("type")));
        mapWhere.put("priority", StringHelper.formattString(mapWhere.get("priority")));
        mapWhere.put("createrId", StringHelper.formattString(mapWhere.get("createrId")));
        mapWhere.put("currentUserId", StringHelper.formattString(mapWhere.get("currentUserId")));
        mapWhere.put("startTime", StringHelper.formattString(mapWhere.get("startTime")));
        if(!ObjectUtils.isEmpty((mapWhere.get("endTime"))))
        {
            Date dayAfter= DateUtil.getDayAfter(DateUtil.getDateFormat(String.valueOf(mapWhere.get("endTime"))),1);
            mapWhere.put("endTime", DateUtil.getDateFormat(dayAfter));
        }
        mapWhere.put("bizId", StringHelper.formattString(mapWhere.get("bizId")));
        mapWhere.put("loginUserId",commUserService.getLoginUser().getId());
        mapWhere.put("id",StringHelper.formattString(mapWhere.get("id")));
        PageHelper.startPage(currentPageNum, pageSize);
        List<QuestionVo> questionVoList=mapper.selectQuestionList(mapWhere);
        PageInfo<QuestionVo> pageInfo = new PageInfo<QuestionVo>(questionVoList);
        if(!ObjectUtils.isEmpty(pageInfo.getList()))
        {
            List<Integer> questIds= ListUtil.toIdList(pageInfo.getList());
            //查询所有问题的record记录
//            Map<String, Object> mapWhere1 = Maps.newHashMap();
//            mapWhere1.put("questIds",questIds);
//            List<QuestionRecordVo> appQueLogs = questionRecordService.selectAppQuestionRecords(mapWhere1);
//            Map<Integer,List<QuestionRecordVo>> quesRecord=Maps.newHashMap();

            List<Integer> bizIds= Lists.newArrayList();
            bizIds.addAll(questIds);
//            if(!ObjectUtils.isEmpty(appQueLogs))
//            {
//                quesRecord=ListUtil.listToMapList(appQueLogs,"questionId",Integer.class);
//                bizIds.addAll(ListUtil.toValueList(appQueLogs,"id",Integer.class));
//            }
            //查询所有问题的文件信息
            List<FileBizType> docFileInfoVoList=mapper.selectQuestionFile(bizIds);//一个文件对应两个记录
            Map<Integer,List<DocFileInfoVo>> bizFilesMap=Maps.newHashMap();//业务与文件映射对象
            if(!ObjectUtils.isEmpty(docFileInfoVoList))
            {
                List<Integer> fileIds=ListUtil.toValueList(docFileInfoVoList,"fileId",Integer.class);
                ApiResult<List<DocFileInfoVo>> fileResult=commFileService.queryFileInfoList(fileIds);
                List<DocFileInfoVo>  docFileInfoVos=fileResult.getData();
                Map<Integer,DocFileInfoVo> docFileInfoVoMap=ListUtil.listToMap(docFileInfoVos,"id",Integer.class);
                for(FileBizType fileBizType:docFileInfoVoList)
                {
                    if(ObjectUtils.isEmpty(bizFilesMap.get(fileBizType.getBizId())))
                    {
                        List<DocFileInfoVo> docFileList=Lists.newArrayList();
                        DocFileInfoVo file=docFileInfoVoMap.get(fileBizType.getFileId());
                        String fileType = file.getFileName().substring(file.getFileName().lastIndexOf(".")+1, file.getFileName().length());
                        file.setFileType(fileType);
                        file.setSuffix(fileType);
                        docFileList.add(file);
                        bizFilesMap.put(fileBizType.getBizId(),docFileList);
                    }
                    else
                    {
                        List<DocFileInfoVo> docFileList=bizFilesMap.get(fileBizType.getBizId());
                        DocFileInfoVo file=docFileInfoVoMap.get(fileBizType.getFileId());
                        String fileType = file.getFileName().substring(file.getFileName().lastIndexOf(".")+1, file.getFileName().length());
                        file.setFileType(fileType);
                        file.setSuffix(fileType);
                        docFileList.add(file);
                    }
                }
            }

            //查出所有站点
            List<StationPo> stationPoList=stationService.selectListAll();

            for(QuestionVo questionVo:pageInfo.getList())
            {
                //List<QuestionRecordVo> appQueLogList=quesRecord.get(questionVo.getId());
                //日志，
//                if(!ObjectUtils.isEmpty(appQueLogList))
//                {
//                    for(QuestionRecordVo questionRecordVo:appQueLogList)
//                    {
//                        questionRecordVo.setDocs(bizFilesMap.get(questionRecordVo.getId()));
//                    }
//                }
 //               questionVo.setLogs(appQueLogList);
                //问题文件信息
                questionVo.setDocs(bizFilesMap.get(questionVo.getId()));
                //站点/区间
                if(!ObjectUtils.isEmpty(questionVo.getStation()))
                {
                    String[] stations = questionVo.getStation().split(",");
                    for (String s : stations)
                    {
                        for(StationPo stationPo:stationPoList)
                        {
                            if(stationPo.getProjectId().equals(questionVo.getProjectId()) && s.equals(stationPo.getCode()))
                            {
                                GeneralVo generalVo = new GeneralVo();
                                generalVo.setCode(stationPo.getCode());
                                generalVo.setName(stationPo.getName());
                                questionVo.getStationVo().add(generalVo);
                            }
                        }
                    }
                }

            }
        }
        return pageInfo;
    }

    @Override public QuestionVo selectAppQuestionsById(Integer id)
    {
        QuestionVo questionVo=mapper.selectQuestion(id);
        //站点
        if(StringHelper.isNotNullAndEmpty(questionVo.getStation()))
        {
            List<StationVo> stationVoList=stationService.queryStationByProjectId(questionVo.getProjectId());
            String[] stations = questionVo.getStation().split(",");
            for (String s : stations)
            {
                for(StationVo stationVo:stationVoList)
                {
                    if(s.equals(stationVo.getCode()))
                    {
                        GeneralVo generalVo = new GeneralVo();
                        generalVo.setCode(stationVo.getCode());
                        generalVo.setName(stationVo.getName());
                        questionVo.getStationVo().add(generalVo);
                    }
                }
            }
        }
        List<Integer> bizIds= Lists.newArrayList();
        bizIds.add(questionVo.getId());
        Map<String, Object> mapWhere1 = Maps.newHashMap();
        mapWhere1.put("questionId",questionVo.getId());
        List<QuestionRecordVo> appQueLogs = questionRecordService.selectAppQuestionRecords(mapWhere1);
        if(!ObjectUtils.isEmpty(appQueLogs))
        {
            bizIds.addAll(ListUtil.toValueList(appQueLogs,"id",Integer.class));
        }
        List<FileBizType> docFileInfoVoList=mapper.selectQuestionFile(bizIds);//
        Map<Integer,List<DocFileInfoVo>> bizFilesMap=Maps.newHashMap();//业务与文件映射对象
        if(!ObjectUtils.isEmpty(docFileInfoVoList))
        {
            List<Integer> fileIds=ListUtil.toValueList(docFileInfoVoList,"fileId",Integer.class);
            ApiResult<List<DocFileInfoVo>> fileResult=commFileService.queryFileInfoList(fileIds);
            List<DocFileInfoVo>  docFileInfoVos=fileResult.getData();
            Map<Integer,DocFileInfoVo> docFileInfoVoMap=ListUtil.listToMap(docFileInfoVos,"id",Integer.class);
            for(FileBizType fileBizType:docFileInfoVoList)
            {
                if(ObjectUtils.isEmpty(bizFilesMap.get(fileBizType.getBizId())))
                {
                    List<DocFileInfoVo> docFileList=Lists.newArrayList();
                    DocFileInfoVo file=docFileInfoVoMap.get(fileBizType.getFileId());
                    String fileType = file.getFileName().substring(file.getFileName().lastIndexOf(".")+1, file.getFileName().length());
                    file.setFileType(fileType);
                    file.setSuffix(fileType);
                    docFileList.add(file);
                    bizFilesMap.put(fileBizType.getBizId(),docFileList);
                }
                else
                {
                    List<DocFileInfoVo> docFileList=bizFilesMap.get(fileBizType.getBizId());
                    DocFileInfoVo file=docFileInfoVoMap.get(fileBizType.getFileId());
                    String fileType = file.getFileName().substring(file.getFileName().lastIndexOf(".")+1, file.getFileName().length());
                    file.setFileType(fileType);
                    file.setSuffix(fileType);
                    docFileList.add(file);
                }
            }
        }
        //问题记录文件赋值
        if(!ObjectUtils.isEmpty(appQueLogs))
        {
            for(QuestionRecordVo questionRecordVo:appQueLogs)
            {
                questionRecordVo.setDocs(bizFilesMap.get(questionRecordVo.getId()));
            }
        }
        questionVo.setLogs(appQueLogs);
        //问题文件信息
        questionVo.setDocs(bizFilesMap.get(questionVo.getId()));
        return questionVo;
    }
}
