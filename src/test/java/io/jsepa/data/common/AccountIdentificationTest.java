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

import io.jsepa.exception.JSepaValidationException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Test the AccountIdentification data class")
class AccountIdentificationTest {

  @Test
  @DisplayName("Should create a valid AccountIdentification")
  void testCreate() {
    final var createdAccountIdentification =
        new AccountIdentification(NAME, IDENTIFICATION, BIC, IBAN);

    jSepaAssertThat(createdAccountIdentification)
        .hasName(NAME)
        .hasIdentifier(IDENTIFICATION)
        .hasBic(BIC)
        .hasIban(IBAN);
  }

  @Test
  @DisplayName("Should create an empty AccountIdentification for JAX-B")
  void testCreateEmptyAccountIdentification() {
    final var createdAccountIdentification = new AccountIdentification();

    jSepaAssertThat(createdAccountIdentification).isEmpty();
  }

  @ParameterizedTest
  @MethodSource("provideNullTestArguments")
  @DisplayName("Should not allow null values")
  void testCreateWithNullValues(
      final String givenName,
      final String givenIdentification,
      final String givenBic,
      final String givenIban,
      final String expectedErrorMessage) {

    assertThatThrownBy(
            () -> new AccountIdentification(givenName, givenIdentification, givenBic, givenIban))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage(expectedErrorMessage);
  }

  private static Stream<Arguments> provideNullTestArguments() {
    return Stream.of(
        Arguments.of(
            null, IDENTIFICATION, BIC, IBAN, "AccountIdentifier 'name' cannot be null"),
        Arguments.of(NAME, null, BIC, IBAN, "AccountIdentifier 'identifier' cannot be null"),
        Arguments.of(
            NAME, IDENTIFICATION, null, IBAN, "AccountIdentifier 'bic' cannot be null"),
        Arguments.of(
            NAME, IDENTIFICATION, BIC, null, "AccountIdentifier 'iban' cannot be null"));
  }
}
