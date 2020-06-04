package dev.glitchedcode.pbd.dbd;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public enum Action implements Icon {

    CARRIED_BODY("carriedBody", "Carried Body");

    private final String name;
    private final String subFolder;
    private final String properName;
    public static final Action[] VALUES = values();

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

    @Nullable
    public static Action fromProperName(@Nonnull String properName) {
        for (Action action : VALUES) {
            if (action.getProperName().equalsIgnoreCase(properName))
                return action;
        }
        return null;
    }
}