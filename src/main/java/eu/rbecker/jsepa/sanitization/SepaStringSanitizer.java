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

package eu.rbecker.jsepa.sanitization;

import java.io.Serializable;
import java.util.regex.Pattern;

public class SepaStringSanitizer implements Serializable {

  /**
   * See
   * https://www.bundesbank.de/Redaktion/DE/Downloads/Aufgaben/Unbarer_Zahlungsverkehr/technische_spezifikation_sepa_lastschriften_november_2014.pdf?__blob=publicationFile
   * Still, at least the Sparkasse does not allow umlaute. Required for: Debitor Name <Dbtr><Nm>
   * Ultimate Debtor Name <UltmtDbtr><Nm> Creditor Name <Cdtr><Nm> Ultimate Creditor Name
   * <UltmtCdtr><Nm>
   */
  private static final String ALLOWED_CHARS_REGEXP =
      "[^a-zA-Z0-9 " + ("&*$%/?:().,'+-".replaceAll("(.)", "\\\\$1")) + "]";

  //    private static final String ALLOWED_CHARS_REGEXP = "[^a-zA-Z0-9ÄäÖöÜüß " +
  // ("&*$%/?:().,'+-".replaceAll("(.)", "\\\\$1")) + "]";

  private static final Pattern ALLOWED_CHARS_PATTERN = Pattern.compile(ALLOWED_CHARS_REGEXP);

  private static final long serialVersionUID = 1L;

  private final String sepaString;

  private Integer maxLength = null;

  private SepaStringSanitizer(String sepaString) {
    this.sepaString = sepaString;
  }

  public static SepaStringSanitizer of(String sepaString) {
    return new SepaStringSanitizer(sepaString);
  }

  public SepaStringSanitizer withMaxLength(int maxLength) {
    this.maxLength = maxLength;
    return this;
  }

  public String sanitze() {
    String result = ALLOWED_CHARS_PATTERN.matcher(sepaString).replaceAll(" ");
    if (maxLength != null) {
      result = result.substring(0, Math.min(result.length(), maxLength));
    }
    return result;
  }
}
