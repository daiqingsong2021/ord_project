package com.wisdom.base.common.feign;
import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.LineFoundationVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
@FeignClient(value = "acm-module-hrb-sys",configuration = FeignConfiguration.class)
public interface HrbSysService {
    default List<LineFoundationVo> queryLineFoundationList(){
        ApiResult<List<LineFoundationVo>> apiResult=this.queryLineFoundationList_();
        if(apiResult.getStatus() == 200)
            return apiResult.getData();
        return null;
    }
    @GetMapping(value = "/odr/foundation/queryLineFoundationList") ApiResult<List<LineFoundationVo>>  queryLineFoundationList_();}
