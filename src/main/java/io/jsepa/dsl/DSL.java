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

package io.jsepa.dsl;

import io.jsepa.data.directdebit.MandateType;
import io.jsepa.dsl.impl.common.AccountStartSelectImpl;
import io.jsepa.dsl.impl.directdebit.DirectDebitStartSelectImpl;
import io.jsepa.dsl.impl.directdebit.MandateStartSelectImpl;
import io.jsepa.dsl.impl.transfer.TransferStartSelectImpl;

public final class DSL {

  private DSL() {}

  public static TransferStartSelect transfer(String messageId) {
    return new TransferStartSelectImpl(messageId);
  }

  public static DirectDebitStartSelect directDebit(String messageId) {
    return new DirectDebitStartSelectImpl(messageId);
  }

  public static AccountStartSelect account() {
    return new AccountStartSelectImpl();
  }

  public static MandateStartSelect oneTimeMandate(String mandateIdentifier) {
    return new MandateStartSelectImpl(mandateIdentifier, MandateType.ONE_OFF);
  }

  public static MandateStartSelect firstUseOfMandate(String mandateIdentifier) {
    return new MandateStartSelectImpl(mandateIdentifier, MandateType.FIRST);
  }

  public static MandateStartSelect lastUseOfMandate(String mandateIdentifier) {
    return new MandateStartSelectImpl(mandateIdentifier, MandateType.FINAL);
  }

  public static MandateStartSelect recurrentMandate(String mandateIdentifier) {
    return new MandateStartSelectImpl(mandateIdentifier, MandateType.RECURRING);
  }
}
