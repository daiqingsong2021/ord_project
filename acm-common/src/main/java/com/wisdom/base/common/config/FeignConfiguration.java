package com.wisdom.base.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 *
 */
@Configuration
public class FeignConfiguration implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
       if (attributes !=  null){
           HttpServletRequest request = attributes.getRequest();
           Enumeration<String> headerNames = request.getHeaderNames();
           if (headerNames != null) {
               while (headerNames.hasMoreElements()) {
                   String name = headerNames.nextElement();
                   String values = request.getHeader(name);
                   // System.out.println("name:"+name+";values="+values);
                   template.header(name, values);
               }
           }
       }
        //解决通过fegin调用，偶尔会出现400报错的问题。
        template.header("Transfer-Encoding","chunked");
    }
}
