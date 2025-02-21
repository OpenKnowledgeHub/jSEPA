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

import static io.jsepa.api.assertions.JSepaAssertions.jSepaAssertThat;
import static io.jsepa.api.objects.DirectDebitTestProvider.MESSAGE_IDENTIFICATION;
import static io.jsepa.api.objects.TestObjects.accountIdentification;
import static io.jsepa.api.objects.TestObjects.directDebit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsepa.data.common.AccountIdentification;
import io.jsepa.exception.JSepaValidationException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.data.TemporalUnitWithinOffset;
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
    final DirectDebitDocumentData createdDirectDebitDocumentData =
        new DirectDebitDocumentData(
            MESSAGE_IDENTIFICATION,
            accountIdentification().defaultAccount(),
            Collections.singletonList(directDebit().payment().defaultPayment()));

    assertThat(createdDirectDebitDocumentData).isNotNull();
    assertThat(createdDirectDebitDocumentData.getMessageId()).isEqualTo(MESSAGE_IDENTIFICATION);
    assertThat(createdDirectDebitDocumentData.getCreationTime())
        .isCloseTo(LocalDateTime.now(), new TemporalUnitWithinOffset(500, ChronoUnit.MILLIS));
    assertThat(createdDirectDebitDocumentData.getPayments()).isNotNull().hasSize(1);

    jSepaAssertThat(createdDirectDebitDocumentData.getCreditor())
        .isNotNull()
        .hasDefaultTestValues();

    jSepaAssertThat(createdDirectDebitDocumentData.getPayments().get(0))
        .isNotNull()
        .hasDefaultTestValues();
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
            "DirectDebitDocumentData 'messageId' should not be null"),
        Arguments.of(
            MESSAGE_IDENTIFICATION,
            null,
            Collections.singletonList(directDebit().payment().defaultPayment()),
            "DirectDebitDocumentData 'creditor' should not be null"),
        Arguments.of(
            MESSAGE_IDENTIFICATION,
            accountIdentification().defaultAccount(),
            null,
            "DirectDebitDocumentData 'payments' should not be null"));
  }
}
