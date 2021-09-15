
package com.wisdom.webservice.internalstaff.entity;

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
 *         &lt;element name="IsContainPartTimeJob" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
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
    "isContainPartTimeJob"
})
@XmlRootElement(name = "GetUserList")
public class GetUserList {

    @XmlElement(name = "IsContainPartTimeJob")
    protected boolean isContainPartTimeJob;

    /**
     * 获取isContainPartTimeJob属性的值。
     * 
     */
    public boolean isIsContainPartTimeJob() {
        return isContainPartTimeJob;
    }

    /**
     * 设置isContainPartTimeJob属性的值。
     * 
     */
    public void setIsContainPartTimeJob(boolean value) {
        this.isContainPartTimeJob = value;
    }

}
