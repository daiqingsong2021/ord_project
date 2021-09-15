package com.wisdom.acm.szxm.vo.wtgl;

import com.wisdom.base.common.vo.StatusVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.List;

@Data
public class QueHanFirmVo {

    private Integer id;

    /**
     * 问题标题
     */
    private String title;

    /**
     * 创建时间
     */
    private String creatTime;

    /**
     * 提出人
     */
    private UserVo creator;

    /**
     * 处理记录
     */
    private List<QueHandleVo> handle;

    /**
     * 问题状态
     */
    private StatusVo statusVo;
}
