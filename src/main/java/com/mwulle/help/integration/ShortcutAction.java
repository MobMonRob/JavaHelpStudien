/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help.integration;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.mwulle.help.Help;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.HelpCtx;

/**
 *
 * @author Melvin Wulle
 */
@ActionID(category="Help", id="com.mwulle.help.integration.ShortcutAction")
@ActionReference(path="Shortcuts", name="F1")
public class ShortcutAction extends AbstractAction{
    
    ShortcutAction() {
    }
    
    private HelpCtx getHelpCtx() {
        Component focusOwner = FocusManager.getCurrentManager().getFocusOwner();
        HelpCtx helpCtx = HelpCtx.findHelp(focusOwner);
        if (helpCtx != null) {
            return helpCtx;
        }
        return HelpCtx.DEFAULT_HELP ;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        new Help().display(getHelpCtx());
    }
    
}
