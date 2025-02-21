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

package io.jsepa.data.common;

import static io.jsepa.api.assertions.JSepaAssertions.jSepaAssertThat;
import static io.jsepa.api.objects.AccountIdentificationTestProvider.BIC;
import static io.jsepa.api.objects.AccountIdentificationTestProvider.IBAN;
import static io.jsepa.api.objects.AccountIdentificationTestProvider.IDENTIFICATION;
import static io.jsepa.api.objects.AccountIdentificationTestProvider.NAME;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsepa.api.objects.AccountIdentificationTestProvider;
import io.jsepa.exception.JSepaValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test the AccountIdentification builder")
class AccountIdentificationBuilderTest {

  @Test
  @DisplayName("Should build a valid AccountIdentification")
  void testCreate() {
    final AccountIdentification createdAccountIdentification =
        AccountIdentificationBuilder.withName(NAME)
            .withBic(AccountIdentificationTestProvider.BIC)
            .withIban(AccountIdentificationTestProvider.IBAN)
            .withIdentifier(AccountIdentificationTestProvider.IDENTIFICATION)
            .build();

    jSepaAssertThat(createdAccountIdentification)
        .hasName(NAME)
        .hasIdentifier(IDENTIFICATION)
        .hasBic(BIC)
        .hasIban(IBAN);
  }

  @Test
  @DisplayName("Should not allow to build with null name")
  void testCreateWithNullName() {
    assertThatThrownBy(() -> AccountIdentificationBuilder.withName(null))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'name' cannot be null");
  }

  @Test
  @DisplayName("Should not allow to build with empty name")
  void testCreateWithEmptyName() {
    assertThatThrownBy(() -> AccountIdentificationBuilder.withName("  "))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'name' cannot be empty");
  }

  @Test
  @DisplayName("Should not allow to build with null identifier")
  void testCreateWithNullIdentifier() {
    AccountIdentificationBuilder builder = AccountIdentificationBuilder.withName(NAME);
    assertThatThrownBy(() -> builder.withIdentifier(null))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'identifier' cannot be null");
  }

  @Test
  @DisplayName("Should not allow to build with empty identifier")
  void testCreateWithEmptyIdentifier() {
    AccountIdentificationBuilder builder = AccountIdentificationBuilder.withName(NAME);
    assertThatThrownBy(() -> builder.withIdentifier("   "))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'identifier' cannot be empty");
  }

  @Test
  @DisplayName("Should not allow to build with null bic")
  void testCreateWithNullBic() {
    AccountIdentificationBuilder builder = AccountIdentificationBuilder.withName(NAME);
    assertThatThrownBy(() -> builder.withBic(null))
            .isInstanceOf(JSepaValidationException.class)
            .hasMessage("'bic' cannot be null");
  }

  @Test
  @DisplayName("Should not allow to build with empty bic")
  void testCreateWithEmptyBic() {
    AccountIdentificationBuilder builder = AccountIdentificationBuilder.withName(NAME);
    assertThatThrownBy(() -> builder.withBic("   "))
            .isInstanceOf(JSepaValidationException.class)
            .hasMessage("'bic' cannot be empty");
  }

  @Test
  @DisplayName("Should not allow to a not valid bic")
  void testCreateWithNotValidBic() {
    AccountIdentificationBuilder builder = AccountIdentificationBuilder.withName(NAME);
    assertThatThrownBy(() -> builder.withBic("BYLAD"))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'bic' is not in a valid shape");
  }

  @Test
  @DisplayName("Should not allow to build with null iban")
  void testCreateWithNullIban() {
    AccountIdentificationBuilder builder = AccountIdentificationBuilder.withName(NAME);
    assertThatThrownBy(() -> builder.withIban(null))
            .isInstanceOf(JSepaValidationException.class)
            .hasMessage("'iban' cannot be null");
  }

  @Test
  @DisplayName("Should not allow to build with empty iban")
  void testCreateWithEmptyIban() {
    AccountIdentificationBuilder builder = AccountIdentificationBuilder.withName(NAME);
    assertThatThrownBy(() -> builder.withIban("   "))
            .isInstanceOf(JSepaValidationException.class)
            .hasMessage("'iban' cannot be empty");
  }
}
