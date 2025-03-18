/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2025 openknowledgehub.org
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

package io.jsepa.api.objects;

import io.jsepa.data.common.AccountIdentification;

public class AccountIdentificationTestProvider {
  public static final String NAME = "AccountId";
  public static final String IDENTIFICATION = "Identification";
  public static final String BIC = "BYLADEM1001";
  public static final String IBAN = "DE89370400440532013000";

  public static final String SPANISH_NAME = "Another_AccountId";
  public static final String SPANISH_IDENTIFICATION = "Anther_Identification";
  public static final String SPANISH_BIC = "CAIXESBBXXX";
  public static final String SPANISH_IBAN = "ES9121000418450200051332";

  public AccountIdentification defaultAccount() {
    return new AccountIdentification(NAME, IDENTIFICATION, BIC, IBAN);
  }

  public AccountIdentification spanish() {
    return new AccountIdentification(
        SPANISH_NAME, SPANISH_IDENTIFICATION, SPANISH_BIC, SPANISH_IBAN);
  }
}
