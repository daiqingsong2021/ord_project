
package com.wisdom.webservice.internalstaff.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>ArrayOfADUser complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayOfADUser"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ADUser" type="{http://tempuri.org/}ADUser" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfADUser", propOrder = {
    "adUser"
})
public class ArrayOfADUser {

    @XmlElement(name = "ADUser", nillable = true)
    protected List<ADUser> adUser;

    /**
     * Gets the value of the adUser property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the adUser property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getADUser().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ADUser }
     * 
     * 
     */
    public List<ADUser> getADUser() {
        if (adUser == null) {
            adUser = new ArrayList<ADUser>();
        }
        return this.adUser;
    }

}
