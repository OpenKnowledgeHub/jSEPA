/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.rbecker.jsepa.transfer;

import eu.rbecker.jsepa.directdebit.util.SepaValidationException;
import java.math.BigDecimal;
import java.util.Calendar;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Robert Becker <robert at rbecker.eu>
 */
public class SepaTransferDocumentBuilderTest {

    public SepaTransferDocumentBuilderTest() {
    }

    /**
     * Test of toXml method, of class SepaTransferDocumentBuilder.
     */
    @Test
    public void testToXml() throws Exception {
        SepaTransferDocumentData data = new SepaTransferDocumentData("MALADE51NWD", "DE89370400440532013000", "Hans Mustermann", "12345");

        Calendar dueDate = Calendar.getInstance();
        dueDate.set(Calendar.HOUR, 0);
        dueDate.set(Calendar.MINUTE, 0);
        dueDate.set(Calendar.SECOND, 0);
        dueDate.add(Calendar.DATE, 14);
        data.setDateOfExecution(dueDate);

        data.addPayment(createTestPayment("123.4539", "Arme Wurst", "MALADE51NWD", "DE89370400440532013000"));
        data.addPayment(createTestPayment("99.9930", "Arme Wurst2", "MALADE51NWD", "DE89370400440532013000"));
        data.addPayment(createTestPayment("10", "Loooooong Loooooong Loooooong Loooooong Loooooong Loooooong Loooooong Name", "MALADE51NWD", "DE89370400440532013000"));

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
        assertTrue(result.contains("Loooooong Loooooong Loooooong Loooooong Loooooong Loooooong Loooooong "));
        assertFalse(result.contains("Loooooong Loooooong Loooooong Loooooong Loooooong Loooooong Loooooong N"));
    }

    private SepaTransferPayment createTestPayment(String sum, String debitorName, String bic, String iban) throws SepaValidationException {
        SepaTransferPayment result = new SepaTransferPayment();

        result.setPayeeBic(bic);
        result.setPayeeIban(iban);
        result.setPayeeName(debitorName);
        result.setPaymentSum(new BigDecimal(sum));
        result.setReasonForPayment("test-Überweisung");
        return result;
    }

}
