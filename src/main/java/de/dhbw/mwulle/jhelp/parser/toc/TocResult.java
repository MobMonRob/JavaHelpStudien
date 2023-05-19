/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package de.dhbw.mwulle.jhelp.parser.toc;

import de.dhbw.mwulle.jhelp.helpset.toc.TOCItemNode;

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
