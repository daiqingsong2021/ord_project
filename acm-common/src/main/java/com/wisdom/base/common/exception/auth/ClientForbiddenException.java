package com.wisdom.base.common.exception.auth;


import com.wisdom.base.common.constant.CommonConstants;
import com.wisdom.base.common.exception.BaseException;

public class ClientForbiddenException extends BaseException {
    public ClientForbiddenException(String message) {
        super(message, CommonConstants.EX_CLIENT_FORBIDDEN_CODE);
    }

}
