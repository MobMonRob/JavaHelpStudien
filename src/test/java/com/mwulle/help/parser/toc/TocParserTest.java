package com.mwulle.help.parser.toc;

import com.mwulle.help.parser.Input;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TocParserTest {

    @Test
    void parse() throws MalformedURLException {
        Input input = Mockito.mock(Input.class);

        Mockito.when(input.getUrl()).thenReturn(new URL("file://toc.xml"));
        Mockito.when(input.getInputStream()).thenReturn(new ByteArrayInputStream(toc().getBytes()));

        Optional<TocResult> optionalResult = TocParser.parse(input);

        if (optionalResult.isPresent()){
            TocResult result = optionalResult.get();
            TocResult expected = expected();

            //assertEquals(expected.getTree(), result.getTree());
        } else {
            fail("Parsing failed and no result is present.");
        }
    }

    private static TocResult expected() {
        TocResult result = new TocResult();

        TocResult.TocItem item1 = new TocResult.TocItem();
        item1.setHelpID("com.mwulle.DisplayEngine.about");
        item1.setText("About Help");

        TocResult.TocItem item2 = new TocResult.TocItem();
        item2.setText("Test Help");

        DefaultMutableTreeNode child = new DefaultMutableTreeNode(item1);
        DefaultMutableTreeNode parent = new DefaultMutableTreeNode(item2);
        parent.add(child);

        DefaultTreeModel tree = new DefaultTreeModel(parent);

        result.setTree(tree);

        return result;
    }

    private static String toc() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE toc PUBLIC \"-//Sun Microsystems Inc.//DTD JavaHelp TOC Version 2.0//EN\" \"http://java.sun.com/products/javahelp/toc_2_0.dtd\">\n" +
                "<toc version=\"2.0\">\n" +
                "    <tocitem text=\"Test Help\">\n" +
                "        <tocitem text=\"About Help\" target=\"com.mwulle.help.about\"/>\n" +
                "    </tocitem>\n" +
                "</toc>\n";
    }
}