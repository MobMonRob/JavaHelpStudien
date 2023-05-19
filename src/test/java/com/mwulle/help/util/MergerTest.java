package com.mwulle.help.util;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MergerTest {

    @Test
    void merge() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("Key 1", "Value 1");
        map1.put("Key 2", "Value 2");
        map1.put("Key 3", "Value 3");

        Map<String, String> map2 = new HashMap<>();
        map2.put("Key 1", "Value 1");
        map2.put("Key 4", "Value 4");
        map2.put("Key 5", "Value 5");

        Map<String, String> mergedMap = new HashMap<>();
        mergedMap.put("Key 1", "Value 1");
        mergedMap.put("Key 2", "Value 2");
        mergedMap.put("Key 3", "Value 3");
        mergedMap.put("Key 4", "Value 4");
        mergedMap.put("Key 5", "Value 5");

        assertEquals(mergedMap, Merger.mergeMaps(map1, map2));
    }
}