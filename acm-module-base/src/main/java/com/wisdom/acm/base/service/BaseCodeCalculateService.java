package com.wisdom.acm.base.service;

import com.wisdom.acm.base.vo.BaseCodeResultVo;

import java.util.Map;

public interface BaseCodeCalculateService {
    /**
     * 计算编码
     * @param map
     * @return
     */
    BaseCodeResultVo calculateCode(Map<String, Object> map);


    /**
     * 判断代码是否重复
     * @param map
     * @return
     */
    boolean checkCodeIsRepeat(Map<String, Object> map);
}
