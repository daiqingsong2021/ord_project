package com.wisdom.acm.base.vo.tmpldelv;

import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;

import java.util.Date;

@Data
public class BaseTmpldelvTypeVo {

    /**
     * id
     */
    private Integer id;

    /**
     * 标题
     */
    private String typeTitle;

    /**
     * 编号
     */
    private String typeNum;

    /**
     * 版本
     */
    private String typeVersion;

    /**
     * 类别
     */
    private DictionaryVo typeType;

    /**
     * 备注
     */
    private String typeDesc;

    /**
     * 创建日期
     */
    protected Date creatTime;

    /**
     * 创建人
     */
    protected UserVo creator;

}