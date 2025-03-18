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

package io.jsepa.dsl;

import static io.jsepa.api.assertions.JSepaAssertions.jSepaAssertThat;
import static io.jsepa.api.objects.AccountIdentificationTestProvider.BIC;
import static io.jsepa.api.objects.AccountIdentificationTestProvider.IBAN;
import static io.jsepa.api.objects.AccountIdentificationTestProvider.IDENTIFICATION;
import static io.jsepa.api.objects.AccountIdentificationTestProvider.NAME;
import static io.jsepa.api.objects.AccountIdentificationTestProvider.SPANISH_BIC;
import static io.jsepa.api.objects.AccountIdentificationTestProvider.SPANISH_IBAN;
import static io.jsepa.api.objects.AccountIdentificationTestProvider.SPANISH_IDENTIFICATION;
import static io.jsepa.api.objects.AccountIdentificationTestProvider.SPANISH_NAME;
import static io.jsepa.api.objects.DirectDebitTestProvider.DirectDebitPaymentTestProvider.DUE_AT;
import static io.jsepa.api.objects.MandateTestProvider.ISSUED_AT;
import static io.jsepa.api.objects.MandateTestProvider.MANDATE_ID;
import static io.jsepa.api.objects.SepaTransferTestProvider.DATE_OF_EXECUTION;
import static io.jsepa.api.objects.SepaTransferTestProvider.MESSAGE_ID;
import static io.jsepa.api.objects.SepaTransferTestProvider.SepaTransferPaymentTestProvider.AMOUNT;
import static io.jsepa.api.objects.SepaTransferTestProvider.SepaTransferPaymentTestProvider.END_TO_END_ID;
import static io.jsepa.api.objects.SepaTransferTestProvider.SepaTransferPaymentTestProvider.REASON_FOR_PAYMENT;

import io.jsepa.data.directdebit.MandateType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Test the DSL")
class DSLTest {

  @Nested
  @DisplayName("Test DSL for Mandate")
  class TestMandate {

    @Test
    @DisplayName("Should create first usage Mandate")
    void testMandateCreationFirstUsage() {
      final var createdMandate = DSL.firstUseOfMandate(MANDATE_ID).issuedAt(ISSUED_AT).get();

      jSepaAssertThat(createdMandate)
          .isNotNull()
          .hasMandateId(MANDATE_ID)
          .isFromType(MandateType.FIRST)
          .wasIssuedAt(ISSUED_AT);
    }

    @Test
    @DisplayName("Should create last usage Mandate")
    void testMandateCreationLastUsage() {
      final var createdMandate = DSL.lastUseOfMandate(MANDATE_ID).issuedAt(ISSUED_AT).get();

      jSepaAssertThat(createdMandate)
          .isNotNull()
          .hasMandateId(MANDATE_ID)
          .isFromType(MandateType.FINAL)
          .wasIssuedAt(ISSUED_AT);
    }

    @Test
    @DisplayName("Should create one time Mandate")
    void testMandateCreationOneTimeUsage() {
      final var createdMandate = DSL.oneTimeMandate(MANDATE_ID).issuedAt(ISSUED_AT).get();

      jSepaAssertThat(createdMandate)
          .isNotNull()
          .hasMandateId(MANDATE_ID)
          .isFromType(MandateType.ONE_OFF)
          .wasIssuedAt(ISSUED_AT);
    }

    @Test
    @DisplayName("Should create recurrent Mandate")
    void testMandateCreationRecurrentUsage() {
      final var createdMandate = DSL.recurrentMandate(MANDATE_ID).issuedAt(ISSUED_AT).get();

      jSepaAssertThat(createdMandate)
          .isNotNull()
          .hasMandateId(MANDATE_ID)
          .isFromType(MandateType.RECURRING)
          .wasIssuedAt(ISSUED_AT);
    }
  }

  @Nested
  @DisplayName("Test DSL for AccountIdentifier")
  class TestAccountIdentifier {

    @Test
    @DisplayName("Should create a valid SepaIdentification")
    void testCreateAccountIdentifier() {
      final var createdIdentification =
          DSL.account().name(NAME).identification(IDENTIFICATION).bic(BIC).iban(IBAN).get();

      jSepaAssertThat(createdIdentification)
          .isNotNull()
          .hasIdentifier(IDENTIFICATION)
          .hasName(NAME)
          .hasBic(BIC)
          .hasIban(IBAN);
    }
  }

  @Nested
  @DisplayName("Test DSL for transfer documents")
  class TestTransferDocument {

    @Test
    @DisplayName("Should create a valid transfer document")
    void testValidTransferDocument() {
      final var createdXml =
          DSL.transfer(MESSAGE_ID)
              .from(DSL.account().name(NAME).identification(IDENTIFICATION).bic(BIC).iban(IBAN))
              .on(DATE_OF_EXECUTION)
              .to(
                  DSL.account()
                      .name(SPANISH_NAME)
                      .identification(SPANISH_IDENTIFICATION)
                      .bic(SPANISH_BIC)
                      .iban(SPANISH_IBAN))
              .amount(AMOUNT.doubleValue())
              .withEndToEndIdentifier(END_TO_END_ID)
              .withReasonForPayment(REASON_FOR_PAYMENT)
              .toXml();

      jSepaAssertThat(createdXml).isNotNull().isNotBlank().isInAValidTransferShape();
    }

    @Test
    @DisplayName("Should create a valid transfer document without reason for payment")
    void testValidTransferDocumentWithoutReasonForPayment() {
      final var createdXml =
          DSL.transfer(MESSAGE_ID)
              .from(DSL.account().name(NAME).identification(IDENTIFICATION).bic(BIC).iban(IBAN))
              .on(DATE_OF_EXECUTION)
              .to(
                  DSL.account()
                      .name(SPANISH_NAME)
                      .identification(SPANISH_IDENTIFICATION)
                      .bic(SPANISH_BIC)
                      .iban(SPANISH_IBAN))
              .amount(AMOUNT.doubleValue())
              .withEndToEndIdentifier(END_TO_END_ID)
              .toXml();

      jSepaAssertThat(createdXml).isNotNull().isNotBlank().isInAValidTransferShape();
    }

    @Test
    @DisplayName("Should create a valid transfer document with multiple payments")
    void testValidTransferDocumentWithMultiplePayments() {
      final var createdXml =
          DSL.transfer(MESSAGE_ID)
              .from(DSL.account().name(NAME).identification(IDENTIFICATION).bic(BIC).iban(IBAN))
              .on(DATE_OF_EXECUTION)
              .to(
                  DSL.account()
                      .name(SPANISH_NAME)
                      .identification(SPANISH_IDENTIFICATION)
                      .bic(SPANISH_BIC)
                      .iban(SPANISH_IBAN))
              .amount(AMOUNT.doubleValue())
              .withEndToEndIdentifier(END_TO_END_ID)
              .withReasonForPayment(REASON_FOR_PAYMENT)
              .and()
              .to(
                  DSL.account()
                      .name(SPANISH_NAME)
                      .identification(SPANISH_IDENTIFICATION)
                      .bic(SPANISH_BIC)
                      .iban(SPANISH_IBAN))
              .amount(AMOUNT.doubleValue())
              .withEndToEndIdentifier(END_TO_END_ID)
              .toXml();

      jSepaAssertThat(createdXml).isNotNull().isNotBlank().isInAValidTransferShape();
    }
  }

  @Nested
  @DisplayName("Test DSL for direct debit documents")
  class TestDirectDebitDocument {

    @Test
    @DisplayName("Should create a valid direct debit document")
    void testValidDirectDebitDocument() {
      final var createdXml =
          DSL.directDebit(MESSAGE_ID)
              .creditor(DSL.account().name(NAME).identification(IDENTIFICATION).bic(BIC).iban(IBAN))
              .receive(AMOUNT.doubleValue())
              .from(
                  DSL.account()
                      .name(SPANISH_NAME)
                      .identification(SPANISH_IDENTIFICATION)
                      .bic(SPANISH_BIC)
                      .iban(SPANISH_IBAN))
              .on(DUE_AT)
              .withPaymentIdentification("PAYMENT_IDENTIFICATION")
              .overMandate(DSL.firstUseOfMandate(MANDATE_ID).issuedAt(ISSUED_AT))
              .withReasonForPayment(REASON_FOR_PAYMENT)
              .toXml();

      jSepaAssertThat(createdXml).isNotNull().isNotBlank().isInAValidDirectDebitShape();
    }

    @Test
    @DisplayName("Should create a valid direct debit document without reason for payment")
    void testValidDirectDebitDocumentWithoutReasonForPayment() {
      final var createdXml =
          DSL.directDebit(MESSAGE_ID)
              .creditor(DSL.account().name(NAME).identification(IDENTIFICATION).bic(BIC).iban(IBAN))
              .receive(AMOUNT.doubleValue())
              .from(
                  DSL.account()
                      .name(SPANISH_NAME)
                      .identification(SPANISH_IDENTIFICATION)
                      .bic(SPANISH_BIC)
                      .iban(SPANISH_IBAN))
              .on(DUE_AT)
              .withPaymentIdentification("PAYMENT_IDENTIFICATION")
              .overMandate(DSL.firstUseOfMandate(MANDATE_ID).issuedAt(ISSUED_AT))
              .toXml();

      jSepaAssertThat(createdXml).isNotNull().isNotBlank().isInAValidDirectDebitShape();
    }

    @Test
    @DisplayName("Should create a valid direct debit document with multiple payments")
    void testValidDirectDebitDocumentWithMultiplePayments() {
      final var createdXml =
          DSL.directDebit(MESSAGE_ID)
              .creditor(DSL.account().name(NAME).identification(IDENTIFICATION).bic(BIC).iban(IBAN))
              .receive(AMOUNT.doubleValue())
              .from(
                  DSL.account()
                      .name(SPANISH_NAME)
                      .identification(SPANISH_IDENTIFICATION)
                      .bic(SPANISH_BIC)
                      .iban(SPANISH_IBAN))
              .on(DUE_AT)
              .withPaymentIdentification("PAYMENT_IDENTIFICATION")
              .overMandate(DSL.firstUseOfMandate(MANDATE_ID).issuedAt(ISSUED_AT))
              .and()
              .receive(AMOUNT.doubleValue())
              .from(
                  DSL.account()
                      .name(SPANISH_NAME)
                      .identification(SPANISH_IDENTIFICATION)
                      .bic(SPANISH_BIC)
                      .iban(SPANISH_IBAN))
              .on(DUE_AT)
              .withPaymentIdentification("PAYMENT_IDENTIFICATION")
              .overMandate(DSL.lastUseOfMandate(MANDATE_ID).issuedAt(ISSUED_AT))
              .withReasonForPayment(REASON_FOR_PAYMENT)
              .toXml();

      jSepaAssertThat(createdXml).isNotNull().isNotBlank().isInAValidDirectDebitShape();
    }
  }
}
