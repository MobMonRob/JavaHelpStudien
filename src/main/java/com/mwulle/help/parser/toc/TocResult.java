/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help.parser.toc;

import com.mwulle.help.helpset.toc.TOCItemNode;

import javax.swing.tree.DefaultTreeModel;

public class TocResult {
    private TOCItemNode tree;

    TocResult() {

    }

    public TOCItemNode getTree() {
        return tree;
    }

    void setTree(TOCItemNode tree) {
        this.tree = tree;
    }

}
