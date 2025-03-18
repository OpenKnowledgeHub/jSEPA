package org.openknowledgehub.data.transfer;

import static org.openknowledgehub.api.assertions.JSepaAssertions.jSepaAssertThat;
import static org.openknowledgehub.api.objects.AccountIdentificationTestProvider.SPANISH_BIC;
import static org.openknowledgehub.api.objects.AccountIdentificationTestProvider.SPANISH_IBAN;
import static org.openknowledgehub.api.objects.AccountIdentificationTestProvider.SPANISH_IDENTIFICATION;
import static org.openknowledgehub.api.objects.AccountIdentificationTestProvider.SPANISH_NAME;
import static org.openknowledgehub.api.objects.SepaTransferTestProvider.DATE_OF_EXECUTION;
import static org.openknowledgehub.api.objects.SepaTransferTestProvider.MESSAGE_ID;
import static org.openknowledgehub.api.objects.SepaTransferTestProvider.SepaTransferPaymentTestProvider.AMOUNT;
import static org.openknowledgehub.api.objects.SepaTransferTestProvider.SepaTransferPaymentTestProvider.END_TO_END_ID;
import static org.openknowledgehub.api.objects.SepaTransferTestProvider.SepaTransferPaymentTestProvider.REASON_FOR_PAYMENT;
import static org.openknowledgehub.api.objects.TestObjects.accountIdentification;
import static org.openknowledgehub.api.objects.TestObjects.transfer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.openknowledgehub.exception.JSepaValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test SepaTransferDocument builder")
class SepaTransferDocumentBuilderTest {

  @Test
  @DisplayName("Should create a valid SepaTransferDocument")
  void testCreate() {
    final var createdTransferDocument =
        SepaTransferDocumentBuilder.create(MESSAGE_ID)
            .addPayment(transfer().payment().defaultBuilder())
            .withDateOfExecution(DATE_OF_EXECUTION)
            .withPayer(accountIdentification().spanish())
            .build();

    jSepaAssertThat(createdTransferDocument)
        .isNotNull()
        .hasMessageId(MESSAGE_ID)
        .hasDateOfExecution(DATE_OF_EXECUTION);

    jSepaAssertThat(createdTransferDocument.getPayer())
        .isNotNull()
        .hasIdentifier(SPANISH_IDENTIFICATION)
        .hasName(SPANISH_NAME)
        .hasIban(SPANISH_IBAN)
        .hasBic(SPANISH_BIC);

    assertThat(createdTransferDocument.getPayments()).isNotNull().isNotEmpty().hasSize(1);

    final var createdPayment = createdTransferDocument.getPayments().get(0);

    jSepaAssertThat(createdPayment)
        .hasReasonForPayment(REASON_FOR_PAYMENT)
        .hasAmount(AMOUNT)
        .hasEndToEndId(END_TO_END_ID);

    jSepaAssertThat(createdPayment.getPayee()).isNotNull().hasDefaultTestValues();
  }

  @Test
  @DisplayName("Should not allow to build with null message identifier")
  void testCreateWithNullIdentifier() {
    assertThatThrownBy(() -> SepaTransferDocumentBuilder.create(null))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'messageId' cannot be null");
  }

  @Test
  @DisplayName("Should not allow to build with empty message identifier")
  void testCreateWithEmptyIdentifier() {
    assertThatThrownBy(() -> SepaTransferDocumentBuilder.create("  "))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'messageId' cannot be empty");
  }

  @Test
  @DisplayName("Should not allow to build with null payer")
  void testCreateWithNullPayer() {
    SepaTransferDocumentBuilder builder = SepaTransferDocumentBuilder.create(MESSAGE_ID);

    assertThatThrownBy(() -> builder.withPayer(null))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'payer' cannot be null");
  }

  @Test
  @DisplayName("Should not allow to build with null date of execution")
  void testCreateWithNullDateOfExecution() {
    SepaTransferDocumentBuilder builder = SepaTransferDocumentBuilder.create(MESSAGE_ID);

    assertThatThrownBy(() -> builder.withDateOfExecution(null))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'dateOfExecution' cannot be null");
  }

  @Test
  @DisplayName("Should not allow to build with null payment builder")
  void testCreateWithNullPaymentBuilder() {
    SepaTransferDocumentBuilder builder = SepaTransferDocumentBuilder.create(MESSAGE_ID);

    assertThatThrownBy(() -> builder.addPayment(null))
        .isInstanceOf(JSepaValidationException.class)
        .hasMessage("'paymentBuilder' cannot be null");
  }
}
