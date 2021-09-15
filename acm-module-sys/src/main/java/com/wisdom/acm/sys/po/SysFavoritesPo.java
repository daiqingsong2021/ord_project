package com.wisdom.acm.sys.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import javax.persistence.*;

@Data
@Table(name = "wsd_sys_favorites")
public class SysFavoritesPo extends BasePo {

    //用户id
    @Column(name = "user_id")
    private Integer userId;

    //菜单id逗号分隔
    @Column(name = "content_")
    private String content;

    //菜单类型
    @Column(name = "biz_type")
    private String bizType;

}
