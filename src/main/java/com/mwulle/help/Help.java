/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help;

import com.mwulle.help.ui.HelpTopComponent;
import org.openide.modules.OnStart;
import org.openide.util.HelpCtx;
import org.openide.windows.WindowManager;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * Help. Initialization of the program and incoming requests of help are handled here.
 *
 * @author Melvin Wulle
 */
public class Help {
    private static final Help INSTANCE = new Help();

    private Help() {
    }
    
    public static Help getInstance() {
        return INSTANCE;
    }

    public void display(HelpCtx helpCtx) {
        HelpSetManager manager = HelpSetManager.getInstance();
        HelpTopComponent component = (HelpTopComponent) WindowManager.getDefault().findTopComponent("HelpTopComponent");

        String helpID = helpCtx.getHelpID();

        String content = "";
        String contentHeader = "";

        if (manager.containsHelp(helpID)) {
            content = manager.contentOf(helpID);
            contentHeader = manager.indexOf(helpID);
        }

        if (!component.isOpened()){
            component.open();
        }

        component.setRootContext(manager.toc());
        component.setContent(content);
        component.setContentHeader(contentHeader);
    }

    @OnStart
    public static class Initialization implements Runnable {

        @Override
        public void run() {
            SearchEngine.deleteIndex();
        }
    }

}
