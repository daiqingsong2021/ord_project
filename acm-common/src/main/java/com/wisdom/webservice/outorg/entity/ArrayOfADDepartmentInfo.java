
package com.wisdom.webservice.outorg.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>ArrayOfADDepartmentInfo complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayOfADDepartmentInfo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ADDepartmentInfo" type="{http://tempuri.org/}ADDepartmentInfo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfADDepartmentInfo", propOrder = {
    "adDepartmentInfo"
})
public class ArrayOfADDepartmentInfo {

    @XmlElement(name = "ADDepartmentInfo", nillable = true)
    protected List<ADDepartmentInfo> adDepartmentInfo;

    /**
     * Gets the value of the adDepartmentInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the adDepartmentInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getADDepartmentInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ADDepartmentInfo }
     * 
     * 
     */
    public List<ADDepartmentInfo> getADDepartmentInfo() {
        if (adDepartmentInfo == null) {
            adDepartmentInfo = new ArrayList<ADDepartmentInfo>();
        }
        return this.adDepartmentInfo;
    }

}
