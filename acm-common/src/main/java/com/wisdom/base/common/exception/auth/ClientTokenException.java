package com.wisdom.base.common.exception.auth;


import com.wisdom.base.common.constant.CommonConstants;
import com.wisdom.base.common.exception.BaseException;

public class ClientTokenException extends BaseException {
    public ClientTokenException(String message) {
        super(message, CommonConstants.EX_CLIENT_INVALID_CODE);
    }
}
