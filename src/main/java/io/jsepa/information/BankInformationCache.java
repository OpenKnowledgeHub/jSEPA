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

package io.jsepa.information;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class BankInformationCache implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String countryCode;

  private final Map<String, BankInformation> byBic = new TreeMap<>();

  private final Map<String, BankInformation> byBankCode = new TreeMap<>();

  public BankInformationCache(String countryCode, Iterable<BankInformation> data) {
    this.countryCode = countryCode;
    readData(data);
  }

  private void readData(Iterable<BankInformation> data) {
    for (BankInformation bi : data) {
      byBic.put(bi.getBic(), bi);
      byBankCode.put(bi.getBankCode(), bi);
    }
  }

  public String getCountryCode() {
    return countryCode;
  }

  public BankInformation findByBic(String bic) {
    return byBic.get(bic);
  }

  public BankInformation findByBankCode(String bankCode) {
    return byBankCode.get(bankCode);
  }

  public BankInformation findByIban(String iban) {
    if (!iban.toLowerCase().startsWith("de")) {
      throw new IllegalArgumentException(
          "findByIban is currently only implemented for german IBANs.");
    }
    String bankCode;
    bankCode = extractGermanBankCodeFromIban(iban);
    return byBankCode.get(bankCode);
  }

  private String extractGermanBankCodeFromIban(String iban) {
    return iban.substring(4, 12);
  }
}
