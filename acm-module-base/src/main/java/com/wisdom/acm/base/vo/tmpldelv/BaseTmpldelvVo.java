package com.wisdom.acm.base.vo.tmpldelv;

import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.Date;

@Data
public class BaseTmpldelvVo {

    /**
     * id
     */
    private Integer id;

    /**
     * 交付物模板id
     */
    private Integer typeId;

    /**
     * 父节点
     */
    private Integer parentId;

    /**
     * 标题
     */
    private String delvTitle;

    /**
     * 编号
     */
    private String delvNum;

    /**
     * 版本
     */
    private String delvVersion;

    /**
     * 类别
     */
    private DictionaryVo delvType;

    /**
     * 备注
     */
    private String delvDesc;

    /**
     * 是否是PBS
     */
    private String type;

    /**
     * 创建日期
     */
    protected Date creatTime;

    /**
     * 创建人
     */
    protected UserVo creator;

}