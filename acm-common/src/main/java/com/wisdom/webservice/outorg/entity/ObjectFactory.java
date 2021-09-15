
package com.wisdom.webservice.outorg.entity;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cn.webservice.in package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.webservice.in
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetUUVPortalOrgList }
     * 
     */
    public GetUUVPortalOrgList createGetUUVPortalOrgList() {
        return new GetUUVPortalOrgList();
    }

    /**
     * Create an instance of {@link GetUUVPortalOrgListResponse }
     * 
     */
    public GetUUVPortalOrgListResponse createGetUUVPortalOrgListResponse() {
        return new GetUUVPortalOrgListResponse();
    }

    /**
     * Create an instance of {@link ArrayOfADDepartmentInfo }
     * 
     */
    public ArrayOfADDepartmentInfo createArrayOfADDepartmentInfo() {
        return new ArrayOfADDepartmentInfo();
    }

    /**
     * Create an instance of {@link CreateUUVPortalOrg }
     * 
     */
    public CreateUUVPortalOrg createCreateUUVPortalOrg() {
        return new CreateUUVPortalOrg();
    }

    /**
     * Create an instance of {@link CreateUUVPortalOrgResponse }
     * 
     */
    public CreateUUVPortalOrgResponse createCreateUUVPortalOrgResponse() {
        return new CreateUUVPortalOrgResponse();
    }

    /**
     * Create an instance of {@link ADDepartmentInfo }
     * 
     */
    public ADDepartmentInfo createADDepartmentInfo() {
        return new ADDepartmentInfo();
    }

}
