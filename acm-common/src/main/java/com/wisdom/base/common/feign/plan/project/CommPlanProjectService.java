package com.wisdom.base.common.feign.plan.project;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.PageUtil;
import com.wisdom.base.common.vo.plan.project.PlanProjectTreeVo;
import com.wisdom.base.common.vo.plan.project.PlanProjectVo;
import com.wisdom.base.common.vo.plan.project.ProjectAppVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: szc
 * @Date: 2019/7/10 10:06
 * @Version 1.0
 */
@FeignClient(value = "acm-module-plan",configuration = FeignConfiguration.class)
public interface CommPlanProjectService {

    default PlanProjectVo getProject(Integer id){
        ApiResult<PlanProjectVo> apiResult = this.get_(id);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @PostMapping("project/id")
    ApiResult<PlanProjectVo> get_(@RequestBody Integer id);


    default PageInfo<ProjectAppVo> queryProjectList(Integer pageSize,Integer currentPageNum,String key){
        ApiResult<List<PlanProjectTreeVo>> apiResult = this.queryProjectListByUser_(key);
        if(apiResult.getStatus() == 200){
            List<PlanProjectTreeVo> projectTreeVos =  apiResult.getData();
            List<ProjectAppVo> projectAppVos = new ArrayList<>();               //返回值
            //遍历项目列表
            for (PlanProjectTreeVo projectTreeVo : projectTreeVos){
                ProjectAppVo projectAppVo = new ProjectAppVo();
                projectAppVo.setId(projectTreeVo.getId());          //id
                projectAppVo.setName(projectTreeVo.getName());      //项目名称
                projectAppVos.add(projectAppVo);
            }
            PageInfo<ProjectAppVo> pageInfo = PageUtil.getPageInfo(projectAppVos,pageSize,currentPageNum);
//            PageInfo<ProjectAppVo> pageInfo = new PageInfo<>(projectAppVos);
            return pageInfo;
//            return PageUtil.getPageInfo(projectAppVos,currentPageNum,pageSize);
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @GetMapping("project/user/auth/list")
    ApiResult<List<PlanProjectTreeVo>> queryProjectListByUser_(@RequestParam(value="key") String key);
}
