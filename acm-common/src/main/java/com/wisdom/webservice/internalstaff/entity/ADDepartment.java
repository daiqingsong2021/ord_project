
package com.wisdom.webservice.internalstaff.entity;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>ADDepartment complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ADDepartment"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Column1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Column2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Column3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Column4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Column5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CreateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Creator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DeptCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Deptduty" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DeptFullName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Line" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DeptID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DeptName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Fax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="GroupMailBox" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="GroupName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IsVOrg" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="Level" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="OrderNo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ParentDeptCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ParentDeptID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PosCount" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="Post" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Region" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SecondLevelDeptFullName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SimpleNameCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Street" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Telephone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="UpdateBy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="UpdateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="UserCount" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ADDepartment", propOrder = {
    "column1",
    "column2",
    "column3",
    "column4",
    "column5",
    "createTime",
    "creator",
    "deptCode",
    "deptduty",
    "deptFullName",
    "line",
    "deptID",
    "deptName",
    "fax",
    "groupMailBox",
    "groupName",
    "isVOrg",
    "level",
    "orderNo",
    "parentDeptCode",
    "parentDeptID",
    "posCount",
    "post",
    "region",
    "secondLevelDeptFullName",
    "simpleNameCode",
    "status",
    "street",
    "telephone",
    "updateBy",
    "updateTime",
    "userCount"
})
public class ADDepartment {

    @XmlElement(name = "Column1")
    protected String column1;
    @XmlElement(name = "Column2")
    protected String column2;
    @XmlElement(name = "Column3")
    protected String column3;
    @XmlElement(name = "Column4")
    protected String column4;
    @XmlElement(name = "Column5")
    protected String column5;
    @XmlElement(name = "CreateTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createTime;
    @XmlElement(name = "Creator")
    protected String creator;
    @XmlElement(name = "DeptCode")
    protected String deptCode;
    @XmlElement(name = "Deptduty")
    protected String deptduty;
    @XmlElement(name = "DeptFullName")
    protected String deptFullName;
    @XmlElement(name = "Line")
    protected String line;
    @XmlElement(name = "DeptID")
    protected String deptID;
    @XmlElement(name = "DeptName")
    protected String deptName;
    @XmlElement(name = "Fax")
    protected String fax;
    @XmlElement(name = "GroupMailBox")
    protected String groupMailBox;
    @XmlElement(name = "GroupName")
    protected String groupName;
    @XmlElement(name = "IsVOrg")
    protected int isVOrg;
    @XmlElement(name = "Level")
    protected int level;
    @XmlElement(name = "OrderNo")
    protected int orderNo;
    @XmlElement(name = "ParentDeptCode")
    protected String parentDeptCode;
    @XmlElement(name = "ParentDeptID")
    protected String parentDeptID;
    @XmlElement(name = "PosCount")
    protected int posCount;
    @XmlElement(name = "Post")
    protected String post;
    @XmlElement(name = "Region")
    protected String region;
    @XmlElement(name = "SecondLevelDeptFullName")
    protected String secondLevelDeptFullName;
    @XmlElement(name = "SimpleNameCode")
    protected String simpleNameCode;
    @XmlElement(name = "Status")
    protected String status;
    @XmlElement(name = "Street")
    protected String street;
    @XmlElement(name = "Telephone")
    protected String telephone;
    @XmlElement(name = "UpdateBy")
    protected String updateBy;
    @XmlElement(name = "UpdateTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar updateTime;
    @XmlElement(name = "UserCount")
    protected int userCount;

    /**
     * 获取column1属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColumn1() {
        return column1;
    }

    /**
     * 设置column1属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumn1(String value) {
        this.column1 = value;
    }

    /**
     * 获取column2属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColumn2() {
        return column2;
    }

    /**
     * 设置column2属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumn2(String value) {
        this.column2 = value;
    }

    /**
     * 获取column3属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColumn3() {
        return column3;
    }

    /**
     * 设置column3属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumn3(String value) {
        this.column3 = value;
    }

    /**
     * 获取column4属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColumn4() {
        return column4;
    }

    /**
     * 设置column4属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumn4(String value) {
        this.column4 = value;
    }

    /**
     * 获取column5属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColumn5() {
        return column5;
    }

    /**
     * 设置column5属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumn5(String value) {
        this.column5 = value;
    }

    /**
     * 获取createTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreateTime() {
        return createTime;
    }

    /**
     * 设置createTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreateTime(XMLGregorianCalendar value) {
        this.createTime = value;
    }

    /**
     * 获取creator属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置creator属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreator(String value) {
        this.creator = value;
    }

    /**
     * 获取deptCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeptCode() {
        return deptCode;
    }

    /**
     * 设置deptCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeptCode(String value) {
        this.deptCode = value;
    }

    /**
     * 获取deptduty属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeptduty() {
        return deptduty;
    }

    /**
     * 设置deptduty属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeptduty(String value) {
        this.deptduty = value;
    }

    /**
     * 获取deptFullName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeptFullName() {
        return deptFullName;
    }

    /**
     * 设置deptFullName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeptFullName(String value) {
        this.deptFullName = value;
    }

    /**
     * 获取line属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLine() {
        return line;
    }

    /**
     * 设置line属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLine(String value) {
        this.line = value;
    }

    /**
     * 获取deptID属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeptID() {
        return deptID;
    }

    /**
     * 设置deptID属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeptID(String value) {
        this.deptID = value;
    }

    /**
     * 获取deptName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeptName() {
        return deptName;
    }

    /**
     * 设置deptName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeptName(String value) {
        this.deptName = value;
    }

    /**
     * 获取fax属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFax() {
        return fax;
    }

    /**
     * 设置fax属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFax(String value) {
        this.fax = value;
    }

    /**
     * 获取groupMailBox属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupMailBox() {
        return groupMailBox;
    }

    /**
     * 设置groupMailBox属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupMailBox(String value) {
        this.groupMailBox = value;
    }

    /**
     * 获取groupName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * 设置groupName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupName(String value) {
        this.groupName = value;
    }

    /**
     * 获取isVOrg属性的值。
     * 
     */
    public int getIsVOrg() {
        return isVOrg;
    }

    /**
     * 设置isVOrg属性的值。
     * 
     */
    public void setIsVOrg(int value) {
        this.isVOrg = value;
    }

    /**
     * 获取level属性的值。
     * 
     */
    public int getLevel() {
        return level;
    }

    /**
     * 设置level属性的值。
     * 
     */
    public void setLevel(int value) {
        this.level = value;
    }

    /**
     * 获取orderNo属性的值。
     * 
     */
    public int getOrderNo() {
        return orderNo;
    }

    /**
     * 设置orderNo属性的值。
     * 
     */
    public void setOrderNo(int value) {
        this.orderNo = value;
    }

    /**
     * 获取parentDeptCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentDeptCode() {
        return parentDeptCode;
    }

    /**
     * 设置parentDeptCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentDeptCode(String value) {
        this.parentDeptCode = value;
    }

    /**
     * 获取parentDeptID属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentDeptID() {
        return parentDeptID;
    }

    /**
     * 设置parentDeptID属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentDeptID(String value) {
        this.parentDeptID = value;
    }

    /**
     * 获取posCount属性的值。
     * 
     */
    public int getPosCount() {
        return posCount;
    }

    /**
     * 设置posCount属性的值。
     * 
     */
    public void setPosCount(int value) {
        this.posCount = value;
    }

    /**
     * 获取post属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPost() {
        return post;
    }

    /**
     * 设置post属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPost(String value) {
        this.post = value;
    }

    /**
     * 获取region属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegion() {
        return region;
    }

    /**
     * 设置region属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegion(String value) {
        this.region = value;
    }

    /**
     * 获取secondLevelDeptFullName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecondLevelDeptFullName() {
        return secondLevelDeptFullName;
    }

    /**
     * 设置secondLevelDeptFullName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecondLevelDeptFullName(String value) {
        this.secondLevelDeptFullName = value;
    }

    /**
     * 获取simpleNameCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSimpleNameCode() {
        return simpleNameCode;
    }

    /**
     * 设置simpleNameCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSimpleNameCode(String value) {
        this.simpleNameCode = value;
    }

    /**
     * 获取status属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置status属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * 获取street属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStreet() {
        return street;
    }

    /**
     * 设置street属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStreet(String value) {
        this.street = value;
    }

    /**
     * 获取telephone属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * 设置telephone属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephone(String value) {
        this.telephone = value;
    }

    /**
     * 获取updateBy属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置updateBy属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpdateBy(String value) {
        this.updateBy = value;
    }

    /**
     * 获取updateTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置updateTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUpdateTime(XMLGregorianCalendar value) {
        this.updateTime = value;
    }

    /**
     * 获取userCount属性的值。
     * 
     */
    public int getUserCount() {
        return userCount;
    }

    /**
     * 设置userCount属性的值。
     * 
     */
    public void setUserCount(int value) {
        this.userCount = value;
    }

}
