package com.wisdom.acm.szxm.service.doc;

import com.wisdom.acm.szxm.po.doc.DocFolderMenuIdPo;
import com.wisdom.base.common.form.doc.DocCorpFolderUpdateForm;
import com.wisdom.base.common.form.doc.DocProjectFolderUpdateForm;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.SelectVo;
import com.wisdom.base.common.vo.doc.DocCorpFolderTreeVo;
import com.wisdom.base.common.vo.doc.DocProjectFolderTreeVo;

import java.util.List;

/**
 * @Author: szc
 * @Date: 2019/7/18 13:49
 * @Version 1.0
 */
public interface FolderMenuCodeService extends CommService<DocFolderMenuIdPo> {

    List<SelectVo> filterFolderByMenuCode(Integer projectId,List<SelectVo> selectVoList, Integer menuId);

    /**
     * 增加文件夹和菜单编码关联关系
     * @param docProjectFolderTreeVo
     * @param menuId
     * @return
     */
    DocProjectFolderTreeVo addFolderMenuCodeRelation(DocProjectFolderTreeVo docProjectFolderTreeVo, Integer menuId);

    /**
     * 增加企业文件夹和菜单id的关联关系
     * @param docCorpFolderTreeVo
     * @param menuId
     * @return
     */
    DocCorpFolderTreeVo addCorpFolderMenuIdRelation(DocCorpFolderTreeVo docCorpFolderTreeVo,Integer menuId);

    /**
     * 更新文件夹和菜单关系
     * @param projectFolderUpdateForm
     * @return
     */
    DocProjectFolderTreeVo updateFolderMenuCodeRelation(DocProjectFolderUpdateForm projectFolderUpdateForm,DocProjectFolderTreeVo docProjectFolderTreeVo);


    /**
     * 更新企业文件夹和菜单关系
     * @param corpFolderUpdateForm
     * @param docProjectFolderTreeVo
     * @return
     */
    DocProjectFolderTreeVo updateCorpFolderMenuIdRealtion(DocCorpFolderUpdateForm corpFolderUpdateForm,DocProjectFolderTreeVo  docProjectFolderTreeVo);

    /**
     * 拼接文件夹的meuncode
     * @param docProjectFolderTreeVoList
     * @return
     */
    List<DocProjectFolderTreeVo> queryMenuCodeByFolderList(List<DocProjectFolderTreeVo> docProjectFolderTreeVoList);

    /**
     * 拼接企业文件夹的menuid
     * @param docCorpFolderTreeVoList
     * @return
     */
    List<DocCorpFolderTreeVo> queryMenuCodeByCorpFolderList(List<DocCorpFolderTreeVo> docCorpFolderTreeVoList,Integer menuId);

    /**
     * 获取文件夹编码
     * @param docProjectFolderTreeVo
     * @return
     */
    DocProjectFolderTreeVo queryFolderMenuCode(DocProjectFolderTreeVo docProjectFolderTreeVo);

    /**
     * 企业文件夹加入菜单id
     * @param docCorpFolderTreeVo
     * @return
     */
    DocCorpFolderTreeVo queryCorpFolderMenuId(DocCorpFolderTreeVo docCorpFolderTreeVo);
    /**
     * 根据文件夹名称过滤掉文件夹。包含子节点
     * @param folderNames
     */
    List<DocProjectFolderTreeVo> filterFolderByFolderNames(List<String> folderNames,List<DocProjectFolderTreeVo> docProjectFolderTreeVoList);

    /**
     * 删除文件夹和菜单id的关联关系对象
     * @param folderIds
     */
    void deleteFolderMenuIdRelation(List<Integer> folderIds);
}
