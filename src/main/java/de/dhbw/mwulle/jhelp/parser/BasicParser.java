/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package de.dhbw.mwulle.jhelp.parser;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStream;

public abstract class BasicParser {
    private static DocumentBuilder builder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Document documentOf(InputStream in) {
        try {
            Document document = builder().parse(in);
            document.normalize();
            return document;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
