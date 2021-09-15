
package com.wisdom.webservice.contract.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>EntityResponseOfSegContractEntity complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="EntityResponseOfSegContractEntity"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://tempuri.org/}BaseInfoResponse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Results" type="{http://tempuri.org/}ArrayOfSegContractEntity" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntityResponseOfSegContractEntity", propOrder = {
    "results"
})
public class EntityResponseOfSegContractEntity
    extends BaseInfoResponse
{

    @XmlElement(name = "Results")
    protected ArrayOfSegContractEntity results;

    /**
     * 获取results属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSegContractEntity }
     *     
     */
    public ArrayOfSegContractEntity getResults() {
        return results;
    }

    /**
     * 设置results属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSegContractEntity }
     *     
     */
    public void setResults(ArrayOfSegContractEntity value) {
        this.results = value;
    }

}
