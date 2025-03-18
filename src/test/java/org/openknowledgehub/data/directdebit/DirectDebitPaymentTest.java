package org.openknowledgehub.data.directdebit;

import static org.openknowledgehub.api.assertions.JSepaAssertions.jSepaAssertThat;
import static org.openknowledgehub.api.objects.DirectDebitTestProvider.DirectDebitPaymentTestProvider.AMOUNT;
import static org.openknowledgehub.api.objects.DirectDebitTestProvider.DirectDebitPaymentTestProvider.DUE_AT;
import static org.openknowledgehub.api.objects.DirectDebitTestProvider.DirectDebitPaymentTestProvider.PAYMENT_IDENTIFICATION;
import static org.openknowledgehub.api.objects.DirectDebitTestProvider.DirectDebitPaymentTestProvider.REASON_FOR_PAYMENT;
import static org.openknowledgehub.api.objects.TestObjects.accountIdentification;
import static org.openknowledgehub.api.objects.TestObjects.mandate;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.openknowledgehub.api.objects.TestObjects;
import org.openknowledgehub.data.common.AccountIdentification;
import org.openknowledgehub.exception.JSepaValidationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Test DirectDebitPayment data class")
class DirectDebitPaymentTest {

  @Test
  @DisplayName("Should create a valid DirectDebitPayment")
  void testCreate() {
    final var createdPayment =
        new DirectDebitPayment(
            PAYMENT_IDENTIFICATION,
            AMOUNT,
            accountIdentification().defaultAccount(),
            REASON_FOR_PAYMENT,
            DUE_AT,
            mandate().defaultMandate());

    jSepaAssertThat(createdPayment)
        .isNotNull()
        .hasIdentification(PAYMENT_IDENTIFICATION)
        .hasAmount(AMOUNT)
        .hasReasonForPayment(REASON_FOR_PAYMENT)
        .hasDueAt(DUE_AT);

    jSepaAssertThat(createdPayment.getDebitor()).isNotNull().hasDefaultTestValues();

    jSepaAssertThat(createdPayment.getMandate()).isNotNull().hasDefaultTestValues();
  }

  @Test
  @DisplayName("Should create an empty DirectDebitPayment for JAX-B")
  void testCreateEmpty() {
    final var createdPayment = new DirectDebitPayment();

    jSepaAssertThat(createdPayment).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("Should allow with null reason for payment")
  void testCreateWithNullReasonForPayment() {
    final var createdPayment =
        new DirectDebitPayment(
            PAYMENT_IDENTIFICATION,
            AMOUNT,
            accountIdentification().defaultAccount(),
            null,
            DUE_AT,
            mandate().defaultMandate());

    jSepaAssertThat(createdPayment)
        .isNotNull()
        .hasIdentification(PAYMENT_IDENTIFICATION)
        .hasAmount(AMOUNT)
        .hasReasonForPayment(null)
        .hasDueAt(DUE_AT);

    jSepaAssertThat(createdPayment.getDebitor()).isNotNull().hasDefaultTestValues();

    jSepaAssertThat(createdPayment.getMandate()).isNotNull().hasDefaultTestValues();
  }

  @ParameterizedTest
  @MethodSource("provideNullTestArguments")
  @DisplayName("Should not allow null values")
  void testCreateWithNullValues(
      final String givenIdentification,
      final BigDecimal givenAmount,
      final AccountIdentification givenDebitor,
      final LocalDate givenDueAt,
      final Mandate givenMandate,
      final String expectedErrorMessage) {

    assertThatThrownBy(
            () ->
                new DirectDebitPayment(
                    givenIdentification, givenAmount, givenDebitor, null, givenDueAt, givenMandate))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage(expectedErrorMessage);
  }

  private static Stream<Arguments> provideNullTestArguments() {
    return Stream.of(
        Arguments.of(
            null,
            AMOUNT,
            TestObjects.accountIdentification().defaultAccount(),
            DUE_AT,
            TestObjects.mandate().defaultMandate(),
            "DirectDebitPayment 'identification' cannot be null"),
        Arguments.of(
            PAYMENT_IDENTIFICATION,
            null,
            TestObjects.accountIdentification().defaultAccount(),
            DUE_AT,
            TestObjects.mandate().defaultMandate(),
            "DirectDebitPayment 'amount' cannot be null"),
        Arguments.of(
            PAYMENT_IDENTIFICATION,
            AMOUNT,
            null,
            DUE_AT,
            TestObjects.mandate().defaultMandate(),
            "DirectDebitPayment 'debitor' cannot be null"),
        Arguments.of(
            PAYMENT_IDENTIFICATION,
            AMOUNT,
            TestObjects.accountIdentification().defaultAccount(),
            null,
            TestObjects.mandate().defaultMandate(),
            "DirectDebitPayment 'dueAt' cannot be null"),
        Arguments.of(
            PAYMENT_IDENTIFICATION,
            AMOUNT,
            TestObjects.accountIdentification().defaultAccount(),
            DUE_AT,
            null,
            "DirectDebitPayment 'mandate' cannot be null"));
  }
}
