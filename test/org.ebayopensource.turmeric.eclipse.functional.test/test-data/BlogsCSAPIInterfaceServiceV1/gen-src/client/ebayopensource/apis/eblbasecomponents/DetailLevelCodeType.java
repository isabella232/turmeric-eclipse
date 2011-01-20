
package ebayopensource.apis.eblbasecomponents;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DetailLevelCodeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DetailLevelCodeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="ReturnAll"/>
 *     &lt;enumeration value="ItemReturnDescription"/>
 *     &lt;enumeration value="ItemReturnAttributes"/>
 *     &lt;enumeration value="ItemReturnCategories"/>
 *     &lt;enumeration value="ReturnSummary"/>
 *     &lt;enumeration value="ReturnHeaders"/>
 *     &lt;enumeration value="ReturnMessages"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DetailLevelCodeType")
@XmlEnum
public enum DetailLevelCodeType {

    @XmlEnumValue("ReturnAll")
    RETURN_ALL("ReturnAll"),
    @XmlEnumValue("ItemReturnDescription")
    ITEM_RETURN_DESCRIPTION("ItemReturnDescription"),
    @XmlEnumValue("ItemReturnAttributes")
    ITEM_RETURN_ATTRIBUTES("ItemReturnAttributes"),
    @XmlEnumValue("ItemReturnCategories")
    ITEM_RETURN_CATEGORIES("ItemReturnCategories"),
    @XmlEnumValue("ReturnSummary")
    RETURN_SUMMARY("ReturnSummary"),
    @XmlEnumValue("ReturnHeaders")
    RETURN_HEADERS("ReturnHeaders"),
    @XmlEnumValue("ReturnMessages")
    RETURN_MESSAGES("ReturnMessages");
    private final String value;

    DetailLevelCodeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DetailLevelCodeType fromValue(String v) {
        for (DetailLevelCodeType c: DetailLevelCodeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
