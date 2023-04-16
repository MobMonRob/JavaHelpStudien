/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help;

import com.mwulle.help.integration.HelpFrame;
import org.openide.util.HelpCtx;
import org.openide.util.lookup.ServiceProvider;

import javax.annotation.PostConstruct;

/**
 * @author Melvin Wulle
 */
@ServiceProvider(service=Help.class)
public class Help {
    private HelpSetManager manager = new HelpSetManager();
    private HelpFrame helpFrame = new HelpFrame();

    public Help() {
    }

    @PostConstruct
    private void init() {
        manager.scan();
        helpFrame.setTOCTree(manager.mergedToc());
    }

    public HelpFrame display(HelpCtx helpCtx) {
        String helpID = helpCtx.getHelpID();
        String content = manager.contentOf(helpID);
        String index = manager.indexOf(helpID);
        helpFrame.setContent(content);
        helpFrame.setContentHeader(index);
        helpFrame.setTOCTree(manager.mergedToc());

        return helpFrame;
    }
    
}
