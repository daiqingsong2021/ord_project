package com.wisdom.acm.base.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.base.form.tmpldoc.BaseTmpldocAddForm;
import com.wisdom.acm.base.form.tmpldoc.BaseTmpldocUpdateForm;
import com.wisdom.acm.base.mapper.BaseTmpldocMapper;
import com.wisdom.acm.base.po.BaseTmpldocPo;
import com.wisdom.acm.base.service.BaseTmplDocService;
import com.wisdom.acm.base.vo.tmpldoc.BaseTmpldocVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class BaseTmplDocServiceImpl extends BaseService<BaseTmpldocMapper, BaseTmpldocPo> implements BaseTmplDocService {

    @Override
    public PageInfo<BaseTmpldocVo> querryTmplDocPageList(Integer pageSize, Integer currentPageNum, String key) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<BaseTmpldocVo> list =  mapper.selectTmplDocList(key);
        PageInfo<BaseTmpldocVo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public List<BaseTmpldocVo> querryTmplDocList(String key) {
        List<BaseTmpldocVo> list =  mapper.selectTmplDocList(key);
        return list;
    }

    @Override
    public BaseTmpldocVo getTmplDocById(Integer tmplDocId) {
        BaseTmpldocVo baseTmpldocVo = this.mapper.selectTmplDocById(tmplDocId);
        return  baseTmpldocVo;
    }

    @Override
    public BaseTmpldocPo addTmplDoc(BaseTmpldocAddForm baseTmpldocAddForm) {
        BaseTmpldocPo baseTmpldocPo = this.dozerMapper.map(baseTmpldocAddForm,BaseTmpldocPo.class);
        if(this.isExistDocNum(baseTmpldocAddForm.getDocNum())){
            throw new BaseException("已存在相同编号的文档，请重新输入编号！");
        }
        super.insert(baseTmpldocPo);
        return baseTmpldocPo;
    }

    @Override
    @AddLog(title = "修改文档", module = LoggerModuleEnum.BM_TMPL_DOC)
    public BaseTmpldocPo updateTmplDoc(BaseTmpldocUpdateForm baseTmpldocUpdateForm) {
        BaseTmpldocPo baseTmpldocPo = this.selectById(baseTmpldocUpdateForm.getId());
        if(ObjectUtils.isEmpty(baseTmpldocPo)){
            throw new BaseException("修改的文档不存在!");
        }
        // 添加修改日志
        this.addChangeLogger(baseTmpldocUpdateForm,baseTmpldocPo);
        this.dozerMapper.map(baseTmpldocUpdateForm,baseTmpldocPo);
        this.updateById(baseTmpldocPo);
        return baseTmpldocPo;
    }

    @Override
    public void deleteTmplDoc(List<Integer> ids) {
        super.deleteByIds(ids);
    }

    /**
     * 判断是否存在相同文档编号
     * @return
     */
    private boolean isExistDocNum(String docNum){
        Example example = new Example(BaseTmpldocPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("docNum",docNum);
        List<BaseTmpldocPo> baseTmpldocPos = this.selectByExample(example);
        return !ObjectUtils.isEmpty(baseTmpldocPos);
    }
}
