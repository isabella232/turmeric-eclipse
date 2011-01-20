
package org.ebayopensource.turmeric.blogs.v1.services;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.ebayopensource.turmeric.blogs.v1.services package. 
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

    private final static QName _GetVersionRequest_QNAME = new QName("http://www.ebayopensource.org/turmeric/blogs/v1/services", "getVersionRequest");
    private final static QName _GetVersionResponse_QNAME = new QName("http://www.ebayopensource.org/turmeric/blogs/v1/services", "getVersionResponse");
    private final static QName _GetVersionResponseVersion_QNAME = new QName("http://www.ebayopensource.org/turmeric/blogs/v1/services", "version");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.ebayopensource.turmeric.blogs.v1.services
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetVersionResponse }
     * 
     */
    public GetVersionResponse createGetVersionResponse() {
        return new GetVersionResponse();
    }

    /**
     * Create an instance of {@link GetVersionRequest }
     * 
     */
    public GetVersionRequest createGetVersionRequest() {
        return new GetVersionRequest();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ebayopensource.org/turmeric/blogs/v1/services", name = "getVersionRequest")
    public JAXBElement<GetVersionRequest> createGetVersionRequest(GetVersionRequest value) {
        return new JAXBElement<GetVersionRequest>(_GetVersionRequest_QNAME, GetVersionRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ebayopensource.org/turmeric/blogs/v1/services", name = "getVersionResponse")
    public JAXBElement<GetVersionResponse> createGetVersionResponse(GetVersionResponse value) {
        return new JAXBElement<GetVersionResponse>(_GetVersionResponse_QNAME, GetVersionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ebayopensource.org/turmeric/blogs/v1/services", name = "version", scope = GetVersionResponse.class)
    public JAXBElement<String> createGetVersionResponseVersion(String value) {
        return new JAXBElement<String>(_GetVersionResponseVersion_QNAME, String.class, GetVersionResponse.class, value);
    }

}
