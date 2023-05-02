/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help.parser.map;

import com.mwulle.help.parser.BasicParser;
import com.mwulle.help.parser.Input;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MapParser extends BasicParser {

    public static Optional<MapResult> parse(Input input) {
        try {
            MapResult result = new MapResult();
            Document document = documentOf(input.getInputStream());

            result.setMap(getMap(document));

            return Optional.of(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static Map<String, String> getMap(Document document) {
        NodeList mapIDNodeList = document.getDocumentElement().getElementsByTagName("mapID");
        Map<String, String> map = new HashMap<>();

        for (int i = 0; i < mapIDNodeList.getLength(); i++) {
            Node mapIDNode = mapIDNodeList.item(i);
            if (mapIDNode.getNodeType() == Node.ELEMENT_NODE) {
                Element mapIDElement = (Element) mapIDNode;

                if (mapIDElement.hasAttribute("target") && mapIDElement.hasAttribute("url")) {
                    map.put(mapIDElement.getAttribute("target"), mapIDElement.getAttribute("url"));
                }
            }
        }

        return map;
    }
}
