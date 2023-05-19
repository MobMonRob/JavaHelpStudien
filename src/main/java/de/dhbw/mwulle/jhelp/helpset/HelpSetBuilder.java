/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package de.dhbw.mwulle.jhelp.helpset;

import de.dhbw.mwulle.jhelp.helpset.toc.TOCItemNode;
import de.dhbw.mwulle.jhelp.util.Merger;

import java.util.Map;

public class HelpSetBuilder {
    private HelpSet helpSet;

    public HelpSetBuilder() {
        reset();
    }

    public void reset() {
        helpSet = new HelpSet();
    }

    public void setIndex(Map<String, String> index) {
        if (helpSet.getIndex() != null){
            helpSet.setIndex(Merger.mergeMaps(helpSet.getIndex(), index));
        } else {
            helpSet.setMap(index);
        }
    }

    public void setMap(Map<String, String> map) {
        if (helpSet.getMap() != null){
            helpSet.setMap(Merger.mergeMaps(helpSet.getMap(), map));
        } else {
            helpSet.setMap(map);
        }
    }

    public void setToc(TOCItemNode toc) {
        if (helpSet.getToc() != null){
            helpSet.setToc(Merger.appendTree(helpSet.getToc(), toc));
        } else {
            helpSet.setToc(toc);
        }
    }

    public HelpSet build() {
        return helpSet;
    }
}
