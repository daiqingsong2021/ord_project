package com.wisdom.base.common.exception.auth;


import com.wisdom.base.common.constant.CommonConstants;
import com.wisdom.base.common.exception.BaseException;

/**
 * Created by ace on 2017/9/8.
 */
public class UserTokenException extends BaseException {
    public UserTokenException(String message) {
        super(message, CommonConstants.EX_USER_INVALID_CODE);
    }
}
