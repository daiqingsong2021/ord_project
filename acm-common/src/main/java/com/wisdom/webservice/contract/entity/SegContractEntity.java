
package com.wisdom.webservice.contract.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>SegContractEntity complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="SegContractEntity"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SegCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ContractName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ContractCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SegContractEntity", propOrder = {
    "segCode",
    "contractName",
    "contractCode",
    "hasfxyj"
})
public class SegContractEntity {

    @XmlElement(name = "SegCode")
    protected String segCode;
    @XmlElement(name = "ContractName")
    protected String contractName;
    @XmlElement(name = "ContractCode")
    protected String contractCode;
    @XmlElement(name = "HASFXYJ")
    protected String hasfxyj;

    /**
     * 获取segCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSegCode() {
        return segCode;
    }

    /**
     * 设置segCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSegCode(String value) {
        this.segCode = value;
    }

    /**
     * 获取contractName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContractName() {
        return contractName;
    }

    /**
     * 设置contractName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContractName(String value) {
        this.contractName = value;
    }

    /**
     * 获取contractCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContractCode() {
        return contractCode;
    }

    /**
     * 设置contractCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContractCode(String value) {
        this.contractCode = value;
    }

    /**
     * 获取HASFXYJ属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHASFXYJ() {
        return hasfxyj;
    }

    /**
     * 设置HASFXYJ属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHASFXYJ(String value) {
        this.hasfxyj = value;
    }
}
