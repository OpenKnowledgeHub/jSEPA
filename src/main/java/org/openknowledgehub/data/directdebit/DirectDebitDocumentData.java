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

import org.openknowledgehub.data.SepaXmlDocument;
import org.openknowledgehub.data.common.AccountIdentification;
import org.openknowledgehub.data.configuration.LocalDateTimeAdapter;
import org.openknowledgehub.exception.JSepaValidationException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@XmlRootElement(name = "DirectDebitDocumentData")
@XmlAccessorType(XmlAccessType.FIELD)
public class DirectDebitDocumentData implements SepaXmlDocument {

  @XmlElement(name = "CreationDateTime")
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  private final LocalDateTime creationTime;

  @XmlElement(name = "PaymentMethod")
  private final String paymentMethod;

  @XmlElement(name = "MessageId")
  private final String messageId;

  @XmlElement(name = "Creditor")
  private final AccountIdentification creditor;

  @XmlElementWrapper(name = "Payments")
  @XmlElement(name = "Payment")
  private final List<DirectDebitPayment> payments;

  /** Default constructor for JAX-B only. You should no used programmatically */
  public DirectDebitDocumentData() {
    this.creationTime = null;
    this.messageId = null;
    this.creditor = null;
    this.payments = null;
    this.paymentMethod = null;
  }

  public DirectDebitDocumentData(
      String messageId, AccountIdentification creditor, List<DirectDebitPayment> payments) {

    if (Objects.isNull(messageId)) {
      throw new JSepaValidationException("DirectDebitDocumentData 'messageId' cannot be null");
    }

    if (Objects.isNull(creditor)) {
      throw new JSepaValidationException("DirectDebitDocumentData 'creditor' cannot be null");
    }

    if (Objects.isNull(payments)) {
      throw new JSepaValidationException("DirectDebitDocumentData 'payments' cannot be null");
    }

    this.creationTime = LocalDateTime.now();
    this.messageId = messageId;
    this.creditor = creditor;
    this.payments = payments;
    this.paymentMethod = "DD";
  }

  public LocalDateTime getCreationTime() {
    return creationTime;
  }

  public String getMessageId() {
    return messageId;
  }

  public AccountIdentification getCreditor() {
    return creditor;
  }

  public List<DirectDebitPayment> getPayments() {
    return payments;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    DirectDebitDocumentData that = (DirectDebitDocumentData) o;
    return Objects.equals(creationTime, that.creationTime)
        && Objects.equals(paymentMethod, that.paymentMethod)
        && Objects.equals(messageId, that.messageId)
        && Objects.equals(creditor, that.creditor)
        && Objects.equals(payments, that.payments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(creationTime, paymentMethod, messageId, creditor, payments);
  }

  @Override
  public String toString() {
    return "DirectDebitDocumentData{"
        + "creationTime="
        + creationTime
        + ", messageId='"
        + messageId
        + '\''
        + '}';
  }
}
