package com.wisdom.base.common.msg.auth;

import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.constant.RestCodeConstants;

/**
 * Created by ace on 2017/8/23.
 */
public class TokenErrorResponse extends ApiResult {
    public TokenErrorResponse(String message) {
        super(RestCodeConstants.TOKEN_ERROR_CODE, message);
    }
}
