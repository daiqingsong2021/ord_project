
package com.wisdom.webservice.schedule.entity;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cn.webservice.daiban package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.webservice.daiban
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SaveWFApplicationInfo }
     * 
     */
    public SaveWFApplicationInfo createSaveWFApplicationInfo() {
        return new SaveWFApplicationInfo();
    }

    /**
     * Create an instance of {@link SaveWFApplicationInfoResponse }
     * 
     */
    public SaveWFApplicationInfoResponse createSaveWFApplicationInfoResponse() {
        return new SaveWFApplicationInfoResponse();
    }

    /**
     * Create an instance of {@link DeleteWFApplicationInfo }
     * 
     */
    public DeleteWFApplicationInfo createDeleteWFApplicationInfo() {
        return new DeleteWFApplicationInfo();
    }

    /**
     * Create an instance of {@link DeleteWFApplicationInfoResponse }
     * 
     */
    public DeleteWFApplicationInfoResponse createDeleteWFApplicationInfoResponse() {
        return new DeleteWFApplicationInfoResponse();
    }

    /**
     * Create an instance of {@link DeleteWFApplicationInfoByTaskID }
     * 
     */
    public DeleteWFApplicationInfoByTaskID createDeleteWFApplicationInfoByTaskID() {
        return new DeleteWFApplicationInfoByTaskID();
    }

    /**
     * Create an instance of {@link DeleteWFApplicationInfoByTaskIDResponse }
     * 
     */
    public DeleteWFApplicationInfoByTaskIDResponse createDeleteWFApplicationInfoByTaskIDResponse() {
        return new DeleteWFApplicationInfoByTaskIDResponse();
    }

}
