package com.wisdom.acm.base.controller;

import com.wisdom.acm.base.form.classify.BaseClassifyAddForm;
import com.wisdom.acm.base.form.classify.BaseClassifyUpdateForm;
import com.wisdom.acm.base.po.BaseClassifyPo;
import com.wisdom.acm.base.service.BaseBoService;
import com.wisdom.acm.base.service.BaseClassifyService;
import com.wisdom.acm.base.vo.classify.BaseClassifyTreeVo;
import com.wisdom.acm.base.vo.classify.BaseClassifyVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/classify")
public class BaseClassifyController extends BaseController {

    @Autowired
    private BaseClassifyService classifyService;

    @Autowired
    private BaseBoService boService;

    @Autowired
    protected org.dozer.Mapper dozerMapper;

    /**
     * 增加分类码
     * @param classifyAddForm
     * @return
     */
    @PostMapping(value = "/add")
    @AddLog(title = "增加分类码",module = LoggerModuleEnum.BM_CLASSIFY)
    public ApiResult addClassify(@RequestBody @Valid BaseClassifyAddForm classifyAddForm) {
        classifyAddForm.setClassifyType(1);
        this.setAcmLogger(new AcmLogger("新增分类码，分类码名称：" + classifyAddForm.getClassifyCode()));
        BaseClassifyPo classifyPo = classifyService.addClassify(classifyAddForm);
        BaseClassifyTreeVo classifyVo = dozerMapper.map(classifyPo, BaseClassifyTreeVo.class);
        return ApiResult.success(classifyVo);
    }

    /**
     * 增加分类码码值
     *
     * @param classifyAddForm
     * @return
     */
    @PostMapping(value = "/value/add")
    @AddLog(title = "增加分类码码值",module = LoggerModuleEnum.BM_CLASSIFY)
    public ApiResult addClassifyValue(@RequestBody @Valid BaseClassifyAddForm classifyAddForm) {
        classifyAddForm.setClassifyType(2);
        this.setAcmLogger(new AcmLogger("新增分类码码值，码值名称：" + classifyAddForm.getClassifyCode()));
        BaseClassifyPo classifyPo = classifyService.addClassify(classifyAddForm);
        BaseClassifyTreeVo classifyVo = dozerMapper.map(classifyPo, BaseClassifyTreeVo.class);
        return ApiResult.success(classifyVo);
    }

    /**
     * 获取分类码列表
     *
     * @return
     */
    @GetMapping(value = "/{boCode}/list")
    public ApiResult queryClassifyList(@PathVariable("boCode")String boCode) {
        List<BaseClassifyTreeVo> retList = classifyService.queryClassifyListByBoCode(boCode);
        return ApiResult.success(retList);
    }

    /**
     * 获取分类码列表
     *
     * @return
     */
    @GetMapping(value = "/{boCode}/{bizId}/list")
    public ApiResult queryClassifyListByBizId(@PathVariable("boCode")String boCode,@PathVariable("bizId")Integer bizId) {
        List<BaseClassifyTreeVo> retList = classifyService.queryClassifyListByBoCodeId(boCode,bizId);
        return ApiResult.success(retList);
    }

    /**
     * 修改分类码列表
     * @param classifyUpdateForm
     * @return
     */
    @PutMapping(value = "/update")
    public ApiResult updateClassify(@RequestBody @Valid BaseClassifyUpdateForm classifyUpdateForm) {
        BaseClassifyPo classifyPo = classifyService.updateClassify(classifyUpdateForm);
        BaseClassifyTreeVo classifyVo = dozerMapper.map(classifyPo, BaseClassifyTreeVo.class);
        return ApiResult.success(classifyVo);
    }


    /**
     * 修改分类码码值
     *
     * @param classifyUpdateForm
     * @return
     */
    @PutMapping(value = "/value/update")
    public ApiResult updateClassifyValue(@RequestBody @Valid BaseClassifyUpdateForm classifyUpdateForm) {
        BaseClassifyPo classifyPo = classifyService.updateClassify(classifyUpdateForm);
        BaseClassifyTreeVo classifyVo = dozerMapper.map(classifyPo, BaseClassifyTreeVo.class);
        return ApiResult.success(classifyVo);
    }

    /**
     * 查询数据字典的基本信息
     * @param classifyId
     * @return
     */
    @GetMapping(value = "/{classifyId}/info")
    public ApiResult queryClassifyInfo(@PathVariable("classifyId")int classifyId) {
        BaseClassifyVo classifyVo = classifyService.getClassifyInfo(classifyId);
        return ApiResult.success(classifyVo);
    }

    /**
     * 删除分类码/码值
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/delete")
    @AddLog(title = "删除分类码/码值",module = LoggerModuleEnum.BM_CLASSIFY)
    public ApiResult deleteClassify(@RequestBody List<Integer> ids) {
        //添加日志
        String names = classifyService.queryNamesByIds(ids,"classifyName");
        this.setAcmLogger(new AcmLogger("批量删除分类码，分类码名称如下：" + names));

        classifyService.deleteClassify(ids);
        return ApiResult.success();
    }

    /**
     * 获取分类码列表
     *
     * @return
     */
    @GetMapping(value = "/bo/list")
    public ApiResult queryclassifyBoList() {
        return ApiResult.success (boService.queryBoList(2));
    }

    /**
     * 获取分类码码值树形列表
     * @param classifyId
     * @return
     */
    @GetMapping(value = "/{classifyId}/value/tree")
    public ApiResult queryClassifyValueList(@PathVariable("classifyId")Integer classifyId){
        List<BaseClassifyTreeVo> list = classifyService.queryClassifyValueList(classifyId);
        return ApiResult.success(list);
    }
}
