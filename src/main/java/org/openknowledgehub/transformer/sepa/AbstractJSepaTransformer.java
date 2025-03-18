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

package org.openknowledgehub.transformer.sepa;

import org.openknowledgehub.data.SepaXmlDocument;
import org.openknowledgehub.exception.JSepaException;
import org.openknowledgehub.transformer.JSepaTransformer;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.util.JAXBSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public abstract class AbstractJSepaTransformer<I extends SepaXmlDocument>
    implements JSepaTransformer<I> {

  private final TransformerFactory transformerFactory;

  protected AbstractJSepaTransformer() {
    transformerFactory = TransformerFactory.newDefaultInstance();
  }

  protected abstract Class<I> getSepaDocumentType();

  protected abstract String getXsltFileName();

  @Override
  public String transform(I sourceDocument) throws JSepaException {
    Transformer transformer = createTransformer();
    Source inputXml = createSource(createContext(), sourceDocument);

    return transformInput(transformer, inputXml);
  }

  private JAXBContext createContext() {
    try {
      return JAXBContext.newInstance(getSepaDocumentType());
    } catch (JAXBException exception) {
      throw new JSepaException("Could not create a new JAXBContext", exception);
    }
  }

  private Transformer createTransformer() {
    try (InputStream transformFileStream =
        DirectDebitTransformer.class.getResourceAsStream(getXsltFileName())) {

      return transformerFactory.newTransformer(new StreamSource(transformFileStream));
    } catch (IOException | TransformerException exception) {
      throw new JSepaException("Could not create a new Transformer", exception);
    }
  }

  private Source createSource(JAXBContext context, I sourceDocument) {
    try {
      return new JAXBSource(context, sourceDocument);
    } catch (JAXBException exception) {
      throw new JSepaException("Could not create a new JAXBSource", exception);
    }
  }

  private String transformInput(Transformer transformer, Source source) {
    try (StringWriter resultWriter = new StringWriter()) {
      transformer.transform(source, new StreamResult(resultWriter));

      return resultWriter.toString();
    } catch (IOException | TransformerException exception) {
      throw new JSepaException("Could not transform source XML", exception);
    }
  }
}
