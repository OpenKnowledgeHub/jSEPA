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

package org.openknowledgehub.data.directdebit;

import org.openknowledgehub.data.common.AccountIdentification;
import org.openknowledgehub.exception.JSepaValidationException;
import org.openknowledgehub.util.JSepaContentSanitizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DirectDebitDocumentBuilder {

  private String messageId;

  private AccountIdentification creditor;

  private List<DirectDebitPayment> payments;

  private DirectDebitDocumentBuilder() {}

  public static DirectDebitDocumentBuilder create(String messageId) {
    if (Objects.isNull(messageId)) {
      throw new JSepaValidationException("'messageId' cannot be null");
    }

    if (messageId.isBlank()) {
      throw new JSepaValidationException("'messageId' cannot be empty");
    }

    DirectDebitDocumentBuilder builder = new DirectDebitDocumentBuilder();
    builder.messageId = JSepaContentSanitizer.of(messageId).withMaxLength(35).sanitize();

    return builder;
  }

  public DirectDebitDocumentBuilder withCreditor(AccountIdentification creditor) {
    if (Objects.isNull(creditor)) {
      throw new JSepaValidationException("'creditor' cannot be null");
    }

    this.creditor = creditor;

    return this;
  }

  public DirectDebitDocumentBuilder addPayment(DirectDebitPaymentBuilder paymentBuilder) {
    if (Objects.isNull(paymentBuilder)) {
      throw new JSepaValidationException("'paymentBuilder' cannot be null");
    }

    if (Objects.isNull(this.payments)) {
      this.payments = new ArrayList<>();
    }

    this.payments.add(paymentBuilder.build());

    return this;
  }

  public DirectDebitDocumentData build() {
    return new DirectDebitDocumentData(messageId, creditor, payments);
  }
}
