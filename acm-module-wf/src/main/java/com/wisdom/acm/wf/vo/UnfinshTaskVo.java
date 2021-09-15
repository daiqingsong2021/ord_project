package com.wisdom.acm.wf.vo;

import lombok.Data;

import java.util.List;

@Data
public class UnfinshTaskVo {

    private Integer size;

    private List<UnfinshTaskDetailVo> detailVos;

}
