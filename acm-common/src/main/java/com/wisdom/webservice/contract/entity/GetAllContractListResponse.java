
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
 *         &lt;element name="GetAllContractListResult" type="{http://tempuri.org/}EntityResponseOfContractListForGC" minOccurs="0"/&gt;
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
    "getAllContractListResult"
})
@XmlRootElement(name = "GetAllContractListResponse")
public class GetAllContractListResponse {

    @XmlElement(name = "GetAllContractListResult")
    protected EntityResponseOfContractListForGC getAllContractListResult;

    /**
     * 获取getAllContractListResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link EntityResponseOfContractListForGC }
     *     
     */
    public EntityResponseOfContractListForGC getGetAllContractListResult() {
        return getAllContractListResult;
    }

    /**
     * 设置getAllContractListResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link EntityResponseOfContractListForGC }
     *     
     */
    public void setGetAllContractListResult(EntityResponseOfContractListForGC value) {
        this.getAllContractListResult = value;
    }

}
