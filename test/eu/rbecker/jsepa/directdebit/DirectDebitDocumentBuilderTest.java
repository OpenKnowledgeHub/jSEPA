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
        System.out.println("toXml");
        DirectDebitDocument ddd = new DirectDebitDocument("MALADE51NWD", "DE89370400440532013000", "DE98ZZZ09999999999", "Hans Mustermann", "12345", DirectDebitType.COR1);

        Calendar dueDate = Calendar.getInstance();
        dueDate.set(Calendar.HOUR, 0);
        dueDate.set(Calendar.MINUTE, 0);
        dueDate.set(Calendar.SECOND, 0);
        dueDate.add(Calendar.DATE, 14);

        for (MandateType mt : MandateType.values()) {
            ddd.addPayment(createTestPayment(mt, 123.45f, "Arme Wurst", "MALADE51NWD", "DE89370400440532013000", dueDate));
            ddd.addPayment(createTestPayment(mt, 99.99f, "Arme Wurst2", "MALADE51NWD", "DE89370400440532013000", dueDate));
        }

        String expResult = "";
        String result = DirectDebitDocumentBuilder.toXml(ddd);
        System.out.println(result);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
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
