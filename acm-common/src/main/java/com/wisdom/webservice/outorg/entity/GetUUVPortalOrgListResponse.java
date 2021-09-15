
package com.wisdom.webservice.outorg.entity;

import javax.xml.bind.annotation.*;

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
 *         &lt;element name="GetUUVPortalOrgListResult" type="{http://tempuri.org/}ArrayOfADDepartmentInfo" minOccurs="0"/&gt;
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
    "getUUVPortalOrgListResult"
})
@XmlRootElement(name = "GetUUVPortalOrgListResponse")
public class GetUUVPortalOrgListResponse {

    @XmlElement(name = "GetUUVPortalOrgListResult")
    protected ArrayOfADDepartmentInfo getUUVPortalOrgListResult;

    /**
     * 获取getUUVPortalOrgListResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfADDepartmentInfo }
     *     
     */
    public ArrayOfADDepartmentInfo getGetUUVPortalOrgListResult() {
        return getUUVPortalOrgListResult;
    }

    /**
     * 设置getUUVPortalOrgListResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfADDepartmentInfo }
     *     
     */
    public void setGetUUVPortalOrgListResult(ArrayOfADDepartmentInfo value) {
        this.getUUVPortalOrgListResult = value;
    }

}
