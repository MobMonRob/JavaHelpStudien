/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help.parser.toc;

import com.mwulle.help.helpset.toc.TOCItem;
import com.mwulle.help.helpset.toc.TOCItemNodeFactory;
import com.mwulle.help.helpset.toc.TOCItemNode;
import com.mwulle.help.parser.BasicParser;
import com.mwulle.help.parser.Input;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import java.util.Optional;

public class TocParser extends BasicParser {

    public static Optional<TocResult> parse(Input input) {
        try {
            TocResult result = new TocResult();
            Document document = documentOf(input.getInputStream());

            result.setTree(getTree(document));

            return Optional.of(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static TOCItemNode getTree(Document document) {
        Node node = document.getElementsByTagName("toc").item(0);

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            return createTOCItemNode(element);
        }else {
            throw new RuntimeException();
        }
    }

    private static TOCItemNode createTOCItemNode(Element element) {
        if (element.getTagName().equals("tocitem")){
            TOCItem tocItem = createTocItem(element);
            if (element.hasChildNodes()){
                TOCItemNodeFactory factory = new TOCItemNodeFactory();
                for (int i = 0; i < element.getChildNodes().getLength(); i++) {
                    Node node = element.getChildNodes().item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element e = (Element) node;
                        if (e.getTagName().equals("tocitem")){
                            factory.addTocItem(createTOCItemNode(element));
                        }
                    }
                }
                return new TOCItemNode(tocItem, factory);
            } else {
                return new TOCItemNode(tocItem);
            }
        }
        return new TOCItemNode(null);
    }

    private static TOCItemNodeFactory createChildren(NodeList nodes) {
        TOCItemNodeFactory factory = new TOCItemNodeFactory();

        return factory;
    }

    private static TOCItem createTocItem(Element element) {
            TOCItem tocItem = new TOCItem();

            if (element.hasAttribute("text")) {
                String text = element.getAttribute("text");
                tocItem.setText(text);
            }
            if (element.hasAttribute("target")) {
                String helpID = element.getAttribute("target");
                tocItem.setHelpID(helpID);
            }
            return tocItem;
    }
}
