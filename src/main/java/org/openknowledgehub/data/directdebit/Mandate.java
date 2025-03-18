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

import org.openknowledgehub.data.configuration.LocalDateAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.Objects;

@XmlRootElement(name = "Mandate")
@XmlAccessorType(XmlAccessType.FIELD)
public class Mandate {
  @XmlElement(name = "Identification")
  private final String mandateId;

  @XmlElement(name = "IssuedAt")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  private final LocalDate mandateIssuedAt;

  @XmlElement(name = "Type")
  private final MandateType mandateType;

  /** Default constructor for JAX-B only. You should no used programmatically */
  public Mandate() {
    this.mandateId = null;
    this.mandateIssuedAt = null;
    this.mandateType = null;
  }

  public Mandate(String mandateId, LocalDate mandateIssuedAt, MandateType mandateType) {
    this.mandateId = mandateId;
    this.mandateIssuedAt = mandateIssuedAt;
    this.mandateType = mandateType;
  }

  public String getMandateId() {
    return mandateId;
  }

  public LocalDate getMandateIssuedAt() {
    return mandateIssuedAt;
  }

  public MandateType getMandateType() {
    return mandateType;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Mandate mandate = (Mandate) o;
    return Objects.equals(mandateId, mandate.mandateId)
        && Objects.equals(mandateIssuedAt, mandate.mandateIssuedAt)
        && mandateType == mandate.mandateType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(mandateId, mandateIssuedAt, mandateType);
  }

  @Override
  public String toString() {
    return "Mandate{"
        + "mandateId='"
        + mandateId
        + '\''
        + ", mandateIssuedAt="
        + mandateIssuedAt
        + ", mandateType="
        + mandateType
        + '}';
  }
}
