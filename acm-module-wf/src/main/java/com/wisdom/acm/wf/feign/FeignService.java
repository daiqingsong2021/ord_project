package com.wisdom.acm.wf.feign;

public interface FeignService {
    <T> T newInstanceByUrl(Class<T> apiType, String url);

    <T> T newInstanceByName(Class<T> apiType, String name);
}
