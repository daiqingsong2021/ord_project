
package com.wisdom.webservice.internalstaff.entity;

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
 *         &lt;element name="GetUserListResult" type="{http://tempuri.org/}ArrayOfADUser" minOccurs="0"/&gt;
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
    "getUserListResult"
})
@XmlRootElement(name = "GetUserListResponse")
public class GetUserListResponse {

    @XmlElement(name = "GetUserListResult")
    protected ArrayOfADUser getUserListResult;

    /**
     * 获取getUserListResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfADUser }
     *     
     */
    public ArrayOfADUser getGetUserListResult() {
        return getUserListResult;
    }

    /**
     * 设置getUserListResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfADUser }
     *     
     */
    public void setGetUserListResult(ArrayOfADUser value) {
        this.getUserListResult = value;
    }

}
