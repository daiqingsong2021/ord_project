
package com.wisdom.webservice.contract.entity;

import javax.xml.bind.annotation.*;

/**
 * <p>BaseInfoResponse complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="BaseInfoResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TotalRecords" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="HasErrors" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="ReturnMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ErrorCode" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseInfoResponse", propOrder = {
    "totalRecords",
    "hasErrors",
    "returnMessage",
    "errorCode"
})
@XmlSeeAlso({
    EntityResponseOfSectionEntity.class,
    EntityResponseOfPaymentForGC.class,
    EntityResponseOfContractListForGC.class,
    EntityResponseOfWFTodoList.class,
    EntityResponseOfWFDoneList.class,
    EntityResponseOfSegContractEntity.class
})
public class BaseInfoResponse {

    @XmlElement(name = "TotalRecords")
    protected int totalRecords;
    @XmlElement(name = "HasErrors")
    protected boolean hasErrors;
    @XmlElement(name = "ReturnMessage")
    protected String returnMessage;
    @XmlElement(name = "ErrorCode")
    protected int errorCode;

    /**
     * 获取totalRecords属性的值。
     * 
     */
    public int getTotalRecords() {
        return totalRecords;
    }

    /**
     * 设置totalRecords属性的值。
     * 
     */
    public void setTotalRecords(int value) {
        this.totalRecords = value;
    }

    /**
     * 获取hasErrors属性的值。
     * 
     */
    public boolean isHasErrors() {
        return hasErrors;
    }

    /**
     * 设置hasErrors属性的值。
     * 
     */
    public void setHasErrors(boolean value) {
        this.hasErrors = value;
    }

    /**
     * 获取returnMessage属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnMessage() {
        return returnMessage;
    }

    /**
     * 设置returnMessage属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnMessage(String value) {
        this.returnMessage = value;
    }

    /**
     * 获取errorCode属性的值。
     * 
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * 设置errorCode属性的值。
     * 
     */
    public void setErrorCode(int value) {
        this.errorCode = value;
    }

}
