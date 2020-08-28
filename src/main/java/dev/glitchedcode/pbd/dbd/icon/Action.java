package dev.glitchedcode.pbd.dbd.icon;

import dev.glitchedcode.pbd.dbd.Character;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Used to represent all of the icons in the "Actions" category.
 * <br />
 * Yes, there is only supposed to be 1. Don't ask me why.
 */
public enum Action implements Icon {

    CARRIED_BODY("carriedBody", "Carried Body");

    private final String name;
    private final String subFolder;
    private final String properName;

    @ParametersAreNonnullByDefault
    Action(String name, String properName) {
        this(name, properName, "");
    }

    @ParametersAreNonnullByDefault
    Action(String name, String properName, String subFolder) {
        this.name = name;
        this.properName = properName;
        this.subFolder = subFolder;
    }

    @Nonnull
    @Override
    public IconCategory getCategory() {
        return IconCategory.ACTION;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Nonnull
    @Override
    public String getFileAdditive() {
        return "iconAction_";
    }

    @Nonnull
    @Override
    public String getProperName() {
        return properName;
    }

    @Nonnull
    @Override
    public String getSubfolderName() {
        return "Actions\\" + subFolder;
    }

    @Nullable
    @Override
    public Character getCharacter() {
        return null;
    }
}