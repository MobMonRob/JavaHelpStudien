package de.dhbw.mwulle.jhelp.parser.index;

import de.dhbw.mwulle.jhelp.parser.Input;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class IndexParserTest {

    @Test
    void parse() throws MalformedURLException {
        Input input = Mockito.mock(Input.class);

        Mockito.when(input.getUrl()).thenReturn(new URL("file://idx.xml"));
        Mockito.when(input.getInputStream()).thenReturn(new ByteArrayInputStream(index().getBytes()));

        Optional<IndexResult> optionalResult = IndexParser.parse(input);

        if (optionalResult.isPresent()){
            IndexResult result = optionalResult.get();
            IndexResult expected = expected();

            assertEquals(expected.getIndexes(), result.getIndexes());
        } else {
            fail("Parsing failed and no result is present.");
        }
    }

    private static IndexResult expected() {
        IndexResult result = new IndexResult();

        Map<String, String> indexes = new HashMap<>(2);
        indexes.put("com.mwulle.index.test.1", "Test Index Item 1");
        indexes.put("com.mwulle.index.test.2", "Test Index Item 2");

        result.setIndexes(indexes);

        return result;
    }

    private static String index() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE index PUBLIC \"-//Sun Microsystems Inc.//DTD JavaHelp Index Version 2.0//EN\" \"http://java.sun.com/products/javahelp/index_2_0.dtd\">\n" +
                "<index version=\"2.0\">\n" +
                "    <indexitem text=\"Test Index Item 1\" target=\"com.mwulle.index.test.1\"/>\n" +
                "    <indexitem text=\"Test Index Item 2\" target=\"com.mwulle.index.test.2\"/>\n" +
                "</index>\n";
    }
}