
package ebayopensource.apis.eblbasecomponents;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * 
 * 				Base type definition of the request payload, which can carry any type of payload
 * 				content plus optional versioning information and detail level requirements. All
 * 				concrete request types (e.g., AddItemRequestType) are derived from the abstract
 * 				request type. The naming convention we use for the concrete type names is the name
 * 				of the service (the verb or call name) followed by "RequestType":
 * 				VerbNameRequestType
 * 			
 * 
 * <p>Java class for AbstractRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AbstractRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DetailLevel" type="{urn:ebayopensource:apis:eBLBaseComponents}DetailLevelCodeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ErrorLanguage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MessageID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EndUserIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PostTransactionData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UsageData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BulkJobID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BulkTaskID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HTTPUserAgent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HTTPAccept" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HTTPAcceptLanguage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HTTPAcceptCharset" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HTTPAcceptEncoding" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Priority" type="{urn:ebayopensource:apis:eBLBaseComponents}ActionPriorityCodeType" minOccurs="0"/>
 *         &lt;element name="RequesterCredentials" type="{urn:ebayopensource:apis:eBLBaseComponents}XMLRequesterCredentialsType" minOccurs="0"/>
 *         &lt;element name="ErrorHandling" type="{urn:ebayopensource:apis:eBLBaseComponents}ErrorHandlingCodeType" minOccurs="0"/>
 *         &lt;element name="InvocationID" type="{urn:ebayopensource:apis:eBLBaseComponents}UUIDType" minOccurs="0"/>
 *         &lt;element name="OutputSelector" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="WarningLevel" type="{urn:ebayopensource:apis:eBLBaseComponents}WarningLevelCodeType" minOccurs="0"/>
 *         &lt;element name="BotBlock" type="{urn:ebayopensource:apis:eBLBaseComponents}BotBlockRequestType" minOccurs="0"/>
 *         &lt;element name="MachineGroupID" type="{urn:ebayopensource:apis:eBLBaseComponents}MachineGroupIDType" minOccurs="0"/>
 *         &lt;element name="MachineGroupValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MachineTagTypes" type="{urn:ebayopensource:apis:eBLBaseComponents}MachineTagTypeArrayType" minOccurs="0"/>
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
@XmlType(name = "AbstractRequestType", propOrder = {
    "detailLevel",
    "errorLanguage",
    "messageID",
    "version",
    "endUserIP",
    "postTransactionData",
    "usageData",
    "bulkJobID",
    "bulkTaskID",
    "guid",
    "httpUserAgent",
    "httpAccept",
    "httpAcceptLanguage",
    "httpAcceptCharset",
    "httpAcceptEncoding",
    "priority",
    "requesterCredentials",
    "errorHandling",
    "invocationID",
    "outputSelector",
    "warningLevel",
    "botBlock",
    "machineGroupID",
    "machineGroupValue",
    "machineTagTypes",
    "any"
})
@XmlSeeAlso({
    CSUpdateMACActivityAddAttachmentsRequestType.class
})
public abstract class AbstractRequestType {

    @XmlElement(name = "DetailLevel")
    protected List<DetailLevelCodeType> detailLevel;
    @XmlElement(name = "ErrorLanguage")
    protected String errorLanguage;
    @XmlElement(name = "MessageID")
    protected String messageID;
    @XmlElement(name = "Version")
    protected String version;
    @XmlElement(name = "EndUserIP")
    protected String endUserIP;
    @XmlElement(name = "PostTransactionData")
    protected String postTransactionData;
    @XmlElement(name = "UsageData")
    protected String usageData;
    @XmlElement(name = "BulkJobID")
    protected String bulkJobID;
    @XmlElement(name = "BulkTaskID")
    protected String bulkTaskID;
    @XmlElement(name = "GUID")
    protected String guid;
    @XmlElement(name = "HTTPUserAgent")
    protected String httpUserAgent;
    @XmlElement(name = "HTTPAccept")
    protected String httpAccept;
    @XmlElement(name = "HTTPAcceptLanguage")
    protected String httpAcceptLanguage;
    @XmlElement(name = "HTTPAcceptCharset")
    protected String httpAcceptCharset;
    @XmlElement(name = "HTTPAcceptEncoding")
    protected String httpAcceptEncoding;
    @XmlElement(name = "Priority")
    protected ActionPriorityCodeType priority;
    @XmlElement(name = "RequesterCredentials")
    protected XMLRequesterCredentialsType requesterCredentials;
    @XmlElement(name = "ErrorHandling")
    protected ErrorHandlingCodeType errorHandling;
    @XmlElement(name = "InvocationID")
    protected String invocationID;
    @XmlElement(name = "OutputSelector")
    protected List<String> outputSelector;
    @XmlElement(name = "WarningLevel")
    protected WarningLevelCodeType warningLevel;
    @XmlElement(name = "BotBlock")
    protected BotBlockRequestType botBlock;
    @XmlElement(name = "MachineGroupID")
    protected String machineGroupID;
    @XmlElement(name = "MachineGroupValue")
    protected String machineGroupValue;
    @XmlElement(name = "MachineTagTypes")
    protected MachineTagTypeArrayType machineTagTypes;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the detailLevel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the detailLevel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDetailLevel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DetailLevelCodeType }
     * 
     * 
     */
    public List<DetailLevelCodeType> getDetailLevel() {
        if (detailLevel == null) {
            detailLevel = new ArrayList<DetailLevelCodeType>();
        }
        return this.detailLevel;
    }

    /**
     * Gets the value of the errorLanguage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorLanguage() {
        return errorLanguage;
    }

    /**
     * Sets the value of the errorLanguage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorLanguage(String value) {
        this.errorLanguage = value;
    }

    /**
     * Gets the value of the messageID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageID() {
        return messageID;
    }

    /**
     * Sets the value of the messageID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageID(String value) {
        this.messageID = value;
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
     * Gets the value of the endUserIP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndUserIP() {
        return endUserIP;
    }

    /**
     * Sets the value of the endUserIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndUserIP(String value) {
        this.endUserIP = value;
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
     * Gets the value of the bulkJobID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBulkJobID() {
        return bulkJobID;
    }

    /**
     * Sets the value of the bulkJobID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBulkJobID(String value) {
        this.bulkJobID = value;
    }

    /**
     * Gets the value of the bulkTaskID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBulkTaskID() {
        return bulkTaskID;
    }

    /**
     * Sets the value of the bulkTaskID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBulkTaskID(String value) {
        this.bulkTaskID = value;
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
     * Gets the value of the httpUserAgent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHTTPUserAgent() {
        return httpUserAgent;
    }

    /**
     * Sets the value of the httpUserAgent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHTTPUserAgent(String value) {
        this.httpUserAgent = value;
    }

    /**
     * Gets the value of the httpAccept property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHTTPAccept() {
        return httpAccept;
    }

    /**
     * Sets the value of the httpAccept property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHTTPAccept(String value) {
        this.httpAccept = value;
    }

    /**
     * Gets the value of the httpAcceptLanguage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHTTPAcceptLanguage() {
        return httpAcceptLanguage;
    }

    /**
     * Sets the value of the httpAcceptLanguage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHTTPAcceptLanguage(String value) {
        this.httpAcceptLanguage = value;
    }

    /**
     * Gets the value of the httpAcceptCharset property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHTTPAcceptCharset() {
        return httpAcceptCharset;
    }

    /**
     * Sets the value of the httpAcceptCharset property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHTTPAcceptCharset(String value) {
        this.httpAcceptCharset = value;
    }

    /**
     * Gets the value of the httpAcceptEncoding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHTTPAcceptEncoding() {
        return httpAcceptEncoding;
    }

    /**
     * Sets the value of the httpAcceptEncoding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHTTPAcceptEncoding(String value) {
        this.httpAcceptEncoding = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link ActionPriorityCodeType }
     *     
     */
    public ActionPriorityCodeType getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActionPriorityCodeType }
     *     
     */
    public void setPriority(ActionPriorityCodeType value) {
        this.priority = value;
    }

    /**
     * Gets the value of the requesterCredentials property.
     * 
     * @return
     *     possible object is
     *     {@link XMLRequesterCredentialsType }
     *     
     */
    public XMLRequesterCredentialsType getRequesterCredentials() {
        return requesterCredentials;
    }

    /**
     * Sets the value of the requesterCredentials property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLRequesterCredentialsType }
     *     
     */
    public void setRequesterCredentials(XMLRequesterCredentialsType value) {
        this.requesterCredentials = value;
    }

    /**
     * Gets the value of the errorHandling property.
     * 
     * @return
     *     possible object is
     *     {@link ErrorHandlingCodeType }
     *     
     */
    public ErrorHandlingCodeType getErrorHandling() {
        return errorHandling;
    }

    /**
     * Sets the value of the errorHandling property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorHandlingCodeType }
     *     
     */
    public void setErrorHandling(ErrorHandlingCodeType value) {
        this.errorHandling = value;
    }

    /**
     * Gets the value of the invocationID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvocationID() {
        return invocationID;
    }

    /**
     * Sets the value of the invocationID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvocationID(String value) {
        this.invocationID = value;
    }

    /**
     * Gets the value of the outputSelector property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the outputSelector property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOutputSelector().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getOutputSelector() {
        if (outputSelector == null) {
            outputSelector = new ArrayList<String>();
        }
        return this.outputSelector;
    }

    /**
     * Gets the value of the warningLevel property.
     * 
     * @return
     *     possible object is
     *     {@link WarningLevelCodeType }
     *     
     */
    public WarningLevelCodeType getWarningLevel() {
        return warningLevel;
    }

    /**
     * Sets the value of the warningLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link WarningLevelCodeType }
     *     
     */
    public void setWarningLevel(WarningLevelCodeType value) {
        this.warningLevel = value;
    }

    /**
     * Gets the value of the botBlock property.
     * 
     * @return
     *     possible object is
     *     {@link BotBlockRequestType }
     *     
     */
    public BotBlockRequestType getBotBlock() {
        return botBlock;
    }

    /**
     * Sets the value of the botBlock property.
     * 
     * @param value
     *     allowed object is
     *     {@link BotBlockRequestType }
     *     
     */
    public void setBotBlock(BotBlockRequestType value) {
        this.botBlock = value;
    }

    /**
     * Gets the value of the machineGroupID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMachineGroupID() {
        return machineGroupID;
    }

    /**
     * Sets the value of the machineGroupID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMachineGroupID(String value) {
        this.machineGroupID = value;
    }

    /**
     * Gets the value of the machineGroupValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMachineGroupValue() {
        return machineGroupValue;
    }

    /**
     * Sets the value of the machineGroupValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMachineGroupValue(String value) {
        this.machineGroupValue = value;
    }

    /**
     * Gets the value of the machineTagTypes property.
     * 
     * @return
     *     possible object is
     *     {@link MachineTagTypeArrayType }
     *     
     */
    public MachineTagTypeArrayType getMachineTagTypes() {
        return machineTagTypes;
    }

    /**
     * Sets the value of the machineTagTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link MachineTagTypeArrayType }
     *     
     */
    public void setMachineTagTypes(MachineTagTypeArrayType value) {
        this.machineTagTypes = value;
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
     * {@link Object }
     * {@link Element }
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
