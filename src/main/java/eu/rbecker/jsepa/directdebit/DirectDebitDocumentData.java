package eu.rbecker.jsepa.directdebit;

import eu.rbecker.jsepa.directdebit.util.SepaUtil;
import eu.rbecker.jsepa.directdebit.util.SepaValidationException;
import eu.rbecker.jsepa.sanitization.SepaStringSanitizer;
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
public class DirectDebitDocumentData {

    private final List<DirectDebitPayment> payments = new ArrayList<>();
    private String creditorBic;
    private String creditorIban;
    private String creditorIdentifier;
    private String creditorName;
    private String documentMessageId;

    public DirectDebitDocumentData() {
    }

    public DirectDebitDocumentData(String creditorBic, String creditorIban, String creditorCreditorIdentifier, String creditorName, String documentMessageId) throws SepaValidationException {
        setCreditorBic(creditorBic);
        setCreditorIban(creditorIban);
        setCreditorIdentifier(creditorCreditorIdentifier);
        setCreditorName(creditorName);
        setDocumentMessageId(documentMessageId);
    }
    
    public String toXml() throws DatatypeConfigurationException {
        return DirectDebitDocumentBuilder.toXml(this);
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

    public final void setCreditorBic(String creditorBic) throws SepaValidationException {
        SepaUtil.validateBic(creditorBic);
        this.creditorBic = creditorBic;
    }

    public String getCreditorIban() {
        return creditorIban;
    }

    public final void setCreditorIban(String creditorIban) throws SepaValidationException {
        SepaUtil.validateIban(creditorIban);
        this.creditorIban = creditorIban;
    }


    public String getCreditorIdentifier() {
        return creditorIdentifier;
    }

    public final void setCreditorIdentifier(String creditorCreditorIdentifier) {
        this.creditorIdentifier = creditorCreditorIdentifier;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public final void setCreditorName(String creditorName) {
        this.creditorName = SepaStringSanitizer.of(creditorName).withMaxLength(70).sanitze();
    }

    public String getDocumentMessageId() {
        return documentMessageId;
    }

    public final void setDocumentMessageId(String documentMessageId) {
        this.documentMessageId = SepaStringSanitizer.of(documentMessageId).withMaxLength(35).sanitze();
    }

    public List<DirectDebitPayment> getPayments() {
        return payments;
    }
}
