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

import io.jsepa.api.objects.AccountIdentificationTestProvider;
import io.jsepa.api.objects.MandateTestProvider;
import io.jsepa.data.common.AccountIdentification;
import io.jsepa.data.directdebit.Mandate;
import io.jsepa.data.directdebit.MandateType;
import java.time.LocalDateTime;
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
      final Mandate createdMandate =
          DSL.firstUseOfMandate(MandateTestProvider.MANDATE_ID)
              .issuedAt(MandateTestProvider.ISSUED_AT)
              .get();

      jSepaAssertThat(createdMandate)
          .isNotNull()
          .hasMandateId(MandateTestProvider.MANDATE_ID)
          .isFromType(MandateType.FIRST)
          .wasIssuedAt(MandateTestProvider.ISSUED_AT);
    }

    @Test
    @DisplayName("Should create last usage Mandate")
    void testMandateCreationLastUsage() {
      final Mandate createdMandate =
          DSL.lastUseOfMandate(MandateTestProvider.MANDATE_ID)
              .issuedAt(MandateTestProvider.ISSUED_AT)
              .get();

      jSepaAssertThat(createdMandate)
          .isNotNull()
          .hasMandateId(MandateTestProvider.MANDATE_ID)
          .isFromType(MandateType.FINAL)
          .wasIssuedAt(MandateTestProvider.ISSUED_AT);
    }

    @Test
    @DisplayName("Should create one time Mandate")
    void testMandateCreationOneTimeUsage() {
      final Mandate createdMandate =
          DSL.oneTimeMandate(MandateTestProvider.MANDATE_ID)
              .issuedAt(MandateTestProvider.ISSUED_AT)
              .get();

      jSepaAssertThat(createdMandate)
          .isNotNull()
          .hasMandateId(MandateTestProvider.MANDATE_ID)
          .isFromType(MandateType.ONE_OFF)
          .wasIssuedAt(MandateTestProvider.ISSUED_AT);
    }

    @Test
    @DisplayName("Should create recurrent Mandate")
    void testMandateCreationRecurrentUsage() {
      final Mandate createdMandate =
          DSL.recurrentMandate(MandateTestProvider.MANDATE_ID)
              .issuedAt(MandateTestProvider.ISSUED_AT)
              .get();

      jSepaAssertThat(createdMandate)
          .isNotNull()
          .hasMandateId(MandateTestProvider.MANDATE_ID)
          .isFromType(MandateType.RECURRING)
          .wasIssuedAt(MandateTestProvider.ISSUED_AT);
    }
  }

  @Nested
  @DisplayName("Test DSL for AccountIdentifier")
  public class TestAccountIdentifier {

    @Test
    @DisplayName("Should create a valid SepaIdentification")
    public void testCreateAccountIdentifier() {

      DSL.transfer("MessageId")
          .from(
              DSL.account()
                  .name("Payer Name")
                  .identification("Payer Identification")
                  .bic("BYLADEM1001")
                  .iban("DE02120300000000202051"))
          .on(LocalDateTime.now().plusWeeks(1))
          .to(
              DSL.account()
                  .name("Payee Name")
                  .identification("Payee Identification")
                  .bic("BYLADEM1001")
                  .iban("DE02120300000000203051"))
          .amount(125)
          .withEndToEndIdentifier("End to end identification")
          .toXml();

      final AccountIdentification createdIdentification =
          DSL.account()
              .name(AccountIdentificationTestProvider.NAME)
              .identification(AccountIdentificationTestProvider.IDENTIFICATION)
              .bic(AccountIdentificationTestProvider.BIC)
              .iban(AccountIdentificationTestProvider.IBAN)
              .get();

      jSepaAssertThat(createdIdentification)
          .isNotNull()
          .hasIdentifier(AccountIdentificationTestProvider.IDENTIFICATION)
          .hasName(AccountIdentificationTestProvider.NAME)
          .hasBic(AccountIdentificationTestProvider.BIC)
          .hasIban(AccountIdentificationTestProvider.IBAN);
    }
  }
}
