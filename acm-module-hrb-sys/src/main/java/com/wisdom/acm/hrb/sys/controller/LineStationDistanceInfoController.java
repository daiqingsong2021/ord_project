package com.wisdom.acm.hrb.sys.controller;

import com.wisdom.acm.hrb.sys.form.*;
import com.wisdom.acm.hrb.sys.service.*;
import com.wisdom.acm.hrb.sys.vo.LineFoundationVo;
import com.wisdom.acm.hrb.sys.vo.StationFoundationVo;
import com.wisdom.acm.hrb.sys.vo.StationStationFoundationVo;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/10/20/020 11:06
 * Description:<线路，站点，线路情况,以及月份，年度客运累计说明>
 */
@Controller
@RestController
@RequestMapping("odr/foundation")
public class LineStationDistanceInfoController extends BaseController {
    @Autowired
    private LineFoundationService lineFoundationService;
    @Autowired
    private StationFoundationService stationFoundationService;
    @Autowired
    private StationStationFoundationService stationStationFoundationService;
    @Autowired
    private TrainFoundationService trainFoundationService;
    @Autowired
    private TransportFoundationService transportFoundationService;
    /**
     * 新增线路信息
     * @param lineFoundationAddForms
     * @return
     */
    @PostMapping(value = "/addLineFoundationList")
    public ApiResult addLineFoundationList(@RequestBody @Valid List<LineFoundationAddForm> lineFoundationAddForms)
    {
        return ApiResult.success(lineFoundationService.addLineFoundationList(lineFoundationAddForms));
    }

    /**
     * 检查线路是否存在
     * @return
     */
    @GetMapping(value = "/checkLineFoundationIsHave")
    public ApiResult checkLineFoundationIsHave(@RequestParam String line)
    {
        return ApiResult.success(lineFoundationService.checkLineFoundationIsHave(line));
    }

    /**
     * 查询线路信息
     * @return
     */
    @GetMapping(value = "/queryLineFoundationList")
    public ApiResult queryLineFoundationList()
    {
        return ApiResult.success(lineFoundationService.queryLineFoundationList());
    }

    /**
     * 删除线路以及相关站点及区间子表
     * @param lineFoundationVos
     * @return
     */
    @DeleteMapping(value = "/delLineFoundationList")
    public ApiResult delLineFoundationList(@RequestBody List<LineFoundationVo> lineFoundationVos)
    {
        if (ObjectUtils.isEmpty(lineFoundationVos))
        {
            return ApiResult.result(1001, "删除数据选择为空，请检查！");
        }
        lineFoundationService.delLineFoundationList(lineFoundationVos);
        return ApiResult.success();
    }

    /**
     * 查询关联站点数据（虚拟数字字典）
     * 若status设置为0则说明是返回前端设置的数据
     * @return
     */
    @GetMapping(value = "/getStationListMap")
    public ApiResult getStationListMap(@RequestParam String line,@RequestParam String status)
    {
        Map<String,String> stationMap= stationFoundationService.getStationListMap(line,status);
        return ApiResult.success(stationMap);
    }

    /**
     * 查询关联站点数据（虚拟数字字典）
     * 若status设置为0则说明是返回前端设置的数据
     * @return
     */
    @GetMapping(value = "/getStationList")
    public ApiResult getStationList(@RequestParam String line,@RequestParam String status)
    {
        return ApiResult.success(stationFoundationService.getStationList(line,status));
    }

    /**
     * 查询关联站点数据（辅助线车辆段）
     * @return
     */
    @GetMapping(value = "/getAuxiliaryStationList")
    public ApiResult getAuxiliaryStationList(@RequestParam String line)
    {
        Map<String,List<String>> stationMap= stationFoundationService.getAuxiliaryStationList(line);
        return ApiResult.success(stationMap);
    }

    /**
     * 查询站点信息
     * @return
     */
    @GetMapping(value = "/queryStationFoundationList")
    public ApiResult queryStationFoundationList(@RequestParam String line,@RequestParam String stationType)
    {
        return ApiResult.success(stationFoundationService.queryStationFoundationList(line,stationType));
    }

    /**
     * 新增站点信息
     * @param stationFoundationAddForms
     * @return
     */
    @PostMapping(value = "/addStationFoundationList")
    public ApiResult addStationFoundationList(@RequestBody @Valid List<StationFoundationAddForm> stationFoundationAddForms)
    {
        return ApiResult.success(stationFoundationService.addStationFoundationList(stationFoundationAddForms));
    }

    /**
     * 修改站点信息（排序改变）
     * @param stationFoundationUpdateForms
     * @return
     */
    @PutMapping(value = "/updateStationFoundationList")
    public ApiResult updateStationFoundationList(@RequestBody @Valid List<StationFoundationUpdateForm> stationFoundationUpdateForms)
    {
        return ApiResult.success(stationFoundationService.updateStationFoundationList(stationFoundationUpdateForms));
    }

    /**
     * 删除站点以及相关站点及区间子表，对应得逻辑：重新排序，站点打乱之后重新对应，站点区间数据重新排序以及数据是否置0
     * @param stationFoundationVos
     * @return
     */
    @DeleteMapping(value = "/delStationFoundationList")
    public ApiResult delStationFoundationList(@RequestBody List<StationFoundationVo> stationFoundationVos)
    {
        if (ObjectUtils.isEmpty(stationFoundationVos))
        {
            return ApiResult.result(1001, "删除数据选择为空，请检查！");
        }
        stationFoundationService.delStationFoundationList(stationFoundationVos);
        return ApiResult.success();
    }

    /**
     * 查询站点间距信息
     * @return
     */
    @GetMapping(value = "/queryStationDistanceFoundationList")
    public ApiResult queryStationDistanceFoundationList(@RequestParam String line)
    {
        return ApiResult.success(stationStationFoundationService.queryStationDistanceFoundationList(line));
    }

    /**
     * 修改站点间距信息
     * @param stationStationFoundationUpdateForms
     * @return
     */
    @PutMapping(value = "/updateStationDistanceFoundationList")
    public ApiResult updateStationDistanceFoundationList(@RequestBody @Valid List<StationStationFoundationUpdateForm> stationStationFoundationUpdateForms)
    {
        return ApiResult.success(stationStationFoundationService.updateStationDistanceFoundationList(stationStationFoundationUpdateForms));
    }




    /**
     * 查询站点信息
     * @return
     * String line
     * String stationType  站点类型（数字字典）：0正线站点，1辅助线站点，2车辆段站点
     * stationCode    用于辅助线查询正线关联站点   取stationFoundationRelation  中正线站点
     * startStationCode   用于正线站点  查询相关的辅助站点  直接取即可   不需要传站点类型
     * stationTypes  多个类型的  中间站和辅助线
     */
    @GetMapping(value = "/queryStationListByParam")
    public ApiResult queryStationListByParam(@RequestParam Map<String, Object> mapWhere)
    {
        if(ObjectUtils.isEmpty(mapWhere.get("line")))
        {
            return ApiResult.result(1001, "line 不能为空");
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("stationType")) )
        {
            if(mapWhere.get("stationType").toString().contains(","))
            {
                String[] stationTypeArray = String.valueOf(mapWhere.get("stationType")).split(",");
                List<String> stationTypes = new ArrayList<String>(Arrays.asList(stationTypeArray));
                mapWhere.remove("stationType");
                mapWhere.put("stationTypes",stationTypes);
            }
        }
        List<StationFoundationVo> stationFoundationList=stationFoundationService.queryStationListByParam(mapWhere);
        return ApiResult.success(stationFoundationList);
    }


    /**
     * 计算里程
     *线路  line
     * 上行下行  stationDirection   0：上行  1：下行
     *
     *出段   String period;
     *正线始发站  String startStation;
     *终点站 String endStation;
     * 进段 inStation;
     *
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/queryStationToStationMileage")
    public ApiResult queryStationToStationMileage(@RequestParam Map<String, Object> mapWhere)
    {
        if(ObjectUtils.isEmpty(mapWhere.get("line")))
        {
            return ApiResult.result(1001, "line 不能为空");
        }
        if(ObjectUtils.isEmpty(mapWhere.get("stationDirection")))
        {
            return ApiResult.result(1001, "stationDirection 站点方向不能为空");
        }

        StationStationFoundationVo stationMileage=stationStationFoundationService.queryStationToStationMileage(mapWhere);
        return ApiResult.success(stationMileage);
    }
    @GetMapping(value = "/checkStationFoundationIsHave")
    public ApiResult checkStationFoundationIsHave(@RequestParam String line,@RequestParam String stationCode,@RequestParam String stationType) { return ApiResult.success(stationFoundationService.checkStationFoundationIsHave(line,stationCode,stationType));}
    @PostMapping(value = "/addTrainFoundation")
    public ApiResult addTrainFoundation(@RequestBody @Valid TrainFoundationAddForm trainFoundationAddForm){return ApiResult.success(trainFoundationService.addTrainFoundation(trainFoundationAddForm));}
    @PutMapping(value = "/updateTrainFoundation")
    public ApiResult updateTrainFoundation(@RequestBody @Valid TrainFoundationUpdateForm trainFoundationUpdateForm){return ApiResult.success(trainFoundationService.updateTrainFoundation(trainFoundationUpdateForm));}
    @DeleteMapping(value = "/delTrainFoundationList")
    public ApiResult delTrainFoundationList(@RequestBody List<Integer> ids) {if (ObjectUtils.isEmpty(ids)) return ApiResult.result(1001, "id不能为空");trainFoundationService.delTrainFoundationList(ids);return ApiResult.success(); }
    @GetMapping(value = "/queryTrainFoundationList/{pageSize}/{currentPageNum}")
    public ApiResult queryTrainFoundationList(@RequestParam String line,@PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum){return new TableResultResponse(trainFoundationService.queryTrainFoundationList(line,null,pageSize,currentPageNum));}
    @GetMapping(value = "/queryTrainFoundationList")
    public ApiResult queryTrainFoundationList(@RequestParam String line){return ApiResult.success(trainFoundationService.getTrainFoundationVos(line,null));}
    @GetMapping(value = "/checkTrainFoundationIsHave")
    public ApiResult checkTrainFoundationIsHave(@RequestParam String line,@RequestParam String trainCode) {return ApiResult.success(trainFoundationService.checkTrainFoundationIsHave(line,trainCode));}
    @PostMapping(value = "/addTransportFoundation")
    public ApiResult addTransportFoundation(@RequestBody @Valid TransportFoundationAddForm transportFoundationAddForm){return ApiResult.success(transportFoundationService.addTransportFoundation(transportFoundationAddForm));}
    @DeleteMapping(value = "/delTransportFoundation")
    public ApiResult delTransportFoundation(@RequestBody Integer id) {if (ObjectUtils.isEmpty(id)) return ApiResult.result(1001, "id不能为空");transportFoundationService.delTransportFoundation(id);return ApiResult.success(); }
    @PutMapping(value = "/updateTransportFoundation")
    public ApiResult updateTransportFoundation(@RequestBody @Valid TransportFoundationUpdateForm transportFoundationUpdateForm){return ApiResult.success(transportFoundationService.updateTransportFoundation(transportFoundationUpdateForm));}
    @GetMapping(value = "/queryTransportFoundationList")
    public ApiResult queryTransportFoundationList(){ return ApiResult.success(transportFoundationService.queryTransportFoundationList());}
}
