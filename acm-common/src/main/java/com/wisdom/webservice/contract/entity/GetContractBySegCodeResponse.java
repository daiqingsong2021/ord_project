
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
 *         &lt;element name="GetContractBySegCodeResult" type="{http://tempuri.org/}EntityResponseOfSegContractEntity" minOccurs="0"/&gt;
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
    "getContractBySegCodeResult"
})
@XmlRootElement(name = "GetContractBySegCodeResponse")
public class GetContractBySegCodeResponse {

    @XmlElement(name = "GetContractBySegCodeResult")
    protected EntityResponseOfSegContractEntity getContractBySegCodeResult;

    /**
     * 获取getContractBySegCodeResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link EntityResponseOfSegContractEntity }
     *     
     */
    public EntityResponseOfSegContractEntity getGetContractBySegCodeResult() {
        return getContractBySegCodeResult;
    }

    /**
     * 设置getContractBySegCodeResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link EntityResponseOfSegContractEntity }
     *     
     */
    public void setGetContractBySegCodeResult(EntityResponseOfSegContractEntity value) {
        this.getContractBySegCodeResult = value;
    }

}
