package com.wisdom.acm.wf.controller;

import com.wisdom.acm.wf.form.WfTypeAddForm;
import com.wisdom.acm.wf.form.WfTypeSearchForm;
import com.wisdom.acm.wf.form.WfTypeUpdateForm;
import com.wisdom.acm.wf.po.WfBizTypePo;
import com.wisdom.acm.wf.service.WfBizTypeService;
import com.wisdom.acm.wf.vo.WfBizTypeVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/biz/type")
public class WfBizTypeController extends BaseController {

    @Autowired
    private WfBizTypeService wfBizTypeService;

    /**
     * 查询流程业务定义列表
     * @return
     */
    @GetMapping(value = "/list")
    public ApiResult queryWfTypeList(){
        List<WfBizTypeVo> wfTypePos = wfBizTypeService.queryWfTypeList();
        return ApiResult.success(wfTypePos);
    }

    /**
     * 增加业务流程定义
     * @param wfTypeAddForm
     * @return
     */
    @AddLog(title = "增加流程业务定义",module = LoggerModuleEnum.WM_BUSSI ,initContent = true)
    @PostMapping(value = "/add")
    public ApiResult addWfType(@RequestBody WfTypeAddForm wfTypeAddForm){
        WfBizTypePo wfBizTypePo = wfBizTypeService.addWfType(wfTypeAddForm);
        WfBizTypeVo wfBizTypeVo = wfBizTypeService.selectWfTypeVo(wfBizTypePo.getId());
        return ApiResult.success(wfBizTypeVo);
    }

    /**
     * 业务基本信息
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}/info")
    public ApiResult queryWfTypeInfo(@PathVariable("id") Integer id){
        WfBizTypeVo wfBizTypeVo = wfBizTypeService.selectWfTypeVo(id);
        return ApiResult.success(wfBizTypeVo);
    }

    /**
     * 业务基本信息
     * @param procInstId
     * @return
     */
    @GetMapping(value = "/procinst/{procInstId}/info")
    public ApiResult queryWfTypeInfoByProcInstId(@PathVariable("procInstId") String procInstId){
        WfBizTypeVo wfBizTypeVo = wfBizTypeService.getWfTypeInfoByProcInstId(procInstId);
        return ApiResult.success(wfBizTypeVo);
    }

    /**
     * 修改流程业务定义
     * @param wfTypeUpdateForm
     * @return
     */
    @PutMapping(value = "/update")
    public ApiResult updateWfType (@RequestBody WfTypeUpdateForm wfTypeUpdateForm){
        WfBizTypePo wfBizTypePo = wfBizTypeService.updateWfType(wfTypeUpdateForm);
        WfBizTypeVo wfBizTypeVo = wfBizTypeService.selectWfTypeVo(wfBizTypePo.getId());
        return ApiResult.success(wfBizTypeVo);
    }

    /**
     * 删除流程业务定义
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}/delete")
    @AddLog(title = "删除流程业务定义",module = LoggerModuleEnum.WM_BUSSI)
    public ApiResult deleteWfType(@PathVariable("id") Integer id){
        WfBizTypePo wfBizTypePo = wfBizTypeService.selectById(id);
        this.setAcmLogger(new AcmLogger("删除流程业务定义，业务名称：" + wfBizTypePo.getTypeName()));
        wfBizTypeService.deleteWfType(id);
        return ApiResult.success();
    }

    /**
     * 搜索流程业务定义
     * @param wfTypeSearchForm
     * @return
     */
    @GetMapping(value = "/search")
    public ApiResult searchWfType(WfTypeSearchForm wfTypeSearchForm){
        List<WfBizTypeVo> wfBizTypeVos = wfBizTypeService.queryWfTypeBySearch(wfTypeSearchForm);
        return ApiResult.success(wfBizTypeVos);
    }

}
