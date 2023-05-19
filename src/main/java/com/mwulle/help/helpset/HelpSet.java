/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help.helpset;


import com.mwulle.help.helpset.toc.TOCItemNode;
import com.mwulle.help.util.Merger;

import java.util.Map;

/**
 *
 * @author Melvin Wulle
 */
public class HelpSet {
    private Map<String, String> index;
    private Map<String, String> map;
    private TOCItemNode toc;

    public HelpSet() {
    }

    public void merge(HelpSet helpSet) {
        this.index = Merger.mergeMaps(this.index, helpSet.getIndex());
        this.map = Merger.mergeMaps(this.map, helpSet.getMap());
        this.toc = Merger.appendTree(this.toc, helpSet.getToc());
    }

    public Map<String, String> getIndex() {
        return this.index;
    }

    public Map<String, String> getMap() {
        return this.map;
    }

    public TOCItemNode getToc() {
        return this.toc;
    }

    void setIndex(Map<String, String> index) {
        this.index = index;
    }

    void setMap(Map<String, String> map) {
        this.map = map;
    }

    void setToc(TOCItemNode toc) {
        this.toc = toc;
    }

}
