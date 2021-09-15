package com.wisdom.acm.dc2.service.message;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.po.message.SmsHistoryDetailsPo;
import com.wisdom.acm.dc2.vo.message.SmsHistoryDetailsVo;
import com.wisdom.base.common.service.CommService;


public interface SmsHistoryDetailsService extends CommService<SmsHistoryDetailsPo>
{
	/**
	 * 短信历史发送明细
	 * @param mapWhere
	 * @param pageSize
	 * @param currentPageNum
	 * @return
	 */
	 PageInfo<SmsHistoryDetailsVo> selectSmsHistoryDetailsList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);
 
	 /**
	  * 修改短信发送历史状态
	  * @param mapWhere
	  */
	 void updateSmsHistoryDetails(Map<String, Object> mapWhere);
	  
	 /**
	   * 删除短信历史详细信息
	   * @param ids
	   */
	 void deleteSmsHistoryDetails(List<Integer> taskIds);

	/**
	 * 新增
	 * @param po
	 */
	 void insertSmsHistoryDetails(SmsHistoryDetailsPo po);
	 
	 /**
	  * 查询手机号发送次数
	  * @param mapWhere
	  * @return
	  */
	 int targetNumberNum(Map<String, Object> mapWhere);
	 
}
