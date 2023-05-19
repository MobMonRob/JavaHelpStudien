/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help;

import com.mwulle.help.ui.HelpTopComponent;
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
public class Help implements HelpCtx.Displayer{
    private static final Help INSTANCE = new Help();

    public Help() {
    }
    
    public static Help getInstance() {
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
