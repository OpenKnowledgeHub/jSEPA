package io.jsepa.transformer.sepa;

import static io.jsepa.api.assertions.JSepaAssertions.jSepaAssertThat;

import io.jsepa.api.objects.TestObjects;
import io.jsepa.data.transfer.SepaTransferDocumentData;
import io.jsepa.transformer.JSepaTransformer;
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
