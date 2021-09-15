package com.wisdom.acm.base.vo.set;

import lombok.Data;

import java.util.List;

@Data
public class BaseSetDocVo {
    // 上传最大值（M）
    private Double uploadMax;
    // 禁止文件类型
    private List<String> banFileType;
}
