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

package io.jsepa.information;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class GermanBankInformationProvider implements BankInformationProvider {

  /**
   * From
   * https://www.bundesbank.de/Redaktion/DE/Downloads/Aufgaben/Unbarer_Zahlungsverkehr/Bankleitzahlen/merkblatt_bankleitzahlendatei.pdf?__blob=publicationFile
   *
   * <p>FeldNr. / Inhalt / Anzahl der Stellen / Nummerierung der Stellen 1 Bankleitzahl 8 1 - 8 2
   * Merkmal, ob bankleitzahlführender Zahlungsdienstleister („1“) oder nicht („2“) 1 9 3
   * Bezeichnung des Zahlungsdienstleisters(ohne Rechtsform) 58 10 - 67 4 Postleitzahl 5 68 - 72 5
   * Ort 35 73 - 107 6 Kurzbezeichnung des Zahlungsdienstleisters mit Ort (ohne Rechtsform) 27 108 -
   * 134 7 Institutsnummer für PAN 5 135 - 139 8 Business Identifier Code – BIC 11 140 - 150 9
   * Kennzeichen für Prüfzifferberechnungsmethode 2 151 - 152 10 Nummer des Datensatzes 6 153 - 158
   * 11 Änderungskennzeichen „A“ (Addition) für neue, „D“ (Deletion) für gelöschte, „U“(Unchanged)
   * für unveränderte und „M“ (Modified) für veränderte Datensätze 1 159 12 Hinweis auf eine
   * beabsichtigte Bankleitzahllöschung "0", sofern keine Angabe "1", sofern BLZ im Feld 1 zur
   * Löschung vorgesehen ist 1 160 13 Hinweis auf Nachfolge-Bankleitzahl 8 161 - 168 14 Kennzeichen
   * für die IBAN-Regel (nur erweiterte Bankleitzahlendatei) 6 169 - 174
   */
  public static final String BANK_DATA_FILE_NAME = "bankdata.de.txt";

  private static final long serialVersionUID = 1L;

  public GermanBankInformationProvider() {}

  @Override
  public List<BankInformation> provide() {
    try (InputStream in = this.getClass().getResourceAsStream("/" + BANK_DATA_FILE_NAME)) {
      return new BufferedReader(new InputStreamReader(in))
          .lines()
          .map(l -> parseLine(l))
          .filter(bi -> bi != null)
          .collect(Collectors.toList());
    } catch (IOException ex) {
      // If this crashes, the library (build) is broken.
      throw new RuntimeException(ex);
    }
  }

  private BankInformation parseLine(String l) {
    boolean paymentProvider = l.charAt(8) == '1';
    if (!paymentProvider) {
      return null;
    }
    String code = l.substring(0, 8).trim();
    String name = l.substring(9, 67).trim();
    String shortName = l.substring(107, 134).trim();
    String bic = l.substring(139, 150).trim();
    BankInformation bi = new BankInformation(name, shortName, code, bic);
    return bi;
  }
}
