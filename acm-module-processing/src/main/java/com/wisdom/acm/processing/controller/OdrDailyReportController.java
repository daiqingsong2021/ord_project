package com.wisdom.acm.processing.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.wisdom.acm.processing.po.construction.ConstructionDailyPo;
import com.wisdom.acm.processing.service.construction.ConstructionService;
import com.wisdom.acm.processing.service.report.OdrCCDailyReportService;
import com.wisdom.acm.processing.service.report.OdrDailyReportService;
import com.wisdom.acm.processing.vo.report.DailyReportVo;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.vo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zll
 * 2020/8/11/011 14:09
 * Description:<描述>
 */
@Controller
@RestController
@RequestMapping("odr/report")
public class OdrDailyReportController extends BaseController {
    @Autowired
    private OdrDailyReportService odrDailyReportService;
    @Autowired
    private OdrCCDailyReportService odrCCDailyReportService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CommUserService commUserService;
    @Autowired
    private ConstructionService constructionService;

    @Value("${doc.odrUrl}")
    private String odrUrl;

    private static final Logger logger = LoggerFactory.getLogger(OdrDailyReportController.class);
    /**
     * 运营日报打印预览 0
     */
    @PostMapping(value = "/generateDailyWorkReportFile")
    public ApiResult generateDailyWorkReportFile(@RequestBody @Valid Map<String,Object> maps){
        String line="";
        List<String>keys= (List<String>) maps.get("keys");
        if(ObjectUtils.isEmpty(keys) && keys.size()==0){
            return ApiResult.result(1001, "请选择日报内容板块");
        }
        List<DailyReportVo> dailyReportVos=Lists.newArrayList();
        if(!"定时器生成".equals(maps.get("description").toString())){
            UserInfo user=commUserService.getLoginUser();
            maps.put("token",request.getHeader("Authorization"));
            maps.put("gateWayHost",request.getHeader("gateway_host"));
            logger.info("====gateWayHost:" +odrUrl);
            logger.info("====maps:" +maps);
            DailyReportVo dailyReportVo = odrDailyReportService.generateDailyWorkReportFile(maps,user);
            dailyReportVos.add(dailyReportVo);
        }else{
            //对于几号线数据重新梳理
            if(!ObjectUtils.isEmpty(maps.get("line1"))){
                DailyReportVo dailyReportVo = odrDailyReportService.generateDailyWorkReportFile1(maps);//系统生成
                dailyReportVos.add(dailyReportVo);
            }
            if(!ObjectUtils.isEmpty(maps.get("line3"))){
                DailyReportVo dailyReportVo = odrDailyReportService.generateDailyWorkReportFile3(maps);//系统生成
                dailyReportVos.add(dailyReportVo);
            }
        }
        return ApiResult.success(dailyReportVos);
    }

    /**
     * 指挥中心打印预览 1
     */
    @PostMapping(value = "/generateCCDailyWorkReportFile")
    public ApiResult generateCCDailyWorkReportFile(@RequestBody Map<String,Object> maps){
        List<String>keys= (List<String>) maps.get("keys");
        if(ObjectUtils.isEmpty(keys) && keys.size()==0){
            return ApiResult.result(1001, "请选择指挥中心内容板块");
        }
        List<DailyReportVo> dailyReportVos= Lists.newArrayList();
        if(!"定时器生成".equals(maps.get("description").toString())){
            UserInfo user=commUserService.getLoginUser();
            maps.put("token",request.getHeader("Authorization"));
            maps.put("gateWayHost",request.getHeader("gateway_host"));
            DailyReportVo dailyReportVo= odrCCDailyReportService.generateCCDailyWorkReportFile(maps,user);
            dailyReportVos.add(dailyReportVo);
        }else{
            //对于几号线数据重新梳理
            if(!ObjectUtils.isEmpty(maps.get("line1"))){
                DailyReportVo dailyReportVo = odrCCDailyReportService.generateCCDailyWorkReportFile1(maps);//系统生成
                dailyReportVos.add(dailyReportVo);
            }
            if(!ObjectUtils.isEmpty(maps.get("line3"))){
                DailyReportVo dailyReportVo = odrCCDailyReportService.generateCCDailyWorkReportFile3(maps);//系统生成
                dailyReportVos.add(dailyReportVo);
            }
        }
        return ApiResult.success(dailyReportVos);
    }

    /**
     * @param mapIds
     * @return
     */
    @DeleteMapping(value = "/deleteReport")
    public ApiResult deleteReport(@RequestBody List<Map<String,Integer>> mapIds) {
        if (ObjectUtils.isEmpty(mapIds))
        {
            return ApiResult.result(1001, "请检查主键id及文件fileId是否为空");
        }
        odrDailyReportService.deleteReport(mapIds);
        return ApiResult.success();
    }

    /**
     * 根据条件查询日报数据
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/queryDailyReportList/{pageSize}/{currentPageNum}")
    public ApiResult queryDailyReportList(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {
        PageInfo<DailyReportVo> queryDailyReportList=odrDailyReportService.queryDailyReportList(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryDailyReportList);
    }

    /**
     * 流程数据
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/queryFlowDailyReportList")
    public ApiResult queryFlowTrafficMainList(@RequestParam Map<String, Object> mapWhere)
    {
        List<DailyReportVo> queryFlowDailyReportList= odrDailyReportService.queryFlowDailyReportList(mapWhere);
        return ApiResult.success(queryFlowDailyReportList);
    }

    /**
     * 获取消息下看到的列表相关流程
     * */
    @GetMapping(value = "/getFlowDailyReportList")
    public ApiResult getFlowDailyReportList(@RequestParam Map<String, Object> mapWhere) {
        String ids = request.getParameter("ids");//获取ID
        String[] idsArray = String.valueOf(mapWhere.get("ids")).split(",");
        List<String> idsList = new ArrayList<String>(Arrays.asList(idsArray));
        mapWhere.put("ids",idsList.stream().map(id -> Integer.valueOf(id)).collect(Collectors.toList()));
        return ApiResult.success(odrDailyReportService.getFlowDailyReportList(mapWhere));
    }

    /**
     * 获取施工数据
     */
    @GetMapping(value = "/getConstructionList")
    public ApiResult getConstructionList(@RequestParam Map<String, Object> mapWhere) {
        ConstructionDailyPo po=constructionService.queryConstructionDailyPo(mapWhere);
        return ApiResult.success(po);
    }
}