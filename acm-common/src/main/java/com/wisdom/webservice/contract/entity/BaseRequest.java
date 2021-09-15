
package com.wisdom.webservice.contract.entity;

import javax.xml.bind.annotation.*;

/**
 * <p>BaseRequest complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="BaseRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IsPaging" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="PageIndex" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="PageCount" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="SortingField" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Direction" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseRequest", propOrder = {
    "isPaging",
    "pageIndex",
    "pageCount",
    "sortingField",
    "direction"
})
@XmlSeeAlso({
    EntityRequestOfSectionEntity.class,
    EntityRequestOfContractListForGC.class
})
public class BaseRequest {

    @XmlElement(name = "IsPaging")
    protected boolean isPaging;
    @XmlElement(name = "PageIndex", required = true, type = Integer.class, nillable = true)
    protected Integer pageIndex;
    @XmlElement(name = "PageCount", required = true, type = Integer.class, nillable = true)
    protected Integer pageCount;
    @XmlElement(name = "SortingField")
    protected String sortingField;
    @XmlElement(name = "Direction")
    protected String direction;

    /**
     * 获取isPaging属性的值。
     * 
     */
    public boolean isIsPaging() {
        return isPaging;
    }

    /**
     * 设置isPaging属性的值。
     * 
     */
    public void setIsPaging(boolean value) {
        this.isPaging = value;
    }

    /**
     * 获取pageIndex属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPageIndex() {
        return pageIndex;
    }

    /**
     * 设置pageIndex属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPageIndex(Integer value) {
        this.pageIndex = value;
    }

    /**
     * 获取pageCount属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPageCount() {
        return pageCount;
    }

    /**
     * 设置pageCount属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPageCount(Integer value) {
        this.pageCount = value;
    }

    /**
     * 获取sortingField属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSortingField() {
        return sortingField;
    }

    /**
     * 设置sortingField属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSortingField(String value) {
        this.sortingField = value;
    }

    /**
     * 获取direction属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirection() {
        return direction;
    }

    /**
     * 设置direction属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirection(String value) {
        this.direction = value;
    }

}
