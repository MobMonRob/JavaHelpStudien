/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help;

import com.mwulle.help.data.HelpSet;
import com.mwulle.help.data.HelpSetBuilder;
import com.mwulle.help.data.Merger;
import com.mwulle.help.io.Parser;
import com.mwulle.help.io.URLLoader;
import org.openide.filesystems.*;
import org.w3c.dom.Document;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Melvin Wulle
 */
public class HelpSetManager {
    private Set<HelpSet> helpSets;

    public HelpSetManager () {
    }

    public TreeModel mergedToc() {
        DefaultTreeModel tree = null;
        for (Iterator<HelpSet> it = helpSets.iterator(); it.hasNext(); ) {
            HelpSet helpSet = it.next();
            if (tree == null) {
                tree = helpSet.getToc();
            } else {
                Merger.merge(tree, helpSet.getToc());
            }
        }

        return tree;
    }

    public String contentOf(String helpID) {
        for (HelpSet helpSet: helpSets) {
            if (helpSet.getMap().containsKey(helpID)) {
                return helpSet.getMap().get(helpID);
            }
        }
        return null;
    }

    public String indexOf(String helpID) {
        for (HelpSet helpSet: helpSets) {
            if (helpSet.getIndex().containsKey(helpID)) {
                return helpSet.getIndex().get(helpID);
            }
        }
        return null;
    }

    void scan() {
        helpSets = new HashSet<>();
        FileObject configFile =  FileUtil.getConfigFile("Services/JavaHelp");
        try {
            configFile.getFileSystem().addFileChangeListener(new HelpConfigChangeListener());
            for (FileObject helpSetFiles: configFile.getChildren()) {
                addHelpSet(new URL(helpSetFiles.getAttribute("url").toString()));
            }
        } catch (FileStateInvalidException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addHelpSet(URL url) {
        HelpSetBuilder builder = new HelpSetBuilder();

        Document document = getDocumentOf(url);
        Set<Document> indexDocuments = getDocumentsOf(Parser.getIndexURLs(document));
        Set<Document> mapDocuments = getDocumentsOf(Parser.getMapURLs(document));
        Set<Document> tocDocuments = getDocumentsOf(Parser.getTocURLs(document));

        builder.setURL(url);
        builder.setTitle(Parser.getTitle(document));

        for (Document indexDocument: indexDocuments) {
            builder.setIndex(Parser.getIndex(indexDocument));
        }
        for (Document mapDocument: mapDocuments) {
            builder.setMap(Parser.getMap(mapDocument));
        }
        for (Document tocDocument: tocDocuments) {
            builder.setToc(Parser.getToc(tocDocument));
        }

        HelpSet helpSet = builder.build();
        helpSets.add(helpSet);
    }

    private Document getDocumentOf(URL url) {
        try (InputStream inputStream = URLLoader.inputStreamOf(url)){
            return Parser.documentOf(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<Document> getDocumentsOf(Set<URL> urls) {
        Set<Document> documents = new HashSet<>(urls.size());
        for (URL url: urls) {
            try (InputStream inputStream = URLLoader.inputStreamOf(url)){
                documents.add(Parser.documentOf(inputStream));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return documents;
    }


    private class HelpConfigChangeListener implements FileChangeListener {
        public HelpConfigChangeListener() {
        }

        @Override
        public void fileFolderCreated(FileEvent fileEvent) {
            SearchEngine.deleteIndex();
            scan();
            helpSets.forEach(SearchEngine::index);
        }

        @Override
        public void fileDataCreated(FileEvent fileEvent) {
            SearchEngine.deleteIndex();
            scan();
            helpSets.forEach(SearchEngine::index);
        }

        @Override
        public void fileChanged(FileEvent fileEvent) {
            SearchEngine.deleteIndex();
            scan();
            helpSets.forEach(SearchEngine::index);
        }

        @Override
        public void fileDeleted(FileEvent fileEvent) {
            SearchEngine.deleteIndex();
            scan();
            helpSets.forEach(SearchEngine::index);
        }

        @Override
        public void fileRenamed(FileRenameEvent fileEvent) {
            SearchEngine.deleteIndex();
            scan();
            helpSets.forEach(SearchEngine::index);
        }

        @Override
        public void fileAttributeChanged(FileAttributeEvent fileEvent) {
            SearchEngine.deleteIndex();
            scan();
            helpSets.forEach(SearchEngine::index);
        }
    }
    
}
