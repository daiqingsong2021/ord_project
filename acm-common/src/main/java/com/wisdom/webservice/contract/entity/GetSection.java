
package com.wisdom.webservice.contract.entity;

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
 *         &lt;element name="Request" type="{http://tempuri.org/}EntityRequestOfSectionEntity" minOccurs="0"/&gt;
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
    "request"
})
@XmlRootElement(name = "GetSection")
public class GetSection {

    @XmlElement(name = "Request")
    protected EntityRequestOfSectionEntity request;

    /**
     * 获取request属性的值。
     * 
     * @return
     *     possible object is
     *     {@link EntityRequestOfSectionEntity }
     *     
     */
    public EntityRequestOfSectionEntity getRequest() {
        return request;
    }

    /**
     * 设置request属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link EntityRequestOfSectionEntity }
     *     
     */
    public void setRequest(EntityRequestOfSectionEntity value) {
        this.request = value;
    }

}
