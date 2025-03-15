# jSEPA

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=OpenKnowledgeHub_jSEPA&metric=bugs)](https://sonarcloud.io/summary/new_code?id=OpenKnowledgeHub_jSEPA)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=OpenKnowledgeHub_jSEPA&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=OpenKnowledgeHub_jSEPA)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=OpenKnowledgeHub_jSEPA&metric=coverage)](https://sonarcloud.io/summary/new_code?id=OpenKnowledgeHub_jSEPA)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=OpenKnowledgeHub_jSEPA&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=OpenKnowledgeHub_jSEPA)
---

A java library to create valid PAIN.008.001.11 SEPA direct debit and PAIN.001.001.12 SEPA transfer XML documents.

Strings are sanitized according to SEPA rules.

## Usage

### Creation of pain.008.001.11 direct debit xml documents

To create a direct debit initialisation XML document you simple start at the `DSL.directDebit()` point and the DSL will
guide you through the steps:

```java
public String generateXml() {
    DSL.directDebit("MessageId")
            .creditor(
                    DSL.account()
                            .name("Creditor Name")
                            .identification("Creditor Identification")
                            .bic("BYLADEM1001")
                            .iban("DE02120300000000202051"))
            .receive(550)
            .from(
                    DSL.account()
                            .name("Debitor Name")
                            .identification("Debitor Identification")
                            .bic("BYLADEM1001")
                            .iban("DE02120300000000203051"))
            .on(LocalDate.now().plusWeeks(1))
            .withPaymentIdentification("PaymentIdentification")
            .overMandate(DSL.oneTimeMandate("Mandate Identifier").issuedAt(LocalDate.now()))
            .toXml();
}
```

### Creation of pain.001.001.12 bank transfer xml documents

Creating bank transfer XML document works as simple as the direct debit ones. This time start at the `DSL.transfer()`
entry point and the DSL will guide you through the steps:

```java
public String generateXml() {
    DSL.transfer("MessageId")
            .from(
                    DSL.account()
                            .name("Payer Name")
                            .identification("Payer Identification")
                            .bic("BYLADEM1001")
                            .iban("DE02120300000000202051"))
            .on(LocalDateTime.now().plusWeeks(1))
            .to(
                    DSL.account()
                            .name("Payee Name")
                            .identification("Payee Identification")
                            .bic("BYLADEM1001")
                            .iban("DE02120300000000203051"))
            .amount(125)
            .withEndToEndIdentifier("End to end identification")
            .toXml();
}
```

## Compiling

Just checkout the repository and run `mvn clean install`. A `.jar` file will be created in the `target/` directory.

## Credits

This library was originally written by Robert Becker <robert at rbecker.eu> and is now maintained by Jelmen
Guhlke <mail at jguhlke.de>.
