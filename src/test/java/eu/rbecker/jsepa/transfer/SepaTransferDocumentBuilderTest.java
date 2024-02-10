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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.rbecker.jsepa.transfer;

import static org.junit.Assert.*;

import eu.rbecker.jsepa.directdebit.util.SepaValidationException;
import java.math.BigDecimal;
import java.util.Calendar;
import org.junit.Test;

/**
 * @author Robert Becker <robert at rbecker.eu>
 */
public class SepaTransferDocumentBuilderTest {

  public SepaTransferDocumentBuilderTest() {}

  /** Test of toXml method, of class SepaTransferDocumentBuilder. */
  @Test
  public void testToXml() throws Exception {
    SepaTransferDocumentData data =
        new SepaTransferDocumentData(
            "MALADE51NWD", "DE89370400440532013000", "Hans Mustermann", "12345");

    Calendar dueDate = Calendar.getInstance();
    dueDate.set(Calendar.HOUR, 0);
    dueDate.set(Calendar.MINUTE, 0);
    dueDate.set(Calendar.SECOND, 0);
    dueDate.add(Calendar.DATE, 14);
    data.setDateOfExecution(dueDate);

    data.addPayment(
        createTestPayment("123.4539", "Arme Wurst", "MALADE51NWD", "DE89370400440532013000"));
    data.addPayment(
        createTestPayment("99.9930", "Arme Wurst2", "MALADE51NWD", "DE89370400440532013000"));
    data.addPayment(
        createTestPayment(
            "10",
            "Loooooong Loooooong Loooooong Loooooong Loooooong Loooooong Loooooong Name",
            "MALADE51NWD",
            "DE89370400440532013000"));

    String result = SepaTransferDocumentBuilder.toXml(data);
    //        System.out.println(result);
    assertTrue(result.contains("<InstdAmt Ccy=\"EUR\">123.45</InstdAmt>"));
    assertTrue(result.contains("<InstdAmt Ccy=\"EUR\">99.99</InstdAmt>"));
    assertTrue(result.contains("<EndToEndId>NOTPROVIDED</EndToEndId>"));
    assertTrue(result.contains("<CtrlSum>233.44</CtrlSum>"));
    assertTrue(result.contains("DE89370400440532013000"));
    assertTrue(result.contains("Arme Wurst2"));
    assertTrue(result.contains("Hans Mustermann"));
    assertTrue(result.contains("test- berweisung"));
    assertTrue(
        result.contains("Loooooong Loooooong Loooooong Loooooong Loooooong Loooooong Loooooong "));
    assertFalse(
        result.contains("Loooooong Loooooong Loooooong Loooooong Loooooong Loooooong Loooooong N"));
  }

  private SepaTransferPayment createTestPayment(
      String sum, String debitorName, String bic, String iban) throws SepaValidationException {
    SepaTransferPayment result = new SepaTransferPayment();

    result.setPayeeBic(bic);
    result.setPayeeIban(iban);
    result.setPayeeName(debitorName);
    result.setPaymentSum(new BigDecimal(sum));
    result.setReasonForPayment("test-Überweisung");
    return result;
  }
}
