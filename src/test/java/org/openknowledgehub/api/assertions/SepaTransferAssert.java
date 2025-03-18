package org.openknowledgehub.api.assertions;

import static org.assertj.core.api.Assertions.assertThat;

import org.openknowledgehub.data.transfer.SepaTransferDocumentData;
import java.time.LocalDateTime;
import org.assertj.core.api.AbstractAssert;

public class SepaTransferAssert
    extends AbstractAssert<SepaTransferAssert, SepaTransferDocumentData> {
  protected SepaTransferAssert(SepaTransferDocumentData sepaTransferDocumentData) {
    super(sepaTransferDocumentData, SepaTransferAssert.class);
  }

  public SepaTransferAssert hasMessageId(String expectedMessageId) {
    assertThat(actual.getMessageId()).isEqualTo(expectedMessageId);

    return this;
  }

  public SepaTransferAssert hasDateOfExecution(LocalDateTime expectedDateOfExecution) {
    assertThat(actual.getDateOfExecution()).isEqualTo(expectedDateOfExecution);

    return this;
  }

  public SepaTransferAssert isEmpty() {
    assertThat(actual.getCreationTime()).isNull();
    assertThat(actual.getPayer()).isNull();
    assertThat(actual.getMessageId()).isNull();
    assertThat(actual.getDateOfExecution()).isNull();
    assertThat(actual.getPayments()).isNull();

    return this;
  }
}
