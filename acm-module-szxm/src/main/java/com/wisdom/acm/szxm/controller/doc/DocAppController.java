package com.wisdom.acm.szxm.controller.doc;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.service.doc.DocAppService;
import com.wisdom.acm.szxm.vo.doc.DocumentInfo;
import com.wisdom.acm.szxm.vo.doc.DocumentVo;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("doc/app")
public class DocAppController {

    @Autowired
    private DocAppService docAppService;

    @GetMapping("/document/{projectId}/{folderId}/{size}/{current}/list")
    public ApiResult queryDocumentVoList(@PathVariable("projectId") Integer projectId, @PathVariable("folderId") Integer folderId,
                                         @PathVariable("size") Integer pageSize,@PathVariable("current") Integer currentPageNum,
                                         String key) throws IOException {
        PageInfo<DocumentVo> documentVoList = docAppService.queryDocumentVoList(projectId,folderId,pageSize,currentPageNum,key);
        return new TableResultResponse(documentVoList);
    }

    @GetMapping("/document/{docId}/info")
    public ApiResult getDocumentInfo(@PathVariable("docId") Integer docId) throws IOException {
        DocumentInfo documentInfo = docAppService.getDocumentInfo(docId);
        return ApiResult.success(documentInfo);
    }


}
