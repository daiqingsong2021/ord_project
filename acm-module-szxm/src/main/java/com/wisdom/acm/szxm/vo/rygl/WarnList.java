package com.wisdom.acm.szxm.vo.rygl;

import lombok.Data;

/**
 * Author：wqd
 * Date：2019-12-09 18:42
 * Description：<描述>
 */
@Data
/**
 * 获取证书预警信息 -- 领导视图
 */
public class WarnList implements Comparable<WarnList>{
    /**
     * 标段ID
     */
    private Integer sectionId;

    /**
     * 标段号
     */
    private String sectionCode;

    /**
     * 证书名称
     */
    private String certificateName;

    /**
     * 姓名
     */
    private String name;

    /**
     * 证书状态
     */
    private String certificateState;

    @Override
    public int compareTo(WarnList o) {
        //String类型的大小比较
        if (this.certificateState.compareTo(o.getCertificateState()) < 0) {
            return 1;
        } else if (this.certificateState.compareTo(o.getCertificateState()) > 0) {
            return -1;
        }
        return 0;
    }
}
