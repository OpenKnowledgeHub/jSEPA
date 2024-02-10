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

package eu.rbecker.jsepa.transfer;

import eu.rbecker.jsepa.directdebit.util.SepaUtil;
import eu.rbecker.jsepa.directdebit.util.SepaValidationException;
import eu.rbecker.jsepa.sanitization.SepaStringSanitizer;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SepaTransferPayment {

  private BigDecimal paymentSum;

  private String payeeIban;

  private String payeeBic;

  private String payeeName;

  private String reasonForPayment;

  private String endToEndId = null;

  public BigDecimal getPaymentSum() {
    return paymentSum;
  }

  public void setPaymentSum(BigDecimal paymentSum) {
    this.paymentSum = paymentSum.setScale(2, RoundingMode.HALF_UP);
  }

  public String getPayeeIban() {
    return payeeIban;
  }

  public void setPayeeIban(String payeeIban) throws SepaValidationException {
    SepaUtil.validateIban(payeeIban);
    this.payeeIban = payeeIban;
  }

  public String getPayeeBic() {
    return payeeBic;
  }

  public void setPayeeBic(String payeeBic) throws SepaValidationException {
    SepaUtil.validateBic(payeeBic);
    this.payeeBic = payeeBic;
  }

  public String getPayeeName() {
    return payeeName;
  }

  public void setPayeeName(String payeeName) {
    this.payeeName = SepaStringSanitizer.of(payeeName).withMaxLength(70).sanitze();
  }

  public String getReasonForPayment() {
    return reasonForPayment;
  }

  public void setReasonForPayment(String reasonForPayment) {
    this.reasonForPayment = SepaStringSanitizer.of(reasonForPayment).sanitze();
  }

  public String getEndToEndId() {
    return endToEndId;
  }

  public void setEndToEndId(String endToEndId) {
    this.endToEndId = endToEndId;
  }
}
