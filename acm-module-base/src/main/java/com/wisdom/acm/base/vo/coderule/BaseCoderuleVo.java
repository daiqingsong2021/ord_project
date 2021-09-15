package com.wisdom.acm.base.vo.coderule;

import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

@Data
public class BaseCoderuleVo {

    private Integer id;

    //名称
    private String ruleName;

    //默认
    private Integer defaultFlag;

    //能否修改
    private Integer modified;

    //状态
    private String status;

    //第X段
    private GeneralVo position1;

    private GeneralVo position2;

    private GeneralVo position3;

    private GeneralVo position4;

    private GeneralVo position5;

    private GeneralVo position6;

    private GeneralVo position7;

}
