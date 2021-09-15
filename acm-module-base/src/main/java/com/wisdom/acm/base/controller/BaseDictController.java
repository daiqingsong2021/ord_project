package com.wisdom.acm.base.controller;

import com.wisdom.acm.base.form.dict.BaseDictAddForm;
import com.wisdom.acm.base.form.dict.BaseDictUpdateForm;
import com.wisdom.acm.base.po.BaseDictPo;
import com.wisdom.acm.base.service.BaseDictService;
import com.wisdom.acm.base.vo.dict.BaseDictTreeVo;
import com.wisdom.acm.base.vo.dict.BaseDictVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.DictionarysMap;
import com.wisdom.base.common.vo.SelectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dict")
public class BaseDictController extends BaseController {

    @Autowired
    private BaseDictService dictService;

    @Autowired
    protected org.dozer.Mapper dozerMapper;

    /**
     * 增加数字字典
     * @param baseDictAddForm
     * @return
     */
    @PostMapping(value = "/add")
    @AddLog(title = "增加字典码值" , module = LoggerModuleEnum.BM_DICT , initContent = true)
    public ApiResult addDict(@RequestBody @Valid BaseDictAddForm baseDictAddForm) {
        BaseDictPo baseDictPo = dictService.addDict(baseDictAddForm);
        BaseDictVo baseDictVo = dozerMapper.map(baseDictPo, BaseDictVo.class);
        return ApiResult.success(baseDictVo);
    }
    /**
     * typeCode获取数字字典列表
     *
     * @return
     */
    @GetMapping(value = "/{typeCode}/treeList")
    public ApiResult queryDictTreeList(@PathVariable("typeCode")String typeCode) {
        List<BaseDictTreeVo> retList = dictService.queryDictTreeListByTypeCode(typeCode);
        return ApiResult.success(retList);
    }

    /**
     * 根据类型代码查找字典码值--为其他模块提供接口(公共接口)
     * @param dictTypeCode
     * @return
     */
    @GetMapping(value = "/{dictTypeCode}/select/tree")
    public ApiResult queryDictTreeDateListByTypeCode(@PathVariable("dictTypeCode") String dictTypeCode) {
        List<SelectVo> retList = dictService.queryDictTreeDateListByTypeCode(dictTypeCode);
        return ApiResult.success(retList);
    }


    /**
     * 根据类型代码查找字典码值--为其他模块提供接口(公共接口)
     * @param dictTypeCode
     * @return
     */
    @GetMapping(value = "/{dictTypeCode}/map")
    public ApiResult getDictMapByTypeCode(@PathVariable("dictTypeCode") String dictTypeCode) {
        Map<String, DictionaryVo> dictMap = dictService.getDictMapByTypeCode(dictTypeCode);
        return ApiResult.success(dictMap);
    }

    /**
     * 根据类型代码查找字典码值--为其他模块提供接口(公共接口)
     *
     * @param dictTypeCode
     * @return
     */
    @PostMapping(value = "/maps")
    public ApiResult getDictMapByTypeCodes(@RequestBody List<String> dictTypeCode) {
        DictionarysMap dictmaps  = dictService.getDictMapByTypeCode(dictTypeCode);
        return ApiResult.success(dictmaps);
    }

    /**
     * 修改数字字典列表
     * @param baseDictUpdateForm
     * @return
     */
    @PutMapping(value = "/update")
    public ApiResult updateDict(@RequestBody @Valid BaseDictUpdateForm baseDictUpdateForm) {
        BaseDictPo baseDictPo = dictService.updateDict(baseDictUpdateForm);
        BaseDictVo baseDictVo = dozerMapper.map(baseDictPo, BaseDictVo.class);
        return ApiResult.success(baseDictVo);
    }

    /**
     * 查询数据字典的基本信息
     * @param dictId
     * @return
     */
    @GetMapping(value = "/{dictId}/info")
    public ApiResult queryDictInfo(@PathVariable("dictId")int dictId) {
        BaseDictVo baseDictVo = dictService.getDictInfo(dictId);
        return ApiResult.success(baseDictVo);
    }

    /**
     * 删除数字字典
     * @param dictIds
     * @return
     */
    @DeleteMapping(value = "/delete")
    @AddLog(title = "删除数字字典码值" , module = LoggerModuleEnum.BM_DICT)
    public ApiResult deleteDict(@RequestBody List<Integer> dictIds) {
        //添加日志
        String names = dictService.queryNamesByIds(dictIds,"dictCode");
        this.setAcmLogger(new AcmLogger("批量删除数据字典码值，字典码值名称如下：" + names));

        dictService.deleteDict(dictIds);
        return ApiResult.success();
    }

    /**
     * 码值排序
     * @param id
     * @param upOrDown
     * @return
     */
    @PutMapping("/update/{id}/{upOrDown}/sort")
    public ApiResult updateDictSortNum(@PathVariable("id") Integer id,@PathVariable("upOrDown")String upOrDown){
        List<BaseDictTreeVo> baseDictTreeVoList = dictService.updateDictSortNum(id,upOrDown);
        return ApiResult.success(baseDictTreeVoList);
    }
}
