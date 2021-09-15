package com.wisdom.acm.sys.vo;
import lombok.Data;

import java.util.List;

@Data
public class MyNewestMsgVo {

    private Integer size;

    private List<MyNewestMessageVo> detailVos;

}
