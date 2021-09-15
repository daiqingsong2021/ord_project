package com.wisdom.acm.sys.controller;

import com.wisdom.acm.sys.form.SysAuthAddForm;
import com.wisdom.acm.sys.form.SysAuthUpdateForm;
import com.wisdom.acm.sys.service.SysFuncService;
import com.wisdom.acm.sys.vo.SysFuncVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.feign.LoggerService;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "func")
public class SysFuncController extends BaseController {

    @Autowired
    private SysFuncService funcService;

    /**
     * 新增权限配置
     *
     * @param sysAuthAddForm
     * @return
     */
    @PostMapping(value = "/add")
    @AddLog(title = "新增权限配置" , module = LoggerModuleEnum.SM_MENU)
    public ApiResult addMenuFunc(@RequestBody @Valid SysAuthAddForm sysAuthAddForm) {
        SysFuncVo sysFuncVo = funcService.addMenuFunc(sysAuthAddForm);
        return ApiResult.success(sysFuncVo);
    }

    /**
     * 修改权限配置
     *
     * @param sysAuthAddForm
     * @return
     */
    @PutMapping(value = "/updatefunc")
    public ApiResult updateMenuFunc(@RequestBody @Valid SysAuthUpdateForm sysAuthAddForm) {
        SysFuncVo sysFuncVo = funcService.updateMenuFunc(sysAuthAddForm);
        return ApiResult.success(sysFuncVo);
    }

    /**
     * 删除权限配置
     *
     * @param funcIds
     * @return
     */
    @DeleteMapping(value = "/delete")
    @AddLog(title = "删除权限配置" , module = LoggerModuleEnum.SM_MENU)
    public ApiResult deleteMenuFunc(@RequestBody List<Integer> funcIds) {
        String names = funcService.queryNamesByIds(funcIds,"funcName");
        this.setAcmLogger(new AcmLogger("删除权限，名称为：\""+names+"\""));
        funcService.deleteMenuFunc(funcIds);
        return ApiResult.success();
    }

    /**
     * 获取权限配置
     *
     * @param menuId
     * @return
     */
    @GetMapping(value = "/{menuId}/funcs")
    public ApiResult queryFuncs(@PathVariable("menuId")Integer menuId) {
        List<SysFuncVo> list = funcService.queryFuncVoById(menuId);
        return ApiResult.success(list);
    }

    /**
     * 获取权限配置信息
     *
     * @param funcId
     * @return
     */
    @GetMapping(value = "/{funcId}/funcinfo")
    public ApiResult getFuncInfo(@PathVariable("funcId")Integer funcId) {
        List<SysFuncVo> list = funcService.getFuncInfo(funcId);
        return ApiResult.success(list);
    }

}
