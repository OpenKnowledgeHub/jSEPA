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

package io.jsepa.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test JSepa content sanitizer")
class JSepaContentSanitizerTest {

  @Test
  @DisplayName("Should remain all allowed chars")
  void testSanitizeAllAllowedCharsRemain() {
    final String givenInput = "Hello123 &*$%/?:().,'+-";

    final String sanitizedContent = JSepaContentSanitizer.of(givenInput).sanitize();

    assertThat(sanitizedContent).isEqualTo(givenInput);
  }

  @Test
  @DisplayName("Should replace forbidden chars")
  void testSanitizeRemovesForbiddenChars() {
    final String givenInput = "Hello@#World!€";
    final String expectedOutput = "Hello  World";

    final String sanitizedContent = JSepaContentSanitizer.of(givenInput).sanitize();

    assertThat(sanitizedContent).isEqualTo(expectedOutput);
  }

  @Test
  @DisplayName("Should cut of too long content")
  void testSanitizeWithMaxLength() {
    final String givenInput = "Hello World 123456";
    final int maxLength = 10;
    final String expectedOutput = "Hello Worl";

    final String sanitizedContent =
        JSepaContentSanitizer.of(givenInput).withMaxLength(maxLength).sanitize();

    assertThat(sanitizedContent).isEqualTo(expectedOutput);
  }

  @Test
  @DisplayName("Should do nothing on empty String")
  void testSanitizeEmptyString() {
    final String givenInput = "";

    final String sanitizedContent = JSepaContentSanitizer.of(givenInput).sanitize();

    assertThat(sanitizedContent).isEmpty();
  }

  @Test
  @DisplayName("Should return empty String if content is null")
  void testSanitizeNullInput() {
    final String sanitizedContent = JSepaContentSanitizer.of(null).sanitize();

    assertThat(sanitizedContent).isEmpty();
  }

  @Test
  @DisplayName("Should replace all forbidden chars")
  void testSanitizeOnlyForbiddenChars() {
    final String givenInput = "@#€§!";

    final String sanitizedContent = JSepaContentSanitizer.of(givenInput).sanitize();

    assertThat(sanitizedContent).isEmpty();
  }

  @Test
  @DisplayName("Should preserve whitespaces")
  void testSanitizeWhitespacePreserved() {
    final String givenInput = "Hello   World";

    final String sanitizedContent = JSepaContentSanitizer.of(givenInput).sanitize();

    assertThat(sanitizedContent).isEqualTo(givenInput);
  }
}
