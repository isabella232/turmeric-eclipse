
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
 * 				Container of token and user reply.
 * 			
 * 
 * <p>Java class for BotBlockRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BotBlockRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BotBlockToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BotBlockUserInput" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "BotBlockRequestType", propOrder = {
    "botBlockToken",
    "botBlockUserInput",
    "any"
})
public class BotBlockRequestType {

    @XmlElement(name = "BotBlockToken")
    protected String botBlockToken;
    @XmlElement(name = "BotBlockUserInput")
    protected String botBlockUserInput;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the botBlockToken property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBotBlockToken() {
        return botBlockToken;
    }

    /**
     * Sets the value of the botBlockToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBotBlockToken(String value) {
        this.botBlockToken = value;
    }

    /**
     * Gets the value of the botBlockUserInput property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBotBlockUserInput() {
        return botBlockUserInput;
    }

    /**
     * Sets the value of the botBlockUserInput property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBotBlockUserInput(String value) {
        this.botBlockUserInput = value;
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
