package com.wisdom.acm.szxm.controller.app;

import com.github.pagehelper.PageInfo;
import com.wisdom.base.common.vo.plan.project.ProjectAppVo;
import com.wisdom.base.common.feign.plan.project.CommPlanProjectService;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.vo.plan.project.PlanProjectTreeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("plan/app")
public class ProjectAppController {

    @Autowired
    private CommPlanProjectService commPlanProjectService;

    @GetMapping("/common/project/{size}/{current}/list")
    public ApiResult queryProjectList(@PathVariable("size") Integer pageSize,@PathVariable("current") Integer currentPageNum,String key) throws IOException {
        PageInfo<ProjectAppVo> projectAppVoList = commPlanProjectService.queryProjectList(pageSize,currentPageNum,key);
        return new TableResultResponse(projectAppVoList);
    }
    
}
