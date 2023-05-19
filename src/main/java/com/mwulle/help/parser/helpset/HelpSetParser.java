/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help.parser.helpset;

import com.mwulle.help.parser.BasicParser;
import com.mwulle.help.parser.Input;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HelpSetParser extends BasicParser {

    public static Optional<HelpSetResult> parse(Input input) {
        try {
            HelpSetResult result = new HelpSetResult();
            Document document = documentOf(input.getInputStream());

            if (!isVersion2(document)) {
                return Optional.empty();
            }

            result.setResource(input.getUrl());
            result.setTitle(getTitle(document));
            result.setMaps(getMaps(document));
            result.setViews(getViews(document));

            return Optional.of(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static boolean isVersion2(Document document) {
        Node node = document.getElementsByTagName("helpset").item(0);

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            return element.getAttribute("version").trim().equals("2.0");
        }

        return false;
    }

    private static String getTitle(Document document) {
        return document.getDocumentElement().getElementsByTagName("title").item(0).getTextContent();
    }

    private static List<String> getMaps(Document document) {
        NodeList mapsNodeList = document.getDocumentElement().getElementsByTagName("maps");
        List<String> maps = new ArrayList<>(mapsNodeList.getLength());

        for (int i = 0; i < mapsNodeList.getLength(); i++) {
            Node mapsNode = mapsNodeList.item(i);

            if (mapsNode.hasChildNodes()){
                NodeList mapsChildNodeList = mapsNode.getChildNodes();

                for (int j = 0; j < mapsChildNodeList.getLength(); j++) {
                    Node mapsChildNode = mapsChildNodeList.item(j);

                    if (mapsChildNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element mapsChildElement = (Element) mapsChildNode;

                        if (mapsChildElement.hasAttribute("location")) {
                            maps.add(mapsChildElement.getAttribute("location"));
                        }
                    }
                }
            }
        }

        return maps;
    }

    private static List<HelpSetResult.View> getViews(Document document) {
        NodeList viewNodeList = document.getDocumentElement().getElementsByTagName("view");
        List<HelpSetResult.View> views = new ArrayList<>(viewNodeList.getLength());

        for (int i = 0; i < viewNodeList.getLength(); i++) {
            NodeList viewNodeChildren = viewNodeList.item(i).getChildNodes();
            HelpSetResult.View view = new HelpSetResult.View();

            for (int j = 0; j < viewNodeChildren.getLength(); j++) {

                if (viewNodeChildren.item(j).getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) viewNodeChildren.item(j);
                    switch (element.getTagName()) {
                        case "name":
                            view.setName(element.getTextContent());
                        case "label":
                            view.setLabel(element.getTextContent());
                        case "type":
                            view.setType(element.getTextContent());
                        case "data":
                            view.setData(element.getTextContent());
                    }
                }
            }
            if (view.getName() != null && (view.getLabel() != null && (view.getType() != null && (view.getData() != null)))) {
                views.add(view);
            }
        }

        return views;
    }

}
