
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
 *         &lt;element name="GetToDoListResult" type="{http://tempuri.org/}EntityResponseOfWF_TodoList" minOccurs="0"/&gt;
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
    "getToDoListResult"
})
@XmlRootElement(name = "GetToDoListResponse")
public class GetToDoListResponse {

    @XmlElement(name = "GetToDoListResult")
    protected EntityResponseOfWFTodoList getToDoListResult;

    /**
     * 获取getToDoListResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link EntityResponseOfWFTodoList }
     *     
     */
    public EntityResponseOfWFTodoList getGetToDoListResult() {
        return getToDoListResult;
    }

    /**
     * 设置getToDoListResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link EntityResponseOfWFTodoList }
     *     
     */
    public void setGetToDoListResult(EntityResponseOfWFTodoList value) {
        this.getToDoListResult = value;
    }

}
