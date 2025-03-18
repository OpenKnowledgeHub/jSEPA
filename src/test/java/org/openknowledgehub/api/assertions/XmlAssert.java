package org.openknowledgehub.api.assertions;


import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.assertj.core.api.AbstractStringAssert;
import org.xml.sax.SAXException;

public class XmlAssert extends AbstractStringAssert<XmlAssert> {

  private final SchemaFactory schemaFactory;

  protected XmlAssert(String actual) {
    super(actual, XmlAssert.class);

    schemaFactory = SchemaFactory.newDefaultInstance();
  }

  public XmlAssert isInAValidDirectDebitShape() {
    Schema schema = createSchemaForXsd("/xsd/pain.008.001.11.xsd");

    return validateXml(actual, schema);
  }

  public XmlAssert isInAValidTransferShape() {
    Schema schema = createSchemaForXsd("/xsd/pain.001.001.12.xsd");

    return validateXml(actual, schema);
  }

  private XmlAssert validateXml(String xml, Schema schema) {
    if (Objects.isNull(schema)) {
      return this;
    }

    try (Reader inputXml = new StringReader(xml)) {
      schema.newValidator().validate(new StreamSource(inputXml));
    } catch (SAXException | IOException e) {
      failWithMessage("Validation of given xml failed. Message: " + e.getMessage());
    }

    return this;
  }

  private Schema createSchemaForXsd(String xsd) {
    try (InputStream transformFileStream = XmlAssert.class.getResourceAsStream(xsd)) {

      return schemaFactory.newSchema(new StreamSource(transformFileStream));
    } catch (IOException | SAXException e) {
      failWithMessage("Schema creation failed. Message: " + e.getMessage());
    }

    return null;
  }
}
