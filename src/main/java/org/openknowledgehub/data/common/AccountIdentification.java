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

package org.openknowledgehub.data.common;

import org.openknowledgehub.dsl.AccountIdentificationSelect;
import org.openknowledgehub.exception.JSepaValidationException;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.Objects;

public class AccountIdentification implements AccountIdentificationSelect {

  @XmlElement(name = "Name")
  private final String name;

  @XmlElement(name = "Identifier")
  private final String identifier;

  @XmlElement(name = "Bic")
  private final String bic;

  @XmlElement(name = "Iban")
  private final String iban;

  /** Default constructor for JAX-B only. You should no used programmatically */
  public AccountIdentification() {
    this.name = null;
    this.identifier = null;
    this.bic = null;
    this.iban = null;
  }

  public AccountIdentification(String name, String identifier, String bic, String iban) {
    if (Objects.isNull(name)) {
      throw new JSepaValidationException("AccountIdentifier 'name' cannot be null");
    }

    if (Objects.isNull(identifier)) {
      throw new JSepaValidationException("AccountIdentifier 'identifier' cannot be null");
    }

    if (Objects.isNull(bic)) {
      throw new JSepaValidationException("AccountIdentifier 'bic' cannot be null");
    }

    if (Objects.isNull(iban)) {
      throw new JSepaValidationException("AccountIdentifier 'iban' cannot be null");
    }

    this.name = name;
    this.identifier = identifier;
    this.bic = bic;
    this.iban = iban;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getIdentifier() {
    return identifier;
  }

  @Override
  public String getBic() {
    return bic;
  }

  @Override
  public String getIban() {
    return iban;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    AccountIdentification that = (AccountIdentification) o;
    return Objects.equals(name, that.name)
        && Objects.equals(identifier, that.identifier)
        && Objects.equals(bic, that.bic)
        && Objects.equals(iban, that.iban);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, identifier, bic, iban);
  }

  @Override
  public String toString() {
    return "AccountIdentification{"
        + "name='"
        + name
        + '\''
        + ", identifier='"
        + identifier
        + '\''
        + '}';
  }
}
