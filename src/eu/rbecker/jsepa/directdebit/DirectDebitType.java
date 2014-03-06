/*
 *  All rights reserved.
 */
package eu.rbecker.jsepa.directdebit;

import eu.rbecker.jsepa.directdebit.xml.schema.SequenceType1Code;

/**
 *
 * @author Robert Becker <robert at rbecker.eu>
 */
public enum DirectDebitType {

    CORE, COR1; // B2B is not yet supported

    /**
     * Returns the string to be put in the <LclInstrm><Cd>...</Cd></LclInstrm> part of the payment instruction information.
     * @return 
     */
    public String getSepaXmlString() {
        return name();
    }
}
