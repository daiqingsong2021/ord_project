
package com.wisdom.webservice.outstaff.entity;

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
 *         &lt;element name="ModifyPasswordResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
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
    "modifyPasswordResult"
})
@XmlRootElement(name = "ModifyPasswordResponse")
public class ModifyPasswordResponse {

    @XmlElement(name = "ModifyPasswordResult")
    protected boolean modifyPasswordResult;

    /**
     * 获取modifyPasswordResult属性的值。
     * 
     */
    public boolean isModifyPasswordResult() {
        return modifyPasswordResult;
    }

    /**
     * 设置modifyPasswordResult属性的值。
     * 
     */
    public void setModifyPasswordResult(boolean value) {
        this.modifyPasswordResult = value;
    }

}
