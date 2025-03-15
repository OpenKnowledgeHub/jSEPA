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

package io.jsepa.api.objects;

import static io.jsepa.api.objects.TestObjects.accountIdentification;
import static io.jsepa.api.objects.TestObjects.mandate;

import io.jsepa.data.directdebit.DirectDebitPayment;
import io.jsepa.data.directdebit.DirectDebitPaymentBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DirectDebitTestProvider {

  public static final String MESSAGE_IDENTIFICATION = "messageId";

  public DirectDebitPaymentTestProvider payment() {
    return new DirectDebitPaymentTestProvider();
  }

  public static class DirectDebitPaymentTestProvider extends DirectDebitTestProvider {
    public static final String PAYMENT_IDENTIFICATION = "identification";
    public static final String REASON_FOR_PAYMENT = "Invoice XXX";
    public static final LocalDate DUE_AT = LocalDate.of(2021, 1, 1);
    public static final BigDecimal AMOUNT = BigDecimal.valueOf(5.56);

    public DirectDebitPaymentBuilder defaultBuilder() {
      return DirectDebitPaymentBuilder.withPayment()
          .withIdentification(PAYMENT_IDENTIFICATION)
          .withDirectDebitDueAt(DUE_AT)
          .withAmount(AMOUNT)
          .withMandate(mandate().defaultMandate())
          .withReasonForPayment(REASON_FOR_PAYMENT)
          .withDebitor(accountIdentification().defaultAccount());
    }

    public DirectDebitPayment defaultPayment() {
      return new DirectDebitPayment(
          PAYMENT_IDENTIFICATION,
          AMOUNT,
          accountIdentification().defaultAccount(),
          REASON_FOR_PAYMENT,
          DUE_AT,
          mandate().defaultMandate());
    }
  }
}
