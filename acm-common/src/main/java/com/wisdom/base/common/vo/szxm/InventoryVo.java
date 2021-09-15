package com.wisdom.base.common.vo.szxm;

import lombok.Data;

@Data
public class InventoryVo
{
    /**
     * 主键ID
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
     * 标段编号
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
     * 本周到货量
     */
    private Integer thisWeekArrival;

    /**
     * 本周出库量
     */
    private Integer thisWeekOutstore;

    /**
     * 本周消耗量
     */
    private Integer thisWeekConsume;

    /**
     * 合同总量
     */
    private Integer contractAmount;

    /**
     * 累计到货量
     */
    private Integer totalArrival;

    /**
     * 累计出库量
     */
    private Integer totalOutstore;

    /**
     * 累计消耗量
     */
    private Integer totalConsume;

    /**
     * 库存量
     */
    private Integer storageQuantity;

    /**
     * 下周计划量
     */
    private Integer nextWeekPlanQuantity;

    /**
     * 物料分类对象
     */
    private ClassificationVo classificationVo = new ClassificationVo();

    /**
     * 仓库位置
     */
    private String storagePosition;

    /**
     * 预警
     */
    private String warning;

}
