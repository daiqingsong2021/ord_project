package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class SysFuncVo {

    private  Integer id;

    private Integer menuId;

    private String funcName;

    private String funcCode;

    private String shortCode;

    private GeneralVo del;

    private Date creatTime;

    private Date lastUpdTime;

}
