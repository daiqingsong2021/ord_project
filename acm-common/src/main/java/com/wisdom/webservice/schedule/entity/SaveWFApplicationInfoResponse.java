
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
 *         &lt;element name="SaveWF_ApplicationInfoResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
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
    "saveWFApplicationInfoResult"
})
@XmlRootElement(name = "SaveWF_ApplicationInfoResponse")
public class SaveWFApplicationInfoResponse {

    @XmlElement(name = "SaveWF_ApplicationInfoResult")
    protected boolean saveWFApplicationInfoResult;

    /**
     * 获取saveWFApplicationInfoResult属性的值。
     * 
     */
    public boolean isSaveWFApplicationInfoResult() {
        return saveWFApplicationInfoResult;
    }

    /**
     * 设置saveWFApplicationInfoResult属性的值。
     * 
     */
    public void setSaveWFApplicationInfoResult(boolean value) {
        this.saveWFApplicationInfoResult = value;
    }

}
