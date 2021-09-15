package com.wisdom.acm.szxm.vo.rygl;

import com.wisdom.base.common.msg.ApiResult;
import lombok.Data;


/**
 * Author：wqd
 * Date：2019-12-04 14:09
 * Description：<描述>
 */
@Data
/**
 * 获取证书预警信息 -- 领导视图
 */
public class WarnVo {
    /**
     *  即将到期数量
     */
    private Integer willExpiringNumber;

    /**
     * 已经过期数量
     */
    private Integer expiringNumber;

    /***
     * 证书预警信息
     */
    private ApiResult warnLists;

}
