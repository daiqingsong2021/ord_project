package com.wisdom.acm.szxm.form.rygl;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PeopleEntryDetailUpdateForm
{
    @NotNull(message = "主键ID不能为空")
    private Integer id;

    @NotNull(message = "进退场主键ID不能为空")
    private Integer enTryId;

    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 人员分类(szxm.rygl.peopleType)
     */
    private String type;

    /**
     * 职务
     */
    private String job;

    /**
     * 性别 0 女 1男
     */
    private String sex;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date bornDate;;

    @NotBlank(message = "手机号不能为空")
    private String telPhone;

    @NotBlank(message = "身份证号不能为空")
    private String idCard;

    /**
     * 人员类型(1 自有 0 分包)
     */
    private String peoType;

    private Double classHour;

    private String gzkh;

    private BigDecimal score;
}
