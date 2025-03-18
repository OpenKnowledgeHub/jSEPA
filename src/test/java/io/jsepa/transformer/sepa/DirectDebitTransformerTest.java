package io.jsepa.transformer.sepa;

import static io.jsepa.api.assertions.JSepaAssertions.jSepaAssertThat;

import io.jsepa.api.objects.TestObjects;
import io.jsepa.data.directdebit.DirectDebitDocumentData;
import io.jsepa.transformer.JSepaTransformer;
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
