package com.wisdom.acm.szxm.vo.rygl;

import com.google.common.collect.Lists;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.List;

@Data
public class CertGlVo
{
    /**
     * 主键ID
     */
    private Integer id;


    private Integer projectId;

    private String projectName;

    private String certName;

    private Integer warnPeriod;

    private String certVerifyUrl;
}
