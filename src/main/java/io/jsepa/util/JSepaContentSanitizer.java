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

import java.util.Objects;
import java.util.regex.Pattern;

public class JSepaContentSanitizer {

  private static final String ALLOWED_CHARS_REGEXP = "[^a-zA-Z0-9 &*$%/?:().,'+-]";
  private static final Pattern ALLOWED_CHARS_PATTERN = Pattern.compile(ALLOWED_CHARS_REGEXP);

  private final String sepaString;
  private final Integer maxLength;

  private JSepaContentSanitizer(String sepaString, Integer maxLength) {
    this.sepaString = Objects.nonNull(sepaString)  ? sepaString : "";
    this.maxLength = maxLength;
  }

  public static JSepaContentSanitizer of(String sepaString) {
    return new JSepaContentSanitizer(sepaString, null);
  }

  public JSepaContentSanitizer withMaxLength(int maxLength) {
    return new JSepaContentSanitizer(this.sepaString, maxLength);
  }

  public String sanitize() {
    String sanitized = ALLOWED_CHARS_PATTERN.matcher(sepaString).replaceAll(" ").trim();
    return (Objects.nonNull(maxLength) && sanitized.length() > maxLength)
        ? sanitized.substring(0, maxLength)
        : sanitized;
  }
}
