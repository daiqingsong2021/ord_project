package com.wisdom.base.common.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * @Author: szc
 * @Date: 2019/5/23 9:44
 * @Version 1.0
 */
@FeignClient(value = "acm-module-sys",configuration = FeignConfiguration.class)
public interface ProjectTeamService {

    /**
     *  删除业务数据时删除项目团队
     * @param bizId
     * @param bizType
     * @return
     */
    default ApiResult deleteByBiz(Integer bizId, String bizType){
        ApiResult apiResult = this.deleteByBiz_(bizType, bizId);
        if(apiResult.getStatus() == 200){
            return apiResult;
        }
        return null;
    }

    @DeleteMapping("/projectteam/delete/{bizType}/{bizId}")
    ApiResult deleteByBiz_(@PathVariable("bizType") String bizType,@PathVariable("bizId") Integer bizId);

    @PostMapping(value = "/projectteam/{sourceBizType}/{sourceBizId}/{targetBizType}/{targetBizId}/copy")
    ApiResult copyProjectTeam(@PathVariable("sourceBizType") String sourceBizType, @PathVariable("sourceBizId") Integer sourceBizId, @PathVariable("targetBizType") String targetBizType,
                                     @PathVariable("targetBizId") Integer targetBizId);
}
