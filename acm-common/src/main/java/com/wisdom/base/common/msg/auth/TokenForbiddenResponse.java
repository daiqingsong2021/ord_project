package com.wisdom.base.common.msg.auth;

import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.constant.RestCodeConstants;

/**
 * Created by ace on 2017/8/25.
 */
public class TokenForbiddenResponse  extends ApiResult {
    public TokenForbiddenResponse(String message) {
        super(RestCodeConstants.TOKEN_FORBIDDEN_CODE, message);
    }
}
