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

package org.openknowledgehub.dsl.impl.directdebit;

import org.openknowledgehub.data.directdebit.MandateBuilder;
import org.openknowledgehub.data.directdebit.MandateType;
import org.openknowledgehub.dsl.MandateEndSelect;
import org.openknowledgehub.dsl.MandateStartSelect;
import java.time.LocalDate;

public class MandateStartSelectImpl implements MandateStartSelect {
  private final MandateBuilder mandateBuilder;

  public MandateStartSelectImpl(String mandateIdentifier, MandateType type) {
    this.mandateBuilder = MandateBuilder.withMandate(mandateIdentifier);
    this.mandateBuilder.withMandateType(type);
  }

  @Override
  public MandateEndSelect issuedAt(LocalDate issuedAt) {
    this.mandateBuilder.withMandateIssuedAt(issuedAt);

    return new MandateEndSelectImpl(mandateBuilder);
  }
}
