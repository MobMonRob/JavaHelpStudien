/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help.parser.index;

import com.mwulle.help.parser.BasicParser;
import com.mwulle.help.parser.Input;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class IndexParser extends BasicParser {

    public static Optional<IndexResult> parse(Input input) {
        try {
            IndexResult result = new IndexResult();
            Document document = documentOf(input.getInputStream());

            result.setIndexes(getIndexes(document));

            return Optional.of(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static Map<String, String> getIndexes(Document document) {
        NodeList indexitemNodeList = document.getDocumentElement().getElementsByTagName("indexitem");
        Map<String, String> indexes = new HashMap<>();

        for (int i = 0; i < indexitemNodeList.getLength(); i++) {
            Node indexitemNode = indexitemNodeList.item(i);
            if (indexitemNode.getNodeType() == Node.ELEMENT_NODE) {
                Element indexitemElement = (Element) indexitemNode;

                if (indexitemElement.hasAttribute("text") && indexitemElement.hasAttribute("target")) {
                    indexes.put(indexitemElement.getAttribute("target"), indexitemElement.getAttribute("text"));
                }
            }
        }

        return indexes;
    }
}
