
package com.wisdom.webservice.contract.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>EntityResponseOfSectionEntity complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="EntityResponseOfSectionEntity"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://tempuri.org/}BaseInfoResponse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Results" type="{http://tempuri.org/}ArrayOfSectionEntity" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntityResponseOfSectionEntity", propOrder = {
    "results"
})
public class EntityResponseOfSectionEntity
    extends BaseInfoResponse
{

    @XmlElement(name = "Results")
    protected ArrayOfSectionEntity results;

    /**
     * 获取results属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSectionEntity }
     *     
     */
    public ArrayOfSectionEntity getResults() {
        return results;
    }

    /**
     * 设置results属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSectionEntity }
     *     
     */
    public void setResults(ArrayOfSectionEntity value) {
        this.results = value;
    }

}
