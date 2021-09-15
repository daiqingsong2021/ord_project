package com.wisdom.acm.base.vo.tmpltask;

import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.Date;

@Data
public class BaseTmplTaskVo {

    /**
     * id
     */
    private Integer id;

    /**
     * 模板ID
     */
    private Integer tmplId;

    /**
     * 父节点
     */
    private Integer parentId;

    /**
     * 代码
     */
    private String taskCode;

    /**
     * 名称
     */
    private String taskName;

    /**
     * 计划工期
     */
    private Double planDrtn;

    /**
     * 计划工时
     */
    private Double planQty;

    /**
     * 计划类型
     */
    private DictionaryVo planType;

    /**
     * 计划级别
     */
    private DictionaryVo planLevel;

    /**
     * 是否WBS反馈
     */
    private Integer isFeedback;

    /**
     * 是否控制账户
     */
    private Integer controlAccount;

    /**
     * 作业类型
     */
    private DictionaryVo taskType;

    /**
     * 工期类型
     */
    private DictionaryVo drtnType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 类型tmpl、wsd、task
     */
    private String type;

    /**
     * 创建人
     */
    private UserVo creator;

    /**
     * 创建日期
     */
    private Date creatTime;

    /**
     * 状态
     */
    //private String status;

}
