package com.wisdom.acm.wf.controller;

import com.wisdom.acm.wf.form.WfAssignAddForm;
import com.wisdom.acm.wf.form.WfAssignUpdateForm;
import com.wisdom.acm.wf.po.WfBizTypePo;
import com.wisdom.acm.wf.service.WfAssignService;
import com.wisdom.acm.wf.service.WfBizTypeService;
import com.wisdom.acm.wf.vo.WfAssignVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.feign.CommActivitiService;
import com.wisdom.base.common.form.ActModelAddForm;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.FormatUtil;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.ActModelVo;
import com.wisdom.base.common.vo.wf.WfProcessDefVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags="流程业务实例服务")
public class WfAssignController extends BaseController {

    @Autowired
    private WfAssignService wfAssignService;

    @Autowired
    private WfBizTypeService wfBizTypeService;

    @Autowired
    private CommActivitiService commActivitiService;

    /**
     * 查询流程定义列表
     * @return
     */
    @GetMapping(value = "/assign/list")
    public ApiResult queryWfProcessList(){
        List<WfAssignVo> wfAssignVoList = wfAssignService.queryWfAssignList();
        return ApiResult.success(wfAssignVoList);
    }

    /**
     * 增加流程定义
     * @param wfAssignAddForm
     * @return
     */
    @PostMapping(value = "/assign/add")
    public ApiResult addWfProcess(@RequestBody WfAssignAddForm wfAssignAddForm){
        return ApiResult.success();
    }

    /**
     * 获取流程定义
     * @return
     */
    @GetMapping(value = "/assign/{id}/info")
    public ApiResult getWfProcessInfo(){
        return ApiResult.success();
    }

    /**
     * 修改流程定义
     * @param wfAssignUpdateForm
     * @return
     */
    @PutMapping(value = "/assign/update")
    public ApiResult updateWfProcess (@RequestBody WfAssignUpdateForm wfAssignUpdateForm){
        return ApiResult.success();
    }

    /**
     * 删除流程业务定义
     * @param id
     * @return
     */
    @DeleteMapping(value = "/assign/{id}/delete")
    public ApiResult deleteWfProcess(@PathVariable("id") Integer id){
        return ApiResult.success();
    }

    /**
     * activity获取新模版id
     * @return
     */
    @PostMapping(value = "/models/new/model")
    public ApiResult addActivityNewModel(@RequestBody ActModelAddForm actModelAddForm){
        WfBizTypePo wfBizTypePo = wfBizTypeService.selectById(actModelAddForm.getParentId());
        actModelAddForm.setCategory(wfBizTypePo.getTypeCode());
        WfAssignVo wfAssignVo = wfAssignService.addActivityNewModel(actModelAddForm);
        //日志
        List<ActModelVo> actModelVos = commActivitiService.queryWfAssignList(ListUtil.toArrayList(FormatUtil.toString(wfAssignVo.getId()))).getData();
        if (!ObjectUtils.isEmpty(actModelVos)){
            this.setAcmLogger(new AcmLogger("\""+wfBizTypePo.getTypeName()+"\"分配了一个模板,模板名称："+actModelVos.get(0).getName()));
        }
        //绑定流程
        WfAssignAddForm wfAssignAddForm = new WfAssignAddForm();
        wfAssignAddForm.setTypeId(wfAssignVo.getParentId());
        wfAssignAddForm.setModelId(FormatUtil.toString(wfAssignVo.getId()));
        wfAssignService.assignBussiNewModel(wfAssignAddForm);
        return ApiResult.success(wfAssignVo);
    }

    /**
     * 发布模版
     * @param modelId
     * @return
     */
    @PostMapping(value = "/assign/release/{modelId}/model")
    @AddLog(title = "流程定义发布模版" , module = LoggerModuleEnum.WM_DEFINE)
    public ApiResult releaseBussiNewModel(@PathVariable("modelId") String modelId){
        List<ActModelVo> actModelVos = commActivitiService.queryWfAssignList(ListUtil.toArrayList(modelId)).getData();
        if (!ObjectUtils.isEmpty(actModelVos)){
            this.setAcmLogger(new AcmLogger("发布模版,模板名称："+actModelVos.get(0).getName()));
        }
        WfAssignVo wfAssignVo = wfAssignService.releaseBussiNewModel(modelId);
        return ApiResult.success(wfAssignVo);
    }

    /**
     * 删除模版
     * @param modelId
     * @return
     */
    @PostMapping(value = "/assign/delete/{modelId}/model")
    @AddLog(title = "流程定义删除模版" , module = LoggerModuleEnum.WM_DEFINE)
    public ApiResult deleteBussiNewModel(@PathVariable("modelId") String modelId){
        List<ActModelVo> actModelVos = commActivitiService.queryWfAssignList(ListUtil.toArrayList(modelId)).getData();
        if (!ObjectUtils.isEmpty(actModelVos)){
            this.setAcmLogger(new AcmLogger("删除模版,模板名称："+actModelVos.get(0).getName()));
        }
        String retMsg = wfAssignService.deleteBussiNewModel(modelId);
        return ApiResult.success(retMsg);
    }

    @ApiOperation(value="根据业务类型获取流程定义")
    @ApiImplicitParams({@ApiImplicitParam(name="typeCode",value= "业务类型")})
    @GetMapping(value = "/assign/{typeCode}/models")
    public ApiResult<WfProcessDefVo> getWfProcessInfo(@PathVariable("typeCode") String typeCode){
        List<WfProcessDefVo> actModelVoList = wfAssignService.getAllModelByTypeId(typeCode);
        return ApiResult.success(actModelVoList);
    }
}
