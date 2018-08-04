package com.greentree.model.services.manager;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class loads and provides access to {@link Properties} for business and
 * service layers.
 *
 * @author david5MX53G
 */
public class PropertyManager extends DefaultHandler {

    /**
     * This {@link java.lang.StringBuffer} is used by the {@link
     * PropertyManager#characters(char[], int, int)} method.
     */
    private static final StringBuffer BUFFER = new StringBuffer();

    /**
     * This logs logs to a log using {@link org.apache.logging.log4j.Logger}.
     */
    private static final Logger LOG = LogManager.getLogger();

    /**
     * Properties are read from XML into this {@link java.util.Properties} obj.
     */
    private static Properties properties;

    /**
     * This XML file will be parsed with {@link javax.xml.parsers.SAXParser} and
     * resulting properties stored in a {@link java.util.Properties} object.
     */
    private static final String PROPSPATH = "config/";
    
    /**
     * The following SCHEMA String values are only required if {@link 
     * javax.xml.parsers.SAXParserFactory#validating} has been set as true.
     */
    private static final String JAXP_SCHEMA_LANGUAGE = 
        "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String W3C_XML_SCHEMA = 
        "http://www.w3.org/2001/XMLSchema";
    private static final String JAXP_SCHEMA_SOURCE = 
        "http://java.sun.com/xml/jaxp/properties/schemaSource";

    /**
     * This {@link javax.xml.parsers.SAXParserFactory} builds the
     * {@link javax.xml.parsers.SAXParser}.
     */
    private static final SAXParserFactory SAXFACT
        = SAXParserFactory.newInstance();

    /**
     * This loads properties from application.properties.xml in {@link 
     * PropertyManager#PROPSPATH} .
     *
     * @throws IOException when application.properties.xml at 
     * <code>PROPSPATH</code> cannot be read
     *
     * @throws javax.xml.parsers.ParserConfigurationException when
     *         {@link javax.xml.parsers.SAXParserFactory} has trouble building a
     *         {@link javax.xml.parsers.SAXParser}
     *
     * @throws org.xml.sax.SAXException when
     *         {@link javax.xml.parsers.SAXParserFactory} has trouble building a
     *         {@link javax.xml.parsers.SAXParser}
     */
    public static boolean loadProperties()
        throws IOException, ParserConfigurationException, SAXException {
        boolean result;
        properties = new java.util.Properties();

        /**
         * This {@link javax.xml.parsers.SAXParser} invokes
         * {@link org.xml.sax.helpers.DefaultHandler} on a given InputStream,
         * File, URL, or SAX InputSource. It does not play the Saxophone.
         */
        SAXParser saxParser;

        try (java.io.FileInputStream fis
            = new java.io.FileInputStream(PROPSPATH 
                + "application.properties.xml")) {

            /**
             * "Activating validation allows the application to tell whether the
             * XML document contains the right tags or whether those tags are in
             * the right sequence."
             * (<a href="https://docs.oracle.com/javase/tutorial/jaxp/sax/validation.html">
             * The Java™ Tutorials</a>)
             * 
             * The parser will only tell whether the XML document is well-formed
             * when validation is inactive.
             */
            SAXFACT.setValidating(true);
            saxParser = SAXFACT.newSAXParser();
            saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            saxParser.setProperty(JAXP_SCHEMA_SOURCE, 
                new File(PROPSPATH + "ApplicationProperties.xsd"));

            saxParser.parse(fis, new PropertyManager());
            result = true;
        } catch (IOException | ParserConfigurationException e) {
            result = false;
            LOG.error(e.getMessage());
            throw e;
        }

        return result;
    }

    /**
     * Returns the value of the given {@link Property} from the properties file.
     * The path to this file is given {@link PropertyManager#PropertyManager()}
     * construct.
     *
     * @param propertyName identifies the key in the properties file from which
     * to return the value
     *
     * @return value of the given key in the properties file
     *
     * @throws IOException when the default properties file fails to load
     *
     * @throws javax.xml.parsers.ParserConfigurationException when
     * {@link javax.xml.parsers.SAXParserFactory} has trouble building a
     * {@link javax.xml.parsers.SAXParser}
     *
     * @throws org.xml.sax.SAXException when
     * {@link javax.xml.parsers.SAXParserFactory} has trouble building a
     * {@link javax.xml.parsers.SAXParser}
     */
    public static String getProperty(String propertyName)
        throws IOException, ParserConfigurationException, SAXException {

        if (!(properties instanceof java.util.Properties)) {
            loadProperties();
        }

        return properties.getProperty(propertyName);
    }

    /**
     * Receive notification of character data inside an element. The objective
     * is to extract the data triggering this method into a
     * {@link java.lang.StringBuffer} so that {@link
     * PropertyManager#endElement(java.lang.String, java.lang.String, java.lang.String)}
     * can copy the data into {@link PropertyManager#properties}.
     *
     * @param ch The characters.
     * @param start The start position in the character array.
     * @param length The number of characters to use from the character array.
     *
     * @exception org.xml.sax.SAXException Any SAX exception, possibly wrapping
     * another exception.
     *
     * @see org.xml.sax.ContentHandler#characters
     */
    @Override
    public void characters(char ch[], int start, int length)
        throws SAXException {
        LOG.debug("characters ("
            + String.valueOf(ch.length) + ", "
            + String.valueOf(start) + ", "
            + String.valueOf(length) + ");");
        BUFFER.append(ch, start, length);
    }

    /**
     * Receive notification of the end of an element. This relies on
     * {@link PropertyManager#characters(char[], int, int)} to extract the data
     * into a {@link java.lang.StringBuffer}. The data in question is whatever
     * precedes the element triggering this <code>endElement</code> method.
     *
     * @param uri The Namespace URI, or the empty string if the element has no
     * Namespace URI or if Namespace processing is not being performed.
     *
     * @param localName The local name (without prefix), or the empty string if
     * Namespace processing is not being performed.
     *
     * @param qName The qualified name (with prefix), or the empty string if
     * qualified names are not available.
     *
     * @exception org.xml.sax.SAXException Any SAX exception, possibly wrapping
     * another exception.
     *
     * @see org.xml.sax.ContentHandler#endElement
     */
    @Override
    public void endElement(String uri, String localName, String qName)
        throws SAXException {
        LOG.debug("endElement ("
            + uri + ", "
            + localName + ", "
            + qName + ");");

        String eleName;
        String eleVal;

        if (qName.equals("interface")) {
            eleName = "service.interface";
            eleVal = BUFFER.toString().trim();
            LOG.debug(eleName + ": " + eleVal);
            properties.setProperty(eleName, eleVal);
        }

        if (qName.equals("impl")) {
            eleName = "service.impl";
            eleVal = BUFFER.toString().trim();
            LOG.debug(eleName + ": " + eleVal);
            properties.setProperty(eleName, eleVal);
        }

        if (qName.equals("tokenfilepath")) {
            eleName = "tokenfilepath";
            eleVal = BUFFER.toString().trim();
            LOG.debug(eleName + ": " + eleVal);
            properties.setProperty(eleName, eleVal);
        }

        if (qName.equals("url")) {
            eleName = "jdbc.url";
            eleVal = BUFFER.toString().trim();
            LOG.debug(eleName + ": " + eleVal);
            properties.setProperty(eleName, eleVal);
        }

        if (qName.equals("user")) {
            eleName = "jdbc.user";
            eleVal = BUFFER.toString().trim();
            LOG.debug(eleName + ": " + eleVal);
            properties.setProperty(eleName, eleVal);
        }

        if (qName.equals("pass")) {
            eleName = "jdbc.pass";
            eleVal = BUFFER.toString().trim();
            LOG.debug(eleName + ": " + eleVal);
            properties.setProperty(eleName, eleVal);
        }

        if (qName.equals("minpoolsize")) {
            eleName = "jdbc.minpoolsize";
            eleVal = BUFFER.toString().trim();
            LOG.debug(eleName + ": " + eleVal);
            properties.setProperty(eleName, eleVal);
        }

        if (qName.equals("maxpoolsize")) {
            eleName = "jdbc.maxpoolsize";
            eleVal = BUFFER.toString().trim();
            LOG.debug(eleName + ": " + eleVal);
            properties.setProperty(eleName, eleVal);
        }

        BUFFER.setLength(0);
    }

    /**
     * Receive notification of a recoverable parser error. This simply logs the
     * message using {@link PropertyManager#LOG} and re-throws the error for
     * somebody else to handle.
     *
     * @param e The error information encoded as an exception.
     *
     * @exception org.xml.sax.SAXException Any SAX exception, possibly wrapping
     * another exception.
     *
     * @see org.xml.sax.ErrorHandler#warning
     * @see org.xml.sax.SAXParseException
     */
    @Override
    public void error(SAXParseException e)
        throws SAXException {
        LOG.error(e.getMessage());
        throw e;
    }

    /**
     * Receive notification of a parser warning. This logs the warning with
     * {@link PropertyManager#LOG} but does not re-throw anything.
     *
     * <p>
     * The default implementation does nothing. Application writers may override
     * this method in a subclass to take specific actions for each warning, such
     * as inserting the message in a log file or printing it to the console.</p>
     *
     * @param e The warning information encoded as an exception.
     *
     * @exception org.xml.sax.SAXException Any SAX exception, possibly wrapping
     * another exception.
     *
     * @see org.xml.sax.ErrorHandler#warning
     * @see org.xml.sax.SAXParseException
     */
    @Override
    public void warning(SAXParseException e)
        throws SAXException {
        LOG.warn(e.getMessage());
    }
}
