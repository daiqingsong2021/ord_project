package com.wisdom.acm.szxm.controller.doc;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.service.doc.DocSectionService;
import com.wisdom.acm.szxm.service.doc.FolderMenuCodeService;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.feign.doc.CommDocService;
import com.wisdom.base.common.form.doc.DocCorpFolderAddForm;
import com.wisdom.base.common.form.doc.DocCorpFolderUpdateForm;
import com.wisdom.base.common.form.doc.DocCorpSearchForm;
import com.wisdom.base.common.form.doc.DocProjectAddForm;
import com.wisdom.base.common.form.doc.DocProjectFolderAddForm;
import com.wisdom.base.common.form.doc.DocProjectFolderUpdateForm;
import com.wisdom.base.common.form.doc.DocProjectSearchForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.DocRelationInfoVo;
import com.wisdom.base.common.vo.SelectVo;
import com.wisdom.base.common.vo.doc.DocCorpFolderTreeVo;
import com.wisdom.base.common.vo.doc.DocProjectFolderTreeVo;
import com.wisdom.base.common.vo.doc.DocProjectReleaseVo;
import com.wisdom.base.common.vo.doc.DocProjectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: szc
 * @Date: 2019/7/17 16:57
 * @Version 1.0
 */
@RestController
@RequestMapping("doc")
public class DocController extends BaseController {

    @Autowired
    private CommDocService commDocService;
    @Autowired
    private DocSectionService docService;
    @Autowired
    private FolderMenuCodeService folderService;


    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    /**
     * 数据字典服务
     */
    @Autowired private CommDictService commDictService;

    /**
     * 上传项目文档
     * @param projectUploadForm
     * @return
     */
    @PostMapping("/project/add")
    public ApiResult addDocProject(@RequestBody DocProjectAddForm projectUploadForm) {
        DocProjectVo projectVo = commDocService.addDocProject(projectUploadForm);
        projectVo = docService.addDocSectionIdRelation(projectVo,projectUploadForm.getSectionId());
        return ApiResult.success(projectVo);
    }

    /**
     * 查询文件信息
     * @param bizId
     * @param bizType
     * @return
     */
    @GetMapping("/reations/{bizId}/{bizType}/list")
    public ApiResult queryDocRelationList(@PathVariable("bizId")Integer bizId, @PathVariable("bizType")String bizType) {
        List<DocRelationInfoVo> docRelationInfoVos = commDocService.queryDocRelationList(bizId, bizType);
        if(ObjectUtils.isEmpty(docRelationInfoVos)){
            return ApiResult.success(docRelationInfoVos);
        }
        return ApiResult.success(docService.addDocScore(bizType, docRelationInfoVos));
    }


    /**
     * 获取项目文档列表
     * @param projectId
     * @param pageSize
     * @param pageNum
     * @return
     */
    @PostMapping("/project/{projectId}/{folderId}/{bizType}/{flag}/{pageSize}/{pageNum}/{sectionIds}/list")
    public ApiResult queryDocProjectList(@PathVariable("projectId")Integer projectId,@PathVariable("folderId") Integer folderId,
                                         @PathVariable("bizType") String bizType, @PathVariable("flag") String flag,
                                         @PathVariable("pageSize")Integer pageSize, @PathVariable("pageNum")Integer pageNum, DocProjectSearchForm docProjectSearchForm,
                                         @PathVariable("sectionIds") String sectionIds){
        List<DocProjectVo> projectVoPageInfoList = new ArrayList<>();
        if("project".equals(bizType)){
            //projectVoPageInfoList = commDocService.queryDocProjectList(projectId,bizType,flag,projectId.toString(),pageSize,pageNum,docProjectSearchForm);
            projectVoPageInfoList = commDocService.queryDocProjectList2(projectId,bizType,flag,projectId.toString(),docProjectSearchForm);
        }else if("folder".equals(bizType)){
            //projectVoPageInfoList = commDocService.queryDocProjectList(folderId,bizType,flag,projectId.toString(),pageSize,pageNum,docProjectSearchForm);
            projectVoPageInfoList = commDocService.queryDocProjectList2(folderId,bizType,flag,projectId.toString(),docProjectSearchForm);
        }

        PageInfo<DocProjectVo> projectVoPageInfo2  = docService.filterDocBySectionIds(projectId,pageSize,pageNum,sectionIds,projectVoPageInfoList);
        return new TableResultResponse(projectVoPageInfo2);
    }

    /**
     * 发布项目文档审批页面
     * @return
     */
    @GetMapping("/project/{projectId}/{folderId}/{sectionIds}/release/list")
    public ApiResult queryDocProjectReleaseList(@PathVariable("projectId")Integer projectId,@PathVariable("folderId") Integer folderId, @PathVariable("sectionIds") String sectionIds){
        List<DocProjectReleaseVo> docProjectReleaseVos = commDocService.queryDocProjectReleaseList(projectId,folderId);
        List<DocProjectReleaseVo> docReleaseVos = docService.filterDocReleaseBySectionIds(projectId,sectionIds,docProjectReleaseVos);
        return ApiResult.success(docReleaseVos);


    }
    /**
     * 分发项目文档页面
     * @return
     */
    @PostMapping("/project/{folderId}/{bizType}/{flag}/{projectId}/{sectionIds}/giving/list")
    public ApiResult queryDocProjectGivingList(@PathVariable("folderId")Integer folderId,@PathVariable("bizType") String bizType,@PathVariable("flag") String flag,
                                                @PathVariable("projectId") String projectId ,DocProjectSearchForm docProjectSearchForm,@PathVariable("sectionIds") String sectionIds){
        List<DocProjectVo> docProjectVos_ = commDocService.getDocProjectGivingList(folderId,bizType,flag, projectId,docProjectSearchForm);
        List<DocProjectVo> docProjectVos = docService.filterDocProjectVosBySectionIds(Integer.valueOf(projectId),sectionIds,docProjectVos_);
        return ApiResult.success(docProjectVos);

    }

    /**
     * 项目文档文件夹列表
     * @param projectId
     * @return
     */
    @PostMapping("/project/folder/{projectId}/tree")
    public ApiResult queryDocProjectFolder(@PathVariable("projectId") Integer projectId, DocProjectSearchForm docProjectSearchForm){
        List<DocProjectFolderTreeVo> docProjectFolderTreeVoList = commDocService.queryDocProjectFolder(projectId,docProjectSearchForm);
        docProjectFolderTreeVoList = folderService.queryMenuCodeByFolderList(docProjectFolderTreeVoList);
        return ApiResult.success(docProjectFolderTreeVoList);
    }

    /**
     * 企业文档文件夹列表
     * @param docCorpSearchForm
     * @return
     */
    @PostMapping("/corp/folder/tree")
    public ApiResult queryDocCorpFolder(DocCorpSearchForm docCorpSearchForm){
        List<DocCorpFolderTreeVo> docCorpFolderTreeVoList = commDocService.queryDocCorpFolder(docCorpSearchForm);
        docCorpFolderTreeVoList = folderService.queryMenuCodeByCorpFolderList(docCorpFolderTreeVoList,null);
        return ApiResult.success(docCorpFolderTreeVoList);
    }

    /**
     * 根据菜单id获取企业文件夹列表
     * @param menuId
     * @param docCorpSearchForm
     * @return
     */
    @PostMapping("/corp/folder/{menuId}/tree")
    public ApiResult queryDocCorpFolderByMenuId(@PathVariable("menuId") Integer menuId,DocCorpSearchForm docCorpSearchForm){
        List<DocCorpFolderTreeVo> docCorpFolderTreeVoList = commDocService.queryDocCorpFolder(docCorpSearchForm);
        docCorpFolderTreeVoList = folderService.queryMenuCodeByCorpFolderList(docCorpFolderTreeVoList,menuId);
        return ApiResult.success(docCorpFolderTreeVoList);
    }


    /**
     * 项目文档文件夹列表
     * @param projectId
     * @return
     */
    @PostMapping("/project/folder/{projectId}/{menuCode}/getFolderByMenuCode")
    public ApiResult getFolderByMenuName(@PathVariable("projectId") Integer projectId,@PathVariable("menuCode") String menuCode, DocProjectSearchForm docProjectSearchForm){
       //根据menuCode查询数据字典 List
        Map<String, DictionaryVo> dictMap = commDictService.getDictMapByTypeCode(menuCode);
        if(!ObjectUtils.isEmpty(dictMap))
        {
            List<String> folderNames= Lists.newArrayList();
            for (Map.Entry<String, DictionaryVo> entry : dictMap.entrySet())
            {
                DictionaryVo value = entry.getValue();
                folderNames.add(value.getName());
            }
            List<DocProjectFolderTreeVo> docProjectFolderTreeVoList = commDocService.queryDocProjectFolder(projectId,docProjectSearchForm);
            //对docProjectFolderTreeVoList过滤，只显示folderNames的文件夹包括其子节点，顶层再加项目节点
            List<DocProjectFolderTreeVo> returnList=folderService.filterFolderByFolderNames(folderNames,docProjectFolderTreeVoList);
            return ApiResult.success(returnList);
        }
        else
        {
            return ApiResult.success(Lists.newArrayList());
        }


    }

    /**
     * 增加项目文件夹
     * @param docProjectFolderAddForm
     * @return
     */
    @PostMapping("/project/folder/add")
    public ApiResult addDocProjectFolder(@RequestBody DocProjectFolderAddForm docProjectFolderAddForm){
        DocProjectFolderTreeVo docProjectFolderTreeVo =  commDocService.addDocProjectFolder(docProjectFolderAddForm);
        docProjectFolderTreeVo = folderService.addFolderMenuCodeRelation(docProjectFolderTreeVo,docProjectFolderAddForm.getMenuId());
        return ApiResult.success(docProjectFolderTreeVo);
    }


    /**
     * 增加企业文件夹
     * @param corpFolderAddForm
     * @return
     */
    @PostMapping("/corp/folder/add")
    public ApiResult addDocCorpFolder(@RequestBody DocCorpFolderAddForm corpFolderAddForm){
        DocCorpFolderTreeVo docCorpFolderTreeVo = commDocService.addDocCorpFolder(corpFolderAddForm);
        docCorpFolderTreeVo = folderService.addCorpFolderMenuIdRelation(docCorpFolderTreeVo,corpFolderAddForm.getMenuId());
        return ApiResult.success(docCorpFolderTreeVo);
    }



    /**
     * 修改项目文档文件夹
     * @param projectFolderUpdateForm
     * @return
     */
    @PutMapping("/project/folder/update")
    public ApiResult updateDocProjectFolder(@RequestBody DocProjectFolderUpdateForm projectFolderUpdateForm){
        DocProjectFolderTreeVo docProjectFolderTreeVo = commDocService.updateDocProjectFolder(projectFolderUpdateForm);
        docProjectFolderTreeVo = folderService.updateFolderMenuCodeRelation(projectFolderUpdateForm,docProjectFolderTreeVo);
        return ApiResult.success(docProjectFolderTreeVo);
    }


    /**
     * 修改企业文档文件夹
     * @param corpFolderUpdateForm
     * @return
     */
    @PutMapping("/corp/folder/update")
    public ApiResult updateCorpDocProjectFolder(@RequestBody DocCorpFolderUpdateForm corpFolderUpdateForm){
        DocProjectFolderTreeVo  docProjectFolderTreeVo = commDocService.updateDocCorpFolder(corpFolderUpdateForm);
        docProjectFolderTreeVo = folderService.updateCorpFolderMenuIdRealtion(corpFolderUpdateForm,docProjectFolderTreeVo);
        return ApiResult.success(docProjectFolderTreeVo);
    }


    /**
     * 文件夹列表下拉框(页签)
     * @param projectId
     * @return
     */
    @GetMapping("/project/folder/{projectId}/{menuId}/select/tree")
    public ApiResult queryDocProjectSelectFolder(@PathVariable("projectId") Integer projectId,@PathVariable("menuId") Integer menuId){
        List<SelectVo> selectVoList = commDocService.queryDocProjectSelectFolder(projectId);
        List<SelectVo> selectVoList1 = folderService.filterFolderByMenuCode(projectId,selectVoList,menuId);
        return ApiResult.success(selectVoList1);
    }

    /**
     * 获取文件夹信息
     * @param folderId
     * @return
     */
    @GetMapping("/project/folder/{folderId}/info")
    public ApiResult getDocProjectFolderInfo(@PathVariable("folderId") Integer folderId){
        DocProjectFolderTreeVo docProjectFolderTreeVo = commDocService.getDocProjectFolderInfo(folderId);
        docProjectFolderTreeVo = folderService.queryFolderMenuCode(docProjectFolderTreeVo);
        return ApiResult.success(docProjectFolderTreeVo);
    }

    /**
     * 获取企业文件夹信息
     * @param folderId
     * @return
     */
    @GetMapping("/corp/folder/{folderId}/info")
    public ApiResult getDocCorpFolderInfo(@PathVariable("folderId") Integer folderId){
        DocCorpFolderTreeVo docCorpFolderTreeVo = commDocService.getDocCorpFolderInfo(folderId);
        docCorpFolderTreeVo = folderService.queryCorpFolderMenuId(docCorpFolderTreeVo);
        return ApiResult.success(docCorpFolderTreeVo);
    }

    /**
     * 删除文档文件夹
     * @param folderId
     * @return
     */
    @DeleteMapping("/folder/{folderId}/delete")
    public ApiResult deleteDocProjectFolder(@PathVariable("folderId") Integer folderId){
      List<Integer> folderIds = commDocService.deleteDocProjectFolder(folderId);
      folderService.deleteFolderMenuIdRelation(folderIds);
      return ApiResult.success();
    }

    public static void main(String args[])
    {
        try
        {
            String keyWord = URLDecoder.decode("技术准备","UTF-8");
            System.out.println(keyWord);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }
}

