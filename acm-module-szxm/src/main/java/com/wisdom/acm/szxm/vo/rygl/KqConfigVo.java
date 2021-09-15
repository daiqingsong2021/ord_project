package com.wisdom.acm.szxm.vo.rygl;

import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.Date;

/**
 * 考勤配置VO对象
 */
@Data
public class KqConfigVo
{
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 项目ID
     */
    private Integer projectId;

    private String projectName;

    /**
     * 标段ID
     */
    private Integer sectionId;

    /**
     * 标段编码
     */
    private String sectionCode;

    /**
     * 标段名称
     */
    private String sectionName;

    /**
     * 考勤日历ID
     */
    private Integer calenderId;

    /**
     * 考勤类型 0 全局 1 标段
     */
    private String type;

    /**
     * 管理人员是否考勤(0 否 1是)
     */
    private GeneralVo mangerKqVo=new GeneralVo();

    /**
     * 劳务人员是否考勤(0 否 1是)
     */
    private GeneralVo workerKqVo=new GeneralVo();

    /**
     * 考勤日历Vo对象
     */
    private GeneralVo calenderVo=new GeneralVo();

}
