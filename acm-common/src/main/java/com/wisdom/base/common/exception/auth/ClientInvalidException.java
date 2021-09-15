package com.wisdom.base.common.exception.auth;


import com.wisdom.base.common.constant.CommonConstants;
import com.wisdom.base.common.exception.BaseException;

public class ClientInvalidException extends BaseException {
    public ClientInvalidException(String message) {
        super(message, CommonConstants.EX_CLIENT_INVALID_CODE);
    }
}
