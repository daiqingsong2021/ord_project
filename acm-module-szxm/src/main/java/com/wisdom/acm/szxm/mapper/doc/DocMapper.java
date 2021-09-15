package com.wisdom.acm.szxm.mapper.doc;

import com.wisdom.acm.szxm.po.doc.DocPo;
import com.wisdom.acm.szxm.vo.doc.DocumentInfo;
import com.wisdom.acm.szxm.vo.doc.DocumentVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DocMapper extends CommMapper<DocPo> {

    List<DocumentVo> selectDocumentVoList(@Param("projectId") Integer projectId, @Param("folderId") Integer folderId, @Param("key") String key);

    //文档详情
    DocumentInfo selectDocumentInfo(@Param("docId") Integer docId);

    Integer getFavoriteIdByDocId(@Param("docId") Integer docId);
}
