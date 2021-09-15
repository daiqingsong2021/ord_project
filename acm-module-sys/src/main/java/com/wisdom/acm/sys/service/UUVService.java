package com.wisdom.acm.sys.service;

import com.wisdom.base.common.msg.ApiResult;

/**
 * Author：wqd
 * Date：2019-09-16 15:22
 * Description：<描述>
 */
public interface UUVService {

    /**
     * 调用uuv同步数据组织 顺序为 内部组织、外部组织
     * @param startUpdateTime 起始更新时间
     * @param endUpdateTime 最后更新时间
     * @return
     */
    ApiResult syncUUVOrg(String startUpdateTime, String endUpdateTime);

    /**
     * 调用uuv同步数据人员 顺序为内部人员、外部人员
     * @param startUpdateTime 起始更新时间
     * @param endUpdateTime 最后更新时间
     * @return
     */
    ApiResult syncUUVUser(String startUpdateTime, String endUpdateTime);

    /**
     * 调用uuv同步数据 顺序为 内部组织、外部组织、内部人员、外部人员
     * 定时任务调用方法   异常捕获
     */
    void tbUuvTask();

    /**
     * 同步uuv内部组织
     */
    ApiResult tbInnerOrg();

    /**
     *  同步uuv外部 组织
     * @param startUpdateTime 起始更新时间
     * @param endUpdateTime 最后更新时间
     */
    ApiResult tbOutOrg(String startUpdateTime, String endUpdateTime);

    /**
     * 同步uuv内部人员
     */
    ApiResult tbInnerUser();

    /**
     * 同步uuv外部人员
     * @param startUpdateTime 起始更新时间
     * @param endUpdateTime 最后更新时间
     */
    ApiResult tbOutUser(String startUpdateTime, String endUpdateTime);
}
