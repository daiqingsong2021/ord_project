
package com.wisdom.webservice.internalstaff.entity;

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
     * Create an instance of {@link GetDepartMentList }
     * 
     */
    public GetDepartMentList createGetDepartMentList() {
        return new GetDepartMentList();
    }

    /**
     * Create an instance of {@link GetDepartMentListResponse }
     * 
     */
    public GetDepartMentListResponse createGetDepartMentListResponse() {
        return new GetDepartMentListResponse();
    }

    /**
     * Create an instance of {@link ArrayOfADDepartment }
     * 
     */
    public ArrayOfADDepartment createArrayOfADDepartment() {
        return new ArrayOfADDepartment();
    }

    /**
     * Create an instance of {@link GetUserList }
     * 
     */
    public GetUserList createGetUserList() {
        return new GetUserList();
    }

    /**
     * Create an instance of {@link GetUserListResponse }
     * 
     */
    public GetUserListResponse createGetUserListResponse() {
        return new GetUserListResponse();
    }

    /**
     * Create an instance of {@link ArrayOfADUser }
     * 
     */
    public ArrayOfADUser createArrayOfADUser() {
        return new ArrayOfADUser();
    }

    /**
     * Create an instance of {@link ADDepartment }
     * 
     */
    public ADDepartment createADDepartment() {
        return new ADDepartment();
    }

    /**
     * Create an instance of {@link ADUser }
     * 
     */
    public ADUser createADUser() {
        return new ADUser();
    }

}
