
package com.wisdom.webservice.outstaff.entity;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cn.webservice.outstaff package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.webservice.outstaff
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetUUVPortalUserList }
     * 
     */
    public GetUUVPortalUserList createGetUUVPortalUserList() {
        return new GetUUVPortalUserList();
    }

    /**
     * Create an instance of {@link GetUUVPortalUserListResponse }
     * 
     */
    public GetUUVPortalUserListResponse createGetUUVPortalUserListResponse() {
        return new GetUUVPortalUserListResponse();
    }

    /**
     * Create an instance of {@link ArrayOfUserInfo }
     * 
     */
    public ArrayOfUserInfo createArrayOfUserInfo() {
        return new ArrayOfUserInfo();
    }

    /**
     * Create an instance of {@link CreateUUVPortalUser }
     * 
     */
    public CreateUUVPortalUser createCreateUUVPortalUser() {
        return new CreateUUVPortalUser();
    }

    /**
     * Create an instance of {@link CreateUUVPortalUserResponse }
     * 
     */
    public CreateUUVPortalUserResponse createCreateUUVPortalUserResponse() {
        return new CreateUUVPortalUserResponse();
    }

    /**
     * Create an instance of {@link ModifyPassword }
     * 
     */
    public ModifyPassword createModifyPassword() {
        return new ModifyPassword();
    }

    /**
     * Create an instance of {@link ModifyPasswordResponse }
     * 
     */
    public ModifyPasswordResponse createModifyPasswordResponse() {
        return new ModifyPasswordResponse();
    }

    /**
     * Create an instance of {@link UserInfo }
     * 
     */
    public UserInfo createUserInfo() {
        return new UserInfo();
    }

}
