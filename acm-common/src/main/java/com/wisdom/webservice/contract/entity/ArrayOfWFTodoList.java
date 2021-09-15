
package com.wisdom.webservice.contract.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>ArrayOfWF_TodoList complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayOfWF_TodoList"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="WF_TodoList" type="{http://tempuri.org/}WF_TodoList" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfWF_TodoList", propOrder = {
    "wfTodoList"
})
public class ArrayOfWFTodoList {

    @XmlElement(name = "WF_TodoList", nillable = true)
    protected List<WFTodoList> wfTodoList;

    /**
     * Gets the value of the wfTodoList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the wfTodoList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWFTodoList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WFTodoList }
     * 
     * 
     */
    public List<WFTodoList> getWFTodoList() {
        if (wfTodoList == null) {
            wfTodoList = new ArrayList<WFTodoList>();
        }
        return this.wfTodoList;
    }

}
