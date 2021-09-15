package com.wisdom.acm.sys.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Table(name = "wsd_sys_ipt")
@Data
public class SysIptPo extends BasePo {

    /**
     * 代码
     */
    @Column(name = "ipt_code")
    private String iptCode;

    /**
     * 项目id
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 名称
     */
    @Column(name = "ipt_name")
    private String iptName;

    /**
     * 父ID
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 等级
     */
    @Column(name = "level_")
    private String level;

    /**
     * 删除标识（1：删除，0：未删除）
     */
    @Column(name = "del")
    private Integer del;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;


}