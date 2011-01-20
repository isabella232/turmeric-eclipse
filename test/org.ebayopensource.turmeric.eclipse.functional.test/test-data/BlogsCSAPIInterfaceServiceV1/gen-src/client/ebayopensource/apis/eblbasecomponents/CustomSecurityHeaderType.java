
package ebayopensource.apis.eblbasecomponents;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * 
 * 				Security header used for SOAP API calls.
 * 			
 * 
 * <p>Java class for CustomSecurityHeaderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomSecurityHeaderType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="eBayAuthToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HardExpirationWarning" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Credentials" type="{urn:ebayopensource:apis:eBLBaseComponents}UserIdPasswordType" minOccurs="0"/>
 *         &lt;element name="NotificationSignature" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "CustomSecurityHeaderType", propOrder = {
    "eBayAuthToken",
    "hardExpirationWarning",
    "credentials",
    "notificationSignature",
    "any"
})
public class CustomSecurityHeaderType {

    protected String eBayAuthToken;
    @XmlElement(name = "HardExpirationWarning")
    protected String hardExpirationWarning;
    @XmlElement(name = "Credentials")
    protected UserIdPasswordType credentials;
    @XmlElement(name = "NotificationSignature")
    protected String notificationSignature;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the eBayAuthToken property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEBayAuthToken() {
        return eBayAuthToken;
    }

    /**
     * Sets the value of the eBayAuthToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEBayAuthToken(String value) {
        this.eBayAuthToken = value;
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
     * Gets the value of the credentials property.
     * 
     * @return
     *     possible object is
     *     {@link UserIdPasswordType }
     *     
     */
    public UserIdPasswordType getCredentials() {
        return credentials;
    }

    /**
     * Sets the value of the credentials property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserIdPasswordType }
     *     
     */
    public void setCredentials(UserIdPasswordType value) {
        this.credentials = value;
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
