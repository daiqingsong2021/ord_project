package com.wisdom.acm.dc3.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.wisdom.acm.dc3.service.EnergyDailyService;
import com.wisdom.acm.dc3.service.EnergyDetailService;
import com.wisdom.acm.dc3.vo.EnergyDailyVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.util.ResourceUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 *能耗日况
 */
@RestController
@RequestMapping("nhrk/energyDaily")
public class EnergyDailyController
{
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private EnergyDailyService energyDailyService;
    @Autowired
    private EnergyDetailService energyDetailService;


    /**
     * 下载Excel模板
     *
     * @return
     */
    @PostMapping(value = "/downEnergyDetailTemplate/{lineNum}")
    @AddLog(title = "下载能耗模板", module = LoggerModuleEnum.ENERGY_DETAILEDSEARCH)
    public void downEnergyDetailTemplate(HttpServletResponse response, @PathVariable("lineNum")String lineNum) {
        try {
            if("1".equals(lineNum))
            {
                InputStream inputStream = ResourceUtil.findResoureFile("template/oneLineEnergyTemplate.xlsx");
                // 设置响应头和客户端保存文件名
                String fileName = URLEncoder.encode("1号线电量统计表模板.xlsx", "UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment;Filename=" + fileName);
                IOUtils.copy(inputStream, response.getOutputStream());
                response.getOutputStream().flush();
                response.getOutputStream().close();
            }
            else if("3".equals(lineNum))
            {
                InputStream inputStream = ResourceUtil.findResoureFile("template/threeLineEnergyTemplate.xlsx");
                // 设置响应头和客户端保存文件名
                String fileName = URLEncoder.encode("3号线电量统计表模板.xlsx", "UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment;Filename=" + fileName);
                IOUtils.copy(inputStream, response.getOutputStream());
                response.getOutputStream().flush();
                response.getOutputStream().close();
            }

        } catch (Exception e) {
            throw new BaseException("导出里程碑信息模板出错!");
        }
    }

    /**
     * 上传能耗信息Excel
     *
     * @return
     */
    @PostMapping(value = "/uploadEnergyDetailInfo/{line}")
    public ApiResult uploadEnergyDetailInfo(@RequestParam("file") MultipartFile file, HttpServletRequest request,@PathVariable("line")Integer line) throws IOException
    {
        Map<String, Object> mapWhere=new HashMap<>();
        mapWhere.put("line",line);
        if (file.getSize() != 0 && !"".equals(file.getName()))
        {
            String errorId = energyDailyService.uploadEnergyDailyTemplate(mapWhere,file);
            if (StringHelper.isNotNullAndEmpty(errorId)) {
                return ApiResult.result(1007, errorId);
            }
            return ApiResult.success();
        }
        return ApiResult.success();
    }


    @DeleteMapping(value = "/delete")
    public ApiResult deleteEnergyDaily(@RequestBody List<Integer> ids)
    {
        if (ObjectUtils.isEmpty(ids))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        //为了添加多条删除日志
        for(Integer id:ids)
        {
            energyDailyService.deleteEnergyDaily(id);
        }
        return ApiResult.success();
    }




    @GetMapping(value = "/{id}")
    public ApiResult getEnergyDailyById(@PathVariable("id")String id)
    {
        if (ObjectUtils.isEmpty(id))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        return ApiResult.success( energyDailyService.selectById(id));
    }

    /**
     * 查询列车能耗日况 有流程的列表页
     * @param mapWhere   endTime  startTime
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/flowDailyPageList/{pageSize}/{currentPageNum}")
    public ApiResult getFlowEnergyDailyPageList(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {
        if(!ObjectUtils.isEmpty(mapWhere.get("startTime")))
        {
            mapWhere.put("startTime", StringHelper.formattString(String.valueOf(mapWhere.get("startTime"))));
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("endTime"))) {
            mapWhere.put("endTime", StringHelper.formattString(String.valueOf(mapWhere.get("endTime"))));
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("ids"))) {
            List<String> idsList = Lists.newArrayList();
            String[] idsArray = mapWhere.get("ids").toString().split(",");
            idsList = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("ids",idsList);
        }
        PageInfo<EnergyDailyVo> queryEnergyDailyList= energyDailyService.selectEnergyDailyPageList(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryEnergyDailyList);
    }

    /**
     * 查询列车能耗日况
     * @param mapWhere    endTime  startTime
     * @return
     */
    @GetMapping(value = "/flowDailyList")
    public ApiResult getFlowEnergyDailyList(@RequestParam Map<String, Object> mapWhere)
    {
        if(ObjectUtils.isEmpty(mapWhere.get("startTime")))
        {
            mapWhere.put("startTime", StringHelper.formattString(String.valueOf(mapWhere.get("startTime"))));
        }
        if(ObjectUtils.isEmpty(mapWhere.get("endTime"))) {
            mapWhere.put("endTime", StringHelper.formattString(String.valueOf(mapWhere.get("endTime"))));
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("ids"))) {
            List<String> idsList = Lists.newArrayList();
            String[] idsArray = mapWhere.get("ids").toString().split(",");
            idsList = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("ids",idsList);
        }
        List< EnergyDailyVo> queryEnergyDailyList= energyDailyService.selectByParams(mapWhere);
        return ApiResult.success(queryEnergyDailyList);
    }





    /**
     * 查询列车能耗日况  不带流程的列表页
     * @param mapWhere    endTime  startTime  line  linePeriod
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/list/{pageSize}/{currentPageNum}")
    public ApiResult getEnergyDailyListPage(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {
        if(!ObjectUtils.isEmpty(mapWhere.get("startTime")))
        {
            mapWhere.put("startTime", StringHelper.formattString(String.valueOf(mapWhere.get("startTime"))));
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("endTime"))) {
            mapWhere.put("endTime", StringHelper.formattString(String.valueOf(mapWhere.get("endTime"))));
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("ids"))) {
            String[] idsArray = String.valueOf(mapWhere.get("ids")).split(",");
            List<String> ids = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("ids",ids);
        }
        mapWhere.put("reviewStatus","APPROVED");
        PageInfo< EnergyDailyVo> queryEnergyDailyList= energyDailyService.selectEnergyDailyPageList(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryEnergyDailyList);
    }


    /**
     * 查询列车能耗日况  不带流程的列表页
     * @param mapWhere    endTime  startTime  line  linePeriod
     * @return
     */
    @GetMapping(value = "/list")
    public ApiResult getEnergyDailyList(@RequestParam Map<String, Object> mapWhere)
    {
        if(ObjectUtils.isEmpty(mapWhere.get("startTime")))
        {
            mapWhere.put("startTime", StringHelper.formattString(String.valueOf(mapWhere.get("startTime"))));
        }
        if(ObjectUtils.isEmpty(mapWhere.get("endTime"))) {
            mapWhere.put("endTime", StringHelper.formattString(String.valueOf(mapWhere.get("endTime"))));
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("ids"))) {
            List<String> idsList = Lists.newArrayList();
            String[] idsArray = mapWhere.get("ids").toString().split(",");
            idsList = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("ids",idsList);
        }
        mapWhere.put("reviewStatus","APPROVED");
        List< EnergyDailyVo> queryEnergyDailyList= energyDailyService.selectByParams(mapWhere);
        return ApiResult.success(queryEnergyDailyList);
    }

    @GetMapping(value = "/approvedEnergyDaily")
    public ApiResult approvedEnergyDaily(@RequestParam String ids)
    {
        if (StringHelper.isNotNullAndEmpty(ids)) {//如果主键Ids 不为空
            String[] idsArray = ids.split(",");
            List<String> idsList = new ArrayList<String>(Arrays.asList(idsArray));
            List<Integer> idsIntegerList=idsList.stream().map(id -> Integer.valueOf(id)).collect(Collectors.toList());
            energyDailyService.approvedEnergyDaily(idsIntegerList);
            return ApiResult.success();
        }
        else
        {
            return ApiResult.result(1001, "id不能为空");
        }
    }
}
