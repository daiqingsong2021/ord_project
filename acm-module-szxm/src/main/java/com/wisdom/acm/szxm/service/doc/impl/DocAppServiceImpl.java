package com.wisdom.acm.szxm.service.doc.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.controller.doc.FastDFSClient;
import com.wisdom.acm.szxm.mapper.doc.DocMapper;
import com.wisdom.acm.szxm.po.doc.DocPo;
import com.wisdom.acm.szxm.service.doc.DocAppService;
import com.wisdom.acm.szxm.vo.doc.DocumentInfo;
import com.wisdom.acm.szxm.vo.doc.DocumentVo;
import com.wisdom.acm.szxm.vo.doc.FdfsFileVo;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.vo.DictionaryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;


@Service
public class DocAppServiceImpl extends BaseService<DocMapper, DocPo> implements DocAppService {

    @Autowired(required = false)
    private CommDictService commDictService;

    /**
     *  获取文件夹或文件列表
      * @param projectId
     * @param folderId
     * @param key
     * @return
     */
    @Override
    public PageInfo<DocumentVo> queryDocumentVoList(Integer projectId, Integer folderId, Integer pageSize, Integer currentPageNum, String key) throws IOException {
        PageHelper.startPage(currentPageNum, pageSize);
        List<DocumentVo> documentVoList = this.mapper.selectDocumentVoList(projectId,folderId,key);
        if (!ObjectUtils.isEmpty(documentVoList)){
            for (DocumentVo documentVo:documentVoList){
                String fileName = documentVo.getName();
                if (!ObjectUtils.isEmpty(fileName)){
                    if(fileName.contains(".")){
                        documentVo.setSuffix(fileName.substring(fileName.lastIndexOf(".") + 1));
                    }
                }
            }
        }
        PageInfo<DocumentVo> pageInfo = new PageInfo<>(documentVoList);
        return pageInfo;
    }

    @Override
    public DocumentInfo getDocumentInfo(Integer docId) throws IOException{
        DocumentInfo documentInfo = this.mapper.selectDocumentInfo(docId);
        if (!ObjectUtils.isEmpty(documentInfo)){
            //设置文档专业
            Map<String, DictionaryVo> professionMap = commDictService.getDictMapByTypeCode("doc.profession");
            if (!ObjectUtils.isEmpty(documentInfo.getDomain()) && !ObjectUtils.isEmpty(professionMap)){
                documentInfo.setDomain(String.valueOf(professionMap.get(documentInfo.getDomain()).getName()));
            }
            //设置文档密级
            Map<String, DictionaryVo> secutyMap = commDictService.getDictMapByTypeCode("comm.secutylevel");
            if (!ObjectUtils.isEmpty(documentInfo.getSecurity()) && !ObjectUtils.isEmpty(secutyMap)){
                documentInfo.setSecurity(String.valueOf(secutyMap.get(documentInfo.getSecurity()).getName()));
            }
            //文档类别
            Map<String, DictionaryVo> docClassifyMap = commDictService.getDictMapByTypeCode("doc.docclassify");
            if (!ObjectUtils.isEmpty(documentInfo.getType()) && !ObjectUtils.isEmpty(docClassifyMap)){
                documentInfo.setType(String.valueOf(docClassifyMap.get(documentInfo.getType()).getName()));
            }
            documentInfo.setCategory("project".equals(documentInfo.getCategory())  ? "项目文档" : "企业文档");
            Integer favoriteId = this.mapper.getFavoriteIdByDocId(docId);
            documentInfo.setFavorite(0);
            if (favoriteId!=null){
                documentInfo.setFavorite(1);
            }

            String fileName = documentInfo.getName();
            if (!ObjectUtils.isEmpty(fileName)){
                if(fileName.contains(".")){
                    documentInfo.setSuffix(fileName.substring(fileName.lastIndexOf(".") + 1));
                }
            }
        }
        return documentInfo;
    }

    public String readableFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


}
