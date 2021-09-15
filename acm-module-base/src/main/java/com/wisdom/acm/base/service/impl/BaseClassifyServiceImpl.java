package com.wisdom.acm.base.service.impl;

import com.wisdom.acm.base.form.classify.BaseClassifyAddForm;
import com.wisdom.acm.base.form.classify.BaseClassifyUpdateForm;
import com.wisdom.acm.base.mapper.BaseClassifyMapper;
import com.wisdom.acm.base.po.BaseClassifyAssignPo;
import com.wisdom.acm.base.po.BaseClassifyPo;
import com.wisdom.acm.base.service.BaseClassifyAssignService;
import com.wisdom.acm.base.service.BaseClassifyService;
import com.wisdom.acm.base.vo.classify.BaseClassifyAssignVo;
import com.wisdom.acm.base.vo.classify.BaseClassifyTreeVo;
import com.wisdom.acm.base.vo.classify.BaseClassifyVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.LogUtil;
import com.wisdom.base.common.util.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class BaseClassifyServiceImpl extends BaseService<BaseClassifyMapper, BaseClassifyPo> implements BaseClassifyService {

    @Autowired
    BaseClassifyAssignService baseClassifyAssignService;

    @Override
    public List<BaseClassifyTreeVo> queryClassifyListByBoCode(String boCode) {

        List<BaseClassifyPo> classifyList = this.queryClassifyPosByBoCode(boCode);

        List<BaseClassifyTreeVo> classifyTreeList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(classifyList)) {
            classifyList.forEach(c -> {
                classifyTreeList.add(this.dozerMapper.map(c, BaseClassifyTreeVo.class));
            });
        }
        return TreeUtil.bulid(classifyTreeList, 0);
    }

    @Override
    public List<BaseClassifyTreeVo> queryClassifyListByBoCodeId(String boCode, Integer bizId) {

        List<BaseClassifyPo> classifyList = this.queryClassifyPosByBoCodeId(boCode, bizId);

        List<BaseClassifyTreeVo> classifyTreeList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(classifyList)) {
            classifyList.forEach(c -> {
                classifyTreeList.add(this.dozerMapper.map(c, BaseClassifyTreeVo.class));
            });
        }
        return TreeUtil.bulid(classifyTreeList, 0);
    }

    @Override
    public List<BaseClassifyPo> queryClassifyPosByBoCodeId(String boCode, Integer bizId) {
        Example example = new Example(BaseClassifyPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("boCode", boCode);
        List<BaseClassifyPo> listAll = this.selectByExample(example);
        List<BaseClassifyAssignVo> baseClassifyAssignVos = baseClassifyAssignService.queryClassifyAssignListByBoCodeAndBoId(boCode, bizId);
        if (ObjectUtils.isEmpty(listAll) || ObjectUtils.isEmpty(baseClassifyAssignVos)) {
            return listAll;
        } else {
            List<BaseClassifyPo> returnList = new ArrayList<BaseClassifyPo>();
            List<Integer> ids = new ArrayList<Integer>();
            for (BaseClassifyPo baseClassifyPo : listAll) {
                for (BaseClassifyAssignVo baseClassifyAssignVo : baseClassifyAssignVos) {
                    if (baseClassifyPo.getBoCode().equals(baseClassifyAssignVo.getBoCode()) && baseClassifyPo.getClassifyName().equals(baseClassifyAssignVo.getClassify().getClassifyName()) && baseClassifyPo.getClassifyCode().equals(baseClassifyAssignVo.getClassify().getClassifyCode())) {
                        Integer parentId = baseClassifyPo.getParentId();
                        ids.add(parentId);
                    }
                }
            }
            for (BaseClassifyPo baseClassifyPo : listAll) {
                Integer id = baseClassifyPo.getId();
                if (!ObjectUtils.isEmpty(ids) && !ids.contains(id)) {
                    returnList.add(baseClassifyPo);
                }
            }
            return returnList;
        }
    }

    @Override
    public List<BaseClassifyPo> queryClassifyPosByBoCode(String boCode) {
        Example example = new Example(BaseClassifyPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("boCode", boCode);
        return this.selectByExample(example);
    }

    @Override
    public BaseClassifyPo addClassify(BaseClassifyAddForm classifyAddForm) {
        BaseClassifyPo classifyPo = this.dozerMapper.map(classifyAddForm, BaseClassifyPo.class);
        //判断分类码/码值是否重复
        List<BaseClassifyPo> list = this.getBaseClassifyPoByAddForm(classifyAddForm);
        // 验证编码是否重复
        if (!ObjectUtils.isEmpty(list) && classifyAddForm.getClassifyType() == 1) {
            throw new BaseException("分类码不能重复!");
        } else if (!ObjectUtils.isEmpty(list) && classifyAddForm.getClassifyType() == 2) {
            throw new BaseException("码值不能重复!");
        }

        super.insert(classifyPo);
        return classifyPo;
    }

    @Override
    @AddLog(title = "修改分类码", module = LoggerModuleEnum.BM_CLASSIFY)
    public BaseClassifyPo updateClassify(BaseClassifyUpdateForm classifyUpdateForm) {

        BaseClassifyPo classifyPo = this.selectById(classifyUpdateForm.getId());

        if (classifyPo == null) {
            throw new BaseException("对象已不存在!");
        }

        // 添加修改日志
        if (classifyPo.getClassifyType() == 1)
            this.addChangeLogger("修改分类码",classifyUpdateForm,classifyPo);
        if (classifyPo.getClassifyType() == 2)
            this.addChangeLogger("修改码值",classifyUpdateForm,classifyPo);

        //判断分类码/码值是否重复
        List<BaseClassifyPo> list = this.getBaseClassifyPoByUpdateForm(classifyUpdateForm);
        if (!ObjectUtils.isEmpty(list) && !list.get(0).getId().equals(classifyUpdateForm.getId()) && classifyPo.getClassifyType() == 1) {
            throw new BaseException("分类码不能重复!");
        } else if (!ObjectUtils.isEmpty(list) && !list.get(0).getId().equals(classifyUpdateForm.getId()) && classifyPo.getClassifyType() == 2) {
            throw new BaseException("码值不能重复!");
        }

        this.dozerMapper.map(classifyUpdateForm, classifyPo);
        super.updateById(classifyPo);
        return classifyPo;
    }

    /**
     * 验证分类码/码值是否重复
     *
     * @return
     */
    public List<BaseClassifyPo> getBaseClassifyPoByAddForm(BaseClassifyAddForm addForm) {
        Example example = new Example(BaseClassifyPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("boCode", addForm.getBoCode());
        criteria.andEqualTo("classifyCode", addForm.getClassifyCode());
        criteria.andEqualTo("classifyType", addForm.getClassifyType());
        List<BaseClassifyPo> list = this.mapper.selectByExample(example);
        return ObjectUtils.isEmpty(list) ? null : list;
    }

    /**
     * 验证分类码/码值是否重复
     *
     * @return
     */
    public List<BaseClassifyPo> getBaseClassifyPoByUpdateForm(BaseClassifyUpdateForm updateForm) {
        //要修改的分类码或码值
        BaseClassifyPo baseClassifyPo = this.selectById(updateForm.getId());
        Example example = new Example(BaseClassifyPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("boCode", baseClassifyPo.getBoCode());
        criteria.andEqualTo("classifyCode", updateForm.getClassifyCode());
        criteria.andEqualTo("classifyType", baseClassifyPo.getClassifyType());
        criteria.andEqualTo("parentId", baseClassifyPo.getParentId());
        List<BaseClassifyPo> list = this.mapper.selectByExample(example);
        return ObjectUtils.isEmpty(list) ? null : list;
    }


    @Override
    public void deleteClassify(List<Integer> ids) {
        List<BaseClassifyAssignPo> baseClassifyAssignPos = baseClassifyAssignService.queryClassifyAssignByClassifyId(ids);
        if(!ObjectUtils.isEmpty(baseClassifyAssignPos)){
            throw new BaseException("分类码被使用，禁止删除!");
        }
        super.deleteChildrenAndMe(ids);
    }

    @Override
    public BaseClassifyVo getClassifyInfo(int classifyId) {
        BaseClassifyPo classifyPo = this.mapper.selectByPrimaryKey(classifyId);
        if (classifyPo == null) {
            return null;
        }
        return this.dozerMapper.map(classifyPo, BaseClassifyVo.class);
    }

    @Override
    public List<BaseClassifyTreeVo> queryClassifyValueList(Integer classifyId) {
        List<BaseClassifyTreeVo> list = mapper.selectClassifyValueList(classifyId);
        List<BaseClassifyTreeVo> tree = TreeUtil.bulid(list, classifyId);
        return tree;
    }
}
