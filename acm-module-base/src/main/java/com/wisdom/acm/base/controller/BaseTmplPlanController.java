package com.wisdom.acm.base.controller;

import com.wisdom.acm.base.service.BaseTmplPlanService;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplPlanVo;
import com.wisdom.base.common.feign.CommOrgService;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.SelectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
public class BaseTmplPlanController {

    @Autowired
    private BaseTmplPlanService baseTmplPlanService;

    @Autowired
    private CommUserService userService;

    @Autowired
    private CommOrgService orgService;

    /**
     * 获取导入计划模板下拉列表
     *
     * @return
     */
    @GetMapping(value = "/tmpl/plan/selectList")
    public ApiResult queryTmplPlanSelectList() {
        Integer userId = userService.getLoginUser().getId();

        List<SelectVo> retSelectVos =  baseTmplPlanService.queryTmplPlanSelectList(userId);

        return ApiResult.success(retSelectVos);
    }

}
