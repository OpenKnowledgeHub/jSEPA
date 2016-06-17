//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.06.17 um 01:15:53 PM CEST 
//


package eu.rbecker.jsepa.directdebit.xml.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ActiveOrHistoricCurrencyCodeEUR.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="ActiveOrHistoricCurrencyCodeEUR">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="EUR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActiveOrHistoricCurrencyCodeEUR")
@XmlEnum
public enum ActiveOrHistoricCurrencyCodeEUR {

    EUR;

    public String value() {
        return name();
    }

    public static ActiveOrHistoricCurrencyCodeEUR fromValue(String v) {
        return valueOf(v);
    }

}
