/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package de.dhbw.mwulle.jhelp.util;

import de.dhbw.mwulle.jhelp.helpset.toc.TOCItemNode;
import de.dhbw.mwulle.jhelp.helpset.toc.TOCItemNodeFactory;
import org.openide.nodes.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Merges Data Structures.
 *
 * @author Melvin Wulle
 */
public class Merger {
    public static Map<String,String> mergeMaps(Map<String,String> map1, Map<String, String> map2) {
        Map<String,String> map3 = new HashMap<>(map1);
        map3.putAll(map2);
        return map3;
    }

    public static TOCItemNode appendTree(TOCItemNode node1, TOCItemNode node2) {
        List<Node> nodes = node1.getChildren().snapshot();
        TOCItemNodeFactory factory = new TOCItemNodeFactory();
        for (Node node: nodes) {
            factory.addTocItem((TOCItemNode) node);
        }
        factory.addTocItem(node2);


        return new TOCItemNode(node1.getTocItem(), factory);
    }
}
