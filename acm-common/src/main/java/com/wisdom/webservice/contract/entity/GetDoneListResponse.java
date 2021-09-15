
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
 *         &lt;element name="GetDoneListResult" type="{http://tempuri.org/}EntityResponseOfWF_DoneList" minOccurs="0"/&gt;
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
    "getDoneListResult"
})
@XmlRootElement(name = "GetDoneListResponse")
public class GetDoneListResponse {

    @XmlElement(name = "GetDoneListResult")
    protected EntityResponseOfWFDoneList getDoneListResult;

    /**
     * 获取getDoneListResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link EntityResponseOfWFDoneList }
     *     
     */
    public EntityResponseOfWFDoneList getGetDoneListResult() {
        return getDoneListResult;
    }

    /**
     * 设置getDoneListResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link EntityResponseOfWFDoneList }
     *     
     */
    public void setGetDoneListResult(EntityResponseOfWFDoneList value) {
        this.getDoneListResult = value;
    }

}
