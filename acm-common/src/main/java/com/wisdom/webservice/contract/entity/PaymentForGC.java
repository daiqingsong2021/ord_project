
package com.wisdom.webservice.contract.entity;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

/**
 * <p>PaymentForGC complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="PaymentForGC"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ContractPayUid" type="{http://microsoft.com/wsdl/types/}guid"/&gt;
 *         &lt;element name="ContractPayCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ContractPayApplyCalcDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="ContractPayApplyDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="ContractPayType" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="ContractPayShouldMoney" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="ContractPayForeignShouldMoney" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="ContractPayActMoney" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="ContractPayForeignActMoney" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="ContractPayApplyedDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="ContractPayDyMoney" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentForGC", propOrder = {
        "contractPayUid",
        "contractPayCode",
        "contractPayApplyCalcDate",
        "contractPayApplyDate",
        "contractPayApplyedDate",
        "contractPayType",
        "contractPayShouldMoney",
        "contractPayForeignShouldMoney",
        "contractPayActMoney",
        "contractPayForeignActMoney",
        "contractPayDyMoney"
})
public class PaymentForGC {

    @XmlElement(name = "ContractPayUid", required = true, nillable = true)
    protected String contractPayUid;
    @XmlElement(name = "ContractPayCode")
    protected String contractPayCode;
    @XmlElement(name = "ContractPayApplyCalcDate", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar contractPayApplyCalcDate;
    @XmlElement(name = "ContractPayApplyDate", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar contractPayApplyDate;
    @XmlElement(name = "ContractPayApplyedDate", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar contractPayApplyedDate;
    @XmlElement(name = "ContractPayType")
    protected int contractPayType;
    @XmlElement(name = "ContractPayShouldMoney", required = true, nillable = true)
    protected BigDecimal contractPayShouldMoney;
    @XmlElement(name = "ContractPayForeignShouldMoney", required = true, nillable = true)
    protected BigDecimal contractPayForeignShouldMoney;
    @XmlElement(name = "ContractPayActMoney", required = true, nillable = true)
    protected BigDecimal contractPayActMoney;
    @XmlElement(name = "ContractPayForeignActMoney", required = true, nillable = true)
    protected BigDecimal contractPayForeignActMoney;
    @XmlElement(name = "ContractPayDyMoney", required = true, nillable = true)
    protected BigDecimal contractPayDyMoney;

    /**
     * 批准时间
     *
     * @return
     */
    public XMLGregorianCalendar getContractPayApplyedDate() {
        return contractPayApplyedDate;
    }

    /**
     * 批准时间
     *
     * @param contractPayApplyedDate
     */
    public void setContractPayApplyedDate(XMLGregorianCalendar contractPayApplyedDate) {
        this.contractPayApplyedDate = contractPayApplyedDate;
    }

    /**
     * 抵押金 金额
     *
     * @return
     */
    public BigDecimal getContractPayDyMoney() {
        return contractPayDyMoney;
    }

    /**
     * 抵押金金额
     *
     * @param contractPayDyMoney
     */
    public void setContractPayDyMoney(BigDecimal contractPayDyMoney) {
        this.contractPayDyMoney = contractPayDyMoney;
    }

    /**
     * 获取contractPayUid属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getContractPayUid() {
        return contractPayUid;
    }

    /**
     * 设置contractPayUid属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setContractPayUid(String value) {
        this.contractPayUid = value;
    }

    /**
     * 获取contractPayCode属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getContractPayCode() {
        return contractPayCode;
    }

    /**
     * 设置contractPayCode属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setContractPayCode(String value) {
        this.contractPayCode = value;
    }

    /**
     * 获取contractPayApplyCalcDate属性的值。
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getContractPayApplyCalcDate() {
        return contractPayApplyCalcDate;
    }

    /**
     * 设置contractPayApplyCalcDate属性的值。
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setContractPayApplyCalcDate(XMLGregorianCalendar value) {
        this.contractPayApplyCalcDate = value;
    }

    /**
     * 获取contractPayApplyDate属性的值。
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getContractPayApplyDate() {
        return contractPayApplyDate;
    }

    /**
     * 设置contractPayApplyDate属性的值。
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setContractPayApplyDate(XMLGregorianCalendar value) {
        this.contractPayApplyDate = value;
    }

    /**
     * 获取contractPayType属性的值。
     */
    public int getContractPayType() {
        return contractPayType;
    }

    /**
     * 设置contractPayType属性的值。
     */
    public void setContractPayType(int value) {
        this.contractPayType = value;
    }

    /**
     * 获取contractPayShouldMoney属性的值。
     *
     * @return possible object is
     * {@link BigDecimal }
     */
    public BigDecimal getContractPayShouldMoney() {
        return contractPayShouldMoney;
    }

    /**
     * 设置contractPayShouldMoney属性的值。
     *
     * @param value allowed object is
     *              {@link BigDecimal }
     */
    public void setContractPayShouldMoney(BigDecimal value) {
        this.contractPayShouldMoney = value;
    }

    /**
     * 获取contractPayForeignShouldMoney属性的值。
     *
     * @return possible object is
     * {@link BigDecimal }
     */
    public BigDecimal getContractPayForeignShouldMoney() {
        return contractPayForeignShouldMoney;
    }

    /**
     * 设置contractPayForeignShouldMoney属性的值。
     *
     * @param value allowed object is
     *              {@link BigDecimal }
     */
    public void setContractPayForeignShouldMoney(BigDecimal value) {
        this.contractPayForeignShouldMoney = value;
    }

    /**
     * 获取contractPayActMoney属性的值。
     *
     * @return possible object is
     * {@link BigDecimal }
     */
    public BigDecimal getContractPayActMoney() {
        return contractPayActMoney;
    }

    /**
     * 设置contractPayActMoney属性的值。
     *
     * @param value allowed object is
     *              {@link BigDecimal }
     */
    public void setContractPayActMoney(BigDecimal value) {
        this.contractPayActMoney = value;
    }

    /**
     * 获取contractPayForeignActMoney属性的值。
     *
     * @return possible object is
     * {@link BigDecimal }
     */
    public BigDecimal getContractPayForeignActMoney() {
        return contractPayForeignActMoney;
    }

    /**
     * 设置contractPayForeignActMoney属性的值。
     *
     * @param value allowed object is
     *              {@link BigDecimal }
     */
    public void setContractPayForeignActMoney(BigDecimal value) {
        this.contractPayForeignActMoney = value;
    }

    @Override
    public String toString() {
        return "PaymentForGC{" +
                "contractPayUid='" + contractPayUid + '\'' +
                ", contractPayCode='" + contractPayCode + '\'' +
                ", contractPayApplyCalcDate=" + contractPayApplyCalcDate +
                ", contractPayApplyDate=" + contractPayApplyDate +
                ", contractPayType=" + contractPayType +
                ", contractPayShouldMoney=" + contractPayShouldMoney +
                ", contractPayForeignShouldMoney=" + contractPayForeignShouldMoney +
                ", contractPayActMoney=" + contractPayActMoney +
                ", contractPayForeignActMoney=" + contractPayForeignActMoney +
                ", contractPayDyMoney=" + contractPayDyMoney +
                ", contractPayApplyedDate=" + contractPayApplyedDate +
                '}';
    }
}
