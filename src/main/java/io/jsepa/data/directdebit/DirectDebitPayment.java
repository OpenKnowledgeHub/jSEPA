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
import io.jsepa.data.configuration.LocalDateAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@XmlRootElement(name = "Payment")
@XmlAccessorType(XmlAccessType.FIELD)
public class DirectDebitPayment {

  @XmlElement(name = "Identification")
  private final String identification;

  @XmlElement(name = "Amount")
  private final BigDecimal amount;

  @XmlElement(name = "Debitor")
  private final AccountIdentification debitor;

  @XmlElement(name = "ReasonForPayment")
  private final String reasonForPayment;

  @XmlElement(name = "DirectDebitDueAt")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  private final LocalDate directDebitDueAt;

  @XmlElement(name = "Currency")
  private final String currency = "EUR";

  @XmlElement(name = "Mandate")
  private final Mandate mandate;

  /** Default constructor for JAX-B only. You should no used programmatically */
  public DirectDebitPayment() {
    this.identification = null;
    this.amount = null;
    this.debitor = null;
    this.reasonForPayment = null;
    this.directDebitDueAt = null;
    this.mandate = null;
  }

  public DirectDebitPayment(
      String identification,
      BigDecimal amount,
      AccountIdentification debitor,
      String reasonForPayment,
      LocalDate directDebitDueAt,
      Mandate mandate) {
    this.identification = identification;
    this.amount = amount;
    this.debitor = debitor;
    this.reasonForPayment = reasonForPayment;
    this.directDebitDueAt = directDebitDueAt;
    this.mandate = mandate;
  }

  public String getIdentification() {
    return identification;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public AccountIdentification getDebitor() {
    return debitor;
  }

  public String getReasonForPayment() {
    return reasonForPayment;
  }

  public LocalDate getDirectDebitDueAt() {
    return directDebitDueAt;
  }

  public String getCurrency() {
    return currency;
  }

  public Mandate getMandate() {
    return mandate;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    DirectDebitPayment that = (DirectDebitPayment) o;
    return Objects.equals(identification, that.identification)
        && Objects.equals(amount, that.amount)
        && Objects.equals(debitor, that.debitor)
        && Objects.equals(reasonForPayment, that.reasonForPayment)
        && Objects.equals(directDebitDueAt, that.directDebitDueAt)
        && Objects.equals(currency, that.currency)
        && Objects.equals(mandate, that.mandate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        identification, amount, debitor, reasonForPayment, directDebitDueAt, currency, mandate);
  }

  @Override
  public String toString() {
    return "DirectDebitPayment{"
        + "identification='"
        + identification
        + '\''
        + ", amount="
        + amount
        + ", reasonForPayment='"
        + reasonForPayment
        + '\''
        + ", mandate="
        + mandate
        + '}';
  }
}
