# jSEPA

A java library to create valid pain.008.003.02 / v2.7 SEPA direct debit XML documents using CORE and pain.001.003.03 SEPA transfer XML documents.

Both types of documents as created by the internal unit tests have been validated with Star Finanz SEPA XML Checker.

Includes IBAN validation by apache commons and a regular expression based validation for BICs. Strings are sanitized according to SEPA rules.

There is no national bank database lookup for IBAN and BIC validation, as that caused more harm than good in the past.

Provides IBAN/BIC based bank information lookup for german banks using data provided by the Bundesbank. This also enables IBAN to BIC conversation.

## License

The MIT License

Copyright 2017 Robert Becker <robert at rbecker.eu>.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

## Usage

Here is a short mainly undocumented code example on how to use this. InvoiceBatch and Invoice classes are not included in jSEPA but the intention should be clear. More detailed documentation is comming soon.

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

## Compiling

Just checkout the repository and run `mvn clean install`. A `.jar` file will be created in the `target/` directory.

The maven build script automatically downloads a text file from the Bundesbank containing bank information data to enable IBAN to BIC transformation.
The file will only be downloaded when it is missing or older than a week. 