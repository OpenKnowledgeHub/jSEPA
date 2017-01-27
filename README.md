jSEPA
=====

A java library mainly to create valid pain.008.003.02 / v2.7 SEPA XML documents using CORE and COR1 schemes. B2B is not yet supported as constraints are unknown.

The library is in an early stage but already used in production.

Here is a short mainly undocumented code example on how to use this. InvoiceBatch and Invoice classes are not included in jSEPA but the intention should be clear. More detailed documentation is comming soon.

Includes IBAN validation by apache commons and a regular expression check for BICs. Strings are sanitized according to SEPA rules.

```java
public String createSepaXml(InvoiceBatch batch) {
    try {
        DirectDebitDocumentData ddd = initDirectDebitDocument(batch);

        for (Invoice i : batch.getInvoices()) {
            addInvoiceToDirectDebitDocument(ddd, i);
        }

        return ddd.toXml();
    } catch (SepaValidationException | DatatypeConfigurationException e) {
        response.sendError(500, e.getMessage());
        LOG.log(Level.SEVERE, "Could not create SEPA document", e);
    }
}

private DirectDebitDocumentData initDirectDebitDocument(InvoiceBatch batch) throws SepaValidationException {
    DirectDebitDocumentData result = new DirectDebitDocument();

    BankData payeeBankData = batch.getPayee().getInvoicingData().getBankData();
    result.setDocumentMessageId(batch.getId().toString());
    result.setCreditorBic(payeeBankData.getBic());
    result.setCreditorIban(payeeBankData.getIban());
    result.setCreditorName(payee.getName());
    result.setCreditorIdentifier(payee.getInvoicingData().getCreditorIdentifier());

    return result;
}

private void addInvoiceToDirectDebitDocument(DirectDebitDocumentData ddd, Invoice i) throws SepaValidationException {
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


Compiling
=====

Just checkout the repository and run `mvn clean install`. A `.jar` file will be created in the `target/` directory.