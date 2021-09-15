package com.wisdom.acm.dc2.service.message;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.po.SmsTaskPo;
import com.wisdom.acm.dc2.vo.SmsTaskVo;
import com.wisdom.base.common.service.CommService;


public interface SmsHistoryService extends CommService<SmsTaskPo>
{
	/**
	 * 短信历史列表分页查询
	 * @param mapWhere
	 * @param pageSize
	 * @param currentPageNum
	 * @return
	 */
	 PageInfo<SmsTaskVo> selectSmsHistoryList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);
 
	 
	  
	  /**
	   * 短信历史列表
	   * @param mapWhere
	   * @return
	   */
	  List<SmsTaskVo> selectBySmsHistoryVoParams(Map<String, Object> mapWhere);

	  /**
	   * 删除短信历史
	   * @param ids
	   */
	  void deleteSmsHistory(List<Integer> ids);
}
