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
import io.jsepa.exception.JSepaValidationException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Objects;

@XmlRootElement(name = "Payment")
@XmlAccessorType(XmlAccessType.FIELD)
public class SepaTransferPayment {

  @XmlElement(name = "Amount")
  private final BigDecimal amount;

  @XmlElement(name = "Payee")
  private final AccountIdentification payee;

  @XmlElement(name = "ReasonForPayment")
  private final String reasonForPayment;

  @XmlElement(name = "EndToEndId")
  private final String endToEndId;

  public SepaTransferPayment() {
    this.amount = null;
    this.payee = null;
    this.reasonForPayment = null;
    this.endToEndId = null;
  }

  public SepaTransferPayment(
      BigDecimal amount, AccountIdentification payee, String reasonForPayment, String endToEndId) {

    if (Objects.isNull(amount)) {
      throw new JSepaValidationException("SepaTransferPayment 'amount' cannot be null");
    }

    if (Objects.isNull(payee)) {
      throw new JSepaValidationException("SepaTransferPayment 'payee' cannot be null");
    }

    if (Objects.isNull(endToEndId)) {
      throw new JSepaValidationException("SepaTransferPayment 'endToEndId' cannot be null");
    }

    this.amount = amount;
    this.payee = payee;
    this.reasonForPayment = reasonForPayment;
    this.endToEndId = endToEndId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public AccountIdentification getPayee() {
    return payee;
  }

  public String getReasonForPayment() {
    return reasonForPayment;
  }

  public String getEndToEndId() {
    return endToEndId;
  }
}
