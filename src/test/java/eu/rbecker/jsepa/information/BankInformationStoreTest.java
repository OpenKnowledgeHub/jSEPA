/*
 * The MIT License
 *
 * Copyright 2017 Robert Becker <robert at rbecker.eu>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package eu.rbecker.jsepa.information;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Robert Becker <robert at rbecker.eu>
 */
public class BankInformationStoreTest {
    
    public BankInformationStoreTest() {
    }

    /**
     * Test of forIban method, of class BankInformationStore.
     */
    @Test
    public void testForIban() {
        assertEquals("50010517", BankInformationStore.forIban("DE12500105170648489890").getBankCode());
    }

    /**
     * Test of forBankCode method, of class BankInformationStore.
     */
    @Test
    public void testForBankCode() {
        assertEquals("50010517", BankInformationStore.forBankCode("de", "50010517").getBankCode());
        assertEquals("NOLADE21LBG", BankInformationStore.forBankCode("de", "24050110").getBic());
        assertEquals("COBADEHHXXX", BankInformationStore.forBankCode("de", "20040000").getBic());
    }

    /**
     * Test of getCacheForIban method, of class BankInformationStore.
     */
    @Test
    public void testGetCacheForIban() {
        assertEquals("de", BankInformationStore.getCacheForIban("DE12500105170648489890").getCountryCode());
    }

    /**
     * Test of getCacheForCountryCode method, of class BankInformationStore.
     */
    @Test
    public void testGetCacheForCountryCode() {
        assertEquals("de", BankInformationStore.getCacheForIban("de").getCountryCode());
    }
    
}
