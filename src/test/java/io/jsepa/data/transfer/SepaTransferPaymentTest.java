package io.jsepa.data.transfer;

import static io.jsepa.api.assertions.JSepaAssertions.jSepaAssertThat;
import static io.jsepa.api.objects.SepaTransferTestProvider.SepaTransferPaymentTestProvider.AMOUNT;
import static io.jsepa.api.objects.SepaTransferTestProvider.SepaTransferPaymentTestProvider.END_TO_END_ID;
import static io.jsepa.api.objects.SepaTransferTestProvider.SepaTransferPaymentTestProvider.REASON_FOR_PAYMENT;
import static io.jsepa.api.objects.TestObjects.accountIdentification;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsepa.data.common.AccountIdentification;
import io.jsepa.exception.JSepaValidationException;
import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Test SepaTransferPayment data class")
class SepaTransferPaymentTest {

  @Test
  @DisplayName("Should create a valid SepaTransferPayment")
  void testCreate() {
    final var createdPayment =
        new SepaTransferPayment(
            AMOUNT, accountIdentification().defaultAccount(), REASON_FOR_PAYMENT, END_TO_END_ID);

    jSepaAssertThat(createdPayment)
        .isNotNull()
        .hasAmount(AMOUNT)
        .hasReasonForPayment(REASON_FOR_PAYMENT)
        .hasEndToEndId(END_TO_END_ID);

    jSepaAssertThat(createdPayment.getPayee()).hasDefaultTestValues();
  }

  @Test
  @DisplayName("Should create an empty SepaTransferPayment for JAX-B")
  void testCreateEmpty() {
    final var createdPayment = new SepaTransferPayment();

    jSepaAssertThat(createdPayment).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("Should allow with null reason for payment")
  void testCreateWithNullReasonForPayment() {
    final var createdPayment =
        new SepaTransferPayment(
            AMOUNT, accountIdentification().defaultAccount(), null, END_TO_END_ID);

    jSepaAssertThat(createdPayment)
        .isNotNull()
        .hasAmount(AMOUNT)
        .hasReasonForPayment(null)
        .hasEndToEndId(END_TO_END_ID);

    jSepaAssertThat(createdPayment.getPayee()).hasDefaultTestValues();
  }

  @ParameterizedTest
  @MethodSource("provideNullTestArguments")
  @DisplayName("Should not allow null values")
  void testCreateWithNullValues(
      final BigDecimal givenAmount,
      final AccountIdentification givenPayee,
      final String givenEndToEndId,
      final String expectedErrorMessage) {

    assertThatThrownBy(
            () ->
                new SepaTransferPayment(
                    givenAmount, givenPayee, REASON_FOR_PAYMENT, givenEndToEndId))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage(expectedErrorMessage);
  }

  private static Stream<Arguments> provideNullTestArguments() {
    return Stream.of(
        Arguments.of(
            null,
            accountIdentification().defaultAccount(),
            END_TO_END_ID,
            "SepaTransferPayment 'amount' cannot be null"),
        Arguments.of(AMOUNT, null, END_TO_END_ID, "SepaTransferPayment 'payee' cannot be null"),
        Arguments.of(
            AMOUNT,
            accountIdentification().defaultAccount(),
            null,
            "SepaTransferPayment 'endToEndId' cannot be null"));
  }
}
