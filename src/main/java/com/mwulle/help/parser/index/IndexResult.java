/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help.parser.index;

import java.util.Map;

public class IndexResult {
    private Map<String, String> indexes;

    IndexResult() {

    }

    public Map<String, String> getIndexes() {
        return indexes;
    }

    void setIndexes(Map<String, String> indexes) {
        this.indexes = indexes;
    }
}
