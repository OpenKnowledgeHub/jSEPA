package io.jsepa.api.assertions;

import static io.jsepa.api.objects.SepaTransferTestProvider.SepaTransferPaymentTestProvider.AMOUNT;
import static io.jsepa.api.objects.SepaTransferTestProvider.SepaTransferPaymentTestProvider.END_TO_END_ID;
import static io.jsepa.api.objects.SepaTransferTestProvider.SepaTransferPaymentTestProvider.REASON_FOR_PAYMENT;
import static org.assertj.core.api.Assertions.assertThat;

import io.jsepa.api.objects.TestObjects;
import io.jsepa.data.transfer.SepaTransferPayment;
import java.math.BigDecimal;
import org.assertj.core.api.AbstractAssert;

public class SepaTransferPaymentAssert
    extends AbstractAssert<SepaTransferPaymentAssert, SepaTransferPayment> {
  protected SepaTransferPaymentAssert(SepaTransferPayment sepaTransferPayment) {
    super(sepaTransferPayment, SepaTransferPaymentAssert.class);
  }

  public SepaTransferPaymentAssert hasDefaultTestValues() {
    assertThat(actual.getAmount()).isEqualTo(AMOUNT);
    assertThat(actual.getPayee()).isEqualTo(TestObjects.accountIdentification().defaultAccount());
    assertThat(actual.getReasonForPayment()).isEqualTo(REASON_FOR_PAYMENT);
    assertThat(actual.getEndToEndId()).isEqualTo(END_TO_END_ID);

    return this;
  }

  public SepaTransferPaymentAssert hasAmount(BigDecimal expectedAmount) {
    assertThat(actual.getAmount()).isEqualTo(expectedAmount);

    return this;
  }

  public SepaTransferPaymentAssert hasEndToEndId(String expectedEndToEndId) {
    assertThat(actual.getEndToEndId()).isEqualTo(expectedEndToEndId);

    return this;
  }

  public SepaTransferPaymentAssert hasReasonForPayment(String expectedReasonForPayment) {
    assertThat(actual.getReasonForPayment()).isEqualTo(expectedReasonForPayment);

    return this;
  }

  public SepaTransferPaymentAssert isEmpty() {
    assertThat(actual.getAmount()).isNull();
    assertThat(actual.getPayee()).isNull();
    assertThat(actual.getReasonForPayment()).isNull();
    assertThat(actual.getEndToEndId()).isNull();

    return this;
  }
}
