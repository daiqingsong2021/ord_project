package com.wisdom.acm.szxm.service.doc;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.po.doc.DocSectionPo;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.DocRelationInfoVo;
import com.wisdom.base.common.vo.doc.DocProjectReleaseVo;
import com.wisdom.base.common.vo.doc.DocProjectVo;

import java.util.List;

/**
 * @Author: szc
 * @Date: 2019/7/17 17:07
 * @Version 1.0
 */
public interface DocSectionService  extends CommService<DocSectionPo> {


    /**
     * 将标段拼接到文档列表
     * @param projectId
     * @param pageSize
     * @param pageNum
     * @param sectionIds
     * @param projectVoPageInfoList
     * @return
     */
    PageInfo<DocProjectVo> filterDocBySectionIds(Integer projectId,Integer pageSize,Integer pageNum,String sectionIds,List<DocProjectVo> projectVoPageInfoList);

    /**
     * 增加文档和标段的关联关系
     * @param docId
     * @param sectionId
     */
    DocProjectVo addDocSectionIdRelation(DocProjectVo docProjectVo,Integer sectionId);

    /**
     * 增加主观评分
     * @param bizType
     * @param docRelationInfoVos
     * @return
     */
    List<DocRelationInfoVo> addDocScore(String bizType, List<DocRelationInfoVo> docRelationInfoVos);

    List<DocProjectReleaseVo> filterDocReleaseBySectionIds(Integer projectId,String sectionIds, List<DocProjectReleaseVo> docProjectReleaseVos);

    List<DocProjectVo> filterDocProjectVosBySectionIds(Integer projectId, String sectionIds, List<DocProjectVo> docProjectVos);
}
