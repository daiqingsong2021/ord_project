package com.wisdom.acm.szxm.vo.rygl;

import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

import java.util.List;
import java.util.regex.Pattern;

@Data
public class AddressBookOrgVo extends TreeVo<AddressBookOrgVo> implements Comparable<AddressBookOrgVo>
{

    /**
     *组织机构ID
     */
    private Integer orgId;

    /**
     * 组织机构名称
     */
    private String orgName;

    /**
     * 仅标段使用  标段编码
     */
    private String sectionCode;

    /**
     * 来源  0 项目团队  1 组织信息
     */
    private String source;

    /*
      组织机构org
      标段section
      项目project
     */
    private String type;

    public void addChildrens(List<AddressBookOrgVo> list){
        if(list != null && !list.isEmpty()){
            if(this.getChildren() != null){
                this.getChildren().addAll(list);
            }else{
                this.setChildren(list);
            }
        }
    }

    @Override
    public int compareTo(AddressBookOrgVo o) {
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
