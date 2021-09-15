package com.wisdom.base.common.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.DictionarysMap;
import com.wisdom.base.common.vo.SelectVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@FeignClient(value = "acm-module-base",configuration = FeignConfiguration.class)
public interface CommDictService {


    default Map<String, DictionaryVo> getDictMapByTypeCode(String dictTypeCode){
        ApiResult<Map<String, DictionaryVo>>  apiResult = this.getDictMapByTypeCode_(dictTypeCode);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    default DictionarysMap getDictMapByTypeCodes(List<String> dictTypeCodes){
        ApiResult<DictionarysMap>  apiResult = this.getDictMapByTypeCodes_(dictTypeCodes);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    default List<SelectVo> queryDictTreeDateListByTypeCode(String dictTypeCode){
        ApiResult<List<SelectVo>>  apiResult = this.queryDictTreeDateListByTypeCode_(dictTypeCode);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping(value = "/dict/{dictTypeCode}/select/tree")
    public ApiResult<List<SelectVo>> queryDictTreeDateListByTypeCode_(@PathVariable("dictTypeCode") String dictTypeCode);

    /**
     * 根据业务字典类型
     *
     * @param dictTypeCode
     * @return
     */
    @GetMapping(value = "/dict/{dictTypeCode}/map")
    public ApiResult<Map<String, DictionaryVo>> getDictMapByTypeCode_(@PathVariable("dictTypeCode") String dictTypeCode);

    /**
     * 根据多个业务字典集合
     *
     * @param dictTypeCodes
     * @return
     */
    @PostMapping(value = "/dict/maps")
    public ApiResult<DictionarysMap> getDictMapByTypeCodes_(@RequestBody List<String> dictTypeCodes);
}
