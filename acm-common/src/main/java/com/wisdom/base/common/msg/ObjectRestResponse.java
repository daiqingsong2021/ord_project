package com.wisdom.base.common.msg;

/**
 * Created by Ace on 2017/6/11.
 */
public class ObjectRestResponse<T> extends ApiResult {

    T objectRestData;
    boolean rel;

    public boolean isRel() {
        return rel;
    }

    public void setRel(boolean rel) {
        this.rel = rel;
    }


    public ObjectRestResponse rel(boolean rel) {
        this.setRel(rel);
        return this;
    }


    public ObjectRestResponse data(T objectRestData) {
        this.setObjectRestData(objectRestData);
        return this;
    }
    public T getObjectRestData() {
        return objectRestData;
    }

    public void setObjectRestData(T objectRestData) {
        this.objectRestData = objectRestData;
    }


}
