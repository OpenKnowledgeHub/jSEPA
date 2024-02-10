/*
 * The MIT License (MIT)
 *
 * Copyright © 2024 jsepa.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the “Software”), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package eu.rbecker.jsepa.transfer;

import eu.rbecker.jsepa.directdebit.util.SepaUtil;
import eu.rbecker.jsepa.directdebit.util.SepaValidationException;
import eu.rbecker.jsepa.sanitization.SepaStringSanitizer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;

public class SepaTransferDocumentData {

  private final List<SepaTransferPayment> payments = new ArrayList<>();
  private String payerBic;
  private String payerIban;
  private String payerName;
  private String documentMessageId;
  private Calendar dateOfExecution = Calendar.getInstance();
  private boolean batchBooking = true;

  public SepaTransferDocumentData() {}

  public SepaTransferDocumentData(
      String payerBic, String payerIban, String payerName, String documentMessageId)
      throws SepaValidationException {
    setPayerBic(payerBic);
    setPayerIban(payerIban);
    setPayerName(payerName);
    setDocumentMessageId(documentMessageId);
  }

  public String toXml() throws DatatypeConfigurationException {
    return SepaTransferDocumentBuilder.toXml(this);
  }

  public BigDecimal getTotalPaymentSum() {
    BigDecimal result = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
    for (SepaTransferPayment p : payments) {
      result = result.add(p.getPaymentSum());
    }
    return result;
  }

  public void addPayment(SepaTransferPayment payment) throws SepaValidationException {
    payments.add(payment);
  }

  public String getPayerBic() {
    return payerBic;
  }

  public final void setPayerBic(String payerBic) throws SepaValidationException {
    SepaUtil.validateBic(payerBic);
    this.payerBic = payerBic;
  }

  public String getPayerIban() {
    return payerIban;
  }

  public final void setPayerIban(String payerIban) throws SepaValidationException {
    SepaUtil.validateIban(payerIban);
    this.payerIban = payerIban;
  }

  public String getPayerName() {
    return payerName;
  }

  public final void setPayerName(String payerName) {
    this.payerName = SepaStringSanitizer.of(payerName).withMaxLength(70).sanitze();
  }

  public String getDocumentMessageId() {
    return documentMessageId;
  }

  public final void setDocumentMessageId(String documentMessageId) {
    this.documentMessageId = SepaStringSanitizer.of(documentMessageId).withMaxLength(35).sanitze();
  }

  public List<SepaTransferPayment> getPayments() {
    return payments;
  }

  public Calendar getDateOfExecution() {
    return dateOfExecution;
  }

  public void setDateOfExecution(Calendar dateOfExecution) {
    this.dateOfExecution = dateOfExecution;
  }

  public boolean isBatchBooking() {
    return batchBooking;
  }

  public void setBatchBooking(boolean batchBooking) {
    this.batchBooking = batchBooking;
  }
}
