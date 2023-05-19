package de.dhbw.mwulle.jhelp.parser.map;

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

class MapParserTest {

    @Test
    void parse() throws MalformedURLException {
        Input input = Mockito.mock(Input.class);

        Mockito.when(input.getUrl()).thenReturn(new URL("file://map.xml"));
        Mockito.when(input.getInputStream()).thenReturn(new ByteArrayInputStream(map().getBytes()));

        Optional<MapResult> optionalResult = MapParser.parse(input);

        if (optionalResult.isPresent()){
            MapResult result = optionalResult.get();
            MapResult expected = expected();

            assertEquals(expected.getMap(), result.getMap());
        } else {
            fail("Parsing failed and no result is present.");
        }
    }

    private static MapResult expected() {
        MapResult result = new MapResult();

        Map<String, String> map = new HashMap<>();
        map.put("com.mwulle.index.test.1", "test1.html");
        map.put("com.mwulle.index.test.2", "test2.html");

        result.setMap(map);

        return result;
    }

    private static String map() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE map PUBLIC \"-//Sun Microsystems Inc.//DTD JavaHelp Map Version 2.0//EN\" \"http://java.sun.com/products/javahelp/map_2_0.dtd\">\n" +
                "<map version=\"2.0\">\n" +
                "    <mapID target=\"com.mwulle.index.test.1\" url=\"test1.html\"/>\n" +
                "    <mapID target=\"com.mwulle.index.test.2\" url=\"test2.html\"/>\n" +
                "</map>\n";
    }
}