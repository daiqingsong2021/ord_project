package com.wisdom.acm.wf.service.impl;

import com.wisdom.acm.wf.form.WfTypeAddForm;
import com.wisdom.acm.wf.form.WfTypeSearchForm;
import com.wisdom.acm.wf.form.WfTypeUpdateForm;
import com.wisdom.acm.wf.mapper.WfBizTypeMapper;
import com.wisdom.acm.wf.po.WfBizTypePo;
import com.wisdom.acm.wf.po.WfFormPo;
import com.wisdom.acm.wf.service.WfBizTypeService;
import com.wisdom.acm.wf.service.WfFormService;
import com.wisdom.acm.wf.vo.WfBizTypeVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class WfTypeServiceImpl extends BaseService<WfBizTypeMapper, WfBizTypePo> implements WfBizTypeService {

	@Autowired
	private WfFormService formService;

	/**
	 * 查询流程定义列表
	 *
	 * @return
	 */
	@Override
	public List<WfBizTypeVo> queryWfTypeList() {
		List<WfBizTypeVo> wfBizTypeVos = mapper.selectWfTypeList();
		return wfBizTypeVos;
	}

	/**
	 * 增加流程定义
	 *
	 * @param wfTypeAddForm
	 * @return
	 */
	@Override
	public WfBizTypePo addWfType(WfTypeAddForm wfTypeAddForm) {
		WfBizTypePo wfBizTypePo = dozerMapper.map(wfTypeAddForm, WfBizTypePo.class);
		this.insert(wfBizTypePo);
		return wfBizTypePo;
	}

	/**
	 * 根据id查询vo
	 *
	 * @param id
	 * @return
	 */
	@Override
	public WfBizTypeVo selectWfTypeVo(Integer id) {
		WfBizTypeVo wfBizTypeVo = mapper.selectWfTypeVo(id);
		return wfBizTypeVo;
	}

	/**
	 * 修改流程定义
	 *
	 * @param wfTypeUpdateForm
	 * @return
	 */
	@Override
	@AddLog(title = "修改流程业务定义", module = LoggerModuleEnum.WM_BUSSI)
	public WfBizTypePo updateWfType(WfTypeUpdateForm wfTypeUpdateForm) {
		WfBizTypePo wfBizTypePo = this.selectById(wfTypeUpdateForm.getId());
		if (ObjectUtils.isEmpty(wfBizTypePo)) {
			throw new BaseException("所选流程定义已不存在");
		}
		// 添加修改日志
		this.addChangeLogger(wfTypeUpdateForm, wfBizTypePo);
		this.dozerMapper.map(wfTypeUpdateForm, wfBizTypePo);
		this.updateById(wfBizTypePo);
		return wfBizTypePo;
	}

	/**
	 * 删除流程定义
	 *
	 * @param id
	 */
	@Override
	public void deleteWfType(Integer id) {
		this.deleteById(id);
	}

	/**
	 * 根据业务代码获取流程定义
	 *
	 * @param typeCode
	 * @return
	 */
	@Override
	public WfBizTypePo queryWfTypeByTypeCode(String typeCode) {
		if(ObjectUtils.isEmpty(typeCode)){
			return null;
		}
		Example example = new Example(WfBizTypePo.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("typeCode", typeCode);
		return this.selectOneByExample(example);
	}

	/**
	 * 搜素流程定义
	 *
	 * @param wfTypeSearchForm
	 * @return
	 */
	@Override
	public List<WfBizTypeVo> queryWfTypeBySearch(WfTypeSearchForm wfTypeSearchForm) {
		List<WfBizTypeVo> wfBizTypeVos = mapper.selectWfTypeBySearch(wfTypeSearchForm);
		return wfBizTypeVos;
	}

	@Override
	public WfBizTypeVo getWfTypeInfoByProcInstId(String procInstId) {
		WfFormPo formPo = this.formService.getFormInfoByProcInstId(procInstId);
		if (formPo != null) {
			WfBizTypePo bizTypePo = this.queryWfTypeByTypeCode(formPo.getTypeCode());
			if (bizTypePo != null) {
				return this.dozerMapper.map(bizTypePo, WfBizTypeVo.class);
			}
		}
		return null;
	}
}
