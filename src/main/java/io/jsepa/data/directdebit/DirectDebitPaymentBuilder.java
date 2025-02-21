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

package io.jsepa.data.directdebit;

import io.jsepa.data.common.AccountIdentification;
import io.jsepa.exception.JSepaException;
import io.jsepa.util.JSepaContentSanitizer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

public class DirectDebitPaymentBuilder {

  private String identification;
  private BigDecimal amount;
  private AccountIdentification debitor;
  private String reasonForPayment;
  private LocalDate directDebitDueAt;
  private Mandate mandate;

  private DirectDebitPaymentBuilder() {}

  public static DirectDebitPaymentBuilder withPayment() {
    return new DirectDebitPaymentBuilder();
  }

  public DirectDebitPaymentBuilder withAmount(BigDecimal amount) {
    Objects.requireNonNull(amount);

    if (amount.compareTo(BigDecimal.ZERO) < 0) {
      throw new JSepaException("Payment amount must be greater than zero");
    }

    this.amount = amount.setScale(2, RoundingMode.HALF_UP);

    return this;
  }

  public DirectDebitPaymentBuilder withDebitor(AccountIdentification identification) {
    Objects.requireNonNull(identification);

    this.debitor = identification;

    return this;
  }

  public DirectDebitPaymentBuilder withIdentification(String identification) {
    Objects.requireNonNull(identification);

    if (identification.isEmpty()) {
      throw new JSepaException("Identification cannot be empty");
    }

    this.identification = JSepaContentSanitizer.of(identification).sanitize();

    return this;
  }

  public DirectDebitPaymentBuilder withMandate(Mandate mandate) {
    Objects.requireNonNull(mandate);

    this.mandate = mandate;
    return this;
  }

  public DirectDebitPaymentBuilder withDirectDebitDueAt(LocalDate directDebitDueAt) {
    Objects.requireNonNull(directDebitDueAt);

    this.directDebitDueAt = directDebitDueAt;

    return this;
  }

  public DirectDebitPaymentBuilder withReasonForPayment(String reasonForPayment) {
    Objects.requireNonNull(reasonForPayment);

    this.reasonForPayment = JSepaContentSanitizer.of(reasonForPayment).sanitize();

    return this;
  }

  public DirectDebitPayment build() {
    return new DirectDebitPayment(
        identification, amount, debitor, reasonForPayment, directDebitDueAt, mandate);
  }
}
