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
                xmlns="urn:iso:std:iso:20022:tech:xsd:pain.001.001.12">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="/">
        <Document>
            <CstmrCdtTrfInitn>
                <GrpHdr>
                    <MsgId>
                        <xsl:value-of select="/SepaTransferDocumentData/MessageId"/>
                    </MsgId>
                    <CreDtTm>
                        <xsl:value-of select="/SepaTransferDocumentData/CreationDateTime"/>
                    </CreDtTm>
                    <NbOfTxs>
                        <xsl:value-of select="count(/SepaTransferDocumentData/Payments/Payment)"/>
                    </NbOfTxs>
                    <CtrlSum>
                        <xsl:value-of select="sum(/SepaTransferDocumentData/Payments/Payment/Amount)"/>
                    </CtrlSum>
                    <InitgPty>
                        <Nm>
                            <xsl:value-of select="/SepaTransferDocumentData/Payer/Name"/>
                        </Nm>
                    </InitgPty>
                </GrpHdr>
                <PmtInf>
                    <PmtInfId>
                        <xsl:value-of select="/SepaTransferDocumentData/MessageId"/>
                    </PmtInfId>
                    <PmtMtd>TRF</PmtMtd>
                    <NbOfTxs>
                        <xsl:value-of select="count(/SepaTransferDocumentData/Payments/Payment)"/>
                    </NbOfTxs>
                    <CtrlSum>
                        <xsl:value-of select="sum(/SepaTransferDocumentData/Payments/Payment/Amount)"/>
                    </CtrlSum>
                    <PmtTpInf>
                        <SvcLvl>
                            <Cd>SEPA</Cd>
                        </SvcLvl>
                    </PmtTpInf>
                    <ReqdExctnDt>
                        <DtTm>
                            <xsl:value-of select="/SepaTransferDocumentData/DateOfExecution"/>
                        </DtTm>
                    </ReqdExctnDt>
                    <Dbtr>
                        <Nm>
                            <xsl:value-of select="/SepaTransferDocumentData/Payer/Name"/>
                        </Nm>
                    </Dbtr>
                    <DbtrAcct>
                        <Id>
                            <IBAN>
                                <xsl:value-of select="/SepaTransferDocumentData/Payer/Iban"/>
                            </IBAN>
                        </Id>
                    </DbtrAcct>
                    <DbtrAgt>
                        <FinInstnId>
                            <BICFI>
                                <xsl:value-of select="/SepaTransferDocumentData/Payer/Bic"/>
                            </BICFI>
                        </FinInstnId>
                    </DbtrAgt>
                    <ChrgBr>SLEV</ChrgBr>

                    <xsl:for-each select="/SepaTransferDocumentData/Payments/Payment">
                        <CdtTrfTxInf>
                            <PmtId>
                                <EndToEndId>
                                    <xsl:value-of select="EndToEndId"/>
                                </EndToEndId>
                            </PmtId>
                            <Amt>
                                <InstdAmt Ccy="EUR">
                                    <xsl:value-of select="Amount"/>
                                </InstdAmt>
                            </Amt>
                            <CdtrAgt>
                                <FinInstnId>
                                    <BICFI>
                                        <xsl:value-of select="Payee/Bic"/>
                                    </BICFI>
                                </FinInstnId>
                            </CdtrAgt>
                            <Cdtr>
                                <Nm>
                                    <xsl:value-of select="Payee/Name"/>
                                </Nm>
                            </Cdtr>
                            <CdtrAcct>
                                <Id>
                                    <IBAN>
                                        <xsl:value-of select="Payee/Iban"/>
                                    </IBAN>
                                </Id>
                            </CdtrAcct>
                            <xsl:if test="ReasonForPayment">
                                <RmtInf>
                                    <Ustrd>
                                        <xsl:value-of select="ReasonForPayment"/>
                                    </Ustrd>
                                </RmtInf>
                            </xsl:if>
                        </CdtTrfTxInf>
                    </xsl:for-each>
                </PmtInf>
            </CstmrCdtTrfInitn>
        </Document>
    </xsl:template>
</xsl:stylesheet>
