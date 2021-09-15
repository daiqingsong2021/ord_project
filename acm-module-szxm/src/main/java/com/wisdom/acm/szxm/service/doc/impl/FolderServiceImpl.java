package com.wisdom.acm.szxm.service.doc.impl;


import com.google.common.collect.Lists;
import com.wisdom.acm.szxm.mapper.doc.FolderMenuCodeMapper;
import com.wisdom.acm.szxm.po.doc.DocFolderMenuIdPo;
import com.wisdom.acm.szxm.service.doc.FolderMenuCodeService;
import com.wisdom.acm.szxm.vo.doc.MenuFolderVo;
import com.wisdom.base.common.feign.menu.CommMenuService;
import com.wisdom.base.common.form.doc.DocCorpFolderUpdateForm;
import com.wisdom.base.common.form.doc.DocProjectFolderUpdateForm;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.SelectVo;
import com.wisdom.base.common.vo.doc.DocCorpFolderTreeVo;
import com.wisdom.base.common.vo.doc.DocProjectFolderTreeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: szc
 * @Date: 2019/7/18 13:50
 * @Version 1.0
 */
@Service
public class FolderServiceImpl extends BaseService<FolderMenuCodeMapper, DocFolderMenuIdPo> implements FolderMenuCodeService {

    @Autowired
    private CommMenuService commMenuService;

    @Override
    public List<SelectVo> filterFolderByMenuCode(Integer projectId,List<SelectVo> selectVoList, Integer menuId) {

        List<SelectVo> resultList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(menuId) && 0 != menuId) {
            //找这个菜单对应的目录（可能存在多个，多个并排显示），然后显示目录的子节点
            List<Integer> menuIds =Lists.newArrayList();
            menuIds.add(menuId);
            List<MenuFolderVo> menuFolderVoList = mapper.selectProjectFolderByMenuIds(projectId,menuIds);
            if (!ObjectUtils.isEmpty(menuFolderVoList)) {
                List<Integer> folderIds = ListUtil.toValueList(menuFolderVoList, "folderId", Integer.class);
                resultList = this.filterFolderByFolderIds(selectVoList, folderIds);
            }
        }
        return resultList;
    }

    /**
     * 递归过滤文件夹
     *
     * @param selectVoList
     * @param folderIds
     * @return
     */
    private List<SelectVo> filterFolderByFolderIds(List<SelectVo> selectVoList, List<Integer> folderIds) {
        List<SelectVo> resultList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(selectVoList)) {
            for (SelectVo selectVo : selectVoList) {
                if (folderIds.contains(selectVo.getId())) {
                    resultList.add(selectVo);
                }
                else
                {
                    List<SelectVo> selectVoChildList = selectVo.getChildren();
                    if (!ObjectUtils.isEmpty(selectVoChildList)) {
                        List<SelectVo> childList = this.filterFolderByFolderIds(selectVoChildList, folderIds);
                        resultList.addAll(childList);
                    }
                }
            }
        }
        return resultList;
    }


    /**
     * 增加文件夹和菜单id关联关系
     *
     * @param docProjectFolderTreeVo
     * @param menuId
     */
    @Override
    public DocProjectFolderTreeVo addFolderMenuCodeRelation(DocProjectFolderTreeVo docProjectFolderTreeVo, Integer menuId) {

        Integer folderId = docProjectFolderTreeVo.getId();

        this.insertDocFolderMenuIdPo(menuId, folderId);

        if (!ObjectUtils.isEmpty(menuId)) {
            SelectVo selectVo = commMenuService.queryMenuById(menuId);
            docProjectFolderTreeVo.setMenu(selectVo);
        }

        return docProjectFolderTreeVo;
    }

    /**
     * 增加企业文件夹和菜单id的关联关系
     *
     * @param docCorpFolderTreeVo
     * @param menuId
     * @return
     */
    @Override
    public DocCorpFolderTreeVo addCorpFolderMenuIdRelation(DocCorpFolderTreeVo docCorpFolderTreeVo, Integer menuId) {

        Integer folderId = docCorpFolderTreeVo.getId();

        this.insertDocFolderMenuIdPo(menuId, folderId);

        SelectVo selectVo = commMenuService.queryMenuById(menuId);
        docCorpFolderTreeVo.setMenu(selectVo);
        return docCorpFolderTreeVo;
    }

    /**
     * 插入对象
     *
     * @param menuId
     * @param folderId
     */
    private void insertDocFolderMenuIdPo(Integer menuId, Integer folderId) {
        DocFolderMenuIdPo docFolderMenuCodePo = new DocFolderMenuIdPo();

        docFolderMenuCodePo.setFolderId(folderId);
        docFolderMenuCodePo.setMenuId(menuId);
        this.insert(docFolderMenuCodePo);
    }

    @Override
    public DocProjectFolderTreeVo updateFolderMenuCodeRelation(DocProjectFolderUpdateForm projectFolderUpdateForm, DocProjectFolderTreeVo docProjectFolderTreeVo) {

        DocFolderMenuIdPo docFolderMenuCodePo = this.seclectfolderMenuIdPoByfolderId(docProjectFolderTreeVo.getId());

        if (docFolderMenuCodePo != null) {
            docFolderMenuCodePo.setMenuId(projectFolderUpdateForm.getMenuId());

            this.updateById(docFolderMenuCodePo);
        } else {
            this.insertDocFolderMenuIdPo(projectFolderUpdateForm.getMenuId(), docProjectFolderTreeVo.getId());
        }

        if (!ObjectUtils.isEmpty(projectFolderUpdateForm.getMenuId())) {
            SelectVo selectVo = commMenuService.queryMenuById(projectFolderUpdateForm.getMenuId());
            docProjectFolderTreeVo.setMenu(selectVo);
        }

        return docProjectFolderTreeVo;
    }

    @Override
    public DocProjectFolderTreeVo updateCorpFolderMenuIdRealtion(DocCorpFolderUpdateForm corpFolderUpdateForm, DocProjectFolderTreeVo docProjectFolderTreeVo) {

        DocFolderMenuIdPo docFolderMenuCodePo = this.seclectfolderMenuIdPoByfolderId(docProjectFolderTreeVo.getId());
        if (docFolderMenuCodePo != null) {
            docFolderMenuCodePo.setMenuId(corpFolderUpdateForm.getMenuId());

            this.updateById(docFolderMenuCodePo);
        } else {
            this.insertDocFolderMenuIdPo(corpFolderUpdateForm.getMenuId(), docProjectFolderTreeVo.getId());
        }
        SelectVo selectVo = commMenuService.queryMenuById(corpFolderUpdateForm.getMenuId());
        docProjectFolderTreeVo.setMenu(selectVo);

        return docProjectFolderTreeVo;
    }

    /**
     * 根据文件夹id获取文件夹和菜单id关联的对象呢
     *
     * @param folderId
     * @return
     */
    private DocFolderMenuIdPo seclectfolderMenuIdPoByfolderId(Integer folderId) {
        if (!ObjectUtils.isEmpty(folderId)) {
            Example example = new Example(DocFolderMenuIdPo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("folderId", folderId);

            return this.selectOneByExample(example);
        }
        return null;
    }


    /**
     * 将菜单id拼接进文件夹树
     *
     * @param docProjectFolderTreeVoList
     * @return
     */
    @Override
    public List<DocProjectFolderTreeVo> queryMenuCodeByFolderList(List<DocProjectFolderTreeVo> docProjectFolderTreeVoList) {

        if (!ObjectUtils.isEmpty(docProjectFolderTreeVoList)) {

            //递归获取所有文件夹id
            List<Integer> allFolderIds = this.queryAllFolderIds(docProjectFolderTreeVoList);

            if (!ObjectUtils.isEmpty(allFolderIds)) {

                //根据文件夹id集合获取文件夹id == 编码id 关连对象集合
                List<DocFolderMenuIdPo> docFolderMenuCodePoList = this.queryDocFolderMenuIdByFolderIds(allFolderIds);

                if (!ObjectUtils.isEmpty(docFolderMenuCodePoList)) {
                    //转化为 folderId=DocFolderMenuIdPo 集合
                    Map<Integer, DocFolderMenuIdPo> folderMenuCodePoMap = ListUtil.listToMap(docFolderMenuCodePoList, "folderId", Integer.class);
                    //获取所有菜单id集合
                    List<Integer> menuIds = ListUtil.toValueList(docFolderMenuCodePoList, "menuId", Integer.class);
                    //获取菜单vo集合
                    List<SelectVo> selectVoList = commMenuService.queryMenusByIds(menuIds);
                    //转化为 value = selectVo 集合
                    Map<Integer, SelectVo> selectVoMap = ListUtil.listToMap(selectVoList, "value", Integer.class);
                    //递归拼接菜单
                    docProjectFolderTreeVoList = this.joinMenuIntoFolderList(docProjectFolderTreeVoList, folderMenuCodePoMap, selectVoMap);
                }
            }
        }
        return docProjectFolderTreeVoList;
    }

    /**
     * 此方法 是有菜单ID 则筛选与此菜单ID对应的文件夹，否则全部
     *
     * @param docCorpFolderTreeVoList
     * @return
     */
    @Override
    public List<DocCorpFolderTreeVo> queryMenuCodeByCorpFolderList(List<DocCorpFolderTreeVo> docCorpFolderTreeVoList,Integer menuId) {
        if (!ObjectUtils.isEmpty(docCorpFolderTreeVoList)) {
            //递归获取所有企业文件夹id
            List<Integer> allFolderIds = this.queryAllCorpFolderIds(docCorpFolderTreeVoList);
            if (!ObjectUtils.isEmpty(allFolderIds)) {
                //根据文件夹id集合获取文件夹id == 编码id 关连对象集合
                List<DocFolderMenuIdPo> docFolderMenuCodePoList = this.queryDocFolderMenuIdByFolderIds(allFolderIds);
                if (!ObjectUtils.isEmpty(docFolderMenuCodePoList)) {
                    //转化为 folderId=DocFolderMenuIdPo 集合
                    Map<Integer, DocFolderMenuIdPo> folderMenuCodePoMap = ListUtil.listToMap(docFolderMenuCodePoList, "folderId", Integer.class);
                    List<Integer> menuIds = new ArrayList<>();
                    if(ObjectUtils.isEmpty(menuId)){
                        //获取所有菜单id集合
                        menuIds = ListUtil.toValueList(docFolderMenuCodePoList, "menuId", Integer.class);
                    }else{
                        menuIds.add(menuId);
                    }
                    //获取菜单vo集合
                    List<SelectVo> selectVoList = commMenuService.queryMenusByIds(menuIds);
                    //转化为 value = selectVo 集合
                    Map<String, SelectVo> selectVoMap = ListUtil.listToMap(selectVoList, "value", String.class);
                    //递归拼接菜单
                    docCorpFolderTreeVoList = this.joinMenuInfoCorpFolderList(docCorpFolderTreeVoList, folderMenuCodePoMap, selectVoMap);
                }
            }
        }
        return docCorpFolderTreeVoList;
    }

    private List<DocFolderMenuIdPo> queryDocFolderMenuIdByFolderIds(List<Integer> allFolderIds) {
        if (!ObjectUtils.isEmpty(allFolderIds)) {
            Example example = new Example(DocFolderMenuIdPo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("folderId", allFolderIds);
            //根据文件夹id集合获取文件夹id == 编码id 关连对象集合
            return this.selectByExample(example);
        }
        return null;
    }


    /**
     * 递归拼接菜单
     *
     * @param docProjectFolderTreeVoList
     * @param folderMenuCodePoMap
     * @param selectVoMap
     * @return
     */
    private List<DocProjectFolderTreeVo> joinMenuIntoFolderList(List<DocProjectFolderTreeVo> docProjectFolderTreeVoList, Map<Integer, DocFolderMenuIdPo> folderMenuCodePoMap, Map<Integer, SelectVo> selectVoMap) {
        for (DocProjectFolderTreeVo docProjectFolderTreeVo : docProjectFolderTreeVoList) {

            String type = docProjectFolderTreeVo.getType();
            if ("folder".equals(type)) {
                Integer folderId = docProjectFolderTreeVo.getId();
                //获取关联对象
                DocFolderMenuIdPo docFolderMenuCodePo = folderMenuCodePoMap.get(folderId);
                if (docFolderMenuCodePo != null) {
                    if (!ObjectUtils.isEmpty(docFolderMenuCodePo.getMenuId())) {
                        SelectVo selectVo = selectVoMap.get(docFolderMenuCodePo.getMenuId().toString());
                        //拼接菜单
                        docProjectFolderTreeVo.setMenu(selectVo);
                    }
                }
            }
            if (!ObjectUtils.isEmpty(docProjectFolderTreeVo.getChildren())) {
                List<DocProjectFolderTreeVo> docProjectFolderTreeVoChildList = docProjectFolderTreeVo.getChildren();
                docProjectFolderTreeVoChildList = this.joinMenuIntoFolderList(docProjectFolderTreeVoChildList, folderMenuCodePoMap, selectVoMap);
                docProjectFolderTreeVo.setChildren(docProjectFolderTreeVoChildList);
            }
        }
        return docProjectFolderTreeVoList;
    }

    /**
     * 递归拼接企业文件夹
     *
     * @param docCorpFolderTreeVos
     * @param folderMenuCodePoMap
     * @param selectVoMap
     * @return
     */
    private List<DocCorpFolderTreeVo> joinMenuInfoCorpFolderList(List<DocCorpFolderTreeVo> docCorpFolderTreeVos, Map<Integer, DocFolderMenuIdPo> folderMenuCodePoMap, Map<String, SelectVo> selectVoMap) {
        List<DocCorpFolderTreeVo> newDocCorpFolderList=Lists.newArrayList();
        for (DocCorpFolderTreeVo docCorpFolderTreeVo : docCorpFolderTreeVos) {
            Integer folderId = docCorpFolderTreeVo.getId();
            //获取关联对象
            DocFolderMenuIdPo docFolderMenuCodePo = folderMenuCodePoMap.get(folderId);
            if(!ObjectUtils.isEmpty(selectVoMap.get(String.valueOf(docFolderMenuCodePo.getMenuId()))))
            {
                SelectVo selectVo = selectVoMap.get(String.valueOf(docFolderMenuCodePo.getMenuId()));
                docCorpFolderTreeVo.setMenu(selectVo);
                if (!ObjectUtils.isEmpty(docCorpFolderTreeVo.getChildren())) {
                    List<DocCorpFolderTreeVo> docCorpFolderChildTreeVo = docCorpFolderTreeVo.getChildren();
                    docCorpFolderChildTreeVo = this.joinMenuInfoCorpFolderList(docCorpFolderChildTreeVo, folderMenuCodePoMap, selectVoMap);
                    docCorpFolderTreeVo.setChildren(docCorpFolderChildTreeVo);
                }
                newDocCorpFolderList.add(docCorpFolderTreeVo);
            }
        }
        return newDocCorpFolderList;
    }


    /**
     * 递归获取所有文件夹id
     *
     * @param docProjectFolderTreeVoList
     * @return
     */
    private List<Integer> queryAllFolderIds(List<DocProjectFolderTreeVo> docProjectFolderTreeVoList) {
        List<Integer> folderIds = new ArrayList<>();
        if (!ObjectUtils.isEmpty(docProjectFolderTreeVoList)) {
            for (DocProjectFolderTreeVo docProjectFolderTreeVo : docProjectFolderTreeVoList) {

                if ("folder".equals(docProjectFolderTreeVo.getType())) {
                    folderIds.add(docProjectFolderTreeVo.getId());
                }
                if (!ObjectUtils.isEmpty(docProjectFolderTreeVo.getChildren())) {
                    List<Integer> childFolderIds = this.queryAllFolderIds(docProjectFolderTreeVo.getChildren());
                    folderIds.addAll(childFolderIds);
                }
            }
        }
        return folderIds;
    }

    /**
     * 递归获取所有企业文件夹id
     *
     * @param docCorpFolderTreeVoList
     * @return
     */
    private List<Integer> queryAllCorpFolderIds(List<DocCorpFolderTreeVo> docCorpFolderTreeVoList) {
        List<Integer> folderIds = new ArrayList<>();

        if (!ObjectUtils.isEmpty(docCorpFolderTreeVoList)) {
            for (DocCorpFolderTreeVo docCorpFolderTreeVo : docCorpFolderTreeVoList) {
                folderIds.add(docCorpFolderTreeVo.getId());
                if (!ObjectUtils.isEmpty(docCorpFolderTreeVo.getChildren())) {
                    List<Integer> childFolderIds = this.queryAllCorpFolderIds(docCorpFolderTreeVo.getChildren());
                    folderIds.addAll(childFolderIds);
                }
            }
        }
        return folderIds;
    }

    @Override
    public DocProjectFolderTreeVo queryFolderMenuCode(DocProjectFolderTreeVo docProjectFolderTreeVo) {

        if (docProjectFolderTreeVo != null) {
            //文件夹id
            Integer id = docProjectFolderTreeVo.getId();

            DocFolderMenuIdPo docFolderMenuCodePo = this.selectOnePoByFolderId(id);

            if (docFolderMenuCodePo != null) {

                Integer menuId = docFolderMenuCodePo.getMenuId();
                SelectVo selectVo = commMenuService.queryMenuById(menuId);

                docProjectFolderTreeVo.setMenu(selectVo);
            }
        }
        return docProjectFolderTreeVo;
    }

    @Override
    public DocCorpFolderTreeVo queryCorpFolderMenuId(DocCorpFolderTreeVo docCorpFolderTreeVo) {

        if (docCorpFolderTreeVo != null) {
            Integer id = docCorpFolderTreeVo.getId();

            DocFolderMenuIdPo docFolderMenuCodePo = this.selectOnePoByFolderId(id);
            if (docFolderMenuCodePo != null){
                Integer menuId = docFolderMenuCodePo.getMenuId();
                SelectVo selectVo = commMenuService.queryMenuById(menuId);
                docCorpFolderTreeVo.setMenu(selectVo);
            }
        }
        return docCorpFolderTreeVo;
    }

    /**
     * 根据文件夹id获取文件夹和菜单id的关联对象
     * @param folderId
     * @return
     */
    private DocFolderMenuIdPo selectOnePoByFolderId(Integer folderId){
        if(!ObjectUtils.isEmpty(folderId)){
            Example example = new Example(DocFolderMenuIdPo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("folderId", folderId);
            return this.selectOneByExample(example);
        }
        return null;
    }

    @Override
    public List<DocProjectFolderTreeVo> filterFolderByFolderNames(List<String> folderNames, List<DocProjectFolderTreeVo> docProjectFolderTreeVoList) {
        if (!ObjectUtils.isEmpty(docProjectFolderTreeVoList)) {
            //递归获取所有文件夹id
            List<Integer> allFolderIds = this.queryAllFolderIds(docProjectFolderTreeVoList);
            if(ObjectUtils.isEmpty(allFolderIds))
            {
                //如果文件夹ID为空，则返回docProjectFolderTreeVoList,因为此时只有一个项目节点
                docProjectFolderTreeVoList.get(0).setDocNum(0);//顶层一定是项目
                return docProjectFolderTreeVoList;
            }

            List<DocProjectFolderTreeVo> returnList=Lists.newArrayList();
            DocProjectFolderTreeVo newRootProject = dozerMapper.map(docProjectFolderTreeVoList.get(0), DocProjectFolderTreeVo.class);

            List<DocProjectFolderTreeVo> newChildren=Lists.newArrayList();
            for(String folderName:folderNames)
            {
                newChildren.addAll(this.queryFolderByName(docProjectFolderTreeVoList, folderName));
            }
            if(!ObjectUtils.isEmpty(newChildren))
            {
                newRootProject.setDocNum(newChildren.get(0).getDocNum());
                newRootProject.setChildren(newChildren);
            }
            else
            {
                newRootProject.setDocNum(0);
                newRootProject.setChildren(null);
            }

            returnList.add(newRootProject);
            return returnList;
        }
        return docProjectFolderTreeVoList;
    }

    @Override
    public void deleteFolderMenuIdRelation(List<Integer> folderIds) {
        List<DocFolderMenuIdPo> docFolderMenuIdPoList = this.queryDocFolderMenuIdByFolderIds(folderIds);
        if(!ObjectUtils.isEmpty(docFolderMenuIdPoList)){
            for (DocFolderMenuIdPo docFolderMenuIdPo : docFolderMenuIdPoList) {
                this.delete(docFolderMenuIdPo);
            }
        }
    }

    private List<DocProjectFolderTreeVo> queryFolderByName(List<DocProjectFolderTreeVo> docProjectFolderTreeVoList,String folderName) {
        List<DocProjectFolderTreeVo> returnList = Lists.newArrayList();
        for (DocProjectFolderTreeVo docProjectFolderTreeVo : docProjectFolderTreeVoList) {
            String type = docProjectFolderTreeVo.getType();
            if ("folder".equals(type)) {
                    if (docProjectFolderTreeVo.getName().equals(folderName)) {
                        //如果找到，将他的子节点一起包含进去，塞入returnList
                        returnList.add(docProjectFolderTreeVo);
                        return returnList;
                    }
            }
            if (!ObjectUtils.isEmpty(docProjectFolderTreeVo.getChildren())) {
                List<DocProjectFolderTreeVo> docProjectFolderTreeVoChildList =
                        docProjectFolderTreeVo.getChildren();
                List<DocProjectFolderTreeVo> docProjectFolderTreeVos = this.queryFolderByName(docProjectFolderTreeVoChildList,folderName);
                if (!ObjectUtils.isEmpty(docProjectFolderTreeVos))
                    return docProjectFolderTreeVos;
            }

        }
        return Lists.newArrayList();
    }
}
