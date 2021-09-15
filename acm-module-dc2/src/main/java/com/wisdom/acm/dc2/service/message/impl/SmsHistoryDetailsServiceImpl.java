package com.wisdom.acm.dc2.service.message.impl;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.mapper.message.SmsHistoryDetailsMapper;
import com.wisdom.acm.dc2.po.message.SmsHistoryDetailsPo;
import com.wisdom.acm.dc2.service.message.SmsHistoryDetailsService;
import com.wisdom.acm.dc2.vo.message.SmsHistoryDetailsVo;
import com.wisdom.base.common.service.BaseService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class SmsHistoryDetailsServiceImpl extends BaseService<SmsHistoryDetailsMapper, SmsHistoryDetailsPo> implements SmsHistoryDetailsService
{


	@Override
	public PageInfo<SmsHistoryDetailsVo> selectSmsHistoryDetailsList(Map<String, Object> mapWhere, Integer pageSize,
			Integer currentPageNum) {
		List<SmsHistoryDetailsVo>  smsHistoryDetailsVo= mapper.selectByParams(mapWhere);
	    PageInfo<SmsHistoryDetailsVo> pageInfo = new PageInfo<SmsHistoryDetailsVo>(smsHistoryDetailsVo);
	 	return pageInfo;
	}

	@Override
	public void updateSmsHistoryDetails(Map<String, Object> mapWhere) {
		mapper.updateSmsHistoryDetails(mapWhere);
	}

	@Override
	public void deleteSmsHistoryDetails(List<Integer> taskIds) {
		mapper.deleteSmsHistoryDetails(taskIds);
	}

	@Override
	public void insertSmsHistoryDetails(SmsHistoryDetailsPo po) {
		super.insert(po);
	}

	@Override
	public int targetNumberNum(Map<String, Object> mapWhere) {
		return mapper.targetNumberNum(mapWhere);
	}
}
