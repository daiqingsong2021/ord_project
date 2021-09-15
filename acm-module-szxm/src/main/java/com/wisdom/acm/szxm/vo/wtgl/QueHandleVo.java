package com.wisdom.acm.szxm.vo.wtgl;

import com.wisdom.base.common.vo.StatusVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.List;

@Data
public class QueHandleVo {

    private Integer id;

    /**
     * 处理人
     */
    private String handleUser;

    /**
     * 处理时间
     */
    private String handleTime;

    /**
     * 处理结果
     */
    private String handleResult;

    /**
     * 跟踪记录
     */
    private List<QueConfirmVo> confirm;

    /**
     * 处理记录状态
     */
    private StatusVo statusVo;

    //附件
    private Integer fileCount;
}
