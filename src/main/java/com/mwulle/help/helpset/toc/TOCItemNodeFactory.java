package com.mwulle.help.helpset.toc;

import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
public class TOCItemNodeFactory extends ChildFactory<TOCItemNode> {
    List tocItems = new ArrayList();

    public void addTocItem(TOCItemNode tocItemNode) {
        tocItems.add(tocItemNode);
        refresh(true);
    }

    @Override
    protected boolean createKeys(List list) {
        list.addAll(tocItems);
        return true;
    }


    @Override
    protected Node createNodeForKey(TOCItemNode tocItemNode) {
        return tocItemNode;
    }
}
