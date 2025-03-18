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

package io.jsepa.api.assertions;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsepa.api.objects.AccountIdentificationTestProvider;
import io.jsepa.data.common.AccountIdentification;
import org.assertj.core.api.AbstractAssert;

public class AccountIdentificationAssert
    extends AbstractAssert<AccountIdentificationAssert, AccountIdentification> {
  protected AccountIdentificationAssert(AccountIdentification accountIdentification) {
    super(accountIdentification, AccountIdentificationAssert.class);
  }

  public AccountIdentificationAssert isEmpty() {
    assertThat(actual.getName()).isNull();
    assertThat(actual.getIdentifier()).isNull();
    assertThat(actual.getBic()).isNull();
    assertThat(actual.getIban()).isNull();

    return this;
  }

  public AccountIdentificationAssert hasDefaultTestValues() {
    hasName(AccountIdentificationTestProvider.NAME);
    hasIdentifier(AccountIdentificationTestProvider.IDENTIFICATION);
    hasIban(AccountIdentificationTestProvider.IBAN);
    hasBic(AccountIdentificationTestProvider.BIC);

    return this;
  }

  public AccountIdentificationAssert hasName(String name) {
    assertThat(name).isEqualTo(actual.getName());

    return this;
  }

  public AccountIdentificationAssert hasIdentifier(String identifier) {
    assertThat(identifier).isEqualTo(actual.getIdentifier());

    return this;
  }

  public AccountIdentificationAssert hasIban(String iban) {
    assertThat(iban).isEqualTo(actual.getIban());

    return this;
  }

  public AccountIdentificationAssert hasBic(String bic) {
    assertThat(bic).isEqualTo(actual.getBic());

    return this;
  }
}
