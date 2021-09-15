package com.wisdom.base.common.dc.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.dc.vo.base.BaseBoVo;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/8/17/017 16:05
 * Description:<用于客车运营至今时间，且是定时器调用>
 */
@FeignClient(value = "acm-module-base", configuration = FeignConfiguration.class)
public interface DcBaseService {
    /**
     * 每天更新一次
     * @param dates
     */
    default void updateTrainTime(Map<String,String> dates){
        this.updateTrainTime_(dates);
    }

    /**
     * 查询客运行数据
     * @return
     */
    default List<BaseBoVo> getTrainTime(){
        ApiResult<List<BaseBoVo>> apiResult= this.getTrainTime_();
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * @param dates
     */
    @RequestMapping(value = "/set/time/update/train", method = RequestMethod.POST)
    public ApiResult updateTrainTime_(@RequestBody Map<String,String> dates);


    /**
     * 查询客车运营时间
     * @return
     */
    @GetMapping(value = "/set/time/train/info")
    public ApiResult<List<BaseBoVo>> getTrainTime_();
}
