
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
 *         &lt;element name="CreateUUVPortalUserResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
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
    "createUUVPortalUserResult"
})
@XmlRootElement(name = "CreateUUVPortalUserResponse")
public class CreateUUVPortalUserResponse {

    @XmlElement(name = "CreateUUVPortalUserResult")
    protected boolean createUUVPortalUserResult;

    /**
     * 获取createUUVPortalUserResult属性的值。
     * 
     */
    public boolean isCreateUUVPortalUserResult() {
        return createUUVPortalUserResult;
    }

    /**
     * 设置createUUVPortalUserResult属性的值。
     * 
     */
    public void setCreateUUVPortalUserResult(boolean value) {
        this.createUUVPortalUserResult = value;
    }

}
