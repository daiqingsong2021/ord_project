package com.wisdom.acm.base.service.impl;

import com.wisdom.acm.base.form.tmpldelv.BaseTmpldelvAddForm;
import com.wisdom.acm.base.form.tmpldelv.BaseTmpldelvUpdateForm;
import com.wisdom.acm.base.mapper.BaseTmpldelvMapper;
import com.wisdom.acm.base.mapper.BaseTmpldelvTypeMapper;
import com.wisdom.acm.base.po.BaseTmplTaskPo;
import com.wisdom.acm.base.po.BaseTmpldelvPo;
import com.wisdom.acm.base.po.BaseTmpldelvTypePo;
import com.wisdom.acm.base.service.BaseTmplDelvService;
import com.wisdom.acm.base.service.BaseTmplDelvTypeService;
import com.wisdom.acm.base.vo.tmpldelv.BaseTmpldelvTreeVo;
import com.wisdom.acm.base.vo.tmpldelv.BaseTmpldelvTypeVo;
import com.wisdom.acm.base.vo.tmpldelv.BaseTmpldelvVo;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplTaskVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BaseTmplDelvServiceImpl extends BaseService<BaseTmpldelvMapper, BaseTmpldelvPo> implements BaseTmplDelvService {

    @Autowired
    BaseTmpldelvTypeMapper baseTmpldelvTypeMapper;

    @Autowired
    BaseTmplDelvTypeService baseTmplDelvTypeService;

    @Override
    public List<BaseTmpldelvTreeVo> querryTmpldelvList() {
        List<BaseTmpldelvTreeVo> treeList = new ArrayList<BaseTmpldelvTreeVo>();
        //查询交付物模板列表
        List<BaseTmpldelvTypeVo> allTmpldelvTypeList = baseTmpldelvTypeMapper.selectTmpldelvTypeList(null);
        //查询PBS交付物列表
        List<BaseTmpldelvVo> allTmpldelvList = this.mapper.selectTmpldelvList();
        Map<Integer, List<BaseTmpldelvVo>> tmpldelvMap = this.listTmpldelvToMap(allTmpldelvList);
        List<BaseTmpldelvTreeVo> subList = this.getChildrenTmpldelv(0,tmpldelvMap);
        if(!ObjectUtils.isEmpty(allTmpldelvTypeList)){
            BaseTmpldelvTreeVo baseTmpldelvTreeVo = null;
            for(BaseTmpldelvTypeVo baseTmpldelvTypeVo:allTmpldelvTypeList){
                baseTmpldelvTreeVo = new BaseTmpldelvTreeVo();
                baseTmpldelvTreeVo.setId(baseTmpldelvTypeVo.getId());
                baseTmpldelvTreeVo.setDelvTitle(baseTmpldelvTypeVo.getTypeTitle());
                baseTmpldelvTreeVo.setDelvNum(baseTmpldelvTypeVo.getTypeNum());
                baseTmpldelvTreeVo.setDelvType(baseTmpldelvTypeVo.getTypeType());
                baseTmpldelvTreeVo.setDelvDesc(baseTmpldelvTypeVo.getTypeDesc());
                baseTmpldelvTreeVo.setDelvVersion(baseTmpldelvTypeVo.getTypeVersion());
                baseTmpldelvTreeVo.setCreator(baseTmpldelvTypeVo.getCreator());
                baseTmpldelvTreeVo.setCreatTime(baseTmpldelvTypeVo.getCreatTime());
                baseTmpldelvTreeVo.setType("tmpl");
                List<BaseTmpldelvTreeVo> subListSub = new ArrayList<BaseTmpldelvTreeVo>();
                if(!ObjectUtils.isEmpty(subList)){
                    for(BaseTmpldelvTreeVo tmpldelvTree:subList){
                        Integer delvTypeId = tmpldelvTree.getTypeId();
                        if(delvTypeId.equals(baseTmpldelvTypeVo.getId())){
                            subListSub.add(tmpldelvTree);
                        }
                    }
                }
                if(!ObjectUtils.isEmpty(subListSub)&&subListSub.size()>0){
                    baseTmpldelvTreeVo.setChildren(subListSub);
                }
                treeList.add(baseTmpldelvTreeVo);
            }
        }
        return treeList;
    }

    @Override
    public List<BaseTmpldelvTreeVo> querryTmpldelvListByTmplId(Integer tmpldelvId) {
        List<BaseTmpldelvTreeVo> treeList = new ArrayList<BaseTmpldelvTreeVo>();
        //查询交付物模板列表
        List<BaseTmpldelvTypeVo> allTmpldelvTypeList = new ArrayList<BaseTmpldelvTypeVo>();
        BaseTmpldelvTypeVo vaseTmpldelvTypeVoNew = baseTmpldelvTypeMapper.selectTmpldelvTypeById(tmpldelvId);
        allTmpldelvTypeList.add(vaseTmpldelvTypeVoNew);
        //查询PBS交付物列表
        List<BaseTmpldelvVo> allTmpldelvList = this.mapper.selectTmpldelvList();
        Map<Integer, List<BaseTmpldelvVo>> tmpldelvMap = this.listTmpldelvToMap(allTmpldelvList);
        List<BaseTmpldelvTreeVo> subList = this.getChildrenTmpldelv(0,tmpldelvMap);
        if(!ObjectUtils.isEmpty(allTmpldelvTypeList)){
            BaseTmpldelvTreeVo baseTmpldelvTreeVo = null;
            for(BaseTmpldelvTypeVo baseTmpldelvTypeVo:allTmpldelvTypeList){
                baseTmpldelvTreeVo = new BaseTmpldelvTreeVo();
                baseTmpldelvTreeVo.setId(baseTmpldelvTypeVo.getId());
                baseTmpldelvTreeVo.setDelvTitle(baseTmpldelvTypeVo.getTypeTitle());
                baseTmpldelvTreeVo.setDelvNum(baseTmpldelvTypeVo.getTypeNum());
                baseTmpldelvTreeVo.setDelvType(baseTmpldelvTypeVo.getTypeType());
                baseTmpldelvTreeVo.setDelvDesc(baseTmpldelvTypeVo.getTypeDesc());
                baseTmpldelvTreeVo.setDelvVersion(baseTmpldelvTypeVo.getTypeVersion());
                baseTmpldelvTreeVo.setCreator(baseTmpldelvTypeVo.getCreator());
                baseTmpldelvTreeVo.setCreatTime(baseTmpldelvTypeVo.getCreatTime());
                baseTmpldelvTreeVo.setType("tmpl");
                List<BaseTmpldelvTreeVo> subListSub = new ArrayList<BaseTmpldelvTreeVo>();
                if(!ObjectUtils.isEmpty(subList)){
                    for(BaseTmpldelvTreeVo tmpldelvTree:subList){
                        Integer delvTypeId = tmpldelvTree.getTypeId();
                        if(delvTypeId.equals(baseTmpldelvTypeVo.getId())){
                            subListSub.add(tmpldelvTree);
                        }
                    }
                }
                if(!ObjectUtils.isEmpty(subListSub)&&subListSub.size()>0){
                    baseTmpldelvTreeVo.setChildren(subListSub);
                }
                treeList.add(baseTmpldelvTreeVo);
            }
        }
        return treeList;
    }

    @Override
    public List<BaseTmpldelvTreeVo> querryTmpldelvListSubByTmplId(Integer tmpldelvId) {
        List<BaseTmpldelvTreeVo> treeList = new ArrayList<BaseTmpldelvTreeVo>();
        //查询交付物模板列表
        List<BaseTmpldelvTypeVo> allTmpldelvTypeList = new ArrayList<BaseTmpldelvTypeVo>();
        BaseTmpldelvTypeVo vaseTmpldelvTypeVoNew = baseTmpldelvTypeMapper.selectTmpldelvTypeById(tmpldelvId);
        allTmpldelvTypeList.add(vaseTmpldelvTypeVoNew);
        //查询PBS交付物列表
        List<BaseTmpldelvVo> allTmpldelvList = this.mapper.selectTmpldelvList();
        Map<Integer, List<BaseTmpldelvVo>> tmpldelvMap = this.listTmpldelvToMap(allTmpldelvList);
        List<BaseTmpldelvTreeVo> subList = this.getChildrenTmpldelv(0,tmpldelvMap);
        List<BaseTmpldelvTreeVo> subListSub = null;
        if(!ObjectUtils.isEmpty(allTmpldelvTypeList)){
            BaseTmpldelvTreeVo baseTmpldelvTreeVo = null;
            for(BaseTmpldelvTypeVo baseTmpldelvTypeVo:allTmpldelvTypeList){
                subListSub = new ArrayList<BaseTmpldelvTreeVo>();
                if(!ObjectUtils.isEmpty(subList)){
                    for(BaseTmpldelvTreeVo tmpldelvTree:subList){
                        Integer delvTypeId = tmpldelvTree.getTypeId();
                        if(delvTypeId.equals(baseTmpldelvTypeVo.getId())){
                            subListSub.add(tmpldelvTree);
                        }
                    }
                }
            }
        }
        return subListSub;
    }

    @Override
    public List<BaseTmpldelvTreeVo> querryPageBaseTmplListAssignTree(Integer tmpldelvId,Integer taskId) {
        List<BaseTmpldelvTreeVo> treeList = new ArrayList<BaseTmpldelvTreeVo>();
        //查询交付物模板列表
        List<BaseTmpldelvTypeVo> allTmpldelvTypeList = new ArrayList<BaseTmpldelvTypeVo>();
        BaseTmpldelvTypeVo vaseTmpldelvTypeVoNew = baseTmpldelvTypeMapper.selectTmpldelvTypeById(tmpldelvId);
        allTmpldelvTypeList.add(vaseTmpldelvTypeVoNew);
        //查询PBS交付物列表
        List<BaseTmpldelvVo> allTmpldelvList = this.mapper.selectTmpldelvAssignList(taskId);
        Map<Integer, List<BaseTmpldelvVo>> tmpldelvMap = this.listTmpldelvToMap(allTmpldelvList);
        List<BaseTmpldelvTreeVo> subList = this.getChildrenTmpldelv(0,tmpldelvMap);
        List<BaseTmpldelvTreeVo> subListSub = null;
        if(!ObjectUtils.isEmpty(allTmpldelvTypeList)){
            BaseTmpldelvTreeVo baseTmpldelvTreeVo = null;
            for(BaseTmpldelvTypeVo baseTmpldelvTypeVo:allTmpldelvTypeList){
                subListSub = new ArrayList<BaseTmpldelvTreeVo>();
                if(!ObjectUtils.isEmpty(subList)){
                    for(BaseTmpldelvTreeVo tmpldelvTree:subList){
                        Integer delvTypeId = tmpldelvTree.getTypeId();
                        if(delvTypeId.equals(baseTmpldelvTypeVo.getId())){
                            subListSub.add(tmpldelvTree);
                        }
                    }
                }
            }
        }
        return subListSub;
    }

    @Override
    public BaseTmpldelvVo getTmpldelvById(Integer tmpldelvId) {
        BaseTmpldelvVo baseTmpldelvVo = this.mapper.selectTmpldelvById(tmpldelvId);
        return  baseTmpldelvVo;
    }

    @Override
    public BaseTmpldelvPo addPbsTmpldelv(BaseTmpldelvAddForm baseTmpldelvAddForm) {
        baseTmpldelvAddForm.setType("pbs");
        List<BaseTmpldelvPo> list = queryBaseTmpldelvPoByDelvNumAndTypeId(baseTmpldelvAddForm.getDelvNum(),baseTmpldelvAddForm.getTypeId());
        if (!ObjectUtils.isEmpty(list)){
            throw new BaseException("编号不能重复!");
        }
        BaseTmpldelvPo baseTmpldelvPo = this.dozerMapper.map(baseTmpldelvAddForm,BaseTmpldelvPo.class);
        super.insert(baseTmpldelvPo);
        return baseTmpldelvPo;
    }

    @Override
    public BaseTmpldelvPo addSubTmpldelv(BaseTmpldelvAddForm baseTmpldelvAddForm) {
        baseTmpldelvAddForm.setType("delv");
        List<BaseTmpldelvPo> list = queryBaseTmpldelvPoByDelvNumAndTypeId(baseTmpldelvAddForm.getDelvNum(),baseTmpldelvAddForm.getTypeId());
        if (!ObjectUtils.isEmpty(list)){
            throw new BaseException("编号不能重复!");
        }
        BaseTmpldelvPo baseTmpldelvPo = this.dozerMapper.map(baseTmpldelvAddForm,BaseTmpldelvPo.class);
        super.insert(baseTmpldelvPo);
        return baseTmpldelvPo;
    }

    @Override
    @AddLog(title = "修改PBS/交付物", module = LoggerModuleEnum.BM_TMPL_DELV)
    public BaseTmpldelvPo updateTmpldelv(BaseTmpldelvUpdateForm baseTmpldelvUpdateForm) {
        BaseTmpldelvPo baseTmpldelvPo = this.selectById(baseTmpldelvUpdateForm.getId());
        if(ObjectUtils.isEmpty(baseTmpldelvPo)){
            throw new BaseException("修改的任务不存在!");
        }

        // 添加修改日志
        this.addUpdateLog(baseTmpldelvUpdateForm,baseTmpldelvPo);

        List<BaseTmpldelvPo> list = queryBaseTmpldelvPoByDelvNumAndTypeId(baseTmpldelvUpdateForm.getDelvNum(),baseTmpldelvPo.getTypeId());
        if (!ObjectUtils.isEmpty(list) && !list.get(0).getId().equals(baseTmpldelvUpdateForm.getId())){
            throw new BaseException("编号不能重复!");
        }
        this.dozerMapper.map(baseTmpldelvUpdateForm,baseTmpldelvPo);
        this.updateById(baseTmpldelvPo);
        return baseTmpldelvPo;
    }

    public void addUpdateLog(BaseTmpldelvUpdateForm baseTmpldelvUpdateForm,BaseTmpldelvPo baseTmpldelvPo){

        if ("pbs".equals(baseTmpldelvPo.getType())) {
            this.addChangeLogger("修改PBS",baseTmpldelvUpdateForm,baseTmpldelvPo);
        }else {
            this.addChangeLogger("修改交付物",baseTmpldelvUpdateForm,baseTmpldelvPo);
        }
    }

    @Override
    public void deleteTmpldelv(List<Integer> ids) {
        //查询PBS交付物列表
        List<BaseTmpldelvVo> allTmpldelvList = this.mapper.selectTmpldelvList();
        Map<Integer, List<BaseTmpldelvVo>> tmpldelvMap = this.listTmpldelvToMap(allTmpldelvList);
        if(!ObjectUtils.isEmpty(ids)){
            for(Integer id:ids){
                //删除选中数据
                this.deleteById(id);
                //删除子节点
                this.deleteChildrenNode(id,tmpldelvMap);
            }
        }
        super.deleteByIds(ids);
    }

    @Override
    public void deleteByDelvTypePo(BaseTmpldelvTypePo baseTmpldelvTypePo) {
        if(!ObjectUtils.isEmpty(baseTmpldelvTypePo)){
            List<BaseTmpldelvVo> tmplDelvList = this.querryDelvByTypeId(baseTmpldelvTypePo.getId());
            for(BaseTmpldelvVo baseTmpldelvVo:tmplDelvList){
                this.mapper.deleteByPrimaryKey(baseTmpldelvVo.getId());
            }
        }
    }

    @Override
    public List<BaseTmpldelvVo> querryDelvByTypeId(Integer delvTypeId){
        List<BaseTmpldelvVo> list = this.mapper.selectDelvByTypeId(delvTypeId);
        return list;
    }

    /**
     * BaseTmpldelvVoList转Map
     * @param treeNodes
     * @return
     */
    private Map<Integer, List<BaseTmpldelvVo>> listTmpldelvToMap(List<BaseTmpldelvVo> treeNodes) {
        Map<Integer, List<BaseTmpldelvVo>> childrenMap = new HashMap<Integer, List<BaseTmpldelvVo>>();
        if (!ObjectUtils.isEmpty(treeNodes)) {
            for (BaseTmpldelvVo t : treeNodes) {
                if (childrenMap.get(t.getParentId()) == null) {
                    List<BaseTmpldelvVo> l = new ArrayList<BaseTmpldelvVo>();
                    l.add(t);
                    childrenMap.put(t.getParentId(), l);
                } else {
                    childrenMap.get(t.getParentId()).add(t);
                }
            }
        }
        return childrenMap;
    }

    private List<BaseTmpldelvTreeVo> getChildrenTmpldelv(Integer id,Map<Integer, List<BaseTmpldelvVo>> tmpldelvMap){
        List<BaseTmpldelvVo> subList = tmpldelvMap.get(id);
        List<BaseTmpldelvTreeVo> treeList = new ArrayList<BaseTmpldelvTreeVo>();
        if(!ObjectUtils.isEmpty(subList)){
            BaseTmpldelvTreeVo tmpldelv = null;
            for(BaseTmpldelvVo tmpldelvVo:subList){
                tmpldelv = new BaseTmpldelvTreeVo();
                tmpldelv.setId(tmpldelvVo.getId());
                //tmpldelv.setParentId(tmpldelvVo.getParentId()==0?tmpldelvVo.getTypeId():tmpldelvVo.getParentId());
                tmpldelv.setParentId(tmpldelvVo.getParentId());
                tmpldelv.setTypeId(tmpldelvVo.getTypeId());
                tmpldelv.setDelvTitle(tmpldelvVo.getDelvTitle());
                tmpldelv.setDelvNum(tmpldelvVo.getDelvNum());
                tmpldelv.setDelvType(tmpldelvVo.getDelvType());
                tmpldelv.setDelvDesc(tmpldelvVo.getDelvDesc());
                tmpldelv.setDelvVersion(tmpldelvVo.getDelvVersion());
                tmpldelv.setCreator(tmpldelvVo.getCreator());
                tmpldelv.setCreatTime(tmpldelvVo.getCreatTime());
                tmpldelv.setType(tmpldelvVo.getType());
                tmpldelv.setParentId(tmpldelvVo.getParentId());
                //查询子节点
                List<BaseTmpldelvTreeVo> children = this.getChildrenTmpldelv(tmpldelvVo.getId(),tmpldelvMap);
                if(!ObjectUtils.isEmpty(children)&&children.size()>0){
                    tmpldelv.setChildren(children);
                }
                treeList.add(tmpldelv);
            }
        }
        return treeList;
    }

    @Override
    public void deleteTmpldelvType(List<Integer> ids) {
        if(!ObjectUtils.isEmpty(ids)){
            for(Integer id:ids){
                //删除交付物模板
                baseTmplDelvTypeService.deleteTmpldelvTypeById(id);
                //删除PBS
                this.deleteByBaseTmpldelvTypeId(id);
            }
        }
    }

    @Override
    public void deleteByBaseTmpldelvTypeId(Integer typeId) {
        //查询PBS交付物列表
        List<BaseTmpldelvVo> allTmpldelvList = this.mapper.selectTmpldelvList();
        Map<Integer, List<BaseTmpldelvVo>> tmpldelvMap = this.listTmpldelvToMap(allTmpldelvList);
        List<BaseTmpldelvVo> baseTmpldelvVoList = this.queryTmpldelvByTypeId(typeId);
        if(!ObjectUtils.isEmpty(baseTmpldelvVoList)){
            for(BaseTmpldelvVo baseTmpldelvVo:baseTmpldelvVoList){
                this.deleteById(baseTmpldelvVo.getId());
                //删除子节点
                this.deleteChildrenNode(baseTmpldelvVo.getId(),tmpldelvMap);
            }
        }
    }

    @Override
    public List<BaseTmpldelvVo> queryTmpldelvByTypeId(Integer typeId){
        List<BaseTmpldelvVo> baseTmplDelvVoList = this.mapper.selectDelvByTypeId(typeId);
        return baseTmplDelvVoList;
    }


    @Override
    public String queryAssignTaskDelv(List<Integer> delvIds){
        String content = "";
        Example example = new Example(BaseTmpldelvPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", delvIds);
        List<BaseTmpldelvPo> list = this.selectByExample(example);
        if (!ObjectUtils.isEmpty(list)){
            content = ListUtil.listToNames(list,"delvTitle");
        }
        return "分配交付清单，交付清单名称如下:" + content;
    }

    /**
     * 根据id集合查询交付物模板
     * @param ids
     * @return
     */
    @Override
    public List<BaseTmpldelvPo> queryTmpldelvPosByIds(List<Integer> ids){
        Example example = new Example(BaseTmpldelvPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id",ids);
        List<BaseTmpldelvPo> list = this.selectByExample(example);
        return !ObjectUtils.isEmpty(list) ? list : null;
    }

    /**
     * 递归删除子节点
     * @param id
     * @param tmpldelvMap
     */
    private void deleteChildrenNode(Integer id,Map<Integer, List<BaseTmpldelvVo>> tmpldelvMap){
        List<BaseTmpldelvVo> subList = tmpldelvMap.get(id);
        if(!ObjectUtils.isEmpty(subList)){
            for(BaseTmpldelvVo baseTmpldelv:subList){
                deleteChildrenNode(baseTmpldelv.getId(),tmpldelvMap);
                this.deleteById(baseTmpldelv.getId());
            }
        }
    }

    public List<BaseTmpldelvPo> queryBaseTmpldelvPoByDelvNumAndTypeId(String delvNum,Integer typeId){
        Example example = new Example(BaseTmpldelvPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("delvNum",delvNum);
        criteria.andEqualTo("typeId",typeId);
        List<BaseTmpldelvPo> list = this.selectByExample(example);
        return !ObjectUtils.isEmpty(list) ? list : null;
    }
}
