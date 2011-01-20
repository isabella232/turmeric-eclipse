
package ebayopensource.apis.eblbasecomponents;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				Container for a set of Machine Tag Types
 * 			
 * 
 * <p>Java class for MachineTagTypeArrayType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MachineTagTypeArrayType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TagType" type="{urn:ebayopensource:apis:eBLBaseComponents}MachineTagTypeCodeType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MachineTagTypeArrayType", propOrder = {
    "tagType"
})
public class MachineTagTypeArrayType {

    @XmlElement(name = "TagType")
    protected List<MachineTagTypeCodeType> tagType;

    /**
     * Gets the value of the tagType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tagType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTagType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MachineTagTypeCodeType }
     * 
     * 
     */
    public List<MachineTagTypeCodeType> getTagType() {
        if (tagType == null) {
            tagType = new ArrayList<MachineTagTypeCodeType>();
        }
        return this.tagType;
    }

}
