package com.wisdom.acm.szxm.service.doc;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.po.doc.DocPo;
import com.wisdom.acm.szxm.vo.doc.DocumentInfo;
import com.wisdom.acm.szxm.vo.doc.DocumentVo;
import com.wisdom.base.common.service.CommService;

import java.io.IOException;

public interface DocAppService extends CommService<DocPo> {


    PageInfo<DocumentVo> queryDocumentVoList(Integer projectId, Integer folderId, Integer pageSize, Integer currentPageNum, String key) throws IOException;

    DocumentInfo getDocumentInfo(Integer docId) throws IOException;
}
