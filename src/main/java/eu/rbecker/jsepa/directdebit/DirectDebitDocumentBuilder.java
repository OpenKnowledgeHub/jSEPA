/*
 *  All rights reserved.
 */
package eu.rbecker.jsepa.directdebit;

import eu.rbecker.jsepa.directdebit.util.SepaXmlDocumentBuilder;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.AccountIdentification4Choice;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.ActiveOrHistoricCurrencyAndAmount;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.CashAccount16;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.ChargeBearerType1Code;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.CustomerDirectDebitInitiationV02;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.DirectDebitTransaction6;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.DirectDebitTransactionInformation9;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.Document;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.GenericPersonIdentification1;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.GroupHeader39;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.LocalInstrument2Choice;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.MandateRelatedInformation6;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.ObjectFactory;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.Party6Choice;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.PartyIdentification32;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.PaymentIdentification1;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.PaymentInstructionInformation4;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.PaymentMethod2Code;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.PaymentTypeInformation20;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.PersonIdentification5;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.PersonIdentificationSchemeName1Choice;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.RemittanceInformation5;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.ServiceLevel8Choice;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Robert Becker <robert at rbecker.eu>
 */
public class DirectDebitDocumentBuilder extends SepaXmlDocumentBuilder {

    public static String toXml(DirectDebitDocumentData ddd) {
        // sepa xml document
        Document doc = new Document();
        // CustomerDirectDebitInitiationV02
        CustomerDirectDebitInitiationV02 cddiv = new CustomerDirectDebitInitiationV02();
        doc.setCstmrDrctDbtInitn(cddiv);

        // group header
        cddiv.setGrpHdr(createGroupHeaderSdd(ddd));

        cddiv.getPmtInf().addAll(createPaymentInstructions(ddd));

        // marshal to string
        StringWriter resultWriter = new StringWriter();
        marshal(doc.getClass().getPackage().getName(), new ObjectFactory().createDocument(doc), resultWriter);
        return resultWriter.toString();
    }

    private static List<PaymentInstructionInformation4> createPaymentInstructions(DirectDebitDocumentData ddd) {
        return Arrays.stream(MandateType.values())
            .filter(mt -> ddd.getNumberOfPaymentsByMandateType(mt) > 0)
            .map(mt -> createPaymentInstructionInformation(ddd, mt))
            .collect(Collectors.toList());
    }

    private static PaymentInstructionInformation4 createPaymentInstructionInformation(DirectDebitDocumentData ddd, MandateType mandateType) {

        PaymentInstructionInformation4 result = new PaymentInstructionInformation4();
        // payment information id
        result.setPmtInfId(ddd.getDocumentMessageId());
        // payment method (fixed)
        result.setPmtMtd(PaymentMethod2Code.DD);
        // batch booking (fixed)
        result.setBtchBookg(Boolean.TRUE);

        // number of transactions
        result.setNbOfTxs(String.valueOf(ddd.getNumberOfPaymentsByMandateType(mandateType)));
        // control sum
        result.setCtrlSum(ddd.getTotalPaymentSumOfPaymentsByMandateType(mandateType));
        // payment type information
        result.setPmtTpInf(createPaymentTypeInformation(mandateType));

        // requested collection due date
        result.setReqdColltnDt(dateToXmlGregorianCalendarDate(ddd.getDueDateByMandateType(mandateType)));

        // creditor name
        result.setCdtr(new PartyIdentification32());
        result.getCdtr().setNm(ddd.getCreditorName());

        // creditor iban
        result.setCdtrAcct(ibanToCashAccountSepa1(ddd.getCreditorIban()));

        // creditor agt(?)
        result.setCdtrAgt(bicToBranchAndFinancialInstitutionIdentification(ddd.getCreditorBic()));

        // whatever, fixed
        result.setChrgBr(ChargeBearerType1Code.SLEV);

        // single payment transactions ... yay!
        result.getDrctDbtTxInf().addAll(createDirectDebitTransactionInformationBlocks(ddd, mandateType));

        return result;
    }

    private static CashAccount16 ibanToCashAccountSepa1(String iban) {
        CashAccount16 result = new CashAccount16();
        result.setId(new AccountIdentification4Choice());
        result.getId().setIBAN(iban);
        return result;
    }

    private static Collection<DirectDebitTransactionInformation9> createDirectDebitTransactionInformationBlocks(DirectDebitDocumentData ddd, MandateType mandateType) {
        List<DirectDebitTransactionInformation9> result = new ArrayList<>();

        for (DirectDebitPayment p : ddd.getPaymentsByMandateType(mandateType)) {
            result.add(createDirectDebitTransaction(ddd, p));
        }

        return result;
    }

    private static DirectDebitTransactionInformation9 createDirectDebitTransaction(DirectDebitDocumentData ddd, DirectDebitPayment p) {
        DirectDebitTransactionInformation9 result = new DirectDebitTransactionInformation9();
        // mandate id
        result.setPmtId(new PaymentIdentification1());
        result.getPmtId().setEndToEndId(p.getMandateId());

        // currency and amount
        result.setInstdAmt(new ActiveOrHistoricCurrencyAndAmount());
        result.getInstdAmt().setCcy("EUR");
        result.getInstdAmt().setValue(p.getPaymentSum());

        // transaction information
        result.setDrctDbtTx(createDirectDebitTransaction(p, ddd));

        // debitor bic
        result.setDbtrAgt(bicToBranchAndFinancialInstitutionIdentification(p.getDebitorBic()));

        // debitor name
        result.setDbtr(new PartyIdentification32());
        result.getDbtr().setNm(p.getDebitorName());

        // debitor iban
        result.setDbtrAcct(new CashAccount16());
        result.getDbtrAcct().setId(new AccountIdentification4Choice());
        result.getDbtrAcct().getId().setIBAN(p.getDebitorIban());

        // reson of payment
        result.setRmtInf(new RemittanceInformation5());
        result.getRmtInf().getUstrd().add(p.getReasonForPayment());

        return result;
    }

    private static DirectDebitTransaction6 createDirectDebitTransaction(DirectDebitPayment p, DirectDebitDocumentData ddd) {
        DirectDebitTransaction6 result = new DirectDebitTransaction6();
        // mandate related info
        result.setMndtRltdInf(new MandateRelatedInformation6());

        // Erforderlich, wenn das Mandat seit letzten SEPA Lastschrift Einreichung ge�ndert wurde.
        // In diesem Fall ist das Feld mit "TRUE" zu belegen, ansonsten bleibt es leer.
        // Relevanz f�r folgende Mandats�nderungen: Gl�ubiger ID, Gl�ubiger Name, Bankverbindung des Zahlungspflichtigen, Mandat ID
        // -- we'll leave it empty for now and see what happens
        // tx.getMndtRltdInf().setAmdmntInd(Boolean.FALSE);
        result.getMndtRltdInf().setMndtId(p.getMandateId());
        result.getMndtRltdInf().setDtOfSgntr(dateToXmlGregorianCalendarDate(p.getMandateDate()));

        // creditor related info
        result.setCdtrSchmeId(new PartyIdentification32());
        result.getCdtrSchmeId().setId(new Party6Choice());
        result.getCdtrSchmeId().getId().setPrvtId(new PersonIdentification5());

        // person identification - (creditor identifier)
        GenericPersonIdentification1 inf = new GenericPersonIdentification1();
        result.getCdtrSchmeId().getId().getPrvtId().getOthr().add(inf);
        inf.setId(ddd.getCreditorIdentifier());

        // whatever, fixed to SEPA
        inf.setSchmeNm(new PersonIdentificationSchemeName1Choice());
        inf.getSchmeNm().setPrtry("SEPA");

        return result;
    }

    private static PaymentTypeInformation20 createPaymentTypeInformation(MandateType mandateType) {
        PaymentTypeInformation20 paymentType = new PaymentTypeInformation20();
        paymentType.setSvcLvl(new ServiceLevel8Choice());
        paymentType.getSvcLvl().setCd("SEPA");
        paymentType.setLclInstrm(new LocalInstrument2Choice());
        paymentType.getLclInstrm().setCd("CORE");
        paymentType.setSeqTp(mandateType.getSepaSequenceType1Code());
        return paymentType;
    }

    private static GroupHeader39 createGroupHeaderSdd(DirectDebitDocumentData ddd) {
        GroupHeader39 result = new GroupHeader39();
        // message id
        result.setMsgId(ddd.getDocumentMessageId());

        // created on
        result.setCreDtTm(calendarToXmlGregorianCalendarDateTime((GregorianCalendar) GregorianCalendar.getInstance()));

        // number of tx
        result.setNbOfTxs(String.valueOf(ddd.getPayments().size()));

        // control sum
        result.setCtrlSum(ddd.getTotalPaymentSum());

        // creditor name
        PartyIdentification32 pi = new PartyIdentification32();
        pi.setNm(ddd.getCreditorName());

        result.setInitgPty(pi);

        return result;
    }

}
