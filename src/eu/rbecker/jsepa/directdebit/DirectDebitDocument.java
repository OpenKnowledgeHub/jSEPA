package eu.rbecker.jsepa.directdebit;

import de.jost_net.OBanToo.StringLatin.Zeichen;
import eu.rbecker.jsepa.sepa.SepaUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.xml.datatype.DatatypeConfigurationException;

/**
 *
 * @author Robert Becker
 */
public class DirectDebitDocument {

    private String creditorBic;

    private String creditorIban;

    private String creditorIdentifier;

    private String creditorName;

    private String documentMessageId;

    private final List<DirectDebitPayment> payments = new ArrayList<>();

    public String toXml() throws DatatypeConfigurationException {
        return DirectDebitDocumentBuilder.toXml(this);
    }

    public DirectDebitDocument() {
    }
    
    public DirectDebitDocument(String creditorBic, String creditorIban, String creditorCreditorIdentifier, String creditorName, String documentMessageId) throws SepaValidationException {
        SepaUtil.validateBic(creditorBic);
        SepaUtil.validateIban(creditorIban);
        SepaUtil.validateCreditorIdentifier(creditorIdentifier);
        this.creditorBic = creditorBic;
        this.creditorIban = creditorIban;
        this.creditorIdentifier = creditorCreditorIdentifier;
        this.creditorName = Zeichen.convert(creditorName);
        this.documentMessageId = documentMessageId;
    }

    /**
     * Returns the due date of the first payment with the given mandate type. The due date has to be equal for all payments with the same mandate type.
     *
     * @param directDebitType
     * @param mandateType
     * @return
     */
    public Date getDueDateByMandateType(MandateType mandateType) {
        for (DirectDebitPayment p : payments) {
            if (mandateType.equals(p.getMandateType())) {
                return p.getDirectDebitDueDate();
            }
        }
        return null;
    }

    public int getNumberOfPaymentsByMandateType(MandateType mt) {
        int result = 0;
        for (DirectDebitPayment p : getPaymentsByMandateType(mt)) {
            result++;
        }
        return result;
    }

    public BigDecimal getTotalPaymentSumOfPaymentsByMandateType(MandateType mt) {
        BigDecimal result = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
        for (DirectDebitPayment p : getPaymentsByMandateType(mt)) {
            result = result.add(p.getPaymentSum());
        }
        return result;
    }

    private void validatePayment(DirectDebitPayment p) throws SepaValidationException {
        p.validate();

        Date currentDueDate = getDueDateByMandateType(p.getMandateType());
        if (currentDueDate != null && !Objects.equals(p.getDirectDebitDueDate(), currentDueDate)) {
            throw new SepaValidationException("Due date of direct debit payment must be equal to other direct debit payments of the same mandate and direct debit type.");
        }
    }

    Iterable<DirectDebitPayment> getPaymentsByMandateType(MandateType mandateType) {
        List<DirectDebitPayment> result = new ArrayList<>();
        for (DirectDebitPayment p : payments) {
            if (mandateType.equals(p.getMandateType())) {
                result.add(p);
            }
        }
        return result;
    }

    public BigDecimal getTotalPaymentSum() {
        BigDecimal result = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
        for (DirectDebitPayment p : payments) {
            result = result.add(p.getPaymentSum());
        }
        return result;
    }

    public void addPayment(DirectDebitPayment payment) throws SepaValidationException {
        validatePayment(payment);
        payments.add(payment);
    }

    public String getCreditorBic() {
        return creditorBic;
    }

    public void setCreditorBic(String creditorBic) throws SepaValidationException {
        SepaUtil.validateBic(creditorBic);
        this.creditorBic = creditorBic;
    }

    public String getCreditorIban() {
        return creditorIban;
    }

    public void setCreditorIban(String creditorIban) throws SepaValidationException {
        SepaUtil.validateIban(creditorIban);
        this.creditorIban = creditorIban;
    }


    public String getCreditorIdentifier() {
        return creditorIdentifier;
    }

    public void setCreditorIdentifier(String creditorCreditorIdentifier) {
        this.creditorIdentifier = creditorCreditorIdentifier;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = Zeichen.convert(creditorName);
    }

    public String getDocumentMessageId() {
        return documentMessageId;
    }

    public void setDocumentMessageId(String documentMessageId) {
        this.documentMessageId = documentMessageId;
    }

    public List<DirectDebitPayment> getPayments() {
        return payments;
    }
}
