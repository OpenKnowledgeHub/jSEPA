/*
 *  All rights reserved.
 */
package eu.rbecker.jsepa.transfer;

import eu.rbecker.jsepa.directdebit.util.SepaXmlDocumentBuilder;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.AccountIdentificationSEPA;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.ActiveOrHistoricCurrencyAndAmountSEPA;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.ActiveOrHistoricCurrencyCodeEUR;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.AmountTypeSEPA;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.BranchAndFinancialInstitutionIdentificationSEPA1;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.BranchAndFinancialInstitutionIdentificationSEPA3;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.CashAccountSEPA1;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.CashAccountSEPA2;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.ChargeBearerTypeSEPACode;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.CreditTransferTransactionInformationSCT;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.CustomerCreditTransferInitiationV03;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.Document;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.FinancialInstitutionIdentificationSEPA1;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.FinancialInstitutionIdentificationSEPA3;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.GroupHeaderSCT;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.ObjectFactory;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.PartyIdentificationSEPA1;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.PartyIdentificationSEPA2;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.PaymentIdentificationSEPA;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.PaymentInstructionInformationSCT;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.PaymentMethodSCTCode;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.PaymentTypeInformationSCT1;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.RemittanceInformationSEPA1Choice;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_001_003_03.ServiceLevelSEPA;
import java.io.StringWriter;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;

/**
 * @author Robert Becker <robert at rbecker.eu>
 */
class SepaTransferDocumentBuilder extends SepaXmlDocumentBuilder {

  public static String toXml(SepaTransferDocumentData source)
      throws DatatypeConfigurationException {
    Document doc = new Document();
    CustomerCreditTransferInitiationV03 transferData = new CustomerCreditTransferInitiationV03();
    doc.setCstmrCdtTrfInitn(transferData);

    transferData.setGrpHdr(createGroupHeaderSdd(source));

    transferData.getPmtInf().add(createPaymentInstructions(source));

    StringWriter resultWriter = new StringWriter();
    marshal(
        doc.getClass().getPackage().getName(),
        new ObjectFactory().createDocument(doc),
        resultWriter);
    return resultWriter.toString();
  }

  private static GroupHeaderSCT createGroupHeaderSdd(SepaTransferDocumentData data)
      throws DatatypeConfigurationException {
    GroupHeaderSCT result = new GroupHeaderSCT();
    // message id
    result.setMsgId(data.getDocumentMessageId());

    // created on
    result.setCreDtTm(calendarToXmlGregorianCalendarDateTime(GregorianCalendar.getInstance()));

    // number of tx
    result.setNbOfTxs(String.valueOf(data.getPayments().size()));

    // control sum
    result.setCtrlSum(data.getTotalPaymentSum());

    // creditor name
    PartyIdentificationSEPA1 partyIdentificationSEPA1 = new PartyIdentificationSEPA1();
    partyIdentificationSEPA1.setNm(data.getPayerName());

    result.setInitgPty(partyIdentificationSEPA1);

    return result;
  }

  private static PaymentInstructionInformationSCT createPaymentInstructions(
      SepaTransferDocumentData data) throws DatatypeConfigurationException {
    PaymentInstructionInformationSCT result = new PaymentInstructionInformationSCT();
    result.setBtchBookg(data.isBatchBooking());
    result.setChrgBr(ChargeBearerTypeSEPACode.SLEV);
    result.setCtrlSum(data.getTotalPaymentSum());
    result.setNbOfTxs(String.valueOf(data.getPayments().size()));

    setPayerName(data, result);

    setPayerIbanAndBic(data, result);

    result.setPmtInfId(data.getDocumentMessageId());
    result.setPmtMtd(PaymentMethodSCTCode.TRF);
    result.setReqdExctnDt(calendarToXmlGregorianCalendarDateTime(data.getDateOfExecution()));

    setPaymentTypeInformation(result);

    for (SepaTransferPayment p : data.getPayments()) {
      addPaymentData(result, p);
    }

    return result;
  }

  private static void addPaymentData(
      PaymentInstructionInformationSCT result, SepaTransferPayment p) {
    result.getCdtTrfTxInf().add(createPaymentData(p));
  }

  private static void setPayerName(
      SepaTransferDocumentData data, PaymentInstructionInformationSCT result) {
    PartyIdentificationSEPA2 pi2 = new PartyIdentificationSEPA2();
    pi2.setNm(data.getPayerName());
    result.setDbtr(pi2);
  }

  private static void setPayerIbanAndBic(
      SepaTransferDocumentData data, PaymentInstructionInformationSCT result) {
    AccountIdentificationSEPA ai = new AccountIdentificationSEPA();
    ai.setIBAN(data.getPayerIban());
    CashAccountSEPA1 ca1 = new CashAccountSEPA1();
    ca1.setId(ai);
    result.setDbtrAcct(ca1);

    BranchAndFinancialInstitutionIdentificationSEPA3 bafii =
        new BranchAndFinancialInstitutionIdentificationSEPA3();
    FinancialInstitutionIdentificationSEPA3 fii = new FinancialInstitutionIdentificationSEPA3();
    fii.setBIC(data.getPayerBic());
    bafii.setFinInstnId(fii);
    result.setDbtrAgt(bafii);
  }

  private static void setPaymentTypeInformation(PaymentInstructionInformationSCT result) {
    PaymentTypeInformationSCT1 pti = new PaymentTypeInformationSCT1();
    ServiceLevelSEPA sls = new ServiceLevelSEPA();
    sls.setCd("SEPA");
    pti.setSvcLvl(sls);
    result.setPmtTpInf(pti);
  }

  private static CreditTransferTransactionInformationSCT createPaymentData(SepaTransferPayment p) {
    CreditTransferTransactionInformationSCT result = new CreditTransferTransactionInformationSCT();
    setPaymentCurrencyAndSum(p, result);
    setPayeeName(p, result);
    setPayeeIbanAndBic(p, result);
    setEndToEndId(p, result);
    setReasonForPayment(p, result);

    return result;
  }

  private static void setPaymentCurrencyAndSum(
      SepaTransferPayment p, CreditTransferTransactionInformationSCT result) {
    AmountTypeSEPA at = new AmountTypeSEPA();
    ActiveOrHistoricCurrencyAndAmountSEPA aohcaa = new ActiveOrHistoricCurrencyAndAmountSEPA();
    aohcaa.setCcy(ActiveOrHistoricCurrencyCodeEUR.EUR);
    aohcaa.setValue(p.getPaymentSum());
    at.setInstdAmt(aohcaa);
    result.setAmt(at);
  }

  private static void setPayeeName(
      SepaTransferPayment p, CreditTransferTransactionInformationSCT result) {
    PartyIdentificationSEPA2 pis2 = new PartyIdentificationSEPA2();
    pis2.setNm(p.getPayeeName());
    result.setCdtr(pis2);
  }

  private static void setEndToEndId(
      SepaTransferPayment p, CreditTransferTransactionInformationSCT result) {
    PaymentIdentificationSEPA pis = new PaymentIdentificationSEPA();
    String id = p.getEndToEndId();
    pis.setEndToEndId(id == null || id.isEmpty() ? "NOTPROVIDED" : id);
    result.setPmtId(pis);
  }

  private static void setReasonForPayment(
      SepaTransferPayment p, CreditTransferTransactionInformationSCT result) {
    RemittanceInformationSEPA1Choice ri = new RemittanceInformationSEPA1Choice();
    ri.setUstrd(p.getReasonForPayment());
    result.setRmtInf(ri);
  }

  private static void setPayeeIbanAndBic(
      SepaTransferPayment p, CreditTransferTransactionInformationSCT ctti) {
    CashAccountSEPA2 ca = new CashAccountSEPA2();
    AccountIdentificationSEPA ai = new AccountIdentificationSEPA();
    ai.setIBAN(p.getPayeeIban());
    ca.setId(ai);
    ctti.setCdtrAcct(ca);

    BranchAndFinancialInstitutionIdentificationSEPA1 bafiis =
        new BranchAndFinancialInstitutionIdentificationSEPA1();
    FinancialInstitutionIdentificationSEPA1 fii = new FinancialInstitutionIdentificationSEPA1();
    fii.setBIC(p.getPayeeBic());
    bafiis.setFinInstnId(fii);
    ctti.setCdtrAgt(bafiis);
  }
}
