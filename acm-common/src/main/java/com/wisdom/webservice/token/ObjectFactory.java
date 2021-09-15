
package com.wisdom.webservice.token;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.szmtr.esb.wsdl.soapheader package. 
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

    private final static QName _ESBSecurityToken_QNAME = new QName("http://esb.szmtr.com/wsdl/soapheader/", "ESBSecurityToken");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.szmtr.esb.wsdl.soapheader
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ESBSecurityToken }
     * 
     */
    public ESBSecurityToken createESBSecurityToken() {
        return new ESBSecurityToken();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ESBSecurityToken }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://esb.szmtr.com/wsdl/soapheader/", name = "ESBSecurityToken")
    public JAXBElement<ESBSecurityToken> createESBSecurityToken(ESBSecurityToken value) {
        return new JAXBElement<ESBSecurityToken>(_ESBSecurityToken_QNAME, ESBSecurityToken.class, null, value);
    }

}
