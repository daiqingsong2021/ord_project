package com.wisdom.base.common.feign.plan;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.ProjectInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "acm-module-plan",configuration = FeignConfiguration.class)
public interface CommPlanService {


    /**
     * 根据项目ID 查询项目信息
     * @param projectId 项目ID
     * @return
     */
    default ProjectInfoVo getProjectInfo(Integer projectId){
        ApiResult<ProjectInfoVo> apiResult = this.getProjectInfo_(projectId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping(value = "/project/{id}")
    ApiResult<ProjectInfoVo> getProjectInfo_(@RequestBody @PathVariable("id") Integer id);



}
