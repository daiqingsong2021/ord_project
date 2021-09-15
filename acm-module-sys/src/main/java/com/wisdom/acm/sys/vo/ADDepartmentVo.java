package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Author：wqd
 * Date：2019-09-17 13:47
 * Description：<描述>
 */
@Data
public class ADDepartmentVo extends TreeVo<ADDepartmentVo> {

    private String column1;
    private String column2;
    private String column3;
    private String column4;
    private String column5;
    private XMLGregorianCalendar createTime;
    private String creator;
    private String deptCode;
    private String deptduty;
    private String deptFullName;
    private String line;
    private String deptID;
    private String deptName;
    private String fax;
    private String groupMailBox;
    private String groupName;
    private int isVOrg;
    private int level;
    private int orderNo;
    private String parentDeptCode;
    private String parentDeptID;
    private int posCount;
    private String post;
    private String region;
    private String secondLevelDeptFullName;
    private String simpleNameCode;
    private String status;
    private String street;
    private String telephone;
    private String updateBy;
    private XMLGregorianCalendar updateTime;
    private int userCount;
}
