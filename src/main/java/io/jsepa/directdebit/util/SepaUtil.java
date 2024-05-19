/*
 * The MIT License (MIT)
 *
 * Copyright © 2024 jsepa.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the “Software”), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.jsepa.directdebit.util;

import io.jsepa.validation.BicValidator;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.validator.routines.IBANValidator;

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
