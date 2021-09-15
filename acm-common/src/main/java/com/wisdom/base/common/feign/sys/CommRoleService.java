package com.wisdom.base.common.feign.sys;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.RoleVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "acm-module-sys",configuration = FeignConfiguration.class)
public interface CommRoleService {

    /**
     * 根据用户ID和组织ID查找用户
     * @param orgId
     * @param userId
     * @return
     */
    default List<RoleVo> queryRoleList(Integer orgId, Integer userId){
        ApiResult<List<RoleVo>> apiResult = this.queryRoleList_(orgId, userId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * 根据用户ID和组织ID查找用户
     * @param orgId
     * @param userId
     * @return
     */
    @RequestMapping(value = "/role/{orgId}/{userId}/info", method = RequestMethod.GET)
    ApiResult<List<RoleVo>> queryRoleList_(@PathVariable("orgId") Integer orgId, @PathVariable("userId") Integer userId);

}
