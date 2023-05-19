/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package de.dhbw.mwulle.jhelp;

import de.dhbw.mwulle.jhelp.ui.HelpTopComponent;
import org.openide.modules.OnStart;
import org.openide.util.HelpCtx;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.WindowManager;

/**
 * Help. Initialization of the program and incoming requests of help are handled here.
 *
 * @author Melvin Wulle
 */
@ServiceProvider(service = HelpCtx.Displayer.class)
public class JHelp implements HelpCtx.Displayer{
    private static final JHelp INSTANCE = new JHelp();

    public JHelp() {
    }
    
    public static JHelp getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean display(HelpCtx helpCtx) {
        HelpSetManager manager = HelpSetManager.getInstance();
        HelpTopComponent component = (HelpTopComponent) WindowManager.getDefault().findTopComponent("HelpTopComponent");

        String helpID = helpCtx.getHelpID();

        String content = "";
        String contentHeader = "";

        if (manager.containsHelp(helpID)) {
            content = manager.contentOf(helpID);
            contentHeader = manager.indexOf(helpID);
        }

        component.open();
        component.setRootContext(manager.toc());
        component.setContent(content);
        component.setContentHeader(contentHeader);
        return true;
    }

    @OnStart
    public static class Initialization implements Runnable {

        @Override
        public void run() {
            SearchEngine.deleteIndex();
        }
    }

}
