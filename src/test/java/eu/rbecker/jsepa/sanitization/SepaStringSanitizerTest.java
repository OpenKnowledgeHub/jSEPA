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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.rbecker.jsepa.sanitization;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Robert Becker <robert at rbecker.eu>
 */
public class SepaStringSanitizerTest {

  public SepaStringSanitizerTest() {}

  /** Test of sanitze method, of class SepaStringSanitizer. */
  @org.junit.Test
  public void testSanitze() {
    assertEquals("abcd", SepaStringSanitizer.of("abcd").sanitze());
    assertEquals(" bcd", SepaStringSanitizer.of("äbcd").sanitze());
    //        assertEquals("äbcd", SepaStringSanitizer.of("äbcd").sanitze());
    assertEquals(" bcd", SepaStringSanitizer.of("@bcd").sanitze());
    // see
    // https://www.bundesbank.de/Redaktion/DE/Downloads/Aufgaben/Unbarer_Zahlungsverkehr/technische_spezifikation_sepa_lastschriften_november_2014.pdf?__blob=publicationFile
    String allowedChars =
        "a b c d e f g h i j k l m n o p q r s t u v w x y z A B C D E F G H I J K L M N O P Q R S T U V W X Y Z 0 1 2 3 4 5 6 7 8 9 / - ? : ( ) . , + &, *, $, %";
    //        String allowedChars = "a b c d e f g h i j k l m n o p q r s t u v w x y z A B C D E F
    // G H I J K L M N O P Q R S T U V W X Y Z 0 1 2 3 4 5 6 7 8 9 / - ? : ( ) . , + Ä, ä, Ö, ö, Ü,
    // ü, ß, &, *, $, %";
    assertEquals(allowedChars, SepaStringSanitizer.of(allowedChars).sanitze());
  }

  /** Test of withMaxLength method, of class SepaStringSanitizer. */
  @Test
  public void testWithMaxLength() {
    assertEquals("abc", SepaStringSanitizer.of("abcd").withMaxLength(3).sanitze());
  }
}
