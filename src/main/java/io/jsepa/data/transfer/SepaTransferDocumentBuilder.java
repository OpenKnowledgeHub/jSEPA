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

package io.jsepa.data.transfer;

import io.jsepa.data.common.AccountIdentification;
import io.jsepa.exception.JSepaException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SepaTransferDocumentBuilder {

  private AccountIdentification payer;
  private String messageId;
  private LocalDateTime dateOfExecution;
  private boolean batchBooking = true;
  private List<SepaTransferPayment> payments;

  private SepaTransferDocumentBuilder() {}

  public static SepaTransferDocumentBuilder create(String messageId) {
    Objects.requireNonNull(messageId);

    if (messageId.isEmpty()) {
      throw new JSepaException("messageId cannot be empty");
    }

    SepaTransferDocumentBuilder builder = new SepaTransferDocumentBuilder();
    builder.messageId = messageId;

    return builder;
  }

  public SepaTransferDocumentBuilder withPayer(AccountIdentification identification) {

    Objects.requireNonNull(identification);

    this.payer = identification;

    return this;
  }

  public SepaTransferDocumentBuilder withDateOfExecution(LocalDateTime dateOfExecution) {
    Objects.requireNonNull(dateOfExecution);

    this.dateOfExecution = dateOfExecution;

    return this;
  }

  public SepaTransferDocumentBuilder withBatchBooking(boolean batchBooking) {
    this.batchBooking = batchBooking;

    return this;
  }

  public SepaTransferDocumentBuilder addPayment(SepaTransferPaymentBuilder paymentBuilder) {
    Objects.requireNonNull(paymentBuilder);

    if (Objects.isNull(this.payments)) {
      this.payments = new ArrayList<>();
    }

    this.payments.add(paymentBuilder.build());

    return this;
  }

  public SepaTransferDocumentData build() {
    return new SepaTransferDocumentData(payer, messageId, dateOfExecution, batchBooking, payments);
  }
}
