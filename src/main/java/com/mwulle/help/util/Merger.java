/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help.util;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.HashMap;
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

    public static DefaultTreeModel mergeTrees(DefaultTreeModel model1, DefaultTreeModel model2) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        root.add((DefaultMutableTreeNode) model1.getRoot());
        root.add((DefaultMutableTreeNode) model2.getRoot());

        return new DefaultTreeModel(root);
    }
}
