/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2025 openknowledgehub.org
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

package org.openknowledgehub.data.transfer;

import org.openknowledgehub.data.common.AccountIdentification;
import org.openknowledgehub.exception.JSepaValidationException;
import org.openknowledgehub.util.JSepaContentSanitizer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class SepaTransferPaymentBuilder {

  private BigDecimal amount;
  private AccountIdentification payee;
  private String reasonForPayment;
  private String endToEndId;

  private SepaTransferPaymentBuilder() {}

  public static SepaTransferPaymentBuilder withPayment() {
    return new SepaTransferPaymentBuilder();
  }

  public SepaTransferPaymentBuilder withAmount(BigDecimal amount) {
    if (Objects.isNull(amount)) {
      throw new JSepaValidationException("'amount' cannot be null");
    }

    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new JSepaValidationException("'amount' should be greater than 0");
    }

    this.amount = amount.setScale(2, RoundingMode.HALF_UP);

    return this;
  }

  public SepaTransferPaymentBuilder withPayee(AccountIdentification payee) {
    if (Objects.isNull(payee)) {
      throw new JSepaValidationException("'payee' cannot be null");
    }

    this.payee = payee;

    return this;
  }

  public SepaTransferPaymentBuilder withReasonForPayment(String reasonForPayment) {
    if (Objects.isNull(reasonForPayment)) {
      throw new JSepaValidationException("'reasonForPayment' cannot be null");
    }

    if (reasonForPayment.isBlank()) {
      throw new JSepaValidationException("'reasonForPayment' cannot be empty");
    }

    this.reasonForPayment = JSepaContentSanitizer.of(reasonForPayment).sanitize();

    return this;
  }

  public SepaTransferPaymentBuilder withEndToEndId(String endToEndId) {
    if (Objects.isNull(endToEndId)) {
      throw new JSepaValidationException("'endToEndId' cannot be null");
    }

    if (endToEndId.isBlank()) {
      throw new JSepaValidationException("'endToEndId' cannot be empty");
    }

    this.endToEndId = endToEndId;

    return this;
  }

  public SepaTransferPayment build() {
    return new SepaTransferPayment(amount, payee, reasonForPayment, endToEndId);
  }
}
