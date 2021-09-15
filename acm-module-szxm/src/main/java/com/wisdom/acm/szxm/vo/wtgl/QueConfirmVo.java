package com.wisdom.acm.szxm.vo.wtgl;

import com.wisdom.base.common.vo.StatusVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

@Data
public class QueConfirmVo {

    private Integer id;

    /**
     * 确认人
     */
    private UserVo confirmUser;

    /**
     * 确认时间
     */
    private String confirmTime;

    /**
     * 送审至
     */
    private UserVo nextConfirmUser;

    /**
     * 意见
     */
    private String desc;

    /**
     * 跟踪记录状态
     */
    private StatusVo statusVo;
}
