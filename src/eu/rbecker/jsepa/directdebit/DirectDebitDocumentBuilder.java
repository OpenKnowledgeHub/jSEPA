/*
 *  All rights reserved.
 */
package eu.rbecker.jsepa.directdebit;

import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.AccountIdentificationSEPA;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.ActiveOrHistoricCurrencyAndAmountSEPA;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.ActiveOrHistoricCurrencyCodeEUR;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.BranchAndFinancialInstitutionIdentificationSEPA3;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.CashAccountSEPA1;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.CashAccountSEPA2;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.ChargeBearerTypeSEPACode;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.CustomerDirectDebitInitiationV02;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.DirectDebitTransactionInformationSDD;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.DirectDebitTransactionSDD;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.Document;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.FinancialInstitutionIdentificationSEPA3;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.GroupHeaderSDD;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.IdentificationSchemeNameSEPA;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.LocalInstrumentSEPA;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.MandateRelatedInformationSDD;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.ObjectFactory;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.PartyIdentificationSEPA1;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.PartyIdentificationSEPA2;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.PartyIdentificationSEPA3;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.PartyIdentificationSEPA5;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.PartySEPA2;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.PaymentIdentificationSEPA;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.PaymentInstructionInformationSDD;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.PaymentMethod2Code;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.PaymentTypeInformationSDD;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.PersonIdentificationSEPA2;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.RemittanceInformationSEPA1Choice;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.RestrictedPersonIdentificationSEPA;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.RestrictedPersonIdentificationSchemeNameSEPA;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_003_02.ServiceLevelSEPA;
import eu.rbecker.jsepa.sepa.SepaUtil;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Robert Becker <robert at rbecker.eu>
 */
class DirectDebitDocumentBuilder {

    public static String toXml(DirectDebitDocument ddd) throws DatatypeConfigurationException {
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
        marshal(doc, resultWriter);
        return resultWriter.toString();
    }

    private static List<PaymentInstructionInformationSDD> createPaymentInstructions(DirectDebitDocument ddd) throws DatatypeConfigurationException {
        List<PaymentInstructionInformationSDD> result = new ArrayList<>();

        for (MandateType mt : MandateType.values()) {
            if (ddd.getNumberOfPaymentsByMandateType(mt) > 0) {
                result.add(createPaymentInstructionInformation(ddd, mt));
            }
        }

        return result;
    }

    private static PaymentInstructionInformationSDD createPaymentInstructionInformation(DirectDebitDocument ddd, MandateType mandateType) throws DatatypeConfigurationException {

        PaymentInstructionInformationSDD result = new PaymentInstructionInformationSDD();
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
        result.setCdtr(new PartyIdentificationSEPA5());
        result.getCdtr().setNm(ddd.getCreditorName());

        // creditor iban
        result.setCdtrAcct(ibanToCashAccountSepa1(ddd.getCreditorIban()));

        // creditor agt(?)
        result.setCdtrAgt(bicToBranchAndFinancialInstitutionIdentification(ddd.getCreditorBic()));

        // whatever, fixed
        result.setChrgBr(ChargeBearerTypeSEPACode.SLEV);

        // single payment transactions ... yay!
        result.getDrctDbtTxInf().addAll(createDirectDebitTransactionInformationBlocks(ddd, mandateType));

        return result;
    }

    private static CashAccountSEPA1 ibanToCashAccountSepa1(String iban) {
        CashAccountSEPA1 result = new CashAccountSEPA1();
        result.setId(new AccountIdentificationSEPA());
        result.getId().setIBAN(iban);
        return result;
    }

    private static Collection<? extends DirectDebitTransactionInformationSDD> createDirectDebitTransactionInformationBlocks(DirectDebitDocument ddd, MandateType mandateType) throws DatatypeConfigurationException {
        List<DirectDebitTransactionInformationSDD> result = new ArrayList<>();

        for (DirectDebitPayment p : ddd.getPaymentsByMandateType(mandateType)) {
            result.add(createDirectDebitTransaction(ddd, p));
        }

        return result;
    }

    private static DirectDebitTransactionInformationSDD createDirectDebitTransaction(DirectDebitDocument ddd, DirectDebitPayment p) throws DatatypeConfigurationException {
        DirectDebitTransactionInformationSDD result = new DirectDebitTransactionInformationSDD();
        // mandate id
        result.setPmtId(new PaymentIdentificationSEPA());
        result.getPmtId().setEndToEndId(p.getMandateId());

        // currency and amount
        result.setInstdAmt(new ActiveOrHistoricCurrencyAndAmountSEPA());
        result.getInstdAmt().setCcy(ActiveOrHistoricCurrencyCodeEUR.EUR);
        result.getInstdAmt().setValue(p.getPaymentSum());

        // transaction information
        result.setDrctDbtTx(createDirectDebitTransaction(p, ddd));

        // debitor bic
        result.setDbtrAgt(bicToBranchAndFinancialInstitutionIdentification(p.getDebitorBic()));

        // debitor name
        result.setDbtr(new PartyIdentificationSEPA2());
        result.getDbtr().setNm(p.getDebitorName());

        // debitor iban
        result.setDbtrAcct(new CashAccountSEPA2());
        result.getDbtrAcct().setId(new AccountIdentificationSEPA());
        result.getDbtrAcct().getId().setIBAN(p.getDebitorIban());

        // reson of payment
        result.setRmtInf(new RemittanceInformationSEPA1Choice());
        result.getRmtInf().setUstrd(p.getReasonForPayment());

        return result;
    }

    private static DirectDebitTransactionSDD createDirectDebitTransaction(DirectDebitPayment p, DirectDebitDocument ddd) throws DatatypeConfigurationException {
        DirectDebitTransactionSDD result = new DirectDebitTransactionSDD();
        // mandate related info
        result.setMndtRltdInf(new MandateRelatedInformationSDD());

        // Erforderlich, wenn das Mandat seit letzten SEPA Lastschrift Einreichung geändert wurde.
        // In diesem Fall ist das Feld mit "TRUE" zu belegen, ansonsten bleibt es leer.
        // Relevanz für folgende Mandatsänderungen: Gläubiger ID, Gläubiger Name, Bankverbindung des Zahlungspflichtigen, Mandat ID
        // -- we'll leave it empty for now and see what happens
        // tx.getMndtRltdInf().setAmdmntInd(Boolean.FALSE);
        result.getMndtRltdInf().setMndtId(p.getMandateId());
        result.getMndtRltdInf().setDtOfSgntr(dateToXmlGregorianCalendarDate(p.getMandateDate()));

        // creditor related info
        result.setCdtrSchmeId(new PartyIdentificationSEPA3());
        result.getCdtrSchmeId().setId(new PartySEPA2());
        result.getCdtrSchmeId().getId().setPrvtId(new PersonIdentificationSEPA2());

        // person identification - (creditor identifier)
        RestrictedPersonIdentificationSEPA inf = new RestrictedPersonIdentificationSEPA();
        result.getCdtrSchmeId().getId().getPrvtId().setOthr(inf);
        inf.setId(ddd.getCreditorIdentifier());

        // whatever, fixed to SEPA
        inf.setSchmeNm(new RestrictedPersonIdentificationSchemeNameSEPA());
        inf.getSchmeNm().setPrtry(IdentificationSchemeNameSEPA.SEPA);

        return result;
    }

    private static PaymentTypeInformationSDD createPaymentTypeInformation(MandateType mandateType) {
        PaymentTypeInformationSDD paymentType = new PaymentTypeInformationSDD();
        paymentType.setSvcLvl(new ServiceLevelSEPA());
        paymentType.getSvcLvl().setCd("SEPA");
        paymentType.setLclInstrm(new LocalInstrumentSEPA());
        paymentType.getLclInstrm().setCd("CORE");
        paymentType.setSeqTp(mandateType.getSepaSequenceType1Code());
        return paymentType;
    }

    private static XMLGregorianCalendar dateToXmlGregorianCalendarDateTime(Date d) throws DatatypeConfigurationException {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(d);
        return calendarToXmlGregorianCalendarDateTime(cal);
    }

    /**
     * Returns a XMLGregorianCalendar in the format of 2014-01-22T17:53:01
     * @param cal
     * @return
     * @throws DatatypeConfigurationException 
     */
    private static XMLGregorianCalendar calendarToXmlGregorianCalendarDateTime(GregorianCalendar cal) throws DatatypeConfigurationException {
        // this way of initialization is required to omit time zone and milli seconds -.-
        XMLGregorianCalendar result = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        result.setYear(cal.get(Calendar.YEAR));
        result.setMonth(cal.get(Calendar.MONTH)+1);
        result.setDay(cal.get(Calendar.DAY_OF_MONTH));
        result.setHour(cal.get(Calendar.HOUR_OF_DAY));
        result.setMinute(cal.get(Calendar.MINUTE));
        result.setSecond(cal.get(Calendar.SECOND));
        return result;
    }

    private static XMLGregorianCalendar dateToXmlGregorianCalendarDate(Date d) throws DatatypeConfigurationException {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(d);
        return calendarToXmlGregorianCalendarDate(cal);
    }

    private static XMLGregorianCalendar calendarToXmlGregorianCalendarDate(GregorianCalendar d) throws DatatypeConfigurationException {
        XMLGregorianCalendar result = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(d.get(Calendar.YEAR), d.get(Calendar.MONTH) + 1, d.get(Calendar.DAY_OF_MONTH), DatatypeConstants.FIELD_UNDEFINED);
        return result;
    }

    private static GroupHeaderSDD createGroupHeaderSdd(DirectDebitDocument ddd) throws DatatypeConfigurationException {
        GroupHeaderSDD result = new GroupHeaderSDD();
        // message id
        result.setMsgId(ddd.getDocumentMessageId());

        // created on
        result.setCreDtTm(calendarToXmlGregorianCalendarDateTime((GregorianCalendar) GregorianCalendar.getInstance()));

        // number of tx
        result.setNbOfTxs(String.valueOf(ddd.getPayments().size()));

        // control sum
        result.setCtrlSum(ddd.getTotalPaymentSum());

        // creditor name
        PartyIdentificationSEPA1 partyIdentificationSEPA1 = new PartyIdentificationSEPA1();
        partyIdentificationSEPA1.setNm(ddd.getCreditorName());

        result.setInitgPty(partyIdentificationSEPA1);

        return result;
    }

    private static void marshal(Document doc, StringWriter resultWriter) {
        try {
            ObjectFactory of = new ObjectFactory();
            JAXBElement<Document> jaxbDoc = of.createDocument(doc);

            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(doc.getClass().getPackage().getName());
            javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(jaxbDoc, resultWriter);
        } catch (javax.xml.bind.JAXBException ex) {
            // XXXTODO Handle exception
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
        }
    }

    private static BranchAndFinancialInstitutionIdentificationSEPA3 bicToBranchAndFinancialInstitutionIdentification(String bic) {
        BranchAndFinancialInstitutionIdentificationSEPA3 result = new BranchAndFinancialInstitutionIdentificationSEPA3();
        result.setFinInstnId(new FinancialInstitutionIdentificationSEPA3());
        result.getFinInstnId().setBIC(bic);
        return result;
    }

}
