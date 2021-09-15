package com.wisdom.acm.szxm.po.rygl;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "szxm_rygl_people_entrydetail")
@Data
public class PeopleEntryDetailPo extends BasePo
{

    /**
     * 项目ID
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 标段ID
     */
    @Column(name = "section_id")
    private Integer sectionId;

    /**
     * 人员进退场外键ID
     */
    @Column(name = "entry_id")
    private Integer enTryId;

    /**
     * 人员姓名
     */
    @Column(name = "name")
    private String name;

    /**
     * 人员分类(szxm.rygl.peopleType)
     */
    @Column(name = "type")
    private String type;

    /**
     * 人员职务
     */
    @Column(name = "job")
    private String job;

    /**
     * 性别 0 女 1男
     */
    @Column(name = "sex")
    private String sex;

    @Column(name = "born_date")
    private Date bornDate;;

    @Column(name = "telphone")
    private String telPhone;

    @Column(name = "id_card")
    private String idCard;

    /**
     * 人员类型(1 自有 0 分包)
     */
    @Column(name = "peo_type")
    private String peoType;

    /**
     *  总学时
     */
    @Column(name = "class_hour")
    private BigDecimal classHour;

    /**
     * 工资卡号
     */
    @Column(name = "gzkh")
    private String gzkh;

    /**
     * 培训成绩
     */
    @Column(name = "score")
    private BigDecimal score;


    @Column(name = "people_id")
    private String peopleId;
}
