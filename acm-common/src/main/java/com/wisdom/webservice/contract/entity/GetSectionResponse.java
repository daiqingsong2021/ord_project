
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
 *         &lt;element name="GetSectionResult" type="{http://tempuri.org/}EntityResponseOfSectionEntity" minOccurs="0"/&gt;
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
    "getSectionResult"
})
@XmlRootElement(name = "GetSectionResponse")
public class GetSectionResponse {

    @XmlElement(name = "GetSectionResult")
    protected EntityResponseOfSectionEntity getSectionResult;

    /**
     * 获取getSectionResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link EntityResponseOfSectionEntity }
     *     
     */
    public EntityResponseOfSectionEntity getGetSectionResult() {
        return getSectionResult;
    }

    /**
     * 设置getSectionResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link EntityResponseOfSectionEntity }
     *     
     */
    public void setGetSectionResult(EntityResponseOfSectionEntity value) {
        this.getSectionResult = value;
    }

}
