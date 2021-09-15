
package com.wisdom.webservice.contract.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>EntityResponseOfContractListForGC complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="EntityResponseOfContractListForGC"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://tempuri.org/}BaseInfoResponse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Results" type="{http://tempuri.org/}ArrayOfContractListForGC" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntityResponseOfContractListForGC", propOrder = {
    "results"
})
public class EntityResponseOfContractListForGC
    extends BaseInfoResponse
{

    @XmlElement(name = "Results")
    protected ArrayOfContractListForGC results;

    /**
     * 获取results属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfContractListForGC }
     *     
     */
    public ArrayOfContractListForGC getResults() {
        return results;
    }

    /**
     * 设置results属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfContractListForGC }
     *     
     */
    public void setResults(ArrayOfContractListForGC value) {
        this.results = value;
    }

}
