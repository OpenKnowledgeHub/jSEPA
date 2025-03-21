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

package org.openknowledgehub.api.assertions;

import static org.assertj.core.api.Assertions.assertThat;

import org.openknowledgehub.api.objects.DirectDebitTestProvider;
import org.openknowledgehub.api.objects.TestObjects;
import org.openknowledgehub.data.common.AccountIdentification;
import org.openknowledgehub.data.directdebit.DirectDebitPayment;
import org.openknowledgehub.data.directdebit.Mandate;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.assertj.core.api.AbstractAssert;

public class DirectDebitPaymentAssert
    extends AbstractAssert<DirectDebitPaymentAssert, DirectDebitPayment> {
  protected DirectDebitPaymentAssert(DirectDebitPayment directDebitPayment) {
    super(directDebitPayment, DirectDebitPaymentAssert.class);
  }

  public DirectDebitPaymentAssert hasDefaultTestValues() {
    hasAmount(DirectDebitTestProvider.DirectDebitPaymentTestProvider.AMOUNT);
    hasIdentification(
        DirectDebitTestProvider.DirectDebitPaymentTestProvider.PAYMENT_IDENTIFICATION);
    hasDebitor(TestObjects.accountIdentification().defaultAccount());
    hasReasonForPayment(DirectDebitTestProvider.DirectDebitPaymentTestProvider.REASON_FOR_PAYMENT);
    hasDueAt(DirectDebitTestProvider.DirectDebitPaymentTestProvider.DUE_AT);
    hasMandate(TestObjects.mandate().defaultMandate());

    return this;
  }

  public DirectDebitPaymentAssert isEmpty() {
    assertThat(actual.getIdentification()).isNull();
    assertThat(actual.getAmount()).isNull();
    assertThat(actual.getAmount()).isNull();
    assertThat(actual.getReasonForPayment()).isNull();
    assertThat(actual.getCurrency()).isNull();
    assertThat(actual.getMandate()).isNull();

    return this;
  }

  public DirectDebitPaymentAssert hasIdentification(String expectedIdentification) {
    assertThat(actual.getIdentification()).isEqualTo(expectedIdentification);

    return this;
  }

  public DirectDebitPaymentAssert hasAmount(BigDecimal expectedAmount) {
    assertThat(actual.getAmount()).isEqualTo(expectedAmount);

    return this;
  }

  public DirectDebitPaymentAssert hasDebitor(AccountIdentification expectedAccountIdentification) {
    assertThat(actual.getDebitor()).isEqualTo(expectedAccountIdentification);

    return this;
  }

  public DirectDebitPaymentAssert hasReasonForPayment(String reasonForPayment) {
    assertThat(actual.getReasonForPayment()).isEqualTo(reasonForPayment);

    return this;
  }

  public DirectDebitPaymentAssert hasDueAt(LocalDate expectedDueAt) {
    assertThat(actual.getDirectDebitDueAt()).isEqualTo(expectedDueAt);

    return this;
  }

  public DirectDebitPaymentAssert hasMandate(Mandate expectedMandate) {
    assertThat(actual.getMandate()).isEqualTo(expectedMandate);

    return this;
  }
}
