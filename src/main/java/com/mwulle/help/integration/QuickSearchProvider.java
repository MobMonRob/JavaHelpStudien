/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help.integration;

import org.netbeans.spi.quicksearch.SearchProvider;
import org.netbeans.spi.quicksearch.SearchRequest;
import org.netbeans.spi.quicksearch.SearchResponse;

/**
 *
 * @author Melvin Wulle
 */
public class QuickSearchProvider implements SearchProvider {

    @Override
    public void evaluate(SearchRequest request, SearchResponse response) {
        request.getText();
    }
}
