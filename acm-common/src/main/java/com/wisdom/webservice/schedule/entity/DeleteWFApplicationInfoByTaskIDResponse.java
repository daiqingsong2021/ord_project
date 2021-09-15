
package com.wisdom.webservice.schedule.entity;

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
 *         &lt;element name="DeleteWF_ApplicationInfoByTaskIDResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
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
    "deleteWFApplicationInfoByTaskIDResult"
})
@XmlRootElement(name = "DeleteWF_ApplicationInfoByTaskIDResponse")
public class DeleteWFApplicationInfoByTaskIDResponse {

    @XmlElement(name = "DeleteWF_ApplicationInfoByTaskIDResult")
    protected boolean deleteWFApplicationInfoByTaskIDResult;

    /**
     * 获取deleteWFApplicationInfoByTaskIDResult属性的值。
     * 
     */
    public boolean isDeleteWFApplicationInfoByTaskIDResult() {
        return deleteWFApplicationInfoByTaskIDResult;
    }

    /**
     * 设置deleteWFApplicationInfoByTaskIDResult属性的值。
     * 
     */
    public void setDeleteWFApplicationInfoByTaskIDResult(boolean value) {
        this.deleteWFApplicationInfoByTaskIDResult = value;
    }

}
