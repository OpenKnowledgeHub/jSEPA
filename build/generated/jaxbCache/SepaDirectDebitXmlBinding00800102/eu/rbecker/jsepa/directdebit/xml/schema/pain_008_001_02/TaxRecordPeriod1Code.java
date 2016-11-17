//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.11.17 um 03:05:05 PM CET 
//


package eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für TaxRecordPeriod1Code.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="TaxRecordPeriod1Code">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MM01"/>
 *     &lt;enumeration value="MM02"/>
 *     &lt;enumeration value="MM03"/>
 *     &lt;enumeration value="MM04"/>
 *     &lt;enumeration value="MM05"/>
 *     &lt;enumeration value="MM06"/>
 *     &lt;enumeration value="MM07"/>
 *     &lt;enumeration value="MM08"/>
 *     &lt;enumeration value="MM09"/>
 *     &lt;enumeration value="MM10"/>
 *     &lt;enumeration value="MM11"/>
 *     &lt;enumeration value="MM12"/>
 *     &lt;enumeration value="QTR1"/>
 *     &lt;enumeration value="QTR2"/>
 *     &lt;enumeration value="QTR3"/>
 *     &lt;enumeration value="QTR4"/>
 *     &lt;enumeration value="HLF1"/>
 *     &lt;enumeration value="HLF2"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TaxRecordPeriod1Code")
@XmlEnum
public enum TaxRecordPeriod1Code {

    @XmlEnumValue("MM01")
    MM_01("MM01"),
    @XmlEnumValue("MM02")
    MM_02("MM02"),
    @XmlEnumValue("MM03")
    MM_03("MM03"),
    @XmlEnumValue("MM04")
    MM_04("MM04"),
    @XmlEnumValue("MM05")
    MM_05("MM05"),
    @XmlEnumValue("MM06")
    MM_06("MM06"),
    @XmlEnumValue("MM07")
    MM_07("MM07"),
    @XmlEnumValue("MM08")
    MM_08("MM08"),
    @XmlEnumValue("MM09")
    MM_09("MM09"),
    @XmlEnumValue("MM10")
    MM_10("MM10"),
    @XmlEnumValue("MM11")
    MM_11("MM11"),
    @XmlEnumValue("MM12")
    MM_12("MM12"),
    @XmlEnumValue("QTR1")
    QTR_1("QTR1"),
    @XmlEnumValue("QTR2")
    QTR_2("QTR2"),
    @XmlEnumValue("QTR3")
    QTR_3("QTR3"),
    @XmlEnumValue("QTR4")
    QTR_4("QTR4"),
    @XmlEnumValue("HLF1")
    HLF_1("HLF1"),
    @XmlEnumValue("HLF2")
    HLF_2("HLF2");
    private final String value;

    TaxRecordPeriod1Code(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TaxRecordPeriod1Code fromValue(String v) {
        for (TaxRecordPeriod1Code c: TaxRecordPeriod1Code.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
