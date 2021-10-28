package com.sso.config;


/**
 * Created by ace on 2017/8/23.
 */
public class ApiResult<T> {
    private int status = 200;
    private String message;
    private Integer total;
    private T data;

    public ApiResult(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public ApiResult() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static ApiResult success(){
        ApiResult apiResult = new ApiResult();
        apiResult.setStatus(ResultEnum.SECCESS.getCode());
        apiResult.setMessage(ResultEnum.SECCESS.getMessage());
        return apiResult;
    }

    public static <T> ApiResult success(T data){
        ApiResult apiResult = new ApiResult();
        apiResult.setStatus(ResultEnum.SECCESS.getCode());
        apiResult.setMessage(ResultEnum.SECCESS.getMessage());
        apiResult.setData(data);
        return apiResult;
    }
    
    public static <T> ApiResult success1(T data,Integer total){
        ApiResult apiResult = new ApiResult();
        apiResult.setStatus(ResultEnum.SECCESS.getCode());
        apiResult.setMessage(ResultEnum.SECCESS.getMessage());
        apiResult.setTotal(total);
        apiResult.setData(data);
        return apiResult;
    }

    public static ApiResult result(ResultEnum resultEnum){
        ApiResult apiResult = new ApiResult();
        apiResult.setStatus(resultEnum.getCode());
        apiResult.setMessage(resultEnum.getMessage());
        return apiResult;
    }

    public static ApiResult result(int status, String message){
        ApiResult apiResult = new ApiResult();
        apiResult.setStatus(status);
        apiResult.setMessage(message);
        return apiResult;
    }

    public boolean isSuccess(){
        return this.status == 200;
    }

    public static <T> ApiResult result(int errorCode,String msg,T data){
        ApiResult result = new ApiResult();
        result.setStatus(errorCode);
        result.setMessage(msg);
        result.setData(data);
        return result;
    }

    public static ApiResult error(String msg){
        ApiResult result = new ApiResult();
        result.setStatus(ResultEnum.ERROR.getCode());
        result.setMessage(msg);
        return result;
    }
}
