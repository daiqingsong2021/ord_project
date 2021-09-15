package com.wisdom.acm.base.form.set;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import java.util.List;

@Data
public class BaseSetDocUpdateForm extends BaseForm {

    // 上传最大值（M）
    @LogParam(title = "上传文件大小")
    private Double uploadMax;
    // 禁止文件类型
    @LogParam(title = "禁止文件格式")
    private List<String> banFileType;
}
