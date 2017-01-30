/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.rbecker.jsepa.sanitization;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Robert Becker <robert at rbecker.eu>
 */
public class SepaStringSanitizerTest {
    
    public SepaStringSanitizerTest() {
    }


    /**
     * Test of sanitze method, of class SepaStringSanitizer.
     */
    @org.junit.Test
    public void testSanitze() {
        assertEquals("abcd", SepaStringSanitizer.of("abcd").sanitze());
        assertEquals("_bcd", SepaStringSanitizer.of("äbcd").sanitze());
//        assertEquals("äbcd", SepaStringSanitizer.of("äbcd").sanitze());
        assertEquals("_bcd", SepaStringSanitizer.of("@bcd").sanitze());
        // see https://www.bundesbank.de/Redaktion/DE/Downloads/Aufgaben/Unbarer_Zahlungsverkehr/technische_spezifikation_sepa_lastschriften_november_2014.pdf?__blob=publicationFile
        String allowedChars = "a b c d e f g h i j k l m n o p q r s t u v w x y z A B C D E F G H I J K L M N O P Q R S T U V W X Y Z 0 1 2 3 4 5 6 7 8 9 / - ? : ( ) . , + &, *, $, %";
//        String allowedChars = "a b c d e f g h i j k l m n o p q r s t u v w x y z A B C D E F G H I J K L M N O P Q R S T U V W X Y Z 0 1 2 3 4 5 6 7 8 9 / - ? : ( ) . , + Ä, ä, Ö, ö, Ü, ü, ß, &, *, $, %";
        assertEquals(allowedChars, SepaStringSanitizer.of(allowedChars).sanitze());
        
    }

    /**
     * Test of withMaxLength method, of class SepaStringSanitizer.
     */
    @Test
    public void testWithMaxLength() {
        assertEquals("abc", SepaStringSanitizer.of("abcd").withMaxLength(3).sanitze());
    }
    
}
