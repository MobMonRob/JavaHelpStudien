/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package de.dhbw.mwulle.jhelp;

import de.dhbw.mwulle.jhelp.helpset.HelpSet;
import de.dhbw.mwulle.jhelp.helpset.HelpSetBuilder;
import de.dhbw.mwulle.jhelp.helpset.toc.TOCItem;
import de.dhbw.mwulle.jhelp.helpset.toc.TOCItemNode;
import de.dhbw.mwulle.jhelp.parser.Input;
import de.dhbw.mwulle.jhelp.parser.helpset.HelpSetParser;
import de.dhbw.mwulle.jhelp.parser.helpset.HelpSetResult;
import de.dhbw.mwulle.jhelp.parser.index.IndexParser;
import de.dhbw.mwulle.jhelp.parser.index.IndexResult;
import de.dhbw.mwulle.jhelp.parser.map.MapParser;
import de.dhbw.mwulle.jhelp.parser.map.MapResult;
import de.dhbw.mwulle.jhelp.parser.toc.TocParser;
import de.dhbw.mwulle.jhelp.parser.toc.TocResult;
import de.dhbw.mwulle.jhelp.util.Merger;
import org.openide.filesystems.*;
import org.openide.util.HelpCtx;

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
    private HelpSet master;

    private HelpSetManager () {
        master = master();
    }

    private static HelpSet master() {
        HelpSetBuilder builder = new HelpSetBuilder();

        TOCItem tocItem = new TOCItem();
        tocItem.setText("JHelp");
        tocItem.setHelpID(HelpCtx.DEFAULT_HELP.getHelpID());
        builder.setToc(new TOCItemNode(tocItem));

        Map<String, String> map = new HashMap<>(1);
        map.put(HelpCtx.DEFAULT_HELP.getHelpID(), "<a href=\"https://github.com/MobMonRob/JavaHelpStudien\">JHelp</a>");
        builder.setMap(map);

        return builder.build();
    }

    public static HelpSetManager getInstance() {
        return manager;
    }
    
    public boolean containsHelp(String helpID) {
        return contentOf(helpID) != null && indexOf(helpID) != null;
    }

    public TOCItemNode toc() {
        return master.getToc();
    }

    public String contentOf(String helpID) {
        return master.getMap().get(helpID);
    }

    public String indexOf(String helpID) {
        return master.getIndex().get(helpID);
    }

    private void scan() {
        try {
            FileObject folder =  FileUtil.getConfigRoot().getFileSystem().findResource("Services/JavaHelp");
            if (folder != null) {
                folder.getFileSystem().addFileChangeListener(new HelpConfigChangeListener());
                for (FileObject helpSetFiles: folder.getChildren()) {
                    loadHelpSet(helpSetFiles.toURL());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadHelpSet(URL url) {
        HelpSetBuilder builder = new HelpSetBuilder();

        Optional<HelpSetResult> optionalHelpSetResult = HelpSetParser.parse(new Input(url));

        if (optionalHelpSetResult.isPresent()) {
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

            TOCItem root = new TOCItem();
            root.setText(helpSetResult.getTitle());
            TOCItemNode toc = new TOCItemNode(root);
            for (HelpSetResult.View view: views) {
                if (view.getType().equals("javax.help.TOCView")) {
                    try {
                        Optional<TocResult> optionalTocResult = TocParser.parse(new Input(url, view.getData()));
                        assert optionalTocResult.isPresent();
                        Merger.appendTree(toc, optionalTocResult.get().getTree());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }

            builder.setIndex(index);
            builder.setMap(map);
            builder.setToc(toc);

            master.merge(builder.build());
        }
    }

    private class HelpConfigChangeListener implements FileChangeListener {
        public HelpConfigChangeListener() {
        }

        @Override
        public void fileFolderCreated(FileEvent fileEvent) {
            SearchEngine.deleteIndex();
            scan();
            SearchEngine.index(master);
        }

        @Override
        public void fileDataCreated(FileEvent fileEvent) {
            SearchEngine.deleteIndex();
            scan();
            SearchEngine.index(master);
        }

        @Override
        public void fileChanged(FileEvent fileEvent) {
            SearchEngine.deleteIndex();
            scan();
            SearchEngine.index(master);
        }

        @Override
        public void fileDeleted(FileEvent fileEvent) {
            SearchEngine.deleteIndex();
            scan();
            SearchEngine.index(master);
        }

        @Override
        public void fileRenamed(FileRenameEvent fileEvent) {
            SearchEngine.deleteIndex();
            scan();
            SearchEngine.index(master);
        }

        @Override
        public void fileAttributeChanged(FileAttributeEvent fileEvent) {
            SearchEngine.deleteIndex();
            scan();
            SearchEngine.index(master);
        }
    }

}
