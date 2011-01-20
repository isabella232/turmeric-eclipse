
package ebayopensource.apis.eblbasecomponents;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ebayopensource.apis.eblbasecomponents package. 
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

    private final static QName _CSUpdateMACActivityAddAttachmentsRequest_QNAME = new QName("urn:ebayopensource:apis:eBLBaseComponents", "CSUpdateMACActivityAddAttachmentsRequest");
    private final static QName _CSUpdateMACActivityAddAttachmentsResponse_QNAME = new QName("urn:ebayopensource:apis:eBLBaseComponents", "CSUpdateMACActivityAddAttachmentsResponse");
    private final static QName _RequesterCredentials_QNAME = new QName("urn:ebayopensource:apis:eBLBaseComponents", "RequesterCredentials");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ebayopensource.apis.eblbasecomponents
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CSUpdateMACActivityAddAttachmentsRequestType }
     * 
     */
    public CSUpdateMACActivityAddAttachmentsRequestType createCSUpdateMACActivityAddAttachmentsRequestType() {
        return new CSUpdateMACActivityAddAttachmentsRequestType();
    }

    /**
     * Create an instance of {@link ErrorType }
     * 
     */
    public ErrorType createErrorType() {
        return new ErrorType();
    }

    /**
     * Create an instance of {@link XMLRequesterCredentialsType }
     * 
     */
    public XMLRequesterCredentialsType createXMLRequesterCredentialsType() {
        return new XMLRequesterCredentialsType();
    }

    /**
     * Create an instance of {@link CSUserIdType }
     * 
     */
    public CSUserIdType createCSUserIdType() {
        return new CSUserIdType();
    }

    /**
     * Create an instance of {@link CSUpdateMACActivityAddAttachmentsResponseType }
     * 
     */
    public CSUpdateMACActivityAddAttachmentsResponseType createCSUpdateMACActivityAddAttachmentsResponseType() {
        return new CSUpdateMACActivityAddAttachmentsResponseType();
    }

    /**
     * Create an instance of {@link BotBlockRequestType }
     * 
     */
    public BotBlockRequestType createBotBlockRequestType() {
        return new BotBlockRequestType();
    }

    /**
     * Create an instance of {@link MACAttachementFileType }
     * 
     */
    public MACAttachementFileType createMACAttachementFileType() {
        return new MACAttachementFileType();
    }

    /**
     * Create an instance of {@link UserIdPasswordType }
     * 
     */
    public UserIdPasswordType createUserIdPasswordType() {
        return new UserIdPasswordType();
    }

    /**
     * Create an instance of {@link MachineTagTypeArrayType }
     * 
     */
    public MachineTagTypeArrayType createMachineTagTypeArrayType() {
        return new MachineTagTypeArrayType();
    }

    /**
     * Create an instance of {@link ErrorParameterType }
     * 
     */
    public ErrorParameterType createErrorParameterType() {
        return new ErrorParameterType();
    }

    /**
     * Create an instance of {@link CustomSecurityHeaderType }
     * 
     */
    public CustomSecurityHeaderType createCustomSecurityHeaderType() {
        return new CustomSecurityHeaderType();
    }

    /**
     * Create an instance of {@link DuplicateInvocationDetailsType }
     * 
     */
    public DuplicateInvocationDetailsType createDuplicateInvocationDetailsType() {
        return new DuplicateInvocationDetailsType();
    }

    /**
     * Create an instance of {@link BotBlockResponseType }
     * 
     */
    public BotBlockResponseType createBotBlockResponseType() {
        return new BotBlockResponseType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CSUpdateMACActivityAddAttachmentsRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ebayopensource:apis:eBLBaseComponents", name = "CSUpdateMACActivityAddAttachmentsRequest")
    public JAXBElement<CSUpdateMACActivityAddAttachmentsRequestType> createCSUpdateMACActivityAddAttachmentsRequest(CSUpdateMACActivityAddAttachmentsRequestType value) {
        return new JAXBElement<CSUpdateMACActivityAddAttachmentsRequestType>(_CSUpdateMACActivityAddAttachmentsRequest_QNAME, CSUpdateMACActivityAddAttachmentsRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CSUpdateMACActivityAddAttachmentsResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ebayopensource:apis:eBLBaseComponents", name = "CSUpdateMACActivityAddAttachmentsResponse")
    public JAXBElement<CSUpdateMACActivityAddAttachmentsResponseType> createCSUpdateMACActivityAddAttachmentsResponse(CSUpdateMACActivityAddAttachmentsResponseType value) {
        return new JAXBElement<CSUpdateMACActivityAddAttachmentsResponseType>(_CSUpdateMACActivityAddAttachmentsResponse_QNAME, CSUpdateMACActivityAddAttachmentsResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomSecurityHeaderType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ebayopensource:apis:eBLBaseComponents", name = "RequesterCredentials")
    public JAXBElement<CustomSecurityHeaderType> createRequesterCredentials(CustomSecurityHeaderType value) {
        return new JAXBElement<CustomSecurityHeaderType>(_RequesterCredentials_QNAME, CustomSecurityHeaderType.class, null, value);
    }

}
