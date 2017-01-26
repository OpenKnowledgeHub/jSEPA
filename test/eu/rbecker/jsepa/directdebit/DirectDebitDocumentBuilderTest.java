/*
 *  All rights reserved.
 */
package eu.rbecker.jsepa.directdebit;

import java.util.Calendar;
import javax.xml.datatype.DatatypeConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Robert Becker <robert at rbecker.eu>
 */
public class DirectDebitDocumentBuilderTest {

    public DirectDebitDocumentBuilderTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of toXml method, of class DirectDebitDocumentBuilder.
     */
    @Test
    public void testToXml() throws DatatypeConfigurationException, SepaValidationException {
        DirectDebitDocument ddd = new DirectDebitDocument("MALADE51NWD", "DE89370400440532013000", "DE98ZZZ09999999999", "Hans Mustermann", "12345");

        Calendar dueDate = Calendar.getInstance();
        dueDate.set(Calendar.HOUR, 0);
        dueDate.set(Calendar.MINUTE, 0);
        dueDate.set(Calendar.SECOND, 0);
        dueDate.add(Calendar.DATE, 14);

        for (MandateType mt : MandateType.values()) {
            ddd.addPayment(createTestPayment(mt, 123.4539f, "Arme Wurst", "MALADE51NWD", "DE89370400440532013000", dueDate));
            ddd.addPayment(createTestPayment(mt, 99.9930f, "Arme Wurst2", "MALADE51NWD", "DE89370400440532013000", dueDate));
        }

        String result = DirectDebitDocumentBuilder.toXml(ddd);
        // TODO: write proper tests
        System.out.println(result);
        assertTrue(result.contains("<InstdAmt Ccy=\"EUR\">123.45</InstdAmt>"));
        assertTrue(result.contains("<InstdAmt Ccy=\"EUR\">99.99</InstdAmt>"));
        assertTrue(result.contains("<CtrlSum>893.76</CtrlSum>"));
        assertTrue(result.contains("DE98ZZZ09999999999"));
        assertTrue(result.contains("DE89370400440532013000"));
        assertTrue(result.contains("Arme Wurst2".toUpperCase()));
        assertTrue(result.contains("Hans Mustermann".toUpperCase()));
    }

    private DirectDebitPayment createTestPayment(MandateType mt, float sum, String debitorName, String bic, String iban, Calendar dueDate) throws SepaValidationException {
        DirectDebitPayment result = new DirectDebitPayment();

        result.setDirectDebitDueDate(dueDate.getTime());
        result.setMandateDate(Calendar.getInstance().getTime());
        result.setMandateId("mandate001");
        result.setMandateType(mt);
        result.setDebitorBic(bic);
        result.setDebitorIban(iban);
        result.setDebitorName(debitorName);
        result.setPaymentSum(sum);
        result.setReasonForPayment("test-überweisung");
        return result;
    }

}
