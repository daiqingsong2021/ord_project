package com.wisdom.base.common.exception.auth;


import com.wisdom.base.common.constant.CommonConstants;
import com.wisdom.base.common.exception.BaseException;

public class UserInvalidException extends BaseException {
    public UserInvalidException(String message) {
        super(message, CommonConstants.EX_USER_PASS_INVALID_CODE);
    }
}
