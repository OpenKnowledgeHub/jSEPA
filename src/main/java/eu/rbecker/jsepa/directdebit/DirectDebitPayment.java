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

package eu.rbecker.jsepa.directdebit;

import eu.rbecker.jsepa.directdebit.util.SepaUtil;
import eu.rbecker.jsepa.directdebit.util.SepaValidationException;
import eu.rbecker.jsepa.sanitization.SepaStringSanitizer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class DirectDebitPayment {

  private BigDecimal paymentSum;

  private String debitorIban;

  private String debitorBic;

  private String debitorName;

  private String reasonForPayment;

  private String mandateId;

  private Date mandateDate;

  private Date directDebitDueDate;

  private MandateType mandateType;

  public void validate() throws SepaValidationException {
    if (directDebitDueDate == null) {
      throw new SepaValidationException("Due date missing.");
    }
  }

  public BigDecimal getPaymentSum() {
    return paymentSum;
  }

  public void setPaymentSum(BigDecimal paymentSum) {
    this.paymentSum = paymentSum.setScale(2, RoundingMode.HALF_UP);
  }

  public String getDebitorIban() {
    return debitorIban;
  }

  public void setDebitorIban(String debitorIban) throws SepaValidationException {
    SepaUtil.validateIban(debitorIban);
    this.debitorIban = debitorIban;
  }

  public String getDebitorBic() {
    return debitorBic;
  }

  public void setDebitorBic(String debitorBic) throws SepaValidationException {
    SepaUtil.validateBic(debitorBic);
    this.debitorBic = debitorBic;
  }

  public String getDebitorName() {
    return debitorName;
  }

  public void setDebitorName(String debitorName) {
    this.debitorName = SepaStringSanitizer.of(debitorName).withMaxLength(70).sanitze();
  }

  public String getReasonForPayment() {
    return reasonForPayment;
  }

  public void setReasonForPayment(String reasonForPayment) {
    this.reasonForPayment = SepaStringSanitizer.of(reasonForPayment).sanitze();
  }

  public String getMandateId() {
    return mandateId;
  }

  public void setMandateId(String mandateId) {
    this.mandateId = mandateId;
  }

  public Date getMandateDate() {
    return mandateDate;
  }

  public void setMandateDate(Date mandateDate) {
    this.mandateDate = mandateDate;
  }

  public Date getDirectDebitDueDate() {
    return directDebitDueDate;
  }

  public void setDirectDebitDueDate(Date directDebitDueDate) {
    this.directDebitDueDate = directDebitDueDate;
  }

  public MandateType getMandateType() {
    return mandateType;
  }

  public void setMandateType(MandateType mandateType) {
    this.mandateType = mandateType;
  }
}
