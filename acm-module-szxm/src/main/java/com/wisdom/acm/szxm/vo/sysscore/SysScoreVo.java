package com.wisdom.acm.szxm.vo.sysscore;

import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Author：wqd
 * Date：2020-01-03 10:39
 * Description：<描述>
 */
/**
 * 系统评分
 */
@Data
public class SysScoreVo implements Comparable<SysScoreVo> {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 总分
     */
    private BigDecimal totalScore;

    /**
     * 客观得分
     */
    private BigDecimal objectiveScore;

    /**
     * 主观得分
     */
    private BigDecimal subjectiveScore;

    /**
     * 标段ID
     */
    private Integer sectionId;
    /**
     * 标段号
     */
    private String sectionCode;
    /**
     * 标段名称
     */
    private String sectionName;
    /**
     * 项目 ID
     */
    private Integer projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 施工单位
     */
    private String sgdw;

    /**
     * 评分所属年份
     */
    private Integer year;

    /**
     * 评分所属月份
     */
    private Integer month;

    /**
     * 是否超过80
     * 0通过；1未通过
     */
    private String isPass;

    /**
     * 流程状态
     */
    private GeneralVo statusVo=new GeneralVo();

    @Override
    public int compareTo(SysScoreVo o) {
        if (!this.sectionCode.contains("-")) {
            this.sectionCode = this.sectionCode + "-0";
        }
        if (!o.getSectionCode().contains("-")) {
            o.setSectionCode(o.getSectionCode() + "-0");
        }
        String[] temp1 = this.sectionCode.split("-");
        String[] temp2 = o.getSectionCode().split("-");
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
