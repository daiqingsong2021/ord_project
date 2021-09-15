
package com.wisdom.webservice.contract.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;

/**
 * <p>ContractListForGC complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ContractListForGC"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ContractCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ListPriceNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ListName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ListSpecification" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ListUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ListQuantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="ListManufacturers" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContractListForGC", propOrder = {
    "contractCode",
    "listPriceNo",
    "listName",
    "listSpecification",
    "listUnit",
    "listQuantity",
    "listManufacturers"
})
public class ContractListForGC {

    @XmlElement(name = "ContractCode")
    protected String contractCode;
    @XmlElement(name = "ListPriceNo")
    protected String listPriceNo;
    @XmlElement(name = "ListName")
    protected String listName;
    @XmlElement(name = "ListSpecification")
    protected String listSpecification;
    @XmlElement(name = "ListUnit")
    protected String listUnit;
    @XmlElement(name = "ListQuantity", required = true, nillable = true)
    protected BigDecimal listQuantity;
    @XmlElement(name = "ListManufacturers")
    protected String listManufacturers;

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
     * 获取listPriceNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getListPriceNo() {
        return listPriceNo;
    }

    /**
     * 设置listPriceNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setListPriceNo(String value) {
        this.listPriceNo = value;
    }

    /**
     * 获取listName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getListName() {
        return listName;
    }

    /**
     * 设置listName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setListName(String value) {
        this.listName = value;
    }

    /**
     * 获取listSpecification属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getListSpecification() {
        return listSpecification;
    }

    /**
     * 设置listSpecification属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setListSpecification(String value) {
        this.listSpecification = value;
    }

    /**
     * 获取listUnit属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getListUnit() {
        return listUnit;
    }

    /**
     * 设置listUnit属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setListUnit(String value) {
        this.listUnit = value;
    }

    /**
     * 获取listQuantity属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getListQuantity() {
        return listQuantity;
    }

    /**
     * 设置listQuantity属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setListQuantity(BigDecimal value) {
        this.listQuantity = value;
    }

    /**
     * 获取listManufacturers属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getListManufacturers() {
        return listManufacturers;
    }

    /**
     * 设置listManufacturers属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setListManufacturers(String value) {
        this.listManufacturers = value;
    }

    @Override
    public String toString() {
        return "ContractListForGC{" +
                "contractCode='" + contractCode + '\'' +
                ", listPriceNo='" + listPriceNo + '\'' +
                ", listName='" + listName + '\'' +
                ", listSpecification='" + listSpecification + '\'' +
                ", listUnit='" + listUnit + '\'' +
                ", listQuantity=" + listQuantity +
                ", listManufacturers='" + listManufacturers + '\'' +
                '}';
    }
}
