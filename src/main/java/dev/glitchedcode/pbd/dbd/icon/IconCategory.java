package dev.glitchedcode.pbd.dbd.icon;

import javax.annotation.Nonnull;

public enum IconCategory {

    ACTION("Action"),
    ADDON("Addon"),
    ARCHIVE("Archive"),
    HELP("Help"),
    HELP_LOADING("Help - Loading"),
    ITEM("Item"),
    FAVOR("Favor"),
    PERK("Perk"),
    PORTRAIT("Portrait"),
    POWER("Power"),
    STATUS_EFFECT("Status Effect");

    private final String name;

    IconCategory(@Nonnull String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}