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

import org.openknowledgehub.data.common.AccountIdentification;
import org.openknowledgehub.data.directdebit.DirectDebitDocumentData;
import org.openknowledgehub.data.directdebit.DirectDebitPayment;
import org.openknowledgehub.data.directdebit.Mandate;
import org.openknowledgehub.data.transfer.SepaTransferDocumentData;
import org.openknowledgehub.data.transfer.SepaTransferPayment;

public class JSepaAssertions {
  private JSepaAssertions() {}

  public static MandateAssert jSepaAssertThat(Mandate mandate) {
    return new MandateAssert(mandate);
  }

  public static AccountIdentificationAssert jSepaAssertThat(
      AccountIdentification accountIdentification) {
    return new AccountIdentificationAssert(accountIdentification);
  }

  public static DirectDebitPaymentAssert jSepaAssertThat(DirectDebitPayment directDebitPayment) {
    return new DirectDebitPaymentAssert(directDebitPayment);
  }

  public static SepaTransferAssert jSepaAssertThat(SepaTransferDocumentData transferDocumentData) {
    return new SepaTransferAssert(transferDocumentData);
  }

  public static SepaTransferPaymentAssert jSepaAssertThat(SepaTransferPayment sepaTransferPayment) {
    return new SepaTransferPaymentAssert(sepaTransferPayment);
  }

  public static DirectDebitDocumentDataAssert jSepaAssertThat(
      DirectDebitDocumentData directDebitDocumentData) {
    return new DirectDebitDocumentDataAssert(directDebitDocumentData);
  }

  public static XmlAssert jSepaAssertThat(String xmlContent) {
    return new XmlAssert(xmlContent);
  }
}
