package com.wisdom.acm.dc2.controller.message;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.service.message.SmsHistoryDetailsService;
import com.wisdom.acm.dc2.service.message.SmsHistoryService;
import com.wisdom.acm.dc2.vo.SmsTaskVo;
import com.wisdom.acm.dc2.vo.message.SmsHistoryDetailsVo;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.util.ExportTemplateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *短信群组管理
 */
@Controller
@RestController
@RequestMapping("sms")
public class SmsHistoryController
{
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SmsHistoryService smsHistoryService;
    @Autowired
    private SmsHistoryDetailsService smsHistoryDetailsService;
    /**
     * 查询短信历史列表
     * @param mapWhere 
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/getSmsHistoryList/{pageSize}/{currentPageNum}")
    public ApiResult getSmsHistoryListPage(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {
        PageInfo<SmsTaskVo> queryTrainDailyList=smsHistoryService.selectSmsHistoryList(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryTrainDailyList);
    }
    
    /**
     * 查询短信历史详情
     * @param mapWhere 
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/getSmsHistoryDetailsList/{pageSize}/{currentPageNum}")
    public ApiResult getSmsHistoryDetailsListPage(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {
    	Object id = mapWhere.get("taskId");
    	if (ObjectUtils.isEmpty(id)) {
            return ApiResult.result(1001, "短信历史id不能为空");
        }
        PageInfo<SmsHistoryDetailsVo> queryTrainDailyList=smsHistoryDetailsService.selectSmsHistoryDetailsList(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryTrainDailyList);
    }
    
    
    /**
     * 导出短信历史列表
     * @param response
     * @param mapWhere
     */
    @PostMapping(value = {"/getSmsHistoryInfo/export"})
    public void dowContractLedger(HttpServletResponse response, @RequestParam Map<String, Object> mapWhere) {
    	Object smsTimeStart = mapWhere.get("smsTimeStart");
    	Object smsTimeEnd = mapWhere.get("smsTimeEnd");
    	List<SmsTaskVo>  smsHistoryVoLists = new ArrayList<SmsTaskVo>();
    	if (!ObjectUtils.isEmpty(smsTimeStart) || !ObjectUtils.isEmpty(smsTimeEnd) ) {
    	  smsHistoryVoLists= smsHistoryService.selectBySmsHistoryVoParams(mapWhere);
        }
        List<Map<String, Object>> smsHistoryVoList = new ArrayList<>();
        smsHistoryVoList =this.getsmsHistoryReportList(smsHistoryVoLists);
        ExportTemplateUtil.export(request, response, "smsHistoryReports.xlsx",
                "短信发送历史信息.xlsx", smsHistoryVoList);
    }
    
    private List<Map<String, Object>> getsmsHistoryReportList(List<SmsTaskVo> smsHistoryLists) {
    	
    	List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
    	for(SmsTaskVo po: smsHistoryLists) {
    		Map<String, Object> mapinfo = new HashMap<>();
    		mapinfo.put("smsSendNumber", po.getSmsSendNumber());
    		mapinfo.put("smsContent", po.getMessageContent());
    		mapinfo.put("sendNum", po.getSendNum());
    		mapinfo.put("receiveNum", po.getReceiveSuccessNum());
    		mapinfo.put("smsTargetDesc", po.getSendPersonStr());
    		mapinfo.put("smsChannelDesc", po.getSendChannelStr());
    		mapinfo.put("smsTime", po.getSendTime());
    		mapList.add(mapinfo);
		}
    	return mapList;
	}
   
    /**
     * 删除短信历史信息
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteSmsHistory")
    public ApiResult deleteGroupManage(@RequestBody List<Integer> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return ApiResult.result(1001, "id不能为空");
        }
        smsHistoryService.deleteSmsHistory(ids);
        return ApiResult.success();
    }
}