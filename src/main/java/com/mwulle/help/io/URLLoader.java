/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author Melvin Wulle
 */
public class URLLoader {
    public static InputStream inputStreamOf(URL url) {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String stringOf(InputStream inputStream) throws IOException {
        int bufferSize = 1024;
        char[] buffer = new char[bufferSize];
        StringBuilder out = new StringBuilder();
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        for (int numRead; (numRead = reader.read(buffer, 0, buffer.length)) > 0; ) {
            out.append(buffer, 0, numRead);
        }

        return out.toString();
    }
}
