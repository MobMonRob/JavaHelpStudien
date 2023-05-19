/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package de.dhbw.mwulle.jhelp.parser.map;

import java.util.Map;

public class MapResult {
    private Map<String, String> map;

    MapResult() {

    }

    public Map<String, String> getMap() {
        return map;
    }

    void setMap(Map<String, String> map) {
        this.map = map;
    }
}
