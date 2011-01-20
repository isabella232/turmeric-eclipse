
package ebayopensource.apis.eblbasecomponents;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ErrorHandlingCodeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ErrorHandlingCodeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Legacy"/>
 *     &lt;enumeration value="BestEffort"/>
 *     &lt;enumeration value="AllOrNothing"/>
 *     &lt;enumeration value="FailOnError"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ErrorHandlingCodeType")
@XmlEnum
public enum ErrorHandlingCodeType {

    @XmlEnumValue("Legacy")
    LEGACY("Legacy"),
    @XmlEnumValue("BestEffort")
    BEST_EFFORT("BestEffort"),
    @XmlEnumValue("AllOrNothing")
    ALL_OR_NOTHING("AllOrNothing"),
    @XmlEnumValue("FailOnError")
    FAIL_ON_ERROR("FailOnError");
    private final String value;

    ErrorHandlingCodeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ErrorHandlingCodeType fromValue(String v) {
        for (ErrorHandlingCodeType c: ErrorHandlingCodeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
