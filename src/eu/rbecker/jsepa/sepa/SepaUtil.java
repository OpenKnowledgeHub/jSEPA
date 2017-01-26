/*
 *  All rights reserved.
 */
package eu.rbecker.jsepa.sepa;

import de.jost_net.OBanToo.SEPA.BIC;
import de.jost_net.OBanToo.SEPA.IBAN;
import de.jost_net.OBanToo.SEPA.SEPAException;
import eu.rbecker.jsepa.directdebit.SepaValidationException;
import java.math.BigDecimal;

/**
 *
 * @author Robert Becker <robert at rbecker.eu>
 */
public class SepaUtil {

    public static void validateIban(String iban) throws SepaValidationException {
        try {
            // use obantoo for validation for now
            IBAN i = new IBAN(iban);
        } catch (SEPAException e) {
            throw new SepaValidationException(e.getMessage());
        }
    }

    public static void validateBic(String bic) throws SepaValidationException {
        //        try {
//            // use obantoo for validation for now 
//            BIC b = new BIC(debitorBic);
//            // obantoo bics are broken (i.e. SKHRDE6W) - skip this validation
//            
//        } catch (SEPAException e) {
        if (bic == null || !bic.matches("([A-Z]{6}[A-Z\\d]{2}([A-Z\\d]{3})?)?")) {
            throw new SepaValidationException("Invalid BIC " + bic);
        }
    }

    public static void validateCreditorIdentifier(String creditorIdentifier) throws SepaValidationException {
        // TODO: implement
    }
    
    public static BigDecimal floatToBigInt2Digit(float f) {
        return new BigDecimal(f).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
