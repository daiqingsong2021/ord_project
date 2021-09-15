
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
 *         &lt;element name="GetPaymentByContractCodeResult" type="{http://tempuri.org/}EntityResponseOfPaymentForGC" minOccurs="0"/&gt;
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
    "getPaymentByContractCodeResult"
})
@XmlRootElement(name = "GetPaymentByContractCodeResponse")
public class GetPaymentByContractCodeResponse {

    @XmlElement(name = "GetPaymentByContractCodeResult")
    protected EntityResponseOfPaymentForGC getPaymentByContractCodeResult;

    /**
     * 获取getPaymentByContractCodeResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link EntityResponseOfPaymentForGC }
     *     
     */
    public EntityResponseOfPaymentForGC getGetPaymentByContractCodeResult() {
        return getPaymentByContractCodeResult;
    }

    /**
     * 设置getPaymentByContractCodeResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link EntityResponseOfPaymentForGC }
     *     
     */
    public void setGetPaymentByContractCodeResult(EntityResponseOfPaymentForGC value) {
        this.getPaymentByContractCodeResult = value;
    }

}
