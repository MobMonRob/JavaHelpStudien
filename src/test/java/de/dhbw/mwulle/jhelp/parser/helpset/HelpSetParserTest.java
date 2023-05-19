package de.dhbw.mwulle.jhelp.parser.helpset;

import de.dhbw.mwulle.jhelp.parser.Input;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HelpSetParserTest {

    @Test
    void parse() throws MalformedURLException {
        Input input = Mockito.mock(Input.class);

        Mockito.when(input.getUrl()).thenReturn(new URL("file://hs.xml"));
        Mockito.when(input.getInputStream()).thenReturn(new ByteArrayInputStream(helpSet().getBytes()));

        Optional<HelpSetResult> optionalResult = HelpSetParser.parse(input);

        if (optionalResult.isPresent()){
            HelpSetResult result = optionalResult.get();
            HelpSetResult expected = expected();

            assertEquals(expected.getResource(), result.getResource());
            assertEquals(expected.getTitle(), result.getTitle());
            assertEquals(expected.getMaps(), result.getMaps());
            assertEquals(expected.getViews(), result.getViews());
        } else {
            fail("Parsing failed and no result is present.");
        }
    }

    private static HelpSetResult expected() throws MalformedURLException {
        HelpSetResult result = new HelpSetResult();

        List<String> maps = new ArrayList<>(1);
        maps.add("map.xml");

        List<HelpSetResult.View> views = new ArrayList<>(2);

        HelpSetResult.View toc = new HelpSetResult.View();
        toc.setName("TOC");
        toc.setLabel("Table of Contents");
        toc.setType("javax.help.TOCView");
        toc.setData("toc.xml");

        HelpSetResult.View index = new HelpSetResult.View();
        index.setName("Index");
        index.setLabel("Index");
        index.setType("javax.help.IndexView");
        index.setData("idx.xml");

        views.add(toc);
        views.add(index);

        result.setResource(new URL("file://hs.xml"));
        result.setTitle("Help Title");
        result.setMaps(maps);
        result.setViews(views);

        return result;
    }

    private static String helpSet() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE helpset PUBLIC \"-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 2.0//EN\" \"http://java.sun.com/products/javahelp/helpset_2_0.dtd\">\n" +
                "<helpset version=\"2.0\">\n" +
                "    <title>Help Title</title>\n" +
                "    <maps>\n" +
                "        <homeID>com.mwulle.DisplayEngine.about</homeID>\n" +
                "        <mapref location=\"map.xml\"/>\n" +
                "    </maps>\n" +
                "    <view mergetype=\"javax.help.AppendMerge\">\n" +
                "        <name>TOC</name>\n" +
                "        <label>Table of Contents</label>\n" +
                "        <type>javax.help.TOCView</type>\n" +
                "        <data>toc.xml</data>\n" +
                "    </view>\n" +
                "    <view mergetype=\"javax.help.AppendMerge\">\n" +
                "        <name>Index</name>\n" +
                "        <label>Index</label>\n" +
                "        <type>javax.help.IndexView</type>\n" +
                "        <data>idx.xml</data>\n" +
                "    </view>\n" +
                "</helpset>\n";
    }
}