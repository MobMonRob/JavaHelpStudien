/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help;

import com.mwulle.help.helpset.HelpSet;
import com.mwulle.help.helpset.HelpSetBuilder;
import com.mwulle.help.parser.Input;
import com.mwulle.help.parser.helpset.HelpSetParser;
import com.mwulle.help.parser.helpset.HelpSetResult;
import com.mwulle.help.parser.index.IndexParser;
import com.mwulle.help.parser.index.IndexResult;
import com.mwulle.help.parser.map.MapParser;
import com.mwulle.help.parser.map.MapResult;
import com.mwulle.help.parser.toc.TocParser;
import com.mwulle.help.parser.toc.TocResult;
import com.mwulle.help.util.Merger;
import org.openide.filesystems.*;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Manages all detected HelpSets.
 *
 * @author Melvin Wulle
 */
public class HelpSetManager {
    private static final HelpSetManager manager = new HelpSetManager();
    private Set<HelpSet> helpSets = new HashSet<>(0);

    private HelpSetManager () {
    }

    public static HelpSetManager getInstance() {
        return manager;
    }
    
    public boolean containsHelp(String helpID) {
        scan();
        return contentOf(helpID) != null && indexOf(helpID) != null;
    }

    public TreeModel mergedToc() {
        scan();
        DefaultTreeModel tree = null;
        for (HelpSet helpSet : helpSets) {
            if (tree == null) {
                tree = helpSet.getToc();
            } else {
                Merger.mergeTrees(tree, helpSet.getToc());
            }
        }

        return tree;
    }

    public String contentOf(String helpID) {
        scan();
        for (HelpSet helpSet: helpSets) {
            if (helpSet.getMap().containsKey(helpID)) {
                return helpSet.getMap().get(helpID);
            }
        }
        return null;
    }

    public String indexOf(String helpID) {
        scan();
        for (HelpSet helpSet: helpSets) {
            if (helpSet.getIndex().containsKey(helpID)) {
                return helpSet.getIndex().get(helpID);
            }
        }
        return null;
    }

    private void scan() {
        helpSets = new HashSet<>();
        try {
            FileObject services = FileUtil.getConfigFile("Services");
            System.out.println(Arrays.toString(services.getChildren()));
            FileObject javahelp = services.getFileObject("JavaHelp");
            System.out.println(javahelp);
            FileObject test = FileUtil.getConfigFile("Services/JavaHelp");
            System.out.println(test);




            FileObject folder =  FileUtil.getConfigRoot().getFileSystem().findResource("Services/JavaHelp");
            if (folder != null) {
                folder.getFileSystem().addFileChangeListener(new HelpConfigChangeListener());
                for (FileObject helpSetFiles: folder.getChildren()) {
                    addHelpSet(helpSetFiles.toURL());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addHelpSet(URL url) {
        HelpSetBuilder builder = new HelpSetBuilder();

        Optional<HelpSetResult> optionalHelpSetResult = HelpSetParser.parse(new Input(url));

        assert optionalHelpSetResult.isPresent();
        HelpSetResult helpSetResult = optionalHelpSetResult.get();

        List<String> maps = helpSetResult.getMaps();
        List<HelpSetResult.View> views = helpSetResult.getViews();

        Map<String, String> map = new HashMap<>();
        for (String mapReference: maps) {
            try {
                Optional<MapResult> optionalMapResult = MapParser.parse(new Input(url, mapReference));
                assert optionalMapResult.isPresent();
                map = Merger.mergeMaps(map, optionalMapResult.get().getMap());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        Map<String, String> index = new HashMap<>();
        for (HelpSetResult.View view: views) {
            if (view.getType().equals("javax.help.IndexView")) {
                try {
                    Optional<IndexResult> optionalIndexResult = IndexParser.parse(new Input(url, view.getData()));
                    assert optionalIndexResult.isPresent();
                    index = Merger.mergeMaps(index, optionalIndexResult.get().getIndexes());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        DefaultTreeModel toc = null;
        for (HelpSetResult.View view: views) {
            if (view.getType().equals("javax.help.TOCView")) {
                try {
                    Optional<TocResult> optionalTocResult = TocParser.parse(new Input(url, view.getData()));
                    assert optionalTocResult.isPresent();
                    if (toc != null) {
                        toc = Merger.mergeTrees(toc, optionalTocResult.get().getTree());
                    } else {
                        toc = optionalTocResult.get().getTree();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        builder.setTitle(helpSetResult.getTitle());
        builder.setURL(url);
        builder.setIndex(index);
        builder.setMap(map);
        builder.setToc(toc);

        HelpSet helpSet = builder.build();
        helpSets.add(helpSet);
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
