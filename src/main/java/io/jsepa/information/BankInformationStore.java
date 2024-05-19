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
import java.util.concurrent.ConcurrentHashMap;

public class BankInformationStore implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Map<String, BankInformationCache> CACHES = new ConcurrentHashMap<>();

  public static BankInformation forIban(String iban) {
    return getCacheForIban(iban).findByIban(iban);
  }

  public static BankInformation forBankCode(String countryCode, String bankCode) {
    return getCacheForCountryCode(countryCode).findByBankCode(bankCode);
  }

  public static BankInformationCache getCacheForIban(String iban) {
    String countryCode = iban.substring(0, 2);
    return getCacheForCountryCode(countryCode);
  }

  public static BankInformationCache getCacheForCountryCode(String countryCode)
      throws IllegalArgumentException {
    countryCode = countryCode.toLowerCase();
    BankInformationCache result = CACHES.get(countryCode);
    if (result != null) {
      return result;
    }
    result = createCache(countryCode, result);
    return result;
  }

  private static BankInformationCache createCache(String countryCode, BankInformationCache result)
      throws IllegalArgumentException {
    switch (countryCode) {
      case "de":
        result =
            new BankInformationCache(countryCode, new GermanBankInformationProvider().provide());
        break;
      default:
        throw new IllegalArgumentException(
            "No bank data lookup implemented for country code " + countryCode);
    }
    CACHES.put(countryCode, result);
    return result;
  }
}
