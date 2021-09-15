package com.wisdom.acm.sys.form;

import lombok.Data;

import java.util.List;

@Data
public class SysFavoritesAddForm {
    // 类型
    private String bizType;
    // 收藏内容
    private List<String> bizs;
}
