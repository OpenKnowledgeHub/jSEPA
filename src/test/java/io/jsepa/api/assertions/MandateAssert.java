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

package io.jsepa.api.assertions;

import static io.jsepa.api.objects.MandateTestProvider.ISSUED_AT;
import static io.jsepa.api.objects.MandateTestProvider.MANDATE_ID;
import static io.jsepa.api.objects.MandateTestProvider.MANDATE_TYPE;
import static org.assertj.core.api.Assertions.assertThat;

import io.jsepa.data.directdebit.Mandate;
import io.jsepa.data.directdebit.MandateType;
import java.time.LocalDate;
import org.assertj.core.api.AbstractAssert;

public class MandateAssert extends AbstractAssert<MandateAssert, Mandate> {
  protected MandateAssert(Mandate mandate) {
    super(mandate, MandateAssert.class);
  }

  public MandateAssert isEmpty() {
    assertThat(actual.getMandateId()).isNull();
    assertThat(actual.getMandateIssuedAt()).isNull();
    assertThat(actual.getMandateType()).isNull();

    return this;
  }

  public MandateAssert hasDefaultTestValues() {
    assertThat(actual.getMandateId()).isEqualTo(MANDATE_ID);
    assertThat(actual.getMandateIssuedAt()).isEqualTo(ISSUED_AT);
    assertThat(actual.getMandateType()).isEqualTo(MANDATE_TYPE);

    return this;
  }

  public MandateAssert hasMandateId(String expectedMandateId) {
    assertThat(actual.getMandateId()).isEqualTo(expectedMandateId);

    return this;
  }

  public MandateAssert isFromType(MandateType expectedType) {
    assertThat(actual.getMandateType()).isEqualTo(expectedType);

    return this;
  }

  public MandateAssert wasIssuedAt(LocalDate expectedIssuedAt) {
    assertThat(actual.getMandateIssuedAt()).isEqualTo(expectedIssuedAt);

    return this;
  }
}
