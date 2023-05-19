/*
 * Copyright (c) 2023. Melvin Wulle
 * All rights reserved.
 */
package com.mwulle.help.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.mwulle.help.Help;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

import javax.swing.FocusManager;

@ActionID(
        category = "Edit",
        id = "com.mwulle.help.ui.Shortcut"
)
@ActionRegistration(
        iconBase = "com/mwulle/help/ui/help-16x16.png",
        displayName = "#CTL_Shortcut"
)
@ActionReferences({
    @ActionReference(path = "Menu/Help", position = 100, separatorAfter = 150),
    @ActionReference(path = "Shortcuts", name = "F1")
})
@Messages("CTL_Shortcut=Get Help")
public final class Shortcut implements ActionListener {

    private HelpCtx getHelpCtx() {
        Component focusOwner = FocusManager.getCurrentManager().getFocusOwner();
        HelpCtx helpCtx = HelpCtx.findHelp(focusOwner);
        if (helpCtx != null) {
            return helpCtx;
        }
        return HelpCtx.DEFAULT_HELP ;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Help help = Help.getInstance();
        help.display(getHelpCtx());
    }
}
