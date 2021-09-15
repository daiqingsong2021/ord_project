package com.wisdom.base.common.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.CodeResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@FeignClient(value = "acm-module-base",configuration = FeignConfiguration.class)
public interface CommCodeCalculateService {

    /**
     * 获取自动生成编码
     *
     * @param map
     * @return
     */
    default String getRuleCode(Map<String,Object> map){

        ApiResult<CodeResultVo> result = this.getDictMapByTypeCode(map);

        if(result.getStatus() != 200){
            throw new BaseException(result.getMessage());
        }

        CodeResultVo codeResultVo = result.getData();
        if(codeResultVo != null){
            return codeResultVo.getCode();
        }
        return null;
    }

    /**
     * 根据业务字典类型
     *
     * @param map
     * @return
     */
    @GetMapping(value = "/code/calculate")
    ApiResult<CodeResultVo> getDictMapByTypeCode(@RequestParam Map<String,Object> map);


    /**
     * 校验编码是否重复
     * @param map
     * @return
     */
    @GetMapping("/code/repeat/check")
    ApiResult<Boolean> checkCodeIsRepeat(Map<String,Object> map);

}
