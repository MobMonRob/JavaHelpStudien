/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help.data;


import javax.swing.tree.DefaultTreeModel;
import java.net.URL;
import java.util.Map;

/**
 *
 * @author Melvin Wulle
 */
public class HelpSet {
    private URL url;
    private String title;
    private Map<String, String> index;
    private Map<String, String> map;
    private DefaultTreeModel toc;

    public HelpSet() {
    }

    public URL getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public Map<String, String> getIndex() {
        return this.index;
    }

    public Map<String, String> getMap() {
        return this.map;
    }

    public DefaultTreeModel getToc() {
        return this.toc;
    }

    void setUrl(URL url) {
        this.url = url;
    }

    void setTitle(String title) {
        this.title = title;
    }

    void setIndex(Map<String, String> index) {
        this.index = index;
    }

    void setMap(Map<String, String> map) {
        this.map = map;
    }

    void setToc(DefaultTreeModel toc) {
        this.toc = toc;
    }

}
