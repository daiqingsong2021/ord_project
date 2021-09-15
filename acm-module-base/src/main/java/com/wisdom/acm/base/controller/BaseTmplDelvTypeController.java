package com.wisdom.acm.base.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.base.form.tmpldelv.BaseTmpldelvTypeAddForm;
import com.wisdom.acm.base.form.tmpldelv.BaseTmpldelvTypeUpdateForm;
import com.wisdom.acm.base.po.BaseTmpldelvTypePo;
import com.wisdom.acm.base.service.BaseTmplDelvService;
import com.wisdom.acm.base.service.BaseTmplDelvTypeService;
import com.wisdom.acm.base.vo.tmpldelv.BaseTmpldelvTypeVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tmpldelv")
public class BaseTmplDelvTypeController extends BaseController {
    @Autowired
    BaseTmplDelvTypeService baseTmplDelvTypeService;

    @Autowired
    BaseTmplDelvService baseTmplDelvService;

    @GetMapping("/list/{currentPageNum}/{pageSize}")
    public ApiResult querryPageBaseTmplList(HttpServletRequest request, @PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum) {
        String key = request.getParameter("searcher");
        PageInfo<BaseTmpldelvTypeVo> pageBaseTmplList = baseTmplDelvTypeService.querryTmpldelvTypePageList(pageSize, currentPageNum,key);
        return new TableResultResponse(pageBaseTmplList);
    }

    @GetMapping("/{id}/info")
    public ApiResult getTmpldelvTypeById(@PathVariable("id") Integer tmpldelvTypeId) {
        BaseTmpldelvTypeVo baseTmpldelvTypeVo = baseTmplDelvTypeService.getTmpldelvTypeById(tmpldelvTypeId);
        return ApiResult.success(baseTmpldelvTypeVo);
    }

    @AddLog(title = "增加交付模板",module = LoggerModuleEnum.BM_TMPL_DELV,initContent = true)
    @PostMapping("/add")
    public ApiResult addTmpldelvType(@RequestBody @Valid BaseTmpldelvTypeAddForm baseTmpldelvTypeAddForm){
        this.setAcmLogger(new AcmLogger("增加交付模板,标题为:"+baseTmpldelvTypeAddForm.getTypeTitle()));
        BaseTmpldelvTypePo baseTmpldelvTypePo = baseTmplDelvTypeService.addTmpldelvType(baseTmpldelvTypeAddForm);
        BaseTmpldelvTypeVo baseTmpldelvTypeVo = baseTmplDelvTypeService.getTmpldelvTypeById(baseTmpldelvTypePo.getId());
        return ApiResult.success(baseTmpldelvTypeVo);
    }

    @PutMapping("/update")
    public ApiResult updateTmpldelvType(@RequestBody @Valid BaseTmpldelvTypeUpdateForm baseTmpldelvTypeUpdateForm) {
        BaseTmpldelvTypePo baseTmpldelvTypePo = baseTmplDelvTypeService.updateTmpldelvType(baseTmpldelvTypeUpdateForm);
        BaseTmpldelvTypeVo baseTmpldelvTypeVo = baseTmplDelvTypeService.getTmpldelvTypeById(baseTmpldelvTypePo.getId());
        return ApiResult.success(baseTmpldelvTypeVo);
    }

    @AddLog(title = "删除交付模板",module = LoggerModuleEnum.BM_TMPL_DELV,initContent = true)
    @DeleteMapping("/delete")
    public ApiResult deleteTmpldelvType(@RequestBody List<Integer> ids){
        String typeTitles = baseTmplDelvTypeService.queryNamesByIds(ids,"typeTitle");
        this.setAcmLogger(new AcmLogger("批量删除交付模板，标题如下："+typeTitles));
        baseTmplDelvService.deleteTmpldelvType(ids);
        return ApiResult.success();
    }
}
