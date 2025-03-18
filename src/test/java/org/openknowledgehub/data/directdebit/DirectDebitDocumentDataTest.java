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

import static org.openknowledgehub.api.assertions.JSepaAssertions.jSepaAssertThat;
import static org.openknowledgehub.api.objects.DirectDebitTestProvider.MESSAGE_IDENTIFICATION;
import static org.openknowledgehub.api.objects.TestObjects.accountIdentification;
import static org.openknowledgehub.api.objects.TestObjects.directDebit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.openknowledgehub.data.common.AccountIdentification;
import org.openknowledgehub.exception.JSepaValidationException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Test the DirectDebitDocumentData data class")
class DirectDebitDocumentDataTest {
  @Test
  @DisplayName("Should create a valid DirectDebitDocumentData")
  void testCreate() {
    final var createdDirectDebitDocumentData =
        new DirectDebitDocumentData(
            MESSAGE_IDENTIFICATION,
            accountIdentification().defaultAccount(),
            Collections.singletonList(directDebit().payment().defaultPayment()));

    jSepaAssertThat(createdDirectDebitDocumentData)
        .isNotNull()
        .hasMessageId(MESSAGE_IDENTIFICATION)
        .hasCreationTimeCloseToNow();

    jSepaAssertThat(createdDirectDebitDocumentData.getCreditor())
        .isNotNull()
        .hasDefaultTestValues();

    assertThat(createdDirectDebitDocumentData.getPayments()).isNotNull().hasSize(1);

    jSepaAssertThat(createdDirectDebitDocumentData.getPayments().get(0))
        .isNotNull()
        .hasDefaultTestValues();
  }

  @Test
  @DisplayName("Should create an empty DirectDebitDocumentData for JAX-B")
  void testCreateEmpty() {
    final var createdDirectDebitDocumentData = new DirectDebitDocumentData();

    jSepaAssertThat(createdDirectDebitDocumentData).isNotNull().isEmpty();
  }

  @ParameterizedTest
  @MethodSource("provideNullTestArguments")
  @DisplayName("Should not allow null values")
  void testCreateWithNullValues(
      final String givenMessageId,
      final AccountIdentification givenCreditor,
      final List<DirectDebitPayment> givenPayments,
      final String expectedErrorMessage) {

    assertThatThrownBy(
            () -> new DirectDebitDocumentData(givenMessageId, givenCreditor, givenPayments))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage(expectedErrorMessage);
  }

  private static Stream<Arguments> provideNullTestArguments() {
    return Stream.of(
        Arguments.of(
            null,
            accountIdentification().defaultAccount(),
            Collections.singletonList(directDebit().payment().defaultPayment()),
            "DirectDebitDocumentData 'messageId' cannot be null"),
        Arguments.of(
            MESSAGE_IDENTIFICATION,
            null,
            Collections.singletonList(directDebit().payment().defaultPayment()),
            "DirectDebitDocumentData 'creditor' cannot be null"),
        Arguments.of(
            MESSAGE_IDENTIFICATION,
            accountIdentification().defaultAccount(),
            null,
            "DirectDebitDocumentData 'payments' cannot be null"));
  }
}
