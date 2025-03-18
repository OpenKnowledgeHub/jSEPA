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

import org.openknowledgehub.exception.JSepaValidationException;
import org.openknowledgehub.util.JSepaContentSanitizer;
import org.openknowledgehub.util.JSepaValidator;
import java.util.Objects;

public class AccountIdentificationBuilder {

  private String name;
  private String identifier;
  private String bic;
  private String iban;

  private AccountIdentificationBuilder() {}

  public static AccountIdentificationBuilder withName(String name) {
    if (Objects.isNull(name)) {
      throw new JSepaValidationException("'name' cannot be null");
    }

    if (name.isBlank()) {
      throw new JSepaValidationException("'name' cannot be empty");
    }

    AccountIdentificationBuilder builder = new AccountIdentificationBuilder();

    builder.name = JSepaContentSanitizer.of(name).withMaxLength(70).sanitize();

    return builder;
  }

  public AccountIdentificationBuilder withIdentifier(String identifier) {
    if (Objects.isNull(identifier)) {
      throw new JSepaValidationException("'identifier' cannot be null");
    }

    if (identifier.isBlank()) {
      throw new JSepaValidationException("'identifier' cannot be empty");
    }

    this.identifier = identifier;

    return this;
  }

  public AccountIdentificationBuilder withBic(String bic) {
    if (Objects.isNull(bic)) {
      throw new JSepaValidationException("'bic' cannot be null");
    }

    if (bic.isBlank()) {
      throw new JSepaValidationException("'bic' cannot be empty");
    }

    if (!JSepaValidator.isValidBic(bic)) {
      throw new JSepaValidationException("'bic' is not in a valid shape");
    }

    this.bic = bic;

    return this;
  }

  public AccountIdentificationBuilder withIban(String iban) {
    if (Objects.isNull(iban)) {
      throw new JSepaValidationException("'iban' cannot be null");
    }

    if (iban.isBlank()) {
      throw new JSepaValidationException("'iban' cannot be empty");
    }

    if (!JSepaValidator.isValidIban(iban)) {
      throw new JSepaValidationException("'iban' is not in a valid shape");
    }

    this.iban = iban;

    return this;
  }

  public AccountIdentification build() {
    return new AccountIdentification(name, identifier, bic, iban);
  }
}
