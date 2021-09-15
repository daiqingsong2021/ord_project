package com.wisdom.base.common.dc.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.io.IOException;
import java.util.Map;

/**
 * @author zll
 * 2020/8/19/019 14:17
 * Description:<描述>
 */
@FeignClient(value = "acm-module-processing", configuration = FeignConfiguration.class)
public interface DcProcessingService{
    /**
     * 运营日报
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/odr/report/generateDailyWorkReportFile")
    public ApiResult generateDailyWorkReportFile(@RequestBody Map<String,Object> maps);

    /**
     * 指挥中心日报
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/odr/report/generateCCDailyWorkReportFile")
    public ApiResult generateCCDailyWorkReportFile(@RequestBody Map<String,Object>maps);
}
