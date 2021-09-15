package com.wisdom.acm.base.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.base.form.tmpldoc.BaseTmpldocAddForm;
import com.wisdom.acm.base.form.tmpldoc.BaseTmpldocUpdateForm;
import com.wisdom.acm.base.po.BaseTmpldocPo;
import com.wisdom.acm.base.service.BaseTmplDocService;
import com.wisdom.acm.base.vo.tmpldoc.BaseTmpldocVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tmpldoc")
public class BaseTmplDocController extends BaseController {
    @Autowired
    BaseTmplDocService baseTmplDocService;

    @GetMapping("/list/{currentPageNum}/{pageSize}/{key}")
    public ApiResult querryPageBaseTmplKeyList(@PathVariable(name="currentPageNum") Integer currentPageNum, @PathVariable(name="pageSize") Integer pageSize, @PathVariable(name = "key",required = false) String key) {
        PageInfo<BaseTmpldocVo> pageBaseTmplList = baseTmplDocService.querryTmplDocPageList(pageSize, currentPageNum, key);
        return new TableResultResponse(pageBaseTmplList);
    }

    @GetMapping("/list/{key}")
    public ApiResult querryBaseTmplKeyList(@PathVariable(name = "key",required = false) String key) {
        List<BaseTmpldocVo> pageBaseTmplList = baseTmplDocService.querryTmplDocList(key);
        return ApiResult.success(pageBaseTmplList);
    }

    @GetMapping("/list/{currentPageNum}/{pageSize}")
    public ApiResult querryPageBaseTmplList(@PathVariable(name="currentPageNum") Integer currentPageNum, @PathVariable(name="pageSize") Integer pageSize) {
        String key = "";
        PageInfo<BaseTmpldocVo> pageBaseTmplList = baseTmplDocService.querryTmplDocPageList(pageSize, currentPageNum, key);
        return new TableResultResponse(pageBaseTmplList);
    }

    @GetMapping("/list")
    public ApiResult querryPageBaseTmplList() {
        String key = "";
        List<BaseTmpldocVo> pageBaseTmplList = baseTmplDocService.querryTmplDocList(key);
        return ApiResult.success(pageBaseTmplList);
    }

    @GetMapping("/{id}/info")
    public ApiResult getTmplDocById(@PathVariable("id") Integer tmplDocId) {
        BaseTmpldocVo baseTmpldocVo = baseTmplDocService.getTmplDocById(tmplDocId);
        return ApiResult.success(baseTmpldocVo);
    }

    @AddLog(title = "增加文档",module = LoggerModuleEnum.BM_TMPL_DOC)
    @PostMapping("/add")
    public ApiResult addTmplDoc(@RequestBody @Valid BaseTmpldocAddForm baseTmpldocAddForm){
        BaseTmpldocPo baseTmpldocPo = baseTmplDocService.addTmplDoc(baseTmpldocAddForm);
        BaseTmpldocVo baseTmpldocVo = baseTmplDocService.getTmplDocById(baseTmpldocPo.getId());
        return ApiResult.success(baseTmpldocVo);
    }

    @PutMapping("/update")
    public ApiResult updateTmplDoc(@RequestBody @Valid BaseTmpldocUpdateForm baseTmpldocUpdateForm) {
        BaseTmpldocPo baseTmpldocPo = baseTmplDocService.updateTmplDoc(baseTmpldocUpdateForm);
        BaseTmpldocVo baseTmpldocVo = baseTmplDocService.getTmplDocById(baseTmpldocPo.getId());
        return ApiResult.success(baseTmpldocVo);
    }

    @AddLog(title = "删除文档",module = LoggerModuleEnum.BM_TMPL_DOC)
    @DeleteMapping("/delete")
    public ApiResult deleteTmplDoc(@RequestBody List<Integer> ids){
        String docTitles = baseTmplDocService.queryNamesByIds(ids,"docTitle");
        this.setAcmLogger(new AcmLogger("批量删除文档，文档标题如下："+docTitles));
        baseTmplDocService.deleteTmplDoc(ids);
        return ApiResult.success();
    }
}
