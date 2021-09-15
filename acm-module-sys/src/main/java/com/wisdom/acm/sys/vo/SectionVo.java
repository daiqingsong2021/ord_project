package com.wisdom.acm.sys.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 标段
 */
@Data
public class SectionVo implements Comparable<SectionVo> {

    /**
     * 项目ID
     */
    private Integer projectId;
    /**
     * ID
     */
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 代码
     */
    private String code;
    /**
     * 标段类型
     */
    private String typeCoe;

    /**
     * 标段类型名称
     */
    private String typeName;
    /**
     * 父级ID
     */
    private Integer parentId;
    /**
     * 类型
     */
    private String type;
    /**
     * 专业
     */
    private String professional;
    /**
     * 单位分类
     */
    private String orgClassification;

    /**
     * 施工单位
     */
    private List<GeneralVo> cuList;
    /**
     * 监理单位
     */
    private List<GeneralVo> ccuList;

    /**
     * 业主代表
     */
    private List<GeneralVo> ownerList;

    /**
     * 品高标段id
     */
    private String pgSectionId;

    /**
     * 标段状态Vo
     */
    private GeneralVo sectionStatusVo = new GeneralVo();


    /**
     * 开工日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;

    /**
     * 完工日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 派工单开始日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date pgdStartDate;

    /**
     * 派工单结束日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date pgdEndDate;

    /**
     * 考核开始日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date examStartDate;

    /**
     * 考核结束日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date examEndDate;

    private String openPgd;

    private String openExam;

    @Override
    public int compareTo(SectionVo o) {
        if (!this.code.contains("-")) {
            this.code = this.code + "-0";
        }
        if (!o.getCode().contains("-")) {
            o.setCode(o.getCode() + "-0");
        }
        String[] temp1 = this.code.split("-");
        String[] temp2 = o.getCode().split("-");
        String[] str1;
        String[] str2;
        if (temp1.length > temp2.length) {
            str2 = temp1;
            str1 = temp2;
        } else {
            str1 = temp2;
            str2 = temp1;
        }
        Pattern p = Pattern.compile("[a-zA-z]");
        for (int i = 0; i < str2.length; i++) {
            if (i <= (str1.length - 1)) {
                if (p.matcher(str2[i]).find() || p.matcher(str1[i]).find()) {
                    if (str2[i].equals(str1[i])) {
                        continue;
                    } else {
                        return str2[i].compareTo(str1[i]);
                    }
                }
                if (Long.valueOf(str2[i]) > Long.valueOf(str1[i])) {
                    return 1;
                } else if (Long.valueOf(str2[i]) < Long.valueOf(str1[i])) {
                    return -1;
                } else {
                    continue;
                }
            } else {
                return 1;
            }
        }
        return 0;
    }
}
