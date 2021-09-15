package com.wisdom.base.common.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.exception.BaseException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@FeignClient(value = "acm-leaf", configuration = FeignConfiguration.class)
public interface LeafService {

    default int getId(int step){
        String id = this.getSegmentID("wisdom-acm", step);
        if(!ObjectUtils.isEmpty(id)){
            return Integer.valueOf(id);
        }
        throw new BaseException("ID服务错误!");
    }

    @GetMapping(value = "/leaf/segment/get/{key}/{step}")
    String getSegmentID(@PathVariable("key") String key, @PathVariable("step") int step);

    default int getId(){
        String id = this.getSegmentID("wisdom-acm");
        if(!ObjectUtils.isEmpty(id)){
            return Integer.valueOf(id);
        }
        throw new BaseException("ID服务错误!");
    }

    @GetMapping(value = "/leaf/segment/get/{key}")
    String getSegmentID(@PathVariable("key") String key);
}
