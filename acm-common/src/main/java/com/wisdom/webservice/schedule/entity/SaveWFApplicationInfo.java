
package com.wisdom.webservice.schedule.entity;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SystemID" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ProcName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ImpoLevel" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="Alias" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PasserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ReceTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="LoginID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SystemURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ProcID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TaskID" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="AppName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CreatorName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "systemID",
    "procName",
    "impoLevel",
    "alias",
    "passerName",
    "receTime",
    "loginID",
    "systemURL",
    "procID",
    "taskID",
    "appName",
    "creatorName"
})
@XmlRootElement(name = "SaveWF_ApplicationInfo")
public class SaveWFApplicationInfo {

    @XmlElement(name = "SystemID", required = true, type = Integer.class, nillable = true)
    protected Integer systemID;
    @XmlElement(name = "ProcName")
    protected String procName;
    @XmlElement(name = "ImpoLevel", required = true, type = Integer.class, nillable = true)
    protected Integer impoLevel;
    @XmlElement(name = "Alias")
    protected String alias;
    @XmlElement(name = "PasserName")
    protected String passerName;
    @XmlElement(name = "ReceTime", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar receTime;
    @XmlElement(name = "LoginID")
    protected String loginID;
    @XmlElement(name = "SystemURL")
    protected String systemURL;
    @XmlElement(name = "ProcID")
    protected String procID;
    @XmlElement(name = "TaskID", required = true, type = Integer.class, nillable = true)
    protected Integer taskID;
    @XmlElement(name = "AppName")
    protected String appName;
    @XmlElement(name = "CreatorName")
    protected String creatorName;

    /**
     * 获取systemID属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSystemID() {
        return systemID;
    }

    /**
     * 设置systemID属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSystemID(Integer value) {
        this.systemID = value;
    }

    /**
     * 获取procName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcName() {
        return procName;
    }

    /**
     * 设置procName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcName(String value) {
        this.procName = value;
    }

    /**
     * 获取impoLevel属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getImpoLevel() {
        return impoLevel;
    }

    /**
     * 设置impoLevel属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setImpoLevel(Integer value) {
        this.impoLevel = value;
    }

    /**
     * 获取alias属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 设置alias属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlias(String value) {
        this.alias = value;
    }

    /**
     * 获取passerName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPasserName() {
        return passerName;
    }

    /**
     * 设置passerName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPasserName(String value) {
        this.passerName = value;
    }

    /**
     * 获取receTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReceTime() {
        return receTime;
    }

    /**
     * 设置receTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReceTime(XMLGregorianCalendar value) {
        this.receTime = value;
    }

    /**
     * 获取loginID属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoginID() {
        return loginID;
    }

    /**
     * 设置loginID属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoginID(String value) {
        this.loginID = value;
    }

    /**
     * 获取systemURL属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemURL() {
        return systemURL;
    }

    /**
     * 设置systemURL属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemURL(String value) {
        this.systemURL = value;
    }

    /**
     * 获取procID属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcID() {
        return procID;
    }

    /**
     * 设置procID属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcID(String value) {
        this.procID = value;
    }

    /**
     * 获取taskID属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTaskID() {
        return taskID;
    }

    /**
     * 设置taskID属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTaskID(Integer value) {
        this.taskID = value;
    }

    /**
     * 获取appName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppName() {
        return appName;
    }

    /**
     * 设置appName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppName(String value) {
        this.appName = value;
    }

    /**
     * 获取creatorName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * 设置creatorName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreatorName(String value) {
        this.creatorName = value;
    }

}
