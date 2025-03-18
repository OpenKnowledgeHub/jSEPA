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

package io.jsepa.data.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test the LocalDate XML adapter")
class LocalDateAdapterTest {

  private final LocalDate testDate = LocalDate.of(2020, 1, 1);
  private final String testDateString = "2020-01-01";

  LocalDateAdapter underTest;

  @BeforeEach
  void setUp() {
    underTest = new LocalDateAdapter();
  }

  @Test
  @DisplayName("Should marshal a LocalDate")
  void testMarshalLocalDate() {
    final var marshalledDate = underTest.marshal(testDate);

    assertThat(marshalledDate).isNotNull().isNotBlank().isEqualTo(testDateString);
  }

  @Test
  @DisplayName("Should marshal null without failing")
  void testMarshalLocalDateNull() {
    final var marshalledDate = underTest.marshal(null);

    assertThat(marshalledDate).isNull();
  }

  @Test
  @DisplayName("Should unmarshal a LocalDate")
  void testUnmarshalLocalDate() {
    final var unmarshalledDate = underTest.unmarshal(testDateString);

    assertThat(unmarshalledDate).isNotNull().isEqualTo(testDate);
  }

  @Test
  @DisplayName("Should unmarshal null without failing")
  void testUnmarshalLocalDateNull() {
    final var unmarshalledDate = underTest.unmarshal(null);

    assertThat(unmarshalledDate).isNull();
  }
}
