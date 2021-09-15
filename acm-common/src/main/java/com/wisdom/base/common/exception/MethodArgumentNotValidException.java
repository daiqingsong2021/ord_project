
package com.wisdom.base.common.exception;

import java.util.Iterator;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

public class MethodArgumentNotValidException extends Exception {

    private final MethodParameter parameter;
    private final BindingResult bindingResult;

    public MethodArgumentNotValidException(MethodParameter parameter, BindingResult bindingResult) {
        this.parameter = parameter;
        this.bindingResult = bindingResult;
    }

    public MethodParameter getParameter() {
        return this.parameter;
    }

    public BindingResult getBindingResult() {
        return this.bindingResult;
    }

    public String getMessage() {

        StringBuilder sb = (new StringBuilder("Validation failed for argument at index ")).append(this.parameter.getParameterIndex()).append(" in method: ").append(this.parameter.getExecutable().toGenericString()).append(", with ").append(this.bindingResult.getErrorCount()).append(" error(s): ");
        Iterator var2 = this.bindingResult.getAllErrors().iterator();

        while(var2.hasNext()) {
            ObjectError error = (ObjectError)var2.next();
            sb.append("[").append(error).append("] ");
        }

        return sb.toString();
    }
}
