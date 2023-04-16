/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help.io;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Melvin Wulle
 */
public class Parser {
    public static Document documentOf(InputStream in) {
        try {
            Document document = builder().parse(in);
            document.normalize();
            return document;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static DocumentBuilder builder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getTitle(Document document) {
        return document.getDocumentElement().getElementsByTagName("title").item(0).getTextContent();
    }

    public static Map<String, String> getIndex(Document document) {
        NodeList nodeList = document.getElementsByTagName("indexitem");
        Map<String, String> index = new HashMap<>(nodeList.getLength());

        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nodeList.item(i);
                index.put(element.getAttribute("target"), element.getAttribute("text"));
            }
        }

        return index;
    }

    public static Set<URL> getIndexURLs(Document document) {
        NodeList nodes = document.getDocumentElement().getElementsByTagName("view");
        Set<URL> urls = new HashSet<>(nodes.getLength());

        for (int i = 0; i < nodes.getLength(); i++) {
            urls.add(getIndexURL(nodes.item(i)));
        }

        return urls;
    }

    private static URL getIndexURL(Node node) {
        String url = null;
        String type = null;

        NodeList children = node.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) children.item(i);
                switch (element.getTagName()) {
                    case "type":
                        type = element.getTextContent();
                    case "data":
                        url = element.getTextContent();
                }
            }
        }

        if (url != null && type != null && type.equalsIgnoreCase("javax.help.IndexView")) {
            try {
                return new URL(url);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    public static Map<String, String> getMap(Document document) {
        NodeList nodeList = document.getDocumentElement().getElementsByTagName("mapID");
        Map<String, String> map = new HashMap<>(nodeList.getLength());

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node mapID = nodeList.item(i);
            if (mapID.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) mapID;

                String target = element.getAttribute("target");
                String url = element.getAttribute("url");
                String content;

                try {
                     content = URLLoader.stringOf(URLLoader.inputStreamOf(new URL(url)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                map.put(target, content);
            }
        }

        return map;
    }

    public static Set<URL> getMapURLs(Document document) {
        NodeList nodes = document.getDocumentElement().getElementsByTagName("maps");
        Set<URL> urls = new HashSet<>(nodes.getLength());

        for (int i = 0; i < nodes.getLength(); i++) {
            urls.add(getMapURL(nodes.item(i)));
        }

        return urls;
    }

    private static URL getMapURL(Node node) {
        String url = null;

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element mapElement = (Element) node;
            Element mapRef = (Element) mapElement.getElementsByTagName("mapref").item(0);
            url = mapRef.getAttribute("location");
        }

        if (url != null) {
            try {
                return new URL(url);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    public static DefaultTreeModel getToc(Document document) {
        MutableTreeNode root = getTocNode(document.getDocumentElement());
        DefaultTreeModel model = new DefaultTreeModel(root);

        return model;
    }

    public static DefaultMutableTreeNode getTocNode(Element element) {
        DefaultMutableTreeNode result = null;

        if (element.hasChildNodes()) {
            for (int i = 0; i < element.getChildNodes().getLength(); i++) {
                Node node = element.getChildNodes().item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    result = new DefaultMutableTreeNode(getTocNode((Element) node));
                }
            }
        }

        return result;
    }

    public static Set<URL> getTocURLs(Document document) {
        NodeList nodes = document.getDocumentElement().getElementsByTagName("view");
        Set<URL> urls = new HashSet<>(nodes.getLength());

        for (int i = 0; i < nodes.getLength(); i++) {
            urls.add(getTocURL(nodes.item(i)));
        }

        return urls;
    }

    private static URL getTocURL(Node node) {
        String url = null;
        String type = null;

        NodeList children = node.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) children.item(i);
                switch (element.getTagName()) {
                    case "type":
                        type = element.getTextContent();
                    case "data":
                        url = element.getTextContent();
                }
            }
        }

        if (url != null && type != null && type.equalsIgnoreCase("javax.help.TOCView")) {
            try {
                return new URL(url);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

}
