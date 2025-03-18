package org.openknowledgehub.transformer.sepa;

import static org.openknowledgehub.api.assertions.JSepaAssertions.jSepaAssertThat;

import org.openknowledgehub.api.objects.TestObjects;
import org.openknowledgehub.data.transfer.SepaTransferDocumentData;
import org.openknowledgehub.transformer.JSepaTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test Transfer transformer")
class TransferTransformerTest {

  JSepaTransformer<SepaTransferDocumentData> underTest;

  @BeforeEach
  void setUp() {
    underTest = new TransferTransformer();
  }

  @Test
  @DisplayName("Should transform valid SepaTransferDocumentData")
  void testTransform() {
    final var transformedXml =
        underTest.transform(TestObjects.transfer().document().defaultDocument());

    jSepaAssertThat(transformedXml).isNotNull().isInAValidTransferShape();
  }
}
