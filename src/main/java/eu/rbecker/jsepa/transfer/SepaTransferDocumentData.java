package eu.rbecker.jsepa.transfer;

import eu.rbecker.jsepa.directdebit.util.SepaValidationException;
import eu.rbecker.jsepa.sanitization.SepaStringSanitizer;
import eu.rbecker.jsepa.directdebit.util.SepaUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.xml.datatype.DatatypeConfigurationException;

/**
 *
 * @author Robert Becker
 */
public class SepaTransferDocumentData {

    private String payerBic;

    private String payerIban;

    private String payerName;

    private String documentMessageId;

    private Calendar dateOfExecution = Calendar.getInstance();
    
    private boolean batchBooking = true;
    
    private final List<SepaTransferPayment> payments = new ArrayList<>();

    public String toXml() throws DatatypeConfigurationException {
        return SepaTransferDocumentBuilder.toXml(this);
    }

    public SepaTransferDocumentData() {
    }
    
    public SepaTransferDocumentData(String payerBic, String payerIban, String payerName, String documentMessageId) throws SepaValidationException {
        setPayerBic(payerBic);
        setPayerIban(payerIban);
        setPayerName(payerName);
        setDocumentMessageId(documentMessageId);
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
