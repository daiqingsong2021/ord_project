package com.wisdom.base.common.feign.menu;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.SelectVo;
import com.wisdom.base.common.vo.menu.SysAuthMenuVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author: szc
 * @Date: 2019/7/19 18:12
 * @Version 1.0
 */
@FeignClient(value = "acm-module-sys",configuration = FeignConfiguration.class)
public interface CommMenuService {

    default SelectVo queryMenuById(Integer menuId){
        ApiResult<SelectVo> apiResult = this.queryMenuById_(menuId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    /**
     * 获取菜单信息
     *
     * @param menuId
     * @return
     */
    @GetMapping("/menu/menuId/{menuId}/menu")
    ApiResult<SelectVo> queryMenuById_(@PathVariable("menuId") Integer menuId);


    default List<SelectVo> queryMenusByIds(@RequestBody List<Integer> menuIds){
        ApiResult<List<SelectVo>> apiResult = this.queryMenusByIds_(menuIds);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    /**
     * 获取多个菜单信息
     * @param menuIds
     * @return
     */
    @PostMapping("/menu/menuIds/menu")
    ApiResult<List<SelectVo>> queryMenusByIds_(@RequestBody List<Integer> menuIds);

    default List<SysAuthMenuVo> selectParentMenuByMenuId(Integer menuId){
       ApiResult<List<SysAuthMenuVo>> apiResult = this.selectParentMenuByMenuId_(menuId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @GetMapping("/menu/menu/tree/parentId/{menuId}")
    ApiResult<List<SysAuthMenuVo>> selectParentMenuByMenuId_(@PathVariable("menuId") Integer menuId);
}
