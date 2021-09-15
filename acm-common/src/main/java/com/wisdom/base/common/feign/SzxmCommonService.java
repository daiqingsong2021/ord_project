package com.wisdom.base.common.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.form.szxm.InventoryUpdateForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.ProjectTeamVo;
import com.wisdom.base.common.vo.szxm.InventoryVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 苏州项目CommonService
 */
@FeignClient(value = "acm-module-szxm",configuration = FeignConfiguration.class)
public interface SzxmCommonService
{
    /**
     * 外部单位增加 项目基础信息（单位类型 非1）
     * @param sgPojectTeamVo
     * @return 返回新增的项目基础信息主键ID
     */
    default Integer addPorjInfoMsg(ProjectTeamVo sgPojectTeamVo){
        ApiResult<Integer>  apiResult = this.addPorjInfoMsg_(sgPojectTeamVo);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * 外部单位修改 项目基础信息（单位类型 非1）
     * @param sgPojectTeamVo
     * @return 返回修改的项目基础信息主键ID
     */
    default Integer updatePorjInfoMsg(ProjectTeamVo sgPojectTeamVo){
        ApiResult<Integer>  apiResult = this.updatePorjInfoMsg_(sgPojectTeamVo);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * 外部单位修改 删除基础信息（单位类型 非1）
     * @param id
     * @return  返回修改的项目基础信息主键ID
     */
    default Integer deletePorjInfoMsg(Integer id){
        ApiResult<Integer>  apiResult = this.deletePorjInfoMsg_(id);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * 查询物料台账信息
     * @param mapWhere
     * @return
     */
    default List<InventoryVo> getInventoryList(Map<String, Object> mapWhere) {
        ApiResult<List<InventoryVo>> apiResult = this.getInventoryList_(mapWhere);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * 更新物料台账信息
     * @param inventoryUpdateForm
     * @return
     */
    default InventoryVo updateInventory(InventoryUpdateForm inventoryUpdateForm) {
        ApiResult<InventoryVo>  apiResult = this.updateInventory_(inventoryUpdateForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * @param sgPojectTeamVo
     * @return
     */
    @RequestMapping(value ="/rygl/projInfo/addPorjInfoMsg", method = RequestMethod.POST)
    ApiResult<Integer> addPorjInfoMsg_(@RequestBody ProjectTeamVo sgPojectTeamVo);

    /**
     * @param sgPojectTeamVo
     * @return
     */
    @RequestMapping(value ="/rygl/projInfo/updatePorjInfoMsg", method = RequestMethod.PUT)
    ApiResult<Integer> updatePorjInfoMsg_(@RequestBody ProjectTeamVo sgPojectTeamVo);

    @RequestMapping(value ="/rygl/projInfo/deletePorjInfoMsg/{sysOrgId}", method = RequestMethod.DELETE)
    ApiResult<Integer> deletePorjInfoMsg_(@PathVariable("sysOrgId") Integer sysOrgId);

    @RequestMapping(value ="/wlgl/inventory/list", method = RequestMethod.GET)
    ApiResult getInventoryList_(@RequestParam Map<String, Object> mapWhere);

    @RequestMapping(value ="/wlgl/inventory/update", method = RequestMethod.PUT)
    ApiResult updateInventory_(@RequestBody InventoryUpdateForm inventoryUpdateForm);

}
