/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help.parser.toc;

import com.mwulle.help.parser.BasicParser;
import com.mwulle.help.parser.Input;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.swing.tree.DefaultMutableTreeNode;
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

    private static DefaultTreeModel getTree(Document document) {
        MutableTreeNode root = getTocNode(document.getDocumentElement());
        return new DefaultTreeModel(root);
    }

    private static DefaultMutableTreeNode getTocNode(Element element) {
        if (element.getTagName().equals("tocitem")){
            TocResult.TocItem tocItem = new TocResult.TocItem();

            if (element.hasAttribute("text")) {
                String text = element.getAttribute("text");
                tocItem.setText(text);
            }
            if (element.hasAttribute("target")) {
                String helpID = element.getAttribute("target");
                tocItem.setHelpID(helpID);
            }


            if (element.hasChildNodes()) {
                DefaultMutableTreeNode result = new DefaultMutableTreeNode(tocItem, true);
                for (int i = 0; i < element.getChildNodes().getLength(); i++) {
                    Node node = element.getChildNodes().item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        result.add(getTocNode((Element) node));
                    }
                }
                return result;
            } else {
                return new DefaultMutableTreeNode(tocItem);
            }
        }
        return null;
    }
}
