
package ebayopensource.apis.eblbasecomponents;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.w3c.dom.Element;


/**
 * 
 * 				Base type definition of a response payload that can carry any
 * 				type of payload content with following optional elements:<br>
 * 				- timestamp of response message<br>
 * 				- application-level acknowledgement<br>
 * 				- application-level (business-level) errors and warnings
 * 			
 * 
 * <p>Java class for AbstractResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AbstractResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="Ack" type="{urn:ebayopensource:apis:eBLBaseComponents}AckCodeType" minOccurs="0"/>
 *         &lt;element name="CorrelationID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Errors" type="{urn:ebayopensource:apis:eBLBaseComponents}ErrorType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Build" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PostTransactionData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UsageData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FilteredElement" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="JobID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Complexity" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="NotificationEventName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DuplicateInvocationDetails" type="{urn:ebayopensource:apis:eBLBaseComponents}DuplicateInvocationDetailsType" minOccurs="0"/>
 *         &lt;element name="RecipientUserID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EIASToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NotificationSignature" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HardExpirationWarning" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BotBlock" type="{urn:ebayopensource:apis:eBLBaseComponents}BotBlockResponseType" minOccurs="0"/>
 *         &lt;any/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractResponseType", propOrder = {
    "timestamp",
    "ack",
    "correlationID",
    "errors",
    "message",
    "version",
    "build",
    "postTransactionData",
    "usageData",
    "guid",
    "filteredElement",
    "jobID",
    "complexity",
    "notificationEventName",
    "duplicateInvocationDetails",
    "recipientUserID",
    "eiasToken",
    "notificationSignature",
    "hardExpirationWarning",
    "botBlock",
    "any"
})
@XmlSeeAlso({
    CSUpdateMACActivityAddAttachmentsResponseType.class
})
public abstract class AbstractResponseType {

    @XmlElement(name = "Timestamp")
    protected XMLGregorianCalendar timestamp;
    @XmlElement(name = "Ack")
    protected AckCodeType ack;
    @XmlElement(name = "CorrelationID")
    protected String correlationID;
    @XmlElement(name = "Errors")
    protected List<ErrorType> errors;
    @XmlElement(name = "Message")
    protected String message;
    @XmlElement(name = "Version")
    protected String version;
    @XmlElement(name = "Build")
    protected String build;
    @XmlElement(name = "PostTransactionData")
    protected String postTransactionData;
    @XmlElement(name = "UsageData")
    protected String usageData;
    @XmlElement(name = "GUID")
    protected String guid;
    @XmlElement(name = "FilteredElement")
    protected List<String> filteredElement;
    @XmlElement(name = "JobID")
    protected String jobID;
    @XmlElement(name = "Complexity")
    protected Double complexity;
    @XmlElement(name = "NotificationEventName")
    protected String notificationEventName;
    @XmlElement(name = "DuplicateInvocationDetails")
    protected DuplicateInvocationDetailsType duplicateInvocationDetails;
    @XmlElement(name = "RecipientUserID")
    protected String recipientUserID;
    @XmlElement(name = "EIASToken")
    protected String eiasToken;
    @XmlElement(name = "NotificationSignature")
    protected String notificationSignature;
    @XmlElement(name = "HardExpirationWarning")
    protected String hardExpirationWarning;
    @XmlElement(name = "BotBlock")
    protected BotBlockResponseType botBlock;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the timestamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTimestamp(XMLGregorianCalendar value) {
        this.timestamp = value;
    }

    /**
     * Gets the value of the ack property.
     * 
     * @return
     *     possible object is
     *     {@link AckCodeType }
     *     
     */
    public AckCodeType getAck() {
        return ack;
    }

    /**
     * Sets the value of the ack property.
     * 
     * @param value
     *     allowed object is
     *     {@link AckCodeType }
     *     
     */
    public void setAck(AckCodeType value) {
        this.ack = value;
    }

    /**
     * Gets the value of the correlationID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorrelationID() {
        return correlationID;
    }

    /**
     * Sets the value of the correlationID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorrelationID(String value) {
        this.correlationID = value;
    }

    /**
     * Gets the value of the errors property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the errors property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getErrors().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ErrorType }
     * 
     * 
     */
    public List<ErrorType> getErrors() {
        if (errors == null) {
            errors = new ArrayList<ErrorType>();
        }
        return this.errors;
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the build property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuild() {
        return build;
    }

    /**
     * Sets the value of the build property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuild(String value) {
        this.build = value;
    }

    /**
     * Gets the value of the postTransactionData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostTransactionData() {
        return postTransactionData;
    }

    /**
     * Sets the value of the postTransactionData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostTransactionData(String value) {
        this.postTransactionData = value;
    }

    /**
     * Gets the value of the usageData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsageData() {
        return usageData;
    }

    /**
     * Sets the value of the usageData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsageData(String value) {
        this.usageData = value;
    }

    /**
     * Gets the value of the guid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGUID() {
        return guid;
    }

    /**
     * Sets the value of the guid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGUID(String value) {
        this.guid = value;
    }

    /**
     * Gets the value of the filteredElement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the filteredElement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFilteredElement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getFilteredElement() {
        if (filteredElement == null) {
            filteredElement = new ArrayList<String>();
        }
        return this.filteredElement;
    }

    /**
     * Gets the value of the jobID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobID() {
        return jobID;
    }

    /**
     * Sets the value of the jobID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobID(String value) {
        this.jobID = value;
    }

    /**
     * Gets the value of the complexity property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getComplexity() {
        return complexity;
    }

    /**
     * Sets the value of the complexity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setComplexity(Double value) {
        this.complexity = value;
    }

    /**
     * Gets the value of the notificationEventName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotificationEventName() {
        return notificationEventName;
    }

    /**
     * Sets the value of the notificationEventName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotificationEventName(String value) {
        this.notificationEventName = value;
    }

    /**
     * Gets the value of the duplicateInvocationDetails property.
     * 
     * @return
     *     possible object is
     *     {@link DuplicateInvocationDetailsType }
     *     
     */
    public DuplicateInvocationDetailsType getDuplicateInvocationDetails() {
        return duplicateInvocationDetails;
    }

    /**
     * Sets the value of the duplicateInvocationDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link DuplicateInvocationDetailsType }
     *     
     */
    public void setDuplicateInvocationDetails(DuplicateInvocationDetailsType value) {
        this.duplicateInvocationDetails = value;
    }

    /**
     * Gets the value of the recipientUserID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipientUserID() {
        return recipientUserID;
    }

    /**
     * Sets the value of the recipientUserID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipientUserID(String value) {
        this.recipientUserID = value;
    }

    /**
     * Gets the value of the eiasToken property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEIASToken() {
        return eiasToken;
    }

    /**
     * Sets the value of the eiasToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEIASToken(String value) {
        this.eiasToken = value;
    }

    /**
     * Gets the value of the notificationSignature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotificationSignature() {
        return notificationSignature;
    }

    /**
     * Sets the value of the notificationSignature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotificationSignature(String value) {
        this.notificationSignature = value;
    }

    /**
     * Gets the value of the hardExpirationWarning property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHardExpirationWarning() {
        return hardExpirationWarning;
    }

    /**
     * Sets the value of the hardExpirationWarning property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHardExpirationWarning(String value) {
        this.hardExpirationWarning = value;
    }

    /**
     * Gets the value of the botBlock property.
     * 
     * @return
     *     possible object is
     *     {@link BotBlockResponseType }
     *     
     */
    public BotBlockResponseType getBotBlock() {
        return botBlock;
    }

    /**
     * Sets the value of the botBlock property.
     * 
     * @param value
     *     allowed object is
     *     {@link BotBlockResponseType }
     *     
     */
    public void setBotBlock(BotBlockResponseType value) {
        this.botBlock = value;
    }

    /**
     * Gets the value of the any property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Element }
     * {@link Object }
     * 
     * 
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }

}
