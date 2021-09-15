package com.wisdom.acm.szxm.vo.rygl;

import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

import java.util.regex.Pattern;

@Data
public class ProjInfoVo extends TreeVo implements Comparable<ProjInfoVo> {

    /**
     * 主键ID
     */
    private Integer id;

    private Integer sort;

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
     * 标段类型 标段类型Code 对应的 Name
     */
    private String sectionType;

    /**
     * 组织机构名称
     */
    private String orgName;

    /**
     * 单位分类Code 对应的 Name base.org.classification
     */
    private GeneralVo orgCategoryVo = new GeneralVo();

    /**
     * 单位类型Code 对应的 Name base.org.type
     */
    private GeneralVo orgTypeVo = new GeneralVo();

    /**
     * 项目部名称
     */
    private String projUnitName;

    /**
     * 责任人电话
     */
    private String telPhone;

    /**
     * 法人代表
     */
    private String corporationer;

    /**
     * 分管项目部领导
     */
    private String leader;

    /**
     * 技术代表
     */
    private String artisan;

    /**
     * 项目部地址
     */
    private String projUnitAddress;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否项目节点 1 是 0 否
     */
    private String isProject;

    @Override
    public int compareTo(ProjInfoVo o) {
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
