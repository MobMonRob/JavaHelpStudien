/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help;

import com.mwulle.help.helpset.HelpSet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fulltext search for all HelpSets build on Apache Lucene.
 *
 * @author Melvin Wulle
 */
public class SearchEngine {
    private static final Path INDEX_DIRECTORY = Paths.get(".help_index");

    public synchronized static void index(HelpSet helpSet) {
        for (String helpID: helpSet.getIndex().keySet()) {
            String title = helpSet.getIndex().get(helpID);
            String content = helpSet.getMap().get(helpID);
            Document document = createIndexDocumentOf(helpID, title, content);
            indexContentOf(document);
        }
    }

    public static List<String> searchFor(String searchQuery) {
        Map<Float, String> helpIDs = new HashMap<>();

        try {
            IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(INDEX_DIRECTORY)));
            TopDocs matches = searcher.search(buildQuery(searchQuery), 100);

            if (matches.scoreDocs != null) {
                for (ScoreDoc scoreDoc : matches.scoreDocs) {
                    Document document = searcher.storedFields().document(scoreDoc.doc);
                    helpIDs.put(scoreDoc.score, document.getField("HELP_ID").stringValue());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> result = new ArrayList<>(helpIDs.size());

        helpIDs.keySet().stream().sorted().forEach(score -> result.add(helpIDs.get(score)));

        return result;
    }

    public static synchronized void deleteIndex() {
        try (IndexWriter writer = new IndexWriter(FSDirectory.open(INDEX_DIRECTORY), new IndexWriterConfig())) {
            writer.deleteAll();
            writer.flush();
            writer.commit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Document createIndexDocumentOf(String helpID, String title, String content) {
        Document result = new Document();

        IndexableField helpIDField = new StringField("HELP_ID", helpID, Field.Store.YES);
        IndexableField titleField = new StringField("TITLE", title, Field.Store.YES);
        IndexableField contentField = new StringField("CONTENT", content, Field.Store.YES);

        result.add(helpIDField);
        result.add(titleField);
        result.add(contentField);

        return result;
    }

    private static void indexContentOf(Document document) {
        try (IndexWriter writer = new IndexWriter(FSDirectory.open(INDEX_DIRECTORY), new IndexWriterConfig())) {
            writer.addDocument(document);
            writer.flush();
            writer.commit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static BooleanQuery buildQuery(String searchQuery) {
        QueryBuilder queryBuilder = new QueryBuilder(new StandardAnalyzer());
        Query helpIDQuery = queryBuilder.createPhraseQuery("HELP_ID", searchQuery);
        Query titleQuery = queryBuilder.createPhraseQuery("TITLE", searchQuery);
        Query contentQuery = queryBuilder.createPhraseQuery("CONTENT", searchQuery);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        builder.add(helpIDQuery, BooleanClause.Occur.SHOULD);
        builder.add(titleQuery, BooleanClause.Occur.SHOULD);
        builder.add(contentQuery, BooleanClause.Occur.SHOULD);

        return builder.build();
    }

}
