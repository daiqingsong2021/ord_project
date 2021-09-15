package com.wisdom.base.common.feign.sys;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.sys.SectionProjectVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 项目标段
 */
@FeignClient(value = "acm-module-sys",configuration = FeignConfiguration.class)
public interface CommSectionService {

    /**
     * 获取用户拥有权限的所有标段IDS
     * @param userId 用户ID
     * @return
     */
    default List<Integer> querySectioinIdsByUserId(Integer userId){
        ApiResult<List<SectionProjectVo>> apiResult = this.querySectioinListByUserId_(userId);
        if(apiResult.getStatus() == 200){
            List<SectionProjectVo> vos = apiResult.getData();
            if (!ObjectUtils.isEmpty(vos)){
                return ListUtil.toIdList(vos);
            }
            return null;
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    /**
     * 获取标段项目信息
     * @param userId 用户ID
     * @return
     */
    default List<SectionProjectVo> querySectioinListByUserId(Integer userId){
        ApiResult<List<SectionProjectVo>>  apiResult = this.querySectioinListByUserId_(userId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }


    /**
     * 根据登陆人获取标段id集合
     * @param projectId
     * @return
     */
    default List<Integer> querySectionIdListList(Integer projectId){
        ApiResult<List<Integer>> apiResult = this.querySectionIdListList_(projectId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    /**
     * 获取标段项目信息
     * @param userId 用户ID
     * @return
     */
    @GetMapping(value = "/section/project/list/{userId}")
    ApiResult<List<SectionProjectVo>> querySectioinListByUserId_(@PathVariable(name = "userId") Integer userId);


    /**
     * 根据用户id获取标段id集合
     * @param projectId
     * @return
     */
    @GetMapping(value = "/section/idList/{projectId}")
    ApiResult<List<Integer>> querySectionIdListList_(@PathVariable(name = "projectId") Integer projectId);


}
