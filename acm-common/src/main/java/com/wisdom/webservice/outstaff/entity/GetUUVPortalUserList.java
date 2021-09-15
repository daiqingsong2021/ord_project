
package com.wisdom.webservice.outstaff.entity;

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
 *         &lt;element name="appId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IsContainPartTimeJob" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="beginLastUpdateDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="endLastUpdateDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
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
    "appId",
    "isContainPartTimeJob",
    "beginLastUpdateDate",
    "endLastUpdateDate"
})
@XmlRootElement(name = "GetUUVPortalUserList")
public class GetUUVPortalUserList {

    protected String appId;
    @XmlElement(name = "IsContainPartTimeJob")
    protected boolean isContainPartTimeJob;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar beginLastUpdateDate;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar endLastUpdateDate;

    /**
     * 获取appId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppId() {
        return appId;
    }

    /**
     * 设置appId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppId(String value) {
        this.appId = value;
    }

    /**
     * 获取isContainPartTimeJob属性的值。
     * 
     */
    public boolean isIsContainPartTimeJob() {
        return isContainPartTimeJob;
    }

    /**
     * 设置isContainPartTimeJob属性的值。
     * 
     */
    public void setIsContainPartTimeJob(boolean value) {
        this.isContainPartTimeJob = value;
    }

    /**
     * 获取beginLastUpdateDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBeginLastUpdateDate() {
        return beginLastUpdateDate;
    }

    /**
     * 设置beginLastUpdateDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBeginLastUpdateDate(XMLGregorianCalendar value) {
        this.beginLastUpdateDate = value;
    }

    /**
     * 获取endLastUpdateDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndLastUpdateDate() {
        return endLastUpdateDate;
    }

    /**
     * 设置endLastUpdateDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndLastUpdateDate(XMLGregorianCalendar value) {
        this.endLastUpdateDate = value;
    }

}
