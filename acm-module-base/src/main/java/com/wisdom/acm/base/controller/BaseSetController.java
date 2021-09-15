package com.wisdom.acm.base.controller;

import com.wisdom.acm.base.form.set.BaseSetDocUpdateForm;
import com.wisdom.acm.base.form.set.BaseSetProjectUpdateForm;
import com.wisdom.acm.base.form.set.BaseSetTimeUdateForm;
import com.wisdom.acm.base.po.BaseSetPo;
import com.wisdom.acm.base.service.BaseSetService;
import com.wisdom.acm.base.vo.BaseBoVo;
import com.wisdom.acm.base.vo.set.BaseSetBoVo;
import com.wisdom.acm.base.vo.set.BaseSetDocVo;
import com.wisdom.acm.base.vo.set.BaseSetProjectVo;
import com.wisdom.acm.base.vo.set.BaseSetTimeVo;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/set")
public class BaseSetController extends BaseController {

    @Autowired
    private BaseSetService baseSetService;

    /**
     * 修改项目全局设置
     *
     * @param baseSetForm
     * @return
     */
    @PostMapping(value = "/project/update")
    public ApiResult updateProject(@RequestBody BaseSetProjectUpdateForm baseSetForm) {
        // boType: project
        baseSetService.updateProject(baseSetForm);
        return ApiResult.success();
    }

    /**
     * 修改文档全局设置
     *
     * @param baseSetForm
     * @return
     */
    @PostMapping(value = "/doc/update")
    public ApiResult updateDoc(@RequestBody BaseSetDocUpdateForm baseSetForm) {
        // boType: doc
        baseSetService.updateDoc(baseSetForm);
        return ApiResult.success();
    }

    /**
     * 修改时间设置
     *
     * @param baseSetForm
     * @return
     */
    @PostMapping(value = "/time/update")
    public ApiResult updateTime(@RequestBody BaseSetTimeUdateForm baseSetForm) {
        // boType: time
        baseSetService.updateTime(baseSetForm);
        return ApiResult.success();
    }

    /**
     * 修改客车运营时间(与定时器连用)
     * @param dates
     * @return
     */
    @PostMapping(value = "/time/update/train")
    public ApiResult updateTrainTime(@RequestBody Map<String,String> dates) {
        // boType: train
        baseSetService.updateTrainTime(dates);
        return ApiResult.success();
    }


    /**
     * 查询客车运营时间
     * @return
     */
    @GetMapping(value = "/time/train/info")
    public ApiResult<List<BaseBoVo>> getTrainTime() {
        // BaseBoVo
        List<BaseBoVo> baseBoVos = baseSetService.getTrainTime();
        return ApiResult.success(baseBoVos);
    }

    /**
     * 项目全局设置信息
     *
     * @return
     */
    @GetMapping(value = "/project/info")
    public ApiResult getProjectInfo() {
        // BaseSetProjectVo
        BaseSetProjectVo baseSetProjectVo = baseSetService.getProjectInfo();
        return ApiResult.success(baseSetProjectVo);
    }

    /**
     * 文档全局设置信息
     *
     * @return
     */
    @GetMapping(value = "/doc/info")
    public ApiResult getDocInfo() {
        // BaseSetDocVo
        BaseSetDocVo baseSetDocVo = baseSetService.getDocInfo();

        return ApiResult.success(baseSetDocVo);
    }

    /**
     * 时间全局设置信息
     *
     * @return
     */
    @GetMapping(value = "/time/info")
    public ApiResult getTimeInfo() {
        // BaseSetTimeVo
        BaseSetTimeVo baseSetTimeVo = baseSetService.getTimeInfo();
        return ApiResult.success(baseSetTimeVo);
    }

    @GetMapping("/bo/list")
    public ApiResult queryBoList() {
        List<BaseSetBoVo> list = baseSetService.queryBoList();
        return ApiResult.success(list);
    }

    /**
     * 时间全局设置信息
     *
     * @return
     */
    @GetMapping(value = "/{boCode}/{bsKey}/info")
    public ApiResult getTimeInfo(@PathVariable("boCode")String boCode, @PathVariable("bsKey")String bsKey) {
        // BaseSetTimeVo
        BaseSetPo setPo = baseSetService.getBaseSetPoByBoCodeAndKey(boCode, bsKey);
        String value = "";
        if(setPo != null){
            value = setPo.getBsValue();
        }
        return ApiResult.success(value);
    }
}
