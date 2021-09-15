package com.wisdom.acm.hrb.sys.form;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;
@Data
public class TrainFoundationUpdateForm extends BaseForm {
    private Integer id;
    private String line;
    private String trainCode;
}
