
package ebayopensource.apis.eblbasecomponents;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MachineTagTypeCodeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MachineTagTypeCodeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="ServerFlash"/>
 *     &lt;enumeration value="ServerHttpCookie"/>
 *     &lt;enumeration value="NoTag"/>
 *     &lt;enumeration value="CustomCode"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MachineTagTypeCodeType")
@XmlEnum
public enum MachineTagTypeCodeType {

    @XmlEnumValue("ServerFlash")
    SERVER_FLASH("ServerFlash"),
    @XmlEnumValue("ServerHttpCookie")
    SERVER_HTTP_COOKIE("ServerHttpCookie"),
    @XmlEnumValue("NoTag")
    NO_TAG("NoTag"),
    @XmlEnumValue("CustomCode")
    CUSTOM_CODE("CustomCode");
    private final String value;

    MachineTagTypeCodeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MachineTagTypeCodeType fromValue(String v) {
        for (MachineTagTypeCodeType c: MachineTagTypeCodeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
