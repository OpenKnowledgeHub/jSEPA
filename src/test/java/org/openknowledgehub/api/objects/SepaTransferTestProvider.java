package org.openknowledgehub.api.objects;

import org.openknowledgehub.data.transfer.SepaTransferDocumentData;
import org.openknowledgehub.data.transfer.SepaTransferPayment;
import org.openknowledgehub.data.transfer.SepaTransferPaymentBuilder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SepaTransferTestProvider {

  public static final String MESSAGE_ID = "MessageId";
  public static final LocalDateTime DATE_OF_EXECUTION = LocalDateTime.now().plusWeeks(2);

  public SepaTransferPaymentTestProvider payment() {
    return new SepaTransferPaymentTestProvider();
  }

  public SepaTransferDocumentTestProvider document() {
    return new SepaTransferDocumentTestProvider();
  }

  public static class SepaTransferDocumentTestProvider {
    public SepaTransferDocumentData defaultDocument() {
      return new SepaTransferDocumentData(
          TestObjects.accountIdentification().defaultAccount(),
          MESSAGE_ID,
          DATE_OF_EXECUTION,
          List.of(TestObjects.transfer().payment().defaultPayment()));
    }
  }

  public static class SepaTransferPaymentTestProvider {

    public static final BigDecimal AMOUNT = new BigDecimal("20.09");
    public static final String END_TO_END_ID = "EndToEndId";
    public static final String REASON_FOR_PAYMENT = "Transfer reason";

    public SepaTransferPaymentBuilder defaultBuilder() {
      return SepaTransferPaymentBuilder.withPayment()
          .withAmount(AMOUNT)
          .withEndToEndId(END_TO_END_ID)
          .withPayee(TestObjects.accountIdentification().defaultAccount())
          .withReasonForPayment(REASON_FOR_PAYMENT);
    }

    public SepaTransferPayment defaultPayment() {
      return new SepaTransferPayment(
          AMOUNT,
          TestObjects.accountIdentification().defaultAccount(),
          REASON_FOR_PAYMENT,
          END_TO_END_ID);
    }
  }
}
