package org.openknowledgehub.api.assertions;

import static org.assertj.core.api.Assertions.assertThat;

import org.openknowledgehub.data.directdebit.DirectDebitDocumentData;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.data.TemporalUnitWithinOffset;

public class DirectDebitDocumentDataAssert
    extends AbstractAssert<DirectDebitDocumentDataAssert, DirectDebitDocumentData> {
  protected DirectDebitDocumentDataAssert(DirectDebitDocumentData directDebitDocumentData) {
    super(directDebitDocumentData, DirectDebitDocumentDataAssert.class);
  }

  public DirectDebitDocumentDataAssert isEmpty() {
    assertThat(actual.getCreationTime()).isNull();
    assertThat(actual.getMessageId()).isNull();
    assertThat(actual.getCreditor()).isNull();
    assertThat(actual.getPayments()).isNull();
    assertThat(actual.getPaymentMethod()).isNull();

    return this;
  }

  public DirectDebitDocumentDataAssert hasMessageId(String expectedMessageId) {
    assertThat(actual.getMessageId()).isEqualTo(expectedMessageId);

    return this;
  }

  public DirectDebitDocumentDataAssert hasCreationTimeCloseToNow() {
    assertThat(actual.getCreationTime())
        .isCloseTo(LocalDateTime.now(), new TemporalUnitWithinOffset(500, ChronoUnit.MILLIS));

    return this;
  }
}
