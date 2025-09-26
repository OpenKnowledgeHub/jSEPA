package org.openknowledgehub.transformer.sepa;

import static org.openknowledgehub.api.assertions.JSepaAssertions.jSepaAssertThat;
import static org.openknowledgehub.api.objects.DirectDebitTestProvider.MESSAGE_IDENTIFICATION;

import org.openknowledgehub.api.objects.TestObjects;
import org.openknowledgehub.data.directdebit.DirectDebitDocumentBuilder;
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

  @Test
  @DisplayName("Should map the configured service level")
  void testTransformWithCustomServiceLevel() {
    final var directDebitDocumentData =
        DirectDebitDocumentBuilder.create(MESSAGE_IDENTIFICATION)
            .withCreditor(TestObjects.accountIdentification().defaultAccount())
            .withServiceLevel("SEPA")
            .addPayment(TestObjects.directDebit().payment().defaultBuilder())
            .build();

    final var transformedXml = underTest.transform(directDebitDocumentData);

    jSepaAssertThat(transformedXml).contains("<SvcLvl>").contains("<Cd>SEPA</Cd>");
  }
}
