
package ebayopensource.apis.eblbasecomponents;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				Definition for adding a MAC attachment to an activity
 * 			
 * 
 * <p>Java class for CSUpdateMACActivityAddAttachmentsRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CSUpdateMACActivityAddAttachmentsRequestType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:ebayopensource:apis:eBLBaseComponents}AbstractRequestType">
 *       &lt;sequence>
 *         &lt;element name="User" type="{urn:ebayopensource:apis:eBLBaseComponents}CSUserIdType" minOccurs="0"/>
 *         &lt;element name="ActivityID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="Attachment" type="{urn:ebayopensource:apis:eBLBaseComponents}MACAttachementFileType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CSUpdateMACActivityAddAttachmentsRequestType", propOrder = {
    "user",
    "activityID",
    "attachment"
})
public class CSUpdateMACActivityAddAttachmentsRequestType
    extends AbstractRequestType
{

    @XmlElement(name = "User")
    protected CSUserIdType user;
    @XmlElement(name = "ActivityID")
    protected Long activityID;
    @XmlElement(name = "Attachment")
    protected MACAttachementFileType attachment;

    /**
     * Gets the value of the user property.
     * 
     * @return
     *     possible object is
     *     {@link CSUserIdType }
     *     
     */
    public CSUserIdType getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     * 
     * @param value
     *     allowed object is
     *     {@link CSUserIdType }
     *     
     */
    public void setUser(CSUserIdType value) {
        this.user = value;
    }

    /**
     * Gets the value of the activityID property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getActivityID() {
        return activityID;
    }

    /**
     * Sets the value of the activityID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setActivityID(Long value) {
        this.activityID = value;
    }

    /**
     * Gets the value of the attachment property.
     * 
     * @return
     *     possible object is
     *     {@link MACAttachementFileType }
     *     
     */
    public MACAttachementFileType getAttachment() {
        return attachment;
    }

    /**
     * Sets the value of the attachment property.
     * 
     * @param value
     *     allowed object is
     *     {@link MACAttachementFileType }
     *     
     */
    public void setAttachment(MACAttachementFileType value) {
        this.attachment = value;
    }

}
