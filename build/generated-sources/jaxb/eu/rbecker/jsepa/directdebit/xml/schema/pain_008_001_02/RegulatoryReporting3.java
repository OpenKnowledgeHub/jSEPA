//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.01.26 um 10:40:43 AM CET 
//


package eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für RegulatoryReporting3 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="RegulatoryReporting3">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DbtCdtRptgInd" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}RegulatoryReportingType1Code" minOccurs="0"/>
 *         &lt;element name="Authrty" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}RegulatoryAuthority2" minOccurs="0"/>
 *         &lt;element name="Dtls" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}StructuredRegulatoryReporting3" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegulatoryReporting3", propOrder = {
    "dbtCdtRptgInd",
    "authrty",
    "dtls"
})
public class RegulatoryReporting3 {

    @XmlElement(name = "DbtCdtRptgInd")
    protected RegulatoryReportingType1Code dbtCdtRptgInd;
    @XmlElement(name = "Authrty")
    protected RegulatoryAuthority2 authrty;
    @XmlElement(name = "Dtls")
    protected List<StructuredRegulatoryReporting3> dtls;

    /**
     * Ruft den Wert der dbtCdtRptgInd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RegulatoryReportingType1Code }
     *     
     */
    public RegulatoryReportingType1Code getDbtCdtRptgInd() {
        return dbtCdtRptgInd;
    }

    /**
     * Legt den Wert der dbtCdtRptgInd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RegulatoryReportingType1Code }
     *     
     */
    public void setDbtCdtRptgInd(RegulatoryReportingType1Code value) {
        this.dbtCdtRptgInd = value;
    }

    /**
     * Ruft den Wert der authrty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RegulatoryAuthority2 }
     *     
     */
    public RegulatoryAuthority2 getAuthrty() {
        return authrty;
    }

    /**
     * Legt den Wert der authrty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RegulatoryAuthority2 }
     *     
     */
    public void setAuthrty(RegulatoryAuthority2 value) {
        this.authrty = value;
    }

    /**
     * Gets the value of the dtls property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dtls property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDtls().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StructuredRegulatoryReporting3 }
     * 
     * 
     */
    public List<StructuredRegulatoryReporting3> getDtls() {
        if (dtls == null) {
            dtls = new ArrayList<StructuredRegulatoryReporting3>();
        }
        return this.dtls;
    }

}
