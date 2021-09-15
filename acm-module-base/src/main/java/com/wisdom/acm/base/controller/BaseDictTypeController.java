package com.wisdom.acm.base.controller;

import com.wisdom.acm.base.form.dict.BaseDictTypeAddForm;
import com.wisdom.acm.base.form.dict.BaseDictTypeUpdateForm;
import com.wisdom.acm.base.po.BaseDictTypePo;
import com.wisdom.acm.base.service.BaseDictTypeService;
import com.wisdom.acm.base.vo.dict.BaseDictTypeInfoVo;
import com.wisdom.acm.base.vo.dict.BaseDictTypeVo;
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
@RequestMapping("/dictType")
public class BaseDictTypeController extends BaseController {

    @Autowired
    private BaseDictTypeService dictTypeService;

    @Autowired
    protected org.dozer.Mapper dozerMapper;

    /**
     * 增加数字字典
     * @param baseDictTypeAddForm
     * @return
     */
    @PostMapping(value = "/add")
    @AddLog(title = "增加数字字典" , module = LoggerModuleEnum.BM_DICT)
    public ApiResult addDictType(@RequestBody @Valid BaseDictTypeAddForm baseDictTypeAddForm) {
        BaseDictTypePo baseDictTypePo = dictTypeService.addDictType(baseDictTypeAddForm);
        BaseDictTypeVo baseDictTypeVo = dozerMapper.map(baseDictTypePo, BaseDictTypeVo.class);
        return ApiResult.success(baseDictTypeVo);
    }

    /**
     * 获取数字字典列表
     *
     * @return
     */
    @GetMapping(value = "/{boCode}/list")
    public ApiResult queryDictTypeList(@PathVariable("boCode")String boCode) {
        List<BaseDictTypeVo> retList = dictTypeService.queryDictTypeListByDictCode(boCode);
        return ApiResult.success(retList);
    }

    /**
     * 获取数字字典列表
     *
     * @return
     */
    @GetMapping(value = "/list")
    public ApiResult queryDictTypeList() {
        List<BaseDictTypeVo> retList = dictTypeService.selectDictTypeDateList();
        return ApiResult.success(retList);
    }

    /**
     * 修改数字字典列表
     * @param baseDictTypeUpdateForm
     * @return
     */
    @PutMapping(value = "/update")
    public ApiResult updateDictType(@RequestBody @Valid BaseDictTypeUpdateForm baseDictTypeUpdateForm) {
        BaseDictTypePo baseDictTypePo = dictTypeService.updateDictType(baseDictTypeUpdateForm);
        BaseDictTypeVo baseDictTypeVo = dozerMapper.map(baseDictTypePo, BaseDictTypeVo.class);
        return ApiResult.success(baseDictTypeVo);
    }

    /**
     * 查询数据字典的基本信息
     * @param dictTypeId
     * @return
     */
    @GetMapping(value = "/{dictTypeId}/info")
    public ApiResult queryDictTypeInfo(@PathVariable("dictTypeId")int dictTypeId) {
        BaseDictTypeInfoVo baseDictTypeInfoVo = dictTypeService.getDictTypeInfo(dictTypeId);
        return ApiResult.success(baseDictTypeInfoVo);
    }

    /**
     * 删除数字字典
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/delete")
    @AddLog(title = "删除数字字典" , module = LoggerModuleEnum.BM_DICT)
    public ApiResult deleteDictType(@RequestBody List<Integer> ids) {
        //添加日志
        String names = dictTypeService.queryNamesByIds(ids,"typeName");
        this.setAcmLogger(new AcmLogger("批量删除数据字典，字典名称如下：" + names));

        dictTypeService.deleteDictType(ids);
        return ApiResult.success();
    }
}
