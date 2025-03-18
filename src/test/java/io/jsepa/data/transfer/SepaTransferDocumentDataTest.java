package io.jsepa.data.transfer;

import static io.jsepa.api.assertions.JSepaAssertions.jSepaAssertThat;
import static io.jsepa.api.objects.DirectDebitTestProvider.MESSAGE_IDENTIFICATION;
import static io.jsepa.api.objects.SepaTransferTestProvider.DATE_OF_EXECUTION;
import static io.jsepa.api.objects.SepaTransferTestProvider.MESSAGE_ID;
import static io.jsepa.api.objects.TestObjects.accountIdentification;
import static io.jsepa.api.objects.TestObjects.transfer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsepa.api.objects.TestObjects;
import io.jsepa.data.common.AccountIdentification;
import io.jsepa.exception.JSepaValidationException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Test SepaTransferDocument data class")
class SepaTransferDocumentDataTest {

  @Test
  @DisplayName("Should create a valid SepaTransferDocumentData")
  void testCreate() {
    final var createdDocumentData =
        new SepaTransferDocumentData(
            TestObjects.accountIdentification().defaultAccount(),
            MESSAGE_ID,
            DATE_OF_EXECUTION,
            List.of(TestObjects.transfer().payment().defaultPayment()));

    jSepaAssertThat(createdDocumentData)
        .isNotNull()
        .hasMessageId(MESSAGE_ID)
        .hasDateOfExecution(DATE_OF_EXECUTION);

    jSepaAssertThat(createdDocumentData.getPayer()).isNotNull().hasDefaultTestValues();

    assertThat(createdDocumentData.getPayments()).isNotNull().isNotEmpty().hasSize(1);

    jSepaAssertThat(createdDocumentData.getPayments().get(0)).isNotNull().hasDefaultTestValues();
  }

  @Test
  @DisplayName("Should create an empty SepaTransferDocumentData for JAX-B")
  void testCreateEmpty() {
    final var createdDocumentData = new SepaTransferDocumentData();

    jSepaAssertThat(createdDocumentData).isNotNull().isEmpty();
  }

  @ParameterizedTest
  @MethodSource("provideNullTestArguments")
  @DisplayName("Should not allow null values")
  void testCreateWithNullValues(
      final AccountIdentification givenPayer,
      final String givenMessageId,
      final LocalDateTime givenDateOfExecution,
      final List<SepaTransferPayment> givenPayments,
      final String expectedErrorMessage) {

    assertThatThrownBy(
            () ->
                new SepaTransferDocumentData(
                    givenPayer, givenMessageId, givenDateOfExecution, givenPayments))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage(expectedErrorMessage);
  }

  private static Stream<Arguments> provideNullTestArguments() {
    return Stream.of(
        Arguments.of(
            null,
            MESSAGE_IDENTIFICATION,
            DATE_OF_EXECUTION,
            Collections.singletonList(transfer().payment().defaultPayment()),
            "SepaTransferDocumentData 'payer' cannot be null"),
        Arguments.of(
            accountIdentification().defaultAccount(),
            null,
            DATE_OF_EXECUTION,
            Collections.singletonList(transfer().payment().defaultPayment()),
            "SepaTransferDocumentData 'messageId' cannot be null"),
        Arguments.of(
            accountIdentification().defaultAccount(),
            MESSAGE_IDENTIFICATION,
            null,
            Collections.singletonList(transfer().payment().defaultPayment()),
            "SepaTransferDocumentData 'dateOfExecution' cannot be null"),
        Arguments.of(
            accountIdentification().defaultAccount(),
            MESSAGE_IDENTIFICATION,
            DATE_OF_EXECUTION,
            null,
            "SepaTransferDocumentData 'payments' cannot be null"));
  }
}
