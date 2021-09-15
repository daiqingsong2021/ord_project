package com.wisdom.acm.szxm.controller.pfe;


import com.wisdom.acm.szxm.form.pfe.StationAddForm;
import com.wisdom.acm.szxm.form.pfe.StationUpdateForm;
import com.wisdom.acm.szxm.service.pfe.StationService;
import com.wisdom.acm.szxm.vo.pfe.StationVo;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 站点控制器
 */

@RestController
@RequestMapping("plan/station")
public class StationController extends BaseController {

    @Autowired
    private StationService stationService;

    /**
     * 获取单条数据基本信息
     * @param id
     * @return
     */
    @GetMapping(value = "/details/{id}/info")
    public ApiResult getSectionDetailsBySectionId(@PathVariable(name = "id") Integer id) {
        StationVo vo =  stationService.getStationInfo(id);
        return ApiResult.success(vo);
    }

    /**
     *
     * @param projectId
     * @return
     */
    @GetMapping(value = "search/{projectId}/list")
    public ApiResult getStationByProjectId(@PathVariable(name = "projectId") Integer projectId){
       List<StationVo> stationVos =  stationService.queryStationByProjectId(projectId);
       return ApiResult.success(stationVos);
    }

    /**
     * 新增站点
     * @param stationAddForm
     * @return
     */
    @PostMapping("/add")
    public ApiResult addStation(@RequestBody StationAddForm stationAddForm){
        StationVo stationVo = stationService.addStationByForm(stationAddForm);
        return ApiResult.success(stationVo);
    }

    /**
     * 修改站点
     * @param stationUpdateForm
     * @return
     */
    @PutMapping("/update")
    public ApiResult updateStation(@RequestBody StationUpdateForm stationUpdateForm){
        StationVo stationVo = stationService.updateStationByForm(stationUpdateForm);
        return ApiResult.success(stationVo);
    }

    /**
     * 删除站点
     * @param ids
     * @return
     */
    @DeleteMapping("/delete")
    public ApiResult deleteStation(@RequestBody List<Integer> ids){
        stationService.deleteStation(ids);
        return ApiResult.success();
    }
}
