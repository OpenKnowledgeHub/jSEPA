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

package org.openknowledgehub.data.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test the LocalDateTime XML adapter")
class LocalDateTimeAdapterTest {

  private final LocalDateTime testDateTime = LocalDateTime.of(2020, 1, 1, 10, 5, 0);
  private final String testDateTimeString = "2020-01-01T10:05:00";

  LocalDateTimeAdapter underTest;

  @BeforeEach
  void setUp() {
    underTest = new LocalDateTimeAdapter();
  }

  @Test
  @DisplayName("Should marshal a LocalDateTime")
  void testMarshalLocalDateTime() {
    final var marshalledDateTime = underTest.marshal(testDateTime);

    assertThat(marshalledDateTime).isNotNull().isNotBlank().isEqualTo(testDateTimeString);
  }

  @Test
  @DisplayName("Should marshal null without failing")
  void testMarshalLocalDateTimeNull() {
    final var marshalledDateTime = underTest.marshal(null);

    assertThat(marshalledDateTime).isNull();
  }

  @Test
  @DisplayName("Should unmarshal a LocalDateTime")
  void testUnmarshalLocalDateTime() {
    final var unmarshalledDateTime = underTest.unmarshal(testDateTimeString);

    assertThat(unmarshalledDateTime).isNotNull().isEqualTo(testDateTime);
  }

  @Test
  @DisplayName("Should unmarshal null without failing")
  void testUnmarshalLocalDateTimeNull() {
    final var unmarshalledDateTime = underTest.unmarshal(null);

    assertThat(unmarshalledDateTime).isNull();
  }
}
