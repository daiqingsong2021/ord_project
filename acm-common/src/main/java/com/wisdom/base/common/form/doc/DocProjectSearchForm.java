package com.wisdom.base.common.form.doc;

import lombok.Data;

@Data
public class DocProjectSearchForm {

    /**
     * 名称
     */
    private String name;
    /**
     * 文档查询类型
     */
    private String docSearchType;

    /**
     * 当前登录用户
     */
    private Integer userId;
}
