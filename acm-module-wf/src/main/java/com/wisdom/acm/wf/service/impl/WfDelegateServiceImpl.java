package com.wisdom.acm.wf.service.impl;

import com.wisdom.acm.wf.form.WfDelegateAddForm;
import com.wisdom.acm.wf.form.WfDelegateUpdateForm;
import com.wisdom.acm.wf.mapper.WfDelegateMapper;
import com.wisdom.acm.wf.po.WfDelegatePo;
import com.wisdom.acm.wf.service.WfDelegateService;
import com.wisdom.acm.wf.vo.WfDelegateVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.calc.calendar.Tools;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class WfDelegateServiceImpl extends BaseService<WfDelegateMapper, WfDelegatePo> implements WfDelegateService {

	/**
	 * 查询流程代理信息
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public List<WfDelegateVo> queryWfDelegateByUserId(Integer userId) {
		List<WfDelegatePo> wfDelegatePos = this.queryWfDelegatePoByUserId(userId);
		//将po转化为vo
		List<Integer> ids = ListUtil.toIdList(wfDelegatePos);
		List<WfDelegateVo> wfDelegateVos = null;
		if (!ObjectUtils.isEmpty(ids)) {
			wfDelegateVos = mapper.selectWfDelegateVosByIds(ids);
		}
		return wfDelegateVos;
	}

	/**
	 * 查询基本信息
	 *
	 * @param id
	 * @return
	 */
	@Override
	public WfDelegateVo getWfDelegateInfo(Integer id) {
		WfDelegateVo wfDelegateVo = mapper.selectWfDelegateInfo(id);
		return wfDelegateVo;
	}

	/**
	 * 增加流程代理
	 *
	 * @param wfDelegateAddForm
	 * @return
	 */
	@Override
	public WfDelegatePo addWfDelegate(WfDelegateAddForm wfDelegateAddForm) {
		if (ObjectUtils.isEmpty(wfDelegateAddForm.getStartTime())) {
			throw new BaseException("开始时间不能为空");
		}
		if (ObjectUtils.isEmpty(wfDelegateAddForm.getEndTime())) {
			throw new BaseException("结束时间不能为空");
		}
		WfDelegatePo wfDelegatePo = dozerMapper.map(wfDelegateAddForm, WfDelegatePo.class);
		this.insert(wfDelegatePo);
		return wfDelegatePo;
	}

	/**
	 * 修改流程代理
	 *
	 * @param updateForm
	 * @return
	 */
	@Override
	public WfDelegatePo updateWfDelegate(WfDelegateUpdateForm updateForm) {
		if (ObjectUtils.isEmpty(updateForm.getStartTime())) {
			throw new BaseException("开始时间不能为空");
		}
		if (ObjectUtils.isEmpty(updateForm.getEndTime())) {
			throw new BaseException("结束时间不能为空");
		}
		WfDelegatePo wfDelegatePo = this.selectById(updateForm.getId());
		if (ObjectUtils.isEmpty(wfDelegatePo)) {
			throw new BaseException("该条代理信息已不存在");
		}
		dozerMapper.map(updateForm, wfDelegatePo);
		this.updateById(wfDelegatePo);

		return wfDelegatePo;
	}

	/**
	 * 删除流程代理
	 *
	 * @param ids
	 */
	@Override
	public void deleteWfDelegate(List<Integer> ids) {
		this.deleteByIds(ids);
	}

	private List<WfDelegatePo> queryWfDelegatePoByUserId(Integer userId) {
		Example example = new Example(WfDelegatePo.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("creator", userId);
		List<WfDelegatePo> list = this.selectByExample(example);
		return list;
	}

	/**
	 * 查询流程代理信息
	 *
	 * @param bizTypeCode 业务代码
	 * @param assignees   委托人
	 * @return
	 */
	public List<WfDelegateVo> selectWfDelegateVosByBizTypeCode(String bizTypeCode, List<Integer> assignees){
		Date date = Tools.toDate(Tools.toDateString(new Date()));
		return this.mapper.selectWfDelegateVosByBizTypeCode(bizTypeCode,assignees, date);
	}
}
