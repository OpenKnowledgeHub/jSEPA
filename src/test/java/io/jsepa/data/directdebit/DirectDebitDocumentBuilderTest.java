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

import io.jsepa.exception.JSepaValidationException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test the DirectDebitDocument builder")
class DirectDebitDocumentBuilderTest {

  @Test
  @DisplayName("Should create a valid DirectDebitDocument")
  void testCreate() {
    final DirectDebitDocumentData createdDirectDebitDocumentData =
        DirectDebitDocumentBuilder.create(MESSAGE_IDENTIFICATION)
            .withCreditor(accountIdentification().defaultAccount())
            .addPayment(directDebit().payment().defaultBuilder())
            .build();

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

  @Test
  @DisplayName("Should not allow to build with null message identifier")
  void testCreateWithNullIdentifier() {
    assertThatThrownBy(() -> DirectDebitDocumentBuilder.create(null))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'messageId' cannot be null");
  }

  @Test
  @DisplayName("Should not allow to build with empty message identifier")
  void testCreateWithEmptyIdentifier() {
    assertThatThrownBy(() -> DirectDebitDocumentBuilder.create("   "))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'messageId' cannot be empty");
  }

  @Test
  @DisplayName("Should not allow to build with null creditor")
  void testCreateWithNullCreditor() {
    DirectDebitDocumentBuilder builder = DirectDebitDocumentBuilder.create(MESSAGE_IDENTIFICATION);

    assertThatThrownBy(() -> builder.withCreditor(null))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'creditor' cannot be null");
  }

  @Test
  @DisplayName("Should not allow to build with null payment")
  void testCreateWithNullPayment() {
    DirectDebitDocumentBuilder builder = DirectDebitDocumentBuilder.create(MESSAGE_IDENTIFICATION);

    assertThatThrownBy(() -> builder.addPayment(null))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'paymentBuilder' cannot be null");
  }
}
