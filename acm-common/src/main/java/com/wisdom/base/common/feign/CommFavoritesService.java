package com.wisdom.base.common.feign;


import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.ListUtil;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@FeignClient(value = "acm-module-sys",configuration = FeignConfiguration.class)
public interface CommFavoritesService {

    /**
     * 根据类型，获取用户收藏的信息
     *
     * @param bizTypes
     * @return
     */
    default Map<String,List<String>> getFavoritesMapByUserAndBizTypes(Integer userId,List<String> bizTypes){
        String bizTypeStr = ListUtil.toStr(bizTypes);
        // 根据类型，获取用户收藏的信息
        ApiResult<Map<String,List<String>>> apiResult = this.queryFavoritesListMap(userId,bizTypeStr);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return new HashMap<>();
    }

    /**
     * 根据类型获取当前登陆用户的收藏信息
     *
     * @param userId
     * @param bizTypes
     * @return
     */
    @GetMapping(value = "/favorites/{userId}/{bizTypes}/list/map")
    ApiResult<Map<String,List<String>>> queryFavoritesListMap(@PathVariable("userId") Integer userId, @PathVariable("bizTypes") String bizTypes);

}
