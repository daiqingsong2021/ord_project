package com.wisdom.acm.dc1.vo;

import lombok.Data;

/**
 * @author zll
 * 2020/9/2/002 11:12
 * Description:<描述>
 */
@Data
public class UploadVo {
    private String status;//0是是否重新导入，1是没有导入，可以直接导入
    private String content;//描述
}
