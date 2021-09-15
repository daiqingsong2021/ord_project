package com.wisdom.base.common.feign.doc;

/**
 * @Author: szc
 * @Date: 2019/7/17 16:49
 * @Version 1.0
 */

import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.form.doc.*;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.DocRelationInfoVo;
import com.wisdom.base.common.vo.SelectVo;
import com.wisdom.base.common.vo.doc.DocCorpFolderTreeVo;
import com.wisdom.base.common.vo.doc.DocProjectFolderTreeVo;
import com.wisdom.base.common.vo.doc.DocProjectReleaseVo;
import com.wisdom.base.common.vo.doc.DocProjectVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: szc
 * @Date: 2019/5/23 9:44
 * @Version 1.0
 */
@FeignClient(value = "acm-module-doc",configuration = FeignConfiguration.class)
public interface CommDocService {


    default DocProjectVo addDocProject(DocProjectAddForm projectUploadForm){
       if(ObjectUtils.isEmpty(projectUploadForm.getProjectId())){
           throw new BaseException("项目不能为空");
       }
       if(ObjectUtils.isEmpty(projectUploadForm.getDocTitle())){
           throw new BaseException("文档标题不能为空");
       }
       if(ObjectUtils.isEmpty(projectUploadForm.getFileId())){
           throw new BaseException("文件不能为空");
       }
       if(ObjectUtils.isEmpty(projectUploadForm.getSecutyLevel())){
           throw new BaseException("密级不能为空");
       }

       ApiResult<DocProjectVo> apiResult = this.addDocProject_(projectUploadForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    /**
     * 上传项目文档
     * @param projectUploadForm
     * @return
     */
    @PostMapping("/project/add")
    @AddLog(title = "上传项目文档" , module = LoggerModuleEnum.DM_PROJECT,initContent = true)
    ApiResult<DocProjectVo> addDocProject_(@RequestBody DocProjectAddForm projectUploadForm);

    /**
     * 获取项目文档列表
     * @param bizType
     * @return
     */
    default List<DocProjectVo> queryDocProjectList2(Integer folderId, String bizType, String flag, String projectIds, DocProjectSearchForm docProjectSearchForm){
        ApiResult<List<DocProjectVo>> apiResult = this.queryDocProjectList_2(folderId, bizType,flag,projectIds,docProjectSearchForm.getName(),docProjectSearchForm.getDocSearchType(),docProjectSearchForm.getUserId());
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    /**
     * 获取项目文档列表
     * @param bizType
     * @return
     */
    default List<DocProjectVo> queryDocProjectList(Integer folderId, String bizType, String flag, String projectIds,Integer pageSize, Integer pageNum, DocProjectSearchForm docProjectSearchForm){
        ApiResult<List<DocProjectVo>> apiResult = this.queryDocProjectList_(folderId, bizType,flag,projectIds,pageSize,pageNum,docProjectSearchForm.getName(),docProjectSearchForm.getDocSearchType(),docProjectSearchForm.getUserId());
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    /**
     * 发布项目文档审批页面
     * @param projectId
     * @param folderId
     * @return
     */
    default List<DocProjectReleaseVo> queryDocProjectReleaseList(Integer projectId,Integer folderId){
        ApiResult<List<DocProjectReleaseVo>> apiResult = this.queryDocProjectReleaseList_(projectId,folderId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    /**
     * 发布项目文档审批页面
     * @param folderId
     * @param bizType
     * @param projectIds
     * @param docProjectSearchForm
     * @return
     */
    default List<DocProjectVo> getDocProjectGivingList(Integer folderId,String bizType,String flag,String projectIds ,DocProjectSearchForm docProjectSearchForm){
        ApiResult<List<DocProjectVo>> apiResult = this.getDocProjectGivingList_(folderId,bizType,flag,projectIds,docProjectSearchForm.getName(),docProjectSearchForm.getDocSearchType(),docProjectSearchForm.getUserId());
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    /**
     * 获取文件列表
     * @param bizId
     * @param bizType
     * @return
     */
    default List<DocRelationInfoVo> queryDocRelationList(Integer bizId, String bizType){
        ApiResult<List<DocRelationInfoVo>> apiResult = this.queryDocRelationList_(bizId, bizType);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    /**
     * 获取文件列表
     * @param bizId
     * @param bizType
     * @return
     */
    @GetMapping("/reations/{bizId}/{bizType}/list")
    ApiResult<List<DocRelationInfoVo>> queryDocRelationList_(@RequestParam("bizId") Integer bizId, @RequestParam("bizType") String bizType);

    /**
     * 获取项目文档列表
     * @param projectId
     * @param folderId
     * @return
     */
    @GetMapping("/project/{projectId}/{folderId}/release/list")
    ApiResult<List<DocProjectReleaseVo>> queryDocProjectReleaseList_(@RequestParam("projectId") Integer projectId,@RequestParam("folderId") Integer folderId);

    /**
     * 获取项目分发文档列表
     * @param folderId
     * @param bizType
     * @param flag
     * @param projectIds
     * @return
     */
    @PostMapping("/project/{folderId}/{bizType}/{flag}/{projectIds}/giving/list")
    ApiResult<List<DocProjectVo>> getDocProjectGivingList_(@RequestParam("folderId") Integer folderId,@RequestParam("bizType") String bizType,@RequestParam("flag") String flag
            ,@RequestParam("projectIds") String projectIds ,@RequestParam("name") String name,@RequestParam("docSearchType") String docSearchType,@RequestParam("userId") Integer userId);

    /**
     * 获取项目文档列表
     * @param folderId
     * @return
     */
    @PostMapping("/project/{folderId}/{bizType}/{flag}/{projectIds}/list")
    ApiResult<List<DocProjectVo>> queryDocProjectList_2(@RequestParam("folderId") Integer folderId, @RequestParam("bizType") String bizType,
                                                       @RequestParam("flag") String flag, @RequestParam("projectIds") String projectIds,
                                                       @RequestParam("name") String name,@RequestParam("docSearchType") String docSearchType,@RequestParam("userId") Integer userId );

    /**
     * 获取项目文档列表
     * @param folderId
     * @param pageSize
     * @param pageNum
     * @return
     */
    @PostMapping("/project/{folderId}/{bizType}/{flag}/{projectIds}/{pageSize}/{pageNum}/list")
    ApiResult<List<DocProjectVo>> queryDocProjectList_(@RequestParam("folderId") Integer folderId, @RequestParam("bizType") String bizType,
                                                       @RequestParam("flag") String flag, @RequestParam("projectIds") String projectIds, @RequestParam("pageSize") Integer pageSize,
                                                       @RequestParam("pageNum") Integer pageNum,@RequestParam("name") String name,@RequestParam("docSearchType") String docSearchType,@RequestParam("userId") Integer userId );
    default List<SelectVo> queryDocProjectSelectFolder(Integer projectId){
        ApiResult<List<SelectVo>> apiResult = this.queryDocProjectSelectFolder_(projectId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    /**
     * 项目文档文件夹列表下拉框
     * @param projectId
     * @return
     */
    @GetMapping("/project/folder/{projectId}/select/tree")
    ApiResult<List<SelectVo>> queryDocProjectSelectFolder_(@RequestParam("projectId") Integer projectId);



    default DocProjectFolderTreeVo addDocProjectFolder(DocProjectFolderAddForm folderAddForm){
        if(ObjectUtils.isEmpty(folderAddForm.getProjectId())){
            throw new BaseException("项目不能为空");
        }
        if(ObjectUtils.isEmpty(folderAddForm.getName())){
            throw new BaseException("文件夹名称不能为空");
        }
/*        if(ObjectUtils.isEmpty(folderAddForm.getMenuId())){
            throw new BaseException("模块不能为空");
        }*/
        ApiResult<DocProjectFolderTreeVo> apiResult = this.addDocProjectFolder_(folderAddForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }


    /**
     * 添加项目文档文件夹
     * @param folderAddForm
     * @return
     */
    @AddLog(title = "新建文件夹",module = LoggerModuleEnum.DM_PROJECT ,initContent = true)
    @PostMapping("/project/folder/add")
    ApiResult<DocProjectFolderTreeVo> addDocProjectFolder_(@RequestBody DocProjectFolderAddForm folderAddForm);



    default DocProjectFolderTreeVo updateDocProjectFolder(DocProjectFolderUpdateForm projectFolderUpdateForm){
        if(ObjectUtils.isEmpty(projectFolderUpdateForm.getName())){
            throw new BaseException("文件夹名称不能为空");
        }
//        if(ObjectUtils.isEmpty(projectFolderUpdateForm.getMenuId())){
//            throw new BaseException("业务模块不能为空");
//        }
        ApiResult<DocProjectFolderTreeVo> apiResult = this.updateDocProjectFolder_(projectFolderUpdateForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    /**
     * 修改文件状态
     * @param projectFolderUpdateForm
     * @return
     */
    @PutMapping("/project/folder/update")
    ApiResult<DocProjectFolderTreeVo> updateDocProjectFolder_(@RequestBody DocProjectFolderUpdateForm projectFolderUpdateForm);


    default List<DocProjectFolderTreeVo> queryDocProjectFolder(Integer projectId, DocProjectSearchForm docProjectSearchForm){
        ApiResult<List<DocProjectFolderTreeVo>> apiResult = this.queryDocProjectFolder_(projectId,docProjectSearchForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }


    /**
     * 项目文档文件夹列表
     * @param projectId
     * @return
     */
    @PostMapping("/project/folder/{projectId}/tree")
    ApiResult<List<DocProjectFolderTreeVo>> queryDocProjectFolder_(@RequestParam("projectId") Integer projectId, @RequestBody DocProjectSearchForm docProjectSearchForm);



     default DocProjectFolderTreeVo getDocProjectFolderInfo(Integer folderId){
         ApiResult<DocProjectFolderTreeVo> apiResult = this.getDocProjectFolderInfo_(folderId);
         if(apiResult.getStatus() == 200){
             return apiResult.getData();
         }else{
             throw new BaseException(apiResult.getMessage());
         }
     }

    /**
     * 获取文件夹信息
     * @param folderId
     * @return
     */
    @GetMapping("/project/folder/{folderId}/info")
    ApiResult<DocProjectFolderTreeVo> getDocProjectFolderInfo_(@RequestParam("folderId") Integer folderId);

   default List<Integer> deleteDocProjectFolder(Integer folderId){
     ApiResult<List<Integer>> apiResult = this.deleteDocProjectFolder_(folderId);
       if(apiResult.getStatus() == 200){
           return apiResult.getData();
       }else{
           throw new BaseException(apiResult.getMessage());
       }
   }

    /**
     * 删除项目文档文件夹
     * @param folderId
     * @return
     */
    @DeleteMapping("/project/folder/{folderId}/delete")
    @AddLog(title = "删除文件夹" , module = LoggerModuleEnum.DM_PROJECT)
    ApiResult<List<Integer>> deleteDocProjectFolder_(@PathVariable("folderId")Integer folderId);

    default List<DocCorpFolderTreeVo> queryDocCorpFolder(DocCorpSearchForm docCorpSearchForm){
        ApiResult<List<DocCorpFolderTreeVo>> apiResult = this.queryDocCorpFolder_(docCorpSearchForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    /**
     * 企业文档文件夹列表
     * @return
     */
    @PostMapping("/corp/folder/tree")
    ApiResult<List<DocCorpFolderTreeVo>> queryDocCorpFolder_(@RequestBody DocCorpSearchForm docCorpSearchForm);


    default DocCorpFolderTreeVo addDocCorpFolder(DocCorpFolderAddForm corpFolderAddForm){
        if(ObjectUtils.isEmpty(corpFolderAddForm.getName())){
            throw new BaseException("文件夹名称不能为空");
        }
        ApiResult<DocCorpFolderTreeVo> apiResult = this.addDocCorpFolder_(corpFolderAddForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    /**
     * 添加企业文档文件夹
     * @param corpFolderAddForm
     * @return
     */
    @AddLog(title = "新建文件夹",module = LoggerModuleEnum.DM_CORP ,initContent = true)
    @PostMapping("/corp/folder/add")
    ApiResult<DocCorpFolderTreeVo> addDocCorpFolder_(@RequestBody DocCorpFolderAddForm corpFolderAddForm);


    default DocCorpFolderTreeVo getDocCorpFolderInfo(Integer folderId){
        ApiResult<DocCorpFolderTreeVo> apiResult = this.getDocCorpFolderInfo_(folderId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    /**
     * 获取企业文件夹信息
     * @param folderId
     * @return
     */
    @GetMapping("/corp/folder/{folderId}/info")
    ApiResult<DocCorpFolderTreeVo> getDocCorpFolderInfo_(@PathVariable("folderId")Integer folderId);



    default DocProjectFolderTreeVo updateDocCorpFolder(DocCorpFolderUpdateForm corpFolderUpdateForm){
        if(ObjectUtils.isEmpty(corpFolderUpdateForm.getName())){
            throw new BaseException("文件夹名称不能为空");
        }
        ApiResult<DocProjectFolderTreeVo> apiResult = this.updateDocCorpFolder_(corpFolderUpdateForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }


    /**
     * 修改企业文档文件夹
     * @param corpFolderUpdateForm
     * @return
     */
    @PutMapping("/corp/folder/update")
    ApiResult<DocProjectFolderTreeVo> updateDocCorpFolder_(@RequestBody DocCorpFolderUpdateForm corpFolderUpdateForm);


    default List<Integer> deleteDocCorpFolder(Integer folderId){

        ApiResult<List<Integer>> apiResult = this.deleteDocCorpFolder_(folderId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }


    /**
     * 删除企业文档文件夹
     * @param folderId
     * @return
     */
    @DeleteMapping("/corp/folder/{folderId}/delete")
    @AddLog(title = "删除文件夹" , module = LoggerModuleEnum.DM_PROJECT)
    ApiResult<List<Integer>> deleteDocCorpFolder_(@PathVariable("folderId")Integer folderId);


    /**
     * 发布相关业务时，文档状态变为已发布
     * @param bizIds
     * @param bizType
     */
    @PostMapping("/release/relation/{bizType}/{bizIds}")
    ApiResult releaseDocByBizTypeAndBizIds(@RequestParam("bizType") String bizType,@RequestParam("bizIds") String bizIds);

    /**
     * 取消发布相关业务时，文档状态变为编制中
     * @param bizType
     * @param bizIds
     * @return
     */
    @PostMapping("cancel/release/relation/{bizType}/{bizIds}")
    ApiResult cancelRelDocByBizTypeAndBizIds(@RequestParam("bizType")String bizType,@RequestParam("bizIds") String bizIds);


    /**
     * 根据目标id和目标类型删除项目文档
     * @param bizType
     * @param bizIds
     * @return
     */
    @PostMapping("delete/relation/{bizType}/{bizIds}")
    public ApiResult deleteDocByBizTypeAndBizIds(@RequestParam("bizType")String bizType,@RequestParam("bizIds") String bizIds);

    /**
     * 根据id删除文件(假删除)
     * @param fileIds
     * @return
     */
    @DeleteMapping("/corp/delete")
    public ApiResult deleteByFileIds(@RequestBody List<Integer> fileIds);

    /**
     * 根据id删除文件(真删除)
     * @param fileIds
     * @return
     */
    @DeleteMapping("/recyclebin/delete")
    public ApiResult deleteFileByFileIds(@RequestBody List<Map<String,Object>> fileIds);
}
