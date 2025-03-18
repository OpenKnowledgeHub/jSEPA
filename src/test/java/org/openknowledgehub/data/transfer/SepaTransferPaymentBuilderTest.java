package org.openknowledgehub.data.transfer;

import static org.openknowledgehub.api.assertions.JSepaAssertions.jSepaAssertThat;
import static org.openknowledgehub.api.objects.SepaTransferTestProvider.SepaTransferPaymentTestProvider.AMOUNT;
import static org.openknowledgehub.api.objects.SepaTransferTestProvider.SepaTransferPaymentTestProvider.END_TO_END_ID;
import static org.openknowledgehub.api.objects.SepaTransferTestProvider.SepaTransferPaymentTestProvider.REASON_FOR_PAYMENT;
import static org.openknowledgehub.api.objects.TestObjects.accountIdentification;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.openknowledgehub.exception.JSepaValidationException;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test SepaTransferPayment builder")
class SepaTransferPaymentBuilderTest {

  SepaTransferPaymentBuilder underTest;

  @BeforeEach
  void setup() {
    underTest = SepaTransferPaymentBuilder.withPayment();
  }

  @Test
  @DisplayName("Should build a valid SepaTransferPayment")
  void testBuildValid() {
    final var createdPayment =
        underTest
            .withReasonForPayment(REASON_FOR_PAYMENT)
            .withAmount(AMOUNT)
            .withEndToEndId(END_TO_END_ID)
            .withPayee(accountIdentification().defaultAccount())
            .build();

    jSepaAssertThat(createdPayment)
        .isNotNull()
        .hasEndToEndId(END_TO_END_ID)
        .hasAmount(AMOUNT)
        .hasReasonForPayment(REASON_FOR_PAYMENT);

    jSepaAssertThat(createdPayment.getPayee()).isNotNull().hasDefaultTestValues();
  }

  @Test
  @DisplayName("Should not allow to build with null reason for payment")
  void testCreateWithNullReasonForPayment() {
    assertThatThrownBy(() -> underTest.withReasonForPayment(null))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'reasonForPayment' cannot be null");
  }

  @Test
  @DisplayName("Should not allow to build with empty reason for payment")
  void testCreateWithEmptyReasonForPayment() {
    assertThatThrownBy(() -> underTest.withReasonForPayment("  "))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'reasonForPayment' cannot be empty");
  }

  @Test
  @DisplayName("Should not allow to build with null amount")
  void testCreateWithNullAmount() {
    assertThatThrownBy(() -> underTest.withAmount(null))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'amount' cannot be null");
  }

  @Test
  @DisplayName("Should not allow to build with negativ amount")
  void testCreateWithNegativAmount() {
    final var negativAmount = BigDecimal.valueOf(-3);

    assertThatThrownBy(() -> underTest.withAmount(negativAmount))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'amount' should be greater than 0");
  }

  @Test
  @DisplayName("Should not allow to build with zero amount")
  void testCreateWithZeroAmount() {
    final var zeroAmount = BigDecimal.valueOf(0);

    assertThatThrownBy(() -> underTest.withAmount(zeroAmount))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'amount' should be greater than 0");
  }

  @Test
  @DisplayName("Should not allow to build with null payee")
  void testCreateWithNullPayee() {
    assertThatThrownBy(() -> underTest.withPayee(null))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'payee' cannot be null");
  }

  @Test
  @DisplayName("Should not allow to build with null end to end id")
  void testCreateWithNullEndToEndId() {
    assertThatThrownBy(() -> underTest.withEndToEndId(null))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'endToEndId' cannot be null");
  }

  @Test
  @DisplayName("Should not allow to build with empty end to end id")
  void testCreateWithEmptyEndToEndId() {
    assertThatThrownBy(() -> underTest.withEndToEndId("  "))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'endToEndId' cannot be empty");
  }
}
