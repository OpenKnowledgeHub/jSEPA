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

import io.jsepa.data.configuration.LocalDateTimeAdapter;
import io.jsepa.data.SepaXmlDocument;
import io.jsepa.data.common.AccountIdentification;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.util.List;

@XmlRootElement(name = "SepaTransferDocumentData")
@XmlAccessorType(XmlAccessType.FIELD)
public class SepaTransferDocumentData implements SepaXmlDocument {

  @XmlElement(name = "CreationDateTime")
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  private final LocalDateTime creationTime;

  @XmlElement(name = "Payer")
  private final AccountIdentification payer;

  @XmlElement(name = "MessageId")
  private final String messageId;

  @XmlElement(name = "DateOfExecution")
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  private final LocalDateTime dateOfExecution;

  @XmlElement(name = "BatchBooking")
  private final boolean batchBooking;

  @XmlElementWrapper(name = "Payments")
  @XmlElement(name = "Payment")
  private final List<SepaTransferPayment> payments;

  public SepaTransferDocumentData() {
    this.creationTime = null;
    this.payer = null;
    this.messageId = null;
    this.dateOfExecution = null;
    this.batchBooking = false;
    this.payments = null;
  }

  public SepaTransferDocumentData(
      AccountIdentification payer,
      String messageId,
      LocalDateTime dateOfExecution,
      boolean batchBooking,
      List<SepaTransferPayment> payments) {
    this.creationTime = LocalDateTime.now();
    this.payer = payer;
    this.messageId = messageId;
    this.dateOfExecution = dateOfExecution;
    this.batchBooking = batchBooking;
    this.payments = payments;
  }

  public AccountIdentification getPayer() {
    return payer;
  }

  public String getMessageId() {
    return messageId;
  }

  public LocalDateTime getDateOfExecution() {
    return dateOfExecution;
  }

  public boolean isBatchBooking() {
    return batchBooking;
  }

  public List<SepaTransferPayment> getPayments() {
    return payments;
  }
}
