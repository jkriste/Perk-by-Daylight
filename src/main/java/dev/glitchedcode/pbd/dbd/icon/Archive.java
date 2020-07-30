package dev.glitchedcode.pbd.dbd.icon;

import dev.glitchedcode.pbd.dbd.Character;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static dev.glitchedcode.pbd.dbd.Killer.*;
import static dev.glitchedcode.pbd.dbd.Survivor.*;

public enum Archive implements Icon {

    KILLER("killer", "Killer"),
    KILLER_TRAPPER("killerTrapper", "Trapper", TRAPPER),
    SHARED("shared", "Shared"),
    SURVIVOR("survivor", "Survivor"),
    SURVIVOR_CLAUDETTE("survivorClaudette", "Claudette", CLAUDETTE),
    KILLER_DO("killer_DO", "Doctor", "Togo", DOCTOR),
    KILLER_HK("killer_HK", "Spirit", "Togo", SPIRIT),
    SURVIVOR_DK("survivor_DK", "David", "Togo", DAVID),
    SURVIVOR_MS("survivor_MS", "Jane", "Togo", JANE),
    KILLER_BE("killer_BE", "Huntress", "Vietnam", HUNTRESS),
    KILLER_KK("killer_KK", "Legion", "Vietnam", LEGION),
    SURVIVOR_DF("survivor_DF", "Dwight", "Vietnam", DWIGHT),
    SURVIVOR_GS("survivor_GS", "Kate", "Vietnam", KATE),
    KILLER_TW("killer_TW", "Wraith", "Xipre", WRAITH),
    KILLER_WI("killer_WI", "Hag", "Xipre", HAG),
    SURVIVOR_AV("survivor_AV", "Ace", "Xipre", ACE),
    SURVIVOR_MT("survivor_MT", "Meg", "Xipre", MEG);

    private final String name;
    private final String subFolder;
    private final String properName;
    private final Character character;

    @ParametersAreNonnullByDefault
    Archive(String name, String properName) {
        this(name, properName, "", null);
    }

    @ParametersAreNonnullByDefault
    Archive(String name, String properName, String subFolder) {
        this(name, properName, subFolder, null);
    }

    @ParametersAreNonnullByDefault
    Archive(String name, String properName, @Nullable Character character) {
        this(name, properName, "", character);
    }

    @ParametersAreNonnullByDefault
    Archive(String name, String properName, String subFolder, @Nullable Character character) {
        this.name = name;
        this.subFolder = subFolder;
        this.properName = properName;
        this.character = character;
    }

    @Nonnull
    @Override
    public IconCategory getCategory() {
        return IconCategory.ARCHIVE;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Nonnull
    @Override
    public String getFileAdditive() {
        return "questIcons_";
    }

    @Nonnull
    @Override
    public String getProperName() {
        return properName;
    }

    @Nonnull
    @Override
    public String getSubfolderName() {
        return "Archive\\" + subFolder;
    }

    @Nullable
    @Override
    public Character getCharacter() {
        return character;
    }
}
