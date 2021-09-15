package com.wisdom.auth.server.common.util.jwt;

public interface IJWTInfo {
    /**
     * 获取用户名
     * @return
     */
    String getUniqueName();

    /**
     * 获取用户ID
     * @return
     */
    String getId();

    /**
     * 获取名称
     * @return
     */
    String getName();

    /**
     * 用户姓名
     *
     * @return
     */
    String getActuName();

    /**
     * 用户类型
     * @return
     */
    String getUserType();
}
