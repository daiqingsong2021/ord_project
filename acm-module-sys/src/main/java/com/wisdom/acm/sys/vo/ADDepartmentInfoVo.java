package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Author：wqd
 * Date：2019-09-17 13:50
 * Description：<描述>
 */
@Data
public class ADDepartmentInfoVo extends TreeVo<ADDepartmentInfoVo> {

    private String deptID;
    private String deptCode;
    private String parentDeptCode;
    private String deptName;
    private String deptFullName;
    private String line;
    private String simpleNameCode;
    private Integer level;
    private Integer orderNo;
    private String telephone;
    private String fax;
    private String region;
    private String street;
    private String post;
    private String deptduty;
    private Integer posCount;
    private Integer userCount;
    private Short isVOrg;
    private String column1;
    private String column2;
    private String column3;
    private String column4;
    private String column5;
    private String status;
    private String creator;
    private XMLGregorianCalendar createTime;
    private String updateBy;
    private XMLGregorianCalendar updateTime;
    private String groupName;
    private String parentDeptID;
    private String groupMailBox;
    private String secondLevelDeptCode;
    private String secondLevelDeptFullName;
    private String groupAlias;
    private String deptType;
    private XMLGregorianCalendar lastUpdateDate;
    private String dataSource;
    private Integer priority;
}
