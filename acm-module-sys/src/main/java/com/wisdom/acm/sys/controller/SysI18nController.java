package com.wisdom.acm.sys.controller;

import com.wisdom.acm.sys.form.SysI18nAddForm;
import com.wisdom.acm.sys.po.SysI18nPo;
import com.wisdom.acm.sys.service.SysI18nService;
import com.wisdom.acm.sys.vo.SysI18nVo;
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
public class SysI18nController extends BaseController {

    @Autowired
    private SysI18nService sysI18nService;

    @Autowired
    private LoggerService loggerService;

    private String module = "SM-MENU";
    /**
     * 新增国际化
     *
     * @param sysI18nAddForm
     * @return
     */
    @PostMapping(value = "/i18n/add")
    @AddLog(title = "新增国际化" , module = LoggerModuleEnum.SM_MENU)
    public ApiResult addSysI18n(@RequestBody @Valid SysI18nAddForm sysI18nAddForm) {
        SysI18nPo sysI18nPo = sysI18nService.addSysI18n(sysI18nAddForm);
//        loggerService.addAcmLoggerInfo(module,"增加国际化","增加国际化，名称为：\""+sysI18nPo.getCode()+"\"");
        return ApiResult.success(sysI18nService.getSysI18nVoInfo(sysI18nPo.getId()));
    }

    /**
     * 删除国际化
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/i18n/delete")
    @AddLog(title = "删除国际化" , module = LoggerModuleEnum.SM_MENU)
    public ApiResult deleteSysI18n(@RequestBody List<Integer> ids) {
        String names = sysI18nService.queryNamesByIds(ids,"code");
        this.setAcmLogger(new AcmLogger("删除国际化，名称为：\""+names+"\""));
//        loggerService.addAcmLoggerInfo(module,"删除国际化","删除国际化，名称为：\""+names+"\"");
        sysI18nService.deleteSysI18n(ids);
        return ApiResult.success();
    }

    /**
     * 获取国际化
     *
     * @param menuId
     * @return
     */
    @GetMapping(value = "/i18n/{menuId}/list")
    public ApiResult queryI18ns(@PathVariable("menuId") Integer menuId) {
        List<SysI18nVo> list = sysI18nService.querySysI18nVoList(menuId);
        return ApiResult.success(list);
    }

    /**
     * 获取国际化信息
     * @param id
     * @return
     */
    @GetMapping(value = "/i18n/{id}/info")
    public ApiResult getI18nInfo(@PathVariable("id") Integer id) {
        SysI18nVo sysI18nVo = sysI18nService.getSysI18nVoInfo(id);
        return ApiResult.success(sysI18nVo);
    }

}
