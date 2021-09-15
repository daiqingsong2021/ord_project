package com.wisdom.acm.base.service.impl;

import com.wisdom.acm.base.form.tmpltask.BaseTmplPlanAddForm;
import com.wisdom.acm.base.form.tmpltask.BaseTmplPlanUpdateForm;
import com.wisdom.acm.base.mapper.BaseTmplPlanMapper;
import com.wisdom.acm.base.mapper.BaseTmplTaskMapper;
import com.wisdom.acm.base.po.BaseTmplPlanPo;
import com.wisdom.acm.base.service.BaseTmplPlanService;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplPlanVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.LogUtil;
import com.wisdom.base.common.vo.SelectVo;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class BaseTmplPlanServiceImpl extends BaseService<BaseTmplPlanMapper, BaseTmplPlanPo> implements BaseTmplPlanService {

    @Autowired
    private BaseTmplTaskMapper baseTmplTaskMapper;

    @Override
    public List<BaseTmplPlanVo> queryTmplPlanList() {
        return this.mapper.selectTmplPlanList();
    }

    @Override
    public BaseTmplPlanPo addTmplPlan(BaseTmplPlanAddForm baseTmplPlanAddForm) {
        List<BaseTmplPlanPo> list = this.queryBaseTmplPlanPoByTmplName(baseTmplPlanAddForm.getTmplName());
        if (!ObjectUtils.isEmpty(list)){
            throw new BaseException("项目模板名称不能重复!");
        }
        BaseTmplPlanPo baseTmplPlanPo = this.dozerMapper.map(baseTmplPlanAddForm, BaseTmplPlanPo.class);
        super.insert(baseTmplPlanPo);
        return baseTmplPlanPo;
    }

    @Override
    public BaseTmplPlanVo getTmplPlanInfoById(Integer id) {
        /*BaseTmplPlanPo baseTmplPlanPo = this.mapper.selectByPrimaryKey(id);
        if(!ObjectUtils.isEmpty(baseTmplPlanPo)){
            return this.dozerMapper.map(baseTmplPlanPo, BaseTmplPlanVo.class);
        }else {
            return null;
        }*/
        BaseTmplPlanVo baseTmplPlanVo = this.mapper.selectTmplPlanById(id);
        return baseTmplPlanVo;
    }

    @Override
    @AddLog(title = "修改计划模板信息",module = LoggerModuleEnum.BM_TMPL_PLAN)
    public BaseTmplPlanPo updateTmplPlan(BaseTmplPlanUpdateForm baseTmplPlanUpdateForm) {
        //BaseTmplPlanPo baseTmplPlanPo = this.dozerMapper.map(baseTmplPlanUpdateForm, BaseTmplPlanPo.class);
        BaseTmplPlanPo baseTmplPlanPo = this.selectById(baseTmplPlanUpdateForm.getId());
        if(baseTmplPlanPo == null){
            throw new BaseException("修改的任务不存在!");
        }
        // 添加修改日志
        this.addChangeLogger(baseTmplPlanUpdateForm,baseTmplPlanPo);
        List<BaseTmplPlanPo> list = queryBaseTmplPlanPoByTmplName(baseTmplPlanUpdateForm.getTmplName());
        if (!ObjectUtils.isEmpty(list) && !list.get(0).getId().equals(baseTmplPlanUpdateForm.getId())){
            throw new BaseException("项目模板名称不能重复!");
        }
        dozerMapper.map(baseTmplPlanUpdateForm,baseTmplPlanPo);
        super.updateById(baseTmplPlanPo);
        return baseTmplPlanPo;
    }

    @Override
    public void deleteTmplPlanById(Integer id) {
        this.mapper.deleteByPrimaryKey(id);
    }

    /**
     * 获取导入计划模板下拉列表
     * @return
     */
    @Override
    public List<SelectVo> queryTmplPlanSelectList(Integer userId) {
        List<BaseTmplPlanVo> tmplPlanVos = this.mapper.selectTmplPlanSelectList(userId);

        List<SelectVo> retSelectVos = new ArrayList<>();
        if(!ObjectUtils.isEmpty(tmplPlanVos)){
            for(BaseTmplPlanVo baseTmplPlanVo : tmplPlanVos){

                SelectVo selectVo = this.dozerMapper.map(baseTmplPlanVo,SelectVo.class);
                selectVo.setValue(baseTmplPlanVo.getId());
                selectVo.setTitle(baseTmplPlanVo.getTmplName());
                retSelectVos.add(selectVo);
            }
        }
        return retSelectVos;
    }

    public List<BaseTmplPlanPo> queryBaseTmplPlanPoByTmplName(String tmplName){
        Example example = new Example(BaseTmplPlanPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("tmplName",tmplName);
        List<BaseTmplPlanPo> list = this.selectByExample(example);
        return !ObjectUtils.isEmpty(list) ? list : null;
    }
}
