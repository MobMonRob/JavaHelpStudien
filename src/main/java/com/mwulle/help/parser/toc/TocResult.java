/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help.parser.toc;

import javax.swing.tree.DefaultTreeModel;

public class TocResult {
    private DefaultTreeModel tree;

    TocResult() {

    }

    public DefaultTreeModel getTree() {
        return tree;
    }

    void setTree(DefaultTreeModel tree) {
        this.tree = tree;
    }

    public static class TocItem {
        private String text;
        private String helpID;

        public TocItem() {
        }

        public String getText() {
            return text;
        }

        void setText(String text) {
            this.text = text;
        }

        public String getHelpID() {
            return helpID;
        }

        void setHelpID(String helpID) {
            this.helpID = helpID;
        }
    }
}
