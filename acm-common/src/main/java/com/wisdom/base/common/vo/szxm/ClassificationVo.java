package com.wisdom.base.common.vo.szxm;

import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

@Data
public class ClassificationVo
{
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 项目ID
     */
    private Integer projectId;

    /**
     * 项目名称
     */
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
     * 物料编码
     */
    private String materialCode;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 来源
     */
    private String source;

    /**
     * 规格型号
     */
    private String specification;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 合同数量
     */
    private Integer contractAmount;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 是否需要第三方检测 0:需要 1:不需要
     */
    private GeneralVo needThirdInspectionVo = new GeneralVo();
    /**
     * 审批状态 0:已审批 1:未审批
     */
    private GeneralVo statusVo = new GeneralVo();

    /**
     * 备注说明
     */
    private String description;

}
