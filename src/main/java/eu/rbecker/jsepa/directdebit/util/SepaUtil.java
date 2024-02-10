/*
 *  All rights reserved.
 */
package eu.rbecker.jsepa.directdebit.util;

import eu.rbecker.jsepa.validation.BicValidator;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.validator.routines.IBANValidator;

/**
 * @author Robert Becker <robert at rbecker.eu>
 */
public class SepaUtil {

  public static void validateIban(String iban) throws SepaValidationException {
    if (!IBANValidator.getInstance().isValid(iban)) {
      throw new SepaValidationException("Invalid IBAN " + iban);
    }
  }

  public static void validateBic(String bic) throws SepaValidationException {
    if (bic == null || bic.isEmpty() || !(new BicValidator().isValid(bic))) {
      throw new SepaValidationException("Invalid BIC " + bic);
    }
  }

  public static BigDecimal floatToBigInt2Digit(float f) {
    return new BigDecimal(f).setScale(2, RoundingMode.HALF_UP);
  }
}
