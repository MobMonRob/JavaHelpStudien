package com.mwulle.help.helpset.toc;

public class TOCItem {
    String text;
    String helpID;

    public TOCItem() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHelpID() {
        return helpID;
    }

    public void setHelpID(String helpID) {
        this.helpID = helpID;
    }
}
