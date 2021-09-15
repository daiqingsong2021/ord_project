package com.wisdom.base.common.service;

import com.wisdom.base.common.log.AcmLogger;
import org.springframework.scheduling.annotation.Async;

public interface LogService {
    void addAcmLogger(AcmLogger log, boolean isSucess, String error);
}
