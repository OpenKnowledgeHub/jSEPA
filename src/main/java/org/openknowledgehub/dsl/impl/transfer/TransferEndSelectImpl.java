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

package org.openknowledgehub.dsl.impl.transfer;

import org.openknowledgehub.data.transfer.SepaTransferDocumentBuilder;
import org.openknowledgehub.data.transfer.SepaTransferPaymentBuilder;
import org.openknowledgehub.dsl.TransferEndSelect;
import org.openknowledgehub.dsl.TransferToSelect;
import org.openknowledgehub.transformer.sepa.TransferTransformer;

public class TransferEndSelectImpl implements TransferEndSelect {

  private final SepaTransferDocumentBuilder documentBuilder;
  private final SepaTransferPaymentBuilder paymentBuilder;

  public TransferEndSelectImpl(
      SepaTransferDocumentBuilder documentBuilder, SepaTransferPaymentBuilder paymentBuilder) {
    this.documentBuilder = documentBuilder;
    this.paymentBuilder = paymentBuilder;
  }

  @Override
  public TransferEndSelect withReasonForPayment(String reasonForPayment) {
    paymentBuilder.withReasonForPayment(reasonForPayment);

    return new TransferEndSelectImpl(documentBuilder, paymentBuilder);
  }

  @Override
  public TransferToSelect and() {
    this.documentBuilder.addPayment(paymentBuilder);
    return new TransferToSelectImpl(this.documentBuilder);
  }

  @Override
  public String toXml() {
    this.documentBuilder.addPayment(paymentBuilder);
    return new TransferTransformer().transform(this.documentBuilder.build());
  }
}
