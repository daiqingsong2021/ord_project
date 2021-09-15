package com.wisdom.acm.sys.form;

import com.wisdom.base.common.form.BaseForm;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class SysIptAddForm extends BaseForm {

    /**
     * 代码
     */
    @NotBlank(message = "代码不能为空")
    private String iptCode;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    private String iptName;

    /**
     * 父ID
     */
    @NotNull( message = "父id不能为空")
    private Integer parentId;


    private String level;

    /**
     * 备注
     */
    private String remark;

    private Integer sort;

    public String getLogContent(){return "增加IPT，IPT名称："+ this.getIptName();}
}
