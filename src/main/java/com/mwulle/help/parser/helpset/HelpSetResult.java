/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help.parser.helpset;

import java.net.URL;
import java.util.List;

public class HelpSetResult {
    private String title;
    private URL resource;
    private List<String> maps;
    private List<View> views;

    HelpSetResult() {
    }

    public String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    public URL getResource() {
        return resource;
    }

    void setResource(URL resource) {
        this.resource = resource;
    }

    public List<String> getMaps() {
        return maps;
    }

    void setMaps(List<String> maps) {
        this.maps = maps;
    }

    public List<View> getViews() {
        return views;
    }

    void setViews(List<View> views) {
        this.views = views;
    }


    public static class View {
        private String name;
        private String label;
        private String type;
        private String data;

        public View() {

        }

        public String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        public String getLabel() {
            return label;
        }

        void setLabel(String label) {
            this.label = label;
        }

        public String getType() {
            return type;
        }

        void setType(String type) {
            this.type = type;
        }

        public String getData() {
            return data;
        }

        void setData(String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "{name: " + name + ", label: " + label + ", type: " + type + ", data: " + data + "}";
        }

        @Override
        public boolean equals(Object object) {
            if (object == null) {
                return false;
            }
            if (!object.getClass().equals(View.class)) {
                return false;
            } else {
                View view = (View) object;
                if (!view.name.equals(name)){
                    return false;
                }
                if (!view.label.equals(label)){
                    return false;
                }
                if (!view.type.equals(type)){
                    return false;
                }
                if (!view.data.equals(data)){
                    return false;
                }
                return true;
            }
        }
    }
}
