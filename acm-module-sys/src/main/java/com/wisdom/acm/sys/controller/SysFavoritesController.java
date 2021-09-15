package com.wisdom.acm.sys.controller;


import com.wisdom.acm.sys.form.SysFavoritesAddForm;
import com.wisdom.acm.sys.service.SysFavoritesService;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@RestController
public class SysFavoritesController {

    @Autowired
    private SysFavoritesService sysFavoritesService;

    @Autowired
    protected HttpServletRequest request;

    /**
     * 获取用户收藏夹
     * @return
     */
    @GetMapping(value = "/favorites/{bizType}/list")
    public ApiResult queryFavorites(@PathVariable String bizType){
        Integer userId = Integer.valueOf(request.getHeader("userId"));
        List<String> list = sysFavoritesService.queryFavoritesContentByUserIdAndBizType(userId,bizType);
        return ApiResult.success(list);
    }


    /**
     * 获取用户收藏夹
     * @return
     */
    @GetMapping(value = "/favorites/{bizTypes}/list/map")
    public ApiResult queryFavorites(@PathVariable List<String> bizTypes){
        Integer userId = Integer.valueOf(request.getHeader("userId"));
        Map<String,List<String>> map  = sysFavoritesService.queryFavoritesContentByUserIdAndBizType(userId,bizTypes);
        return ApiResult.success(map);
    }

    /**
     * 获取用户收藏夹
     *
     * @return
     */
    @GetMapping(value = "/favorites/{userId}/{bizTypes}/list/map")
    public ApiResult queryFavoritesByUserAndBizTypes(@PathVariable("userId") Integer userId, @PathVariable("bizTypes") List<String> bizTypes){
        Map<String,List<String>> map  = sysFavoritesService.queryFavoritesContentByUserIdAndBizType(userId,bizTypes);
        return ApiResult.success(map);
    }

    /**
     * 增加用户收藏
     *
     * @param sysFavoritesAddForm
     * @return
     */
    @PostMapping(value = "/favorites/add")
    public ApiResult addFavorites(@RequestBody SysFavoritesAddForm sysFavoritesAddForm){
        Integer userId = Integer.valueOf(request.getHeader("userId"));
        sysFavoritesService.addFavorites(sysFavoritesAddForm,userId);
        return ApiResult.success();
    }

    /**
     * 重置收藏
     *
     * @param sysFavoritesAddForm
     * @return
     */
    @PostMapping(value = "/favorites/rest")
    public ApiResult restFavorites(@RequestBody SysFavoritesAddForm sysFavoritesAddForm){
        Integer userId = Integer.valueOf(request.getHeader("userId"));
        sysFavoritesService.restFavorites(sysFavoritesAddForm,userId);
        return ApiResult.success();
    }

    /**
     * 批量重置收藏
     *
     * @param favoritesAddFormList
     * @return
     */
    @PostMapping(value = "/favorites/list/rest")
    public ApiResult restMapFavorites(@RequestBody List<SysFavoritesAddForm> favoritesAddFormList){
        Integer userId = Integer.valueOf(request.getHeader("userId"));
        sysFavoritesService.restFavorites(favoritesAddFormList,userId);
        return ApiResult.success();
    }

    /**
     * 撤销收藏
     *
     * @param
     * @return
     */
    @DeleteMapping(value = "/favorites/{bizType}/{bizs}/delete")
    public ApiResult deleteFavorites(@PathVariable("bizType")String bizType,@PathVariable("bizs")List<String> bizs){
        Integer userId = Integer.valueOf(request.getHeader("userId"));
        sysFavoritesService.deleteFavorites(bizType,bizs,userId);
        return ApiResult.success();
    }
}
