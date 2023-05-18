package com.mwulle.help.helpset.toc;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

public class TOCItemNode extends AbstractNode {
    TOCItem tocItem;
    public TOCItemNode(TOCItem tocItem, TOCItemNodeFactory factory) {
        super(Children.create(factory, true));
        setDisplayName(tocItem.text);
    }

    public TOCItemNode(TOCItem tocItem) {
        super(Children.LEAF);
        setDisplayName(tocItem.text);
    }

    public TOCItem getTocItem() {
        return tocItem;
    }
}
