package com.wisdom.fescar.config;

import com.alibaba.fescar.core.context.RootContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang.StringUtils;

public class CustomFeignIntercepter implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String xid = RootContext.getXID();
        //System.out.println("==>feign xid: " + xid);
        if (StringUtils.isNotBlank(xid)) {
            requestTemplate.header("Fescar-Xid", xid);
        }
    }
}
