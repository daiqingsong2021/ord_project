package com.wisdom.acm.base.service.impl;

import com.wisdom.acm.base.form.dict.BaseDictAddForm;
import com.wisdom.acm.base.form.dict.BaseDictUpdateForm;
import com.wisdom.acm.base.mapper.BaseDictMapper;
import com.wisdom.acm.base.po.BaseDictPo;
import com.wisdom.acm.base.service.BaseDictService;
import com.wisdom.acm.base.service.BaseDictTypeService;
import com.wisdom.acm.base.vo.dict.BaseDictTreeVo;
import com.wisdom.acm.base.vo.dict.BaseDictVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.LogUtil;
import com.wisdom.base.common.util.TreeUtil;
import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.DictionarysMap;
import com.wisdom.base.common.vo.SelectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BaseDictServiceImpl extends BaseService<BaseDictMapper, BaseDictPo> implements BaseDictService {

    @Autowired
    private BaseDictTypeService gbTypeService;


    /**
     * 根据业务对象查找数据字典
     * @return
     */
    @Override
    public List<BaseDictVo> queryDictListByBoCode(String boCode) {
        List<BaseDictVo> dictList = this.mapper.selectDictDateListByBoCode(boCode);
        return dictList;
    }


    /**
     * 增加数据字典
     * @param basedigitdirAddForm
     */
    @Override
    public BaseDictPo addDict(BaseDictAddForm basedigitdirAddForm) {
        BaseDictPo baseDictPo = this.getDictByTypeCodeAndDictCode(basedigitdirAddForm.getTypeCode(),basedigitdirAddForm.getDictCode());
        if (!ObjectUtils.isEmpty(baseDictPo)){
            throw new BaseException("字典代码不能重复");
        }
        BaseDictPo dictPo = this.dozerMapper.map(basedigitdirAddForm, BaseDictPo.class);
        //默认不是内置
        dictPo.setBuiltIn(0);

        Example example = new Example(BaseDictPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("typeCode", basedigitdirAddForm.getTypeCode());
        criteria.andEqualTo("parentId", basedigitdirAddForm.getParentId());
        dictPo.setSort(this.selectNextSortByExample(example));
        super.insert(dictPo);
        return dictPo;
    }


    public BaseDictPo getDictByTypeCodeAndDictCode(String typeCode,String dictCode){
        Example example = new Example(BaseDictPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("typeCode",typeCode);
        criteria.andEqualTo("dictCode",dictCode);
        List<BaseDictPo> list = this.mapper.selectByExample(example);
        return ObjectUtils.isEmpty(list)? null : list.get(0);
    }

    /**
     * 修改数字字典
     * @param baseDigitdirUpdateForm
     */
    @Override
    @AddLog(title = "修改数字字典码值" , module = LoggerModuleEnum.BM_DICT)
    public BaseDictPo updateDict(BaseDictUpdateForm baseDigitdirUpdateForm) {
        BaseDictPo baseDictPo1 = mapper.selectByPrimaryKey(baseDigitdirUpdateForm.getId());
        if (ObjectUtils.isEmpty(baseDictPo1)){
            throw new BaseException("该数据不存在");
        }

        // 添加修改日志
        this.addChangeLogger(baseDigitdirUpdateForm,baseDictPo1);
        BaseDictPo baseDictPo = this.getDictByTypeCodeAndDictCode(baseDictPo1.getTypeCode(),baseDigitdirUpdateForm.getDictCode());
        if (!ObjectUtils.isEmpty(baseDictPo) && !baseDigitdirUpdateForm.getId().equals(baseDictPo.getId())){
            throw new BaseException("字典代码不能重复");
        }
        dozerMapper.map(baseDigitdirUpdateForm,baseDictPo1);
        super.updateById(baseDictPo1);
        return baseDictPo1;
    }

    /**
     * 删除数据字典
     * @param ids
     * @return
     */
    @Override
    public void deleteDict(List<Integer> ids) {
        this.deleteChildrenAndMe(ids);
    }

    /**
     * 获取数据字典
     * @param dictId
     * @return
     */
    @Override
    public BaseDictVo getDictInfo(int dictId) {
        BaseDictPo dictPo = this.mapper.selectByPrimaryKey(dictId);
        if(!ObjectUtils.isEmpty(dictPo)){
            return this.dozerMapper.map(dictPo, BaseDictVo.class);
        }else {
            return null;
        }

    }


    @Override
    public void deletedictByDictPo(List<String> typeCodes){
        if (!ObjectUtils.isEmpty(typeCodes)) {
            mapper.deleteDictByTypeCodes(typeCodes);
        }
    }

    /**
     * 根据typeCode对象查找数据字典
     * @return
     */
    @Override
    public List<BaseDictTreeVo> queryDictTreeListByTypeCode(String typeCode) {
        List<BaseDictTreeVo> dictList = this.mapper.selectDictTreeVoByTypeCode(typeCode);
        List<BaseDictTreeVo> treeVo = TreeUtil.bulid(dictList,0);
//        Map<Integer,List<BaseDictPo>> dictMap = new HashMap<Integer,List<BaseDictPo>>();
//        if(!ObjectUtils.isArray(dictList)){
//            for (BaseDictPo t : dictList) {
//                if (dictMap.get(t.getParentId()) == null) {
//                    List<BaseDictPo> l = new ArrayList<BaseDictPo>();
//                    l.add(t);
//                    dictMap.put(t.getParentId(), l);
//                } else {
//                    dictMap.get(t.getParentId()).add(t);
//                }
//            }
//        }
//        treeList = this.getChildrenNode(0,dictMap);
        return treeVo;
    }

    @Override
    public List<SelectVo> queryDictTreeDateListByTypeCode(String typeCode) {

        List<BaseDictPo> dictList = this.queryDictPosByTypeCods(typeCode);
        Map<Integer,List<BaseDictPo>> dictMap = ListUtil.bulidTreeListMap(dictList,"parentId",Integer.class);
        return this.getChildrenTreeNode(0,dictMap);
    }

    @Override
    public Map<String, DictionaryVo> getDictMapByTypeCode(String dictTypeCode) {

        List<BaseDictPo> dictList = this.queryDictPosByTypeCods(dictTypeCode);
        Map<String,DictionaryVo> dictMap = new HashMap<>();
        for(BaseDictPo dict : dictList){
            DictionaryVo dictVo = new DictionaryVo(dict.getDictCode(),dict.getDictName());
            dictMap.put(dict.getDictCode(),dictVo);
        }

        return dictMap;
    }

    @Override
    public DictionarysMap getDictMapByTypeCode(List<String> dictTypeCode) {

        DictionarysMap dictMap = new DictionarysMap();
        List<BaseDictPo> dictList = this.queryDictPosByTypeCods(dictTypeCode.toArray(new String[dictTypeCode.size()]));
        for(BaseDictPo dict : dictList){
            dictMap.putDictionaryVo(dict.getTypeCode(),dict.getDictCode(),dict.getDictName());
        }

        return dictMap;
    }

    @Override
    public List<BaseDictPo> queryDictPosByDictCods(String... dictCodes){

        Example example = new Example(BaseDictPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("dictCode",ListUtil.toArrayList(dictCodes));
        return this.selectByExample(example);
    }

    @Override
    public List<BaseDictPo> queryDictPosByTypeCods(String... typeCodes){

        Example example = new Example(BaseDictPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("typeCode",ListUtil.toArrayList(typeCodes));
        example.orderBy("sort").desc();
        return this.selectByExample(example);
    }

    @Override
    public List<BaseDictTreeVo> updateDictSortNum(Integer id,String upOrDown) {
        if(!ObjectUtils.isEmpty(id)){
            BaseDictPo baseDictPo = this.selectById(id);

            Integer sortNum = baseDictPo.getSort();
            String typeCode = baseDictPo.getTypeCode();

            if("up".equals(upOrDown) && !ObjectUtils.isEmpty(sortNum)){
                //获取上面一个码值对象
                BaseDictPo upDictPo = this.selectDictPoByTypeCodeAndSort(sortNum+1,typeCode);
                if(upDictPo != null){
                    upDictPo.setSort(sortNum);
                    this.updateById(upDictPo);

                    baseDictPo.setSort(sortNum+1);
                    this.updateById(baseDictPo);
                }
            }else if("down".equals(upOrDown) && !ObjectUtils.isEmpty(sortNum)){
                //获取下面一个码值对象
                BaseDictPo downDictPo = this.selectDictPoByTypeCodeAndSort(sortNum-1,typeCode);
                if(downDictPo != null){
                    downDictPo.setSort(sortNum);
                    this.updateById(downDictPo);

                    baseDictPo.setSort(sortNum-1);
                    this.updateById(baseDictPo);
                }
            }
            List<BaseDictTreeVo> baseDictTreeVoList = this.queryDictTreeListByTypeCode(baseDictPo.getTypeCode());
            return baseDictTreeVoList;
        }
        return null;
    }

    /**
     * 根据码值类型和排序号获取对象
     * @return
     */
    private BaseDictPo selectDictPoByTypeCodeAndSort(Integer sortNum,String typeCode){
        if(!ObjectUtils.isEmpty(sortNum) && !ObjectUtils.isEmpty(typeCode)){
            Example example = new Example(BaseDictPo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("typeCode",typeCode);
            criteria.andEqualTo("sort",sortNum);
            return this.selectOneByExample(example);
        }
        return null;
    }

    /**
     * 查询子节点
     * @param parentId
     * @param dictMap
     * @return
     */
    private List<BaseDictTreeVo> getChildrenNode(Integer parentId,Map<Integer,List<BaseDictPo>> dictMap){

        List<BaseDictTreeVo> treeList = new ArrayList<BaseDictTreeVo>();
        List<BaseDictPo> subList = dictMap.get(parentId);
        if(!ObjectUtils.isEmpty(subList)){
            BaseDictTreeVo baseDictTreeVo = null;
            for (BaseDictPo t : subList) {
                baseDictTreeVo = new BaseDictTreeVo();
                baseDictTreeVo.setId(t.getId());
                baseDictTreeVo.setParentId(t.getParentId());
                baseDictTreeVo.setDictCode(t.getDictCode());
                List<BaseDictTreeVo> childrenList = this.getChildrenNode(parentId,dictMap);
                baseDictTreeVo.setChildren(childrenList);
                treeList.add(baseDictTreeVo);
            }
        }
        return treeList;
    }

    /**
     * 查询子节点
     * @param parentId
     * @param dictMap
     * @return
     */
    private List<SelectVo> getChildrenTreeNode(Integer parentId, Map<Integer,List<BaseDictPo>> dictMap){

        List<SelectVo> treeList = new ArrayList<>();
        List<BaseDictPo> subList = dictMap.get(parentId);
        if(!ObjectUtils.isEmpty(subList)){
            SelectVo baseDictTreeDataVo = null;
            for (BaseDictPo t : subList) {

                baseDictTreeDataVo = new SelectVo();
                baseDictTreeDataVo.setValue(t.getDictCode());
                baseDictTreeDataVo.setTitle(t.getDictName());
                List<SelectVo> childrenList = this.getChildrenTreeNode(t.getId(),dictMap);

                if(!ObjectUtils.isEmpty(childrenList)){
                    baseDictTreeDataVo.setChildren(childrenList);
                }

                treeList.add(baseDictTreeDataVo);
            }
        }
        return treeList;
    }

}
