package dev.glitchedcode.pbd.dbd.icon;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum IconCategory {

    ACTION("Actions"),
    ADDON("Addons", "ItemAddons"),
    ARCHIVE("Archive"),
    HELP("Help"),
    HELP_LOADING("Help - Loading", "HelpLoading"),
    ITEM("Items"),
    FAVOR("Offerings", "Favors"),
    PERK("Perks"),
    PORTRAIT("Portraits", "CharPortraits"),
    POWER("Powers"),
    STATUS_EFFECT("Status Effect", "StatusEffects");

    private final String name;
    private final String folderName;
    public static final List<String> FOLDER_NAMES;

    static {
        List<String> list = new ArrayList<>();
        for (IconCategory ic : values()) {
            list.add(ic.getFolderName());
        }
        FOLDER_NAMES = Collections.unmodifiableList(list);
    }

    IconCategory(@Nonnull String name) {
        this(name, name);
    }

    IconCategory(@Nonnull String name, @Nonnull String folderName) {
        this.name = name;
        this.folderName = folderName;
    }

    public String getName() {
        return name;
    }

    public String getFolderName() {
        return folderName;
    }
}