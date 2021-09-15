package com.wisdom.acm.base.vo.tmpltask;

import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.Date;

@Data
public class BaseTmplTaskDelvVo {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 计划
     */
    private Integer taskId;

    /**
     * 名称
     */
    private String delvName;

    /**
     * 代码
     */
    private String delvCode;

    /**
     * 类别
     */
    private DictionaryVo delvType;

    /**
     * 创建者
     */
    private UserVo creator;

    /**
     * 创建时间
     */
    private Date creatTime;

}
