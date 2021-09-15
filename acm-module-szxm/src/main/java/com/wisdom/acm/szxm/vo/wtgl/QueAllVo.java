package com.wisdom.acm.szxm.vo.wtgl;

import com.wisdom.base.common.vo.StatusVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;
import java.util.Date;

@Data
public class QueAllVo {

    private Integer id;

    private Integer handleId;

    private Integer questionId;

    /**
     * 问题标题
     */
    private String title;

    /**
     * 创建时间
     */
    private String queCreatTime;

    /**
     * 提出人
     */
    private UserVo queUser;

    /**
     * 问题状态
     */
    private StatusVo queStatvo;

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
     * 确认人
     */
    private UserVo confirmUser;

    /**
     * 确认时间
     */
    private String confirmTime;

    private Date creatTime;

    /**
     * 送审至
     */
    private UserVo nextConfirmUser;

    /**
     * 跟踪记录状态
     */
    private StatusVo confirmStatvo;

    /**
     * 意见
     */
    private String desc;

    //附件
    private Integer fileCount;

    private String orgName;
}
