package com.wisdom.acm.dc2.service.message.impl;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.mapper.message.SmsHistoryMapper;
import com.wisdom.acm.dc2.po.SmsTaskPo;
import com.wisdom.acm.dc2.service.SmsTaskService;
import com.wisdom.acm.dc2.service.message.SmsHistoryDetailsService;
import com.wisdom.acm.dc2.service.message.SmsHistoryService;
import com.wisdom.acm.dc2.vo.SmsTaskVo;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class SmsHistoryServiceImpl extends BaseService<SmsHistoryMapper, SmsTaskPo> implements SmsHistoryService
{

    @Autowired
    private SmsTaskService smsTaskService;
    @Autowired
    private SmsHistoryDetailsService smsHistoryDetailsService;
    
	@Override
	public PageInfo<SmsTaskVo> selectSmsHistoryList(Map<String, Object> mapWhere, Integer pageSize,
			Integer currentPageNum) {
		List<SmsTaskVo>  smsHistoryVo= smsTaskService.querySmsTaskList(mapWhere);
		PageInfo<SmsTaskVo> pageInfo = new PageInfo<SmsTaskVo>(smsHistoryVo);
	 	return pageInfo;
	}
	
	
	@Override
	public List<SmsTaskVo> selectBySmsHistoryVoParams(Map<String, Object> mapWhere) {
		List<SmsTaskVo>  smsHistoryVo= smsTaskService.querySmsTaskList(mapWhere);
		return smsHistoryVo;
	}
	
	
	@Override
	public void deleteSmsHistory(List<Integer> ids) {
		smsTaskService.delSmsTask(ids);
		//smsHistoryDetailsService.deleteSmsHistoryDetails(ids);
	}
  
}
