
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
 * 				Contains properties that provide information on duplicate uses of InvocationIDs.
 * 			
 * 
 * <p>Java class for DuplicateInvocationDetailsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DuplicateInvocationDetailsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DuplicateInvocationID" type="{urn:ebayopensource:apis:eBLBaseComponents}UUIDType" minOccurs="0"/>
 *         &lt;element name="Status" type="{urn:ebayopensource:apis:eBLBaseComponents}InvocationStatusType" minOccurs="0"/>
 *         &lt;element name="InvocationTrackingID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "DuplicateInvocationDetailsType", propOrder = {
    "duplicateInvocationID",
    "status",
    "invocationTrackingID",
    "any"
})
public class DuplicateInvocationDetailsType {

    @XmlElement(name = "DuplicateInvocationID")
    protected String duplicateInvocationID;
    @XmlElement(name = "Status")
    protected InvocationStatusType status;
    @XmlElement(name = "InvocationTrackingID")
    protected String invocationTrackingID;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the duplicateInvocationID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDuplicateInvocationID() {
        return duplicateInvocationID;
    }

    /**
     * Sets the value of the duplicateInvocationID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDuplicateInvocationID(String value) {
        this.duplicateInvocationID = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link InvocationStatusType }
     *     
     */
    public InvocationStatusType getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link InvocationStatusType }
     *     
     */
    public void setStatus(InvocationStatusType value) {
        this.status = value;
    }

    /**
     * Gets the value of the invocationTrackingID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvocationTrackingID() {
        return invocationTrackingID;
    }

    /**
     * Sets the value of the invocationTrackingID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvocationTrackingID(String value) {
        this.invocationTrackingID = value;
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
