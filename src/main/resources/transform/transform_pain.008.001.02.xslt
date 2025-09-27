<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~ The MIT License (MIT)
  ~
  ~ Copyright (c) 2025 openknowledgehub.org
  ~
  ~ Permission is hereby granted, free of charge, to any person obtaining a copy of this software
  ~ and associated documentation files (the “Software”), to deal in the Software without restriction,
  ~ including without limitation the rights to use, copy, modify, merge, publish, distribute,
  ~ sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
  ~ furnished to do so, subject to the following conditions:
  ~
  ~ The above copyright notice and this permission notice shall be included in all copies or
  ~ substantial portions of the Software.
  ~
  ~ THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  ~ IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
  ~ FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
  ~ COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
  ~ AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
  ~ WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
  -->

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns="urn:iso:std:iso:20022:tech:xsd:pain.008.001.02">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="/">
        <Document>
            <CstmrDrctDbtInitn>
                <GrpHdr>
                    <MsgId>
                        <xsl:value-of select="/DirectDebitDocumentData/MessageId"/>
                    </MsgId>
                    <CreDtTm>
                        <xsl:value-of select="/DirectDebitDocumentData/CreationDateTime"/>
                    </CreDtTm>
                    <NbOfTxs>
                        <xsl:value-of select="count(/DirectDebitDocumentData/Payments/Payment)"/>
                    </NbOfTxs>
                    <CtrlSum>
                        <xsl:value-of select="sum(/DirectDebitDocumentData/Payments/Payment/Amount)"/>
                    </CtrlSum>
                    <InitgPty>
                        <Nm>
                            <xsl:value-of select="/DirectDebitDocumentData/Creditor/Name"/>
                        </Nm>
                    </InitgPty>
                </GrpHdr>
                <PmtInf>
                    <PmtInfId>
                        <xsl:value-of select="/DirectDebitDocumentData/MessageId"/>
                    </PmtInfId>
                    <PmtMtd>
                        <xsl:value-of select="/DirectDebitDocumentData/PaymentMethod"/>
                    </PmtMtd>
                    <BtchBookg>true</BtchBookg>
                    <NbOfTxs>
                        <xsl:value-of select="count(/DirectDebitDocumentData/Payments/Payment)"/>
                    </NbOfTxs>
                    <CtrlSum>
                        <xsl:value-of select="sum(/DirectDebitDocumentData/Payments/Payment/Amount)"/>
                    </CtrlSum>
                    <PmtTpInf>
                        <SvcLvl>
                            <Cd>
                                <xsl:value-of select="/DirectDebitDocumentData/ServiceLevel"/>
                            </Cd>
                        </SvcLvl>
                        <LclInstrm>
                            <Cd>CORE</Cd>
                        </LclInstrm>
                        <SeqTp>
                            <xsl:value-of select="/DirectDebitDocumentData/Payments/Payment[1]/Mandate/Type"/>
                        </SeqTp>
                    </PmtTpInf>
                    <ReqdColltnDt>
                        <xsl:value-of select="/DirectDebitDocumentData/Payments/Payment[1]/DirectDebitDueAt"/>
                    </ReqdColltnDt>
                    <Cdtr>
                        <Nm>
                            <xsl:value-of select="/DirectDebitDocumentData/Creditor/Name"/>
                        </Nm>
                    </Cdtr>
                    <CdtrAcct>
                        <Id>
                            <IBAN>
                                <xsl:value-of select="/DirectDebitDocumentData/Creditor/Iban"/>
                            </IBAN>
                        </Id>
                    </CdtrAcct>
                    <CdtrAgt>
                        <FinInstnId>
                            <BIC>
                                <xsl:value-of select="/DirectDebitDocumentData/Creditor/Bic"/>
                            </BIC>
                        </FinInstnId>
                    </CdtrAgt>
                    <ChrgBr>SLEV</ChrgBr>
                    <CdtrSchmeId>
                        <Id>
                            <PrvtId>
                                <Othr>
                                    <Id>
                                        <xsl:value-of select="/DirectDebitDocumentData/Creditor/Identifier"/>
                                    </Id>
                                    <SchmeNm>
                                        <Prtry>SEPA</Prtry>
                                    </SchmeNm>
                                </Othr>
                            </PrvtId>
                        </Id>
                    </CdtrSchmeId>
                    <xsl:for-each select="/DirectDebitDocumentData/Payments/Payment">
                        <DrctDbtTxInf>
                            <PmtId>
                                <EndToEndId>
                                    <xsl:value-of select="Mandate/Identification"/>
                                </EndToEndId>
                            </PmtId>
                            <InstdAmt Ccy="">
                                <xsl:attribute name="Ccy">
                                    <xsl:value-of select="Currency"/>
                                </xsl:attribute>
                                <xsl:value-of select="Amount"/>
                            </InstdAmt>
                            <DrctDbtTx>
                                <MndtRltdInf>
                                    <MndtId>
                                        <xsl:value-of select="Mandate/Identification"/>
                                    </MndtId>
                                    <DtOfSgntr>
                                        <xsl:value-of select="Mandate/IssuedAt"/>
                                    </DtOfSgntr>
                                </MndtRltdInf>
                            </DrctDbtTx>
                            <DbtrAgt>
                                <FinInstnId>
                                    <BIC>
                                        <xsl:value-of select="Debitor/Bic"/>
                                    </BIC>
                                </FinInstnId>
                            </DbtrAgt>
                            <Dbtr>
                                <Nm>
                                    <xsl:value-of select="Debitor/Name"/>
                                </Nm>
                            </Dbtr>
                            <DbtrAcct>
                                <Id>
                                    <IBAN>
                                        <xsl:value-of select="Debitor/Iban"/>
                                    </IBAN>
                                </Id>
                            </DbtrAcct>
                            <xsl:if test="ReasonForPayment">
                                <RmtInf>
                                    <Ustrd>
                                        <xsl:value-of select="ReasonForPayment"/>
                                    </Ustrd>
                                </RmtInf>
                            </xsl:if>
                        </DrctDbtTxInf>
                    </xsl:for-each>
                </PmtInf>
            </CstmrDrctDbtInitn>
        </Document>
    </xsl:template>
</xsl:stylesheet>
