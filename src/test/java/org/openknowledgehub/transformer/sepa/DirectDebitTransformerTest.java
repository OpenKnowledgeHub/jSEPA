package org.openknowledgehub.transformer.sepa;

import static org.openknowledgehub.api.assertions.JSepaAssertions.jSepaAssertThat;

import org.openknowledgehub.api.objects.TestObjects;
import org.openknowledgehub.data.directdebit.DirectDebitDocumentData;
import org.openknowledgehub.transformer.JSepaTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test DirectDebit transformer")
class DirectDebitTransformerTest {

  JSepaTransformer<DirectDebitDocumentData> underTest;

  @BeforeEach
  void setUp() {
    underTest = new DirectDebitTransformer();
  }

  @Test
  @DisplayName("Should transform valid DirectDebitDocumentData")
  void testTransform() {
    final var transformedXml =
        underTest.transform(TestObjects.directDebit().document().defaultDocument());

    jSepaAssertThat(transformedXml).isNotNull().isInAValidDirectDebitShape();
  }
}
