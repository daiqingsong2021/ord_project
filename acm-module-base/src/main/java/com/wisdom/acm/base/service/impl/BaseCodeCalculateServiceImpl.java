package com.wisdom.acm.base.service.impl;

import com.wisdom.acm.base.service.BaseCodeCalculateService;
import com.wisdom.acm.base.vo.BaseCodeResultVo;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class BaseCodeCalculateServiceImpl implements BaseCodeCalculateService {

    @Override
    public BaseCodeResultVo calculateCode(Map<String, Object> map) {
        return new BaseCodeResultVo(RandomStringUtils.randomAlphabetic(1)+RandomStringUtils.randomNumeric(10));
    }

    @Override
    public boolean checkCodeIsRepeat(Map<String, Object> map) {
        return false;
    }
}
