package com.wisdom.base.common.handler;

import com.wisdom.base.common.constant.CommonConstants;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.exception.auth.ClientTokenException;
import com.wisdom.base.common.exception.auth.UserInvalidException;
import com.wisdom.base.common.exception.auth.UserTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@ControllerAdvice("com.wisdom")
@ResponseBody
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ClientTokenException.class)
    public ApiResult clientTokenExceptionHandler(HttpServletResponse response, ClientTokenException ex) {
        response.setStatus(403);
        logger.error(ex.getMessage(),ex);
        return new ApiResult(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(UserTokenException.class)
    public ApiResult userTokenExceptionHandler(HttpServletResponse response, UserTokenException ex) {
        response.setStatus(200);
        logger.error(ex.getMessage(),ex);
        return new ApiResult(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(UserInvalidException.class)
    public ApiResult userInvalidExceptionHandler(HttpServletResponse response, UserInvalidException ex) {
        response.setStatus(200);
        logger.error(ex.getMessage(),ex);
        return new ApiResult(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(BaseException.class)
    public ApiResult baseExceptionHandler(HttpServletResponse response, BaseException ex) {
        logger.error(ex.getMessage(),ex);
        response.setStatus(200);
        return new ApiResult(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResult otherExceptionHandler(HttpServletResponse response, Exception ex) {
        response.setStatus(200);
        logger.error(ex.getMessage(),ex);
        return new ApiResult(CommonConstants.EX_OTHER_CODE, ex.getMessage());
    }

    /**
     * 处理所有接口数据验证异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiResult handleMethodArgumentNotValidException(HttpServletResponse response,MethodArgumentNotValidException e){

        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        StringBuffer err = new StringBuffer();
        for(ObjectError error : errors){
            if(err.length() > 0){
                err.append("\n");
            }
            err.append(error.getDefaultMessage());
        }
        logger.error(e.getMessage(), e);
        response.setStatus(200);
        return new ApiResult(CommonConstants.EX_RUNTIME_COMPARA_CODE, err.toString());
    }
}
