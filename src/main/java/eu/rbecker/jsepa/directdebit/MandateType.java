/*
 *  All rights reserved.
 */
package eu.rbecker.jsepa.directdebit;

import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.SequenceType1Code;

/**
 *
 * @author Robert Becker <robert at rbecker.eu>
 */
public enum MandateType {

    ONE_OFF, RECURRENT, RECURRENT_FIRST, RECURRENT_FINAL;

    public SequenceType1Code getSepaSequenceType1Code() {
        switch (this) {
            case ONE_OFF:
                return SequenceType1Code.OOFF;
            case RECURRENT:
                return SequenceType1Code.RCUR;
            case RECURRENT_FINAL:
                return SequenceType1Code.FNAL;
            case RECURRENT_FIRST:
                return SequenceType1Code.FRST;
        }
        return null;
    }
}
