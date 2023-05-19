/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package de.dhbw.mwulle.jhelp.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Input {
    private URL url;
    public Input(URL url) {
        this.url = url;
    }

    public Input(URL url, String reference) throws MalformedURLException {
        this.url = new URL(url, reference);
    }

    public URL getUrl() {
        return url;
    }

    public InputStream getInputStream() {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
