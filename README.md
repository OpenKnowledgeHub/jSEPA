jSEPA
=====

A java library mainly to create valid pain.008.003.02 / v2.7 SEPA XML documents

Relies on obantoo for IBAN validation for now which will be removed in future releases.

The library is in an early stage but already used in production.

Here is a short mainly undocumented code example on how to use this. InvoiceBatch and Invoice classes are not included in jSEPA but the intention should be clear. More detailed documentation is comming soon.


```java
public String createSepaXml(InvoiceBatch batch) {
    try {
        DirectDebitDocument ddd = initDirectDebitDocument(batch);

        for (Invoice i : batch.getInvoices()) {
            addInvoiceToDirectDebitDocument(ddd, i);
        }

        response.setContentType("text/plain;charset=DIN-66003");
        response.setHeader("Content-Disposition", "attachment; filename=\"SEPA-" + invoiceBatchId + ".xml\"");

        response.getOutputStream().print(ddd.toXml());
    } catch (SepaValidationException | DatatypeConfigurationException e) {
        response.sendError(500, e.getMessage());
        LOG.log(Level.SEVERE, "Could not create SEPA document", e);
    }
}

private DirectDebitDocument initDirectDebitDocument(InvoiceBatch batch) throws SepaValidationException {
    DirectDebitDocument result = new DirectDebitDocument();

    BankData payeeBankData = batch.getPayee().getInvoicingData().getBankData();
    result.setDocumentMessageId(batch.getId().toString());
    result.setCreditorBic(payeeBankData.getBic());
    result.setCreditorIban(payeeBankData.getIban());
    result.setCreditorName(payee.getName());
    result.setCreditorIdentifier(payee.getInvoicingData().getCreditorIdentifier());
    applySepaDirectDebitSchemeFromPayee(payee, result);

    return result;
}

private void applySepaDirectDebitSchemeFromPayee(Association payee, DirectDebitDocument ddd) {
    switch (payee.getInvoicingData().getDirectDebitScheme()) {
        case SEPA_COR1:
            ddd.setDirectDebitType(DirectDebitType.COR1);
            break;
        case SEPA_CORE:
            ddd.setDirectDebitType(DirectDebitType.CORE);
            break;
        default:
            throw new NotYetImplementedException("Unknown SEPA Scheme " + payee.getInvoicingData().getDirectDebitScheme());
    }
}

private void addInvoiceToDirectDebitDocument(DirectDebitDocument ddd, Invoice i) throws SepaValidationException {
    DirectDebitPayment result = new DirectDebitPayment();
    BankData bd = i.getRecipientBankData();
    result.setPaymentSum(i.getAfterTaxTotal());
    result.setDebitorBic(bd.getBic());
    result.setDebitorIban(bd.getIban());
    DirectDebitAuthorization dda = i.getDirectDebitAuthorization();
    result.setMandateDate(dda.getAuthorizedOn());
    result.setMandateId(dda.getAuthorizationReferenceIdentifier());
    result.setDebitorName(i.getRecipientName());
    // german "ueberweisungszweck"
    result.setReasonForPayment(sanitizeCharacters(i.getLocalizedTypeName() + " " + i.getInvoiceNumber()));

    if (!i.getDirectDebitAuthorization().isRecurrent()) {
        result.setMandateType(MandateType.ONE_OFF);
    } else if (i.isFirstUsageOfDirectDebitAuthorization()) {
        result.setMandateType(MandateType.RECURRENT_FIRST);
    } else {
        result.setMandateType(MandateType.RECURRENT);
    }

    result.setDirectDebitDueDate(i.getDirectDebitDueDate());

    ddd.addPayment(result);
}
```
