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
package eu.rbecker.jsepa.directdebit.util;

import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.BranchAndFinancialInstitutionIdentification4;
import eu.rbecker.jsepa.directdebit.xml.schema.pain_008_001_02.FinancialInstitutionIdentification7;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public abstract class SepaXmlDocumentBuilder implements Serializable {

  protected static final long serialVersionUID = 1L;

  /**
   * Returns a XMLGregorianCalendar in the format of 2014-01-22T17:53:01
   *
   * @param cal
   * @return
   * @throws DatatypeConfigurationException
   */
  protected static XMLGregorianCalendar calendarToXmlGregorianCalendarDateTime(Calendar cal) {
    // this way of initialization is required to omit time zone and milli seconds -.-
    XMLGregorianCalendar result;
    try {
      result = DatatypeFactory.newInstance().newXMLGregorianCalendar();
    } catch (DatatypeConfigurationException ex) {
      throw new RuntimeException(ex);
    }
    result.setYear(cal.get(Calendar.YEAR));
    result.setMonth(cal.get(Calendar.MONTH) + 1);
    result.setDay(cal.get(Calendar.DAY_OF_MONTH));
    result.setHour(cal.get(Calendar.HOUR_OF_DAY));
    result.setMinute(cal.get(Calendar.MINUTE));
    result.setSecond(cal.get(Calendar.SECOND));
    return result;
  }

  protected static XMLGregorianCalendar dateToXmlGregorianCalendarDateTime(Date d) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(d);
    return calendarToXmlGregorianCalendarDateTime(cal);
  }

  protected static XMLGregorianCalendar dateToXmlGregorianCalendarDate(Date d) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(d);
    return calendarToXmlGregorianCalendarDate(cal);
  }

  protected static XMLGregorianCalendar calendarToXmlGregorianCalendarDate(GregorianCalendar d) {
    XMLGregorianCalendar result;
    try {
      result =
          DatatypeFactory.newInstance()
              .newXMLGregorianCalendarDate(
                  d.get(Calendar.YEAR),
                  d.get(Calendar.MONTH) + 1,
                  d.get(Calendar.DAY_OF_MONTH),
                  DatatypeConstants.FIELD_UNDEFINED);
    } catch (DatatypeConfigurationException ex) {
      throw new RuntimeException(ex);
    }
    return result;
  }

  protected static void marshal(
      String contextName, JAXBElement jaxbDoc, StringWriter resultWriter) {
    try {
      javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(contextName);
      javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
      marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); // NOI18N
      marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      marshaller.marshal(jaxbDoc, resultWriter);
    } catch (JAXBException e) {
      // If something crashes here it needs to be fixed in the library, not by the user.
      throw new RuntimeException(e);
    }
  }

  protected static BranchAndFinancialInstitutionIdentification4
      bicToBranchAndFinancialInstitutionIdentification(String bic) {
    BranchAndFinancialInstitutionIdentification4 result =
        new BranchAndFinancialInstitutionIdentification4();
    result.setFinInstnId(new FinancialInstitutionIdentification7());
    result.getFinInstnId().setBIC(bic);
    return result;
  }
}
