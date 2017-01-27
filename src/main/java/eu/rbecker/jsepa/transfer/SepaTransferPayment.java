/*
 *  All rights reserved.
 */
package eu.rbecker.jsepa.transfer;

import eu.rbecker.jsepa.directdebit.util.SepaValidationException;
import eu.rbecker.jsepa.sanitization.SepaStringSanitizer;
import eu.rbecker.jsepa.directdebit.util.SepaUtil;
import java.math.BigDecimal;

/**
 *
 * @author Robert Becker <robert at rbecker.eu>
 */
public class SepaTransferPayment {

    private BigDecimal paymentSum;

    private String payeeIban;

    private String payeeBic;

    private String payeeName;

    private String reasonForPayment;
    
    private String endToEndId = null;

    public BigDecimal getPaymentSum() {
        return paymentSum;
    }

    public void setPaymentSum(BigDecimal paymentSum) {
        this.paymentSum = paymentSum.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public String getPayeeIban() {
        return payeeIban;
    }

    public void setPayeeIban(String payeeIban) throws SepaValidationException {
        SepaUtil.validateIban(payeeIban);
        this.payeeIban = payeeIban;
    }

    public String getPayeeBic() {
        return payeeBic;
    }

    public void setPayeeBic(String payeeBic) throws SepaValidationException {
        SepaUtil.validateBic(payeeBic);
        this.payeeBic = payeeBic;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = SepaStringSanitizer.of(payeeName).withMaxLength(70).sanitze();
    }

    public String getReasonForPayment() {
        return reasonForPayment;
    }

    public void setReasonForPayment(String reasonForPayment) {
        this.reasonForPayment = SepaStringSanitizer.of(reasonForPayment).sanitze();
    }

    public String getEndToEndId() {
        return endToEndId;
    }

    public void setEndToEndId(String endToEndId) {
        this.endToEndId = endToEndId;
    }
    

}
