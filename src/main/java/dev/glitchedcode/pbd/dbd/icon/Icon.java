package dev.glitchedcode.pbd.dbd.icon;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.dbd.Character;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

public interface Icon {

    /**
     * Gets the category of the {@link Icon}.
     *
     * @return The category of the {@link Icon}.
     */
    @Nonnull
    IconCategory getCategory();

    /**
     * Gets the file name of the {@link Icon}.
     *
     * @return The file name of the {@link Icon}.
     */
    @Nonnull
    String getName();

    /**
     * Gets the prefix for the {@link Icon}.
     * <br />
     * e.g. "iconPerks_NAMEHERE"
     *
     * @return The prefix for the {@link Icon}.
     */
    @Nonnull
    String getFileAdditive();

    /**
     * Gets a properly formatted name of the {@link Icon}.
     *
     * @return A properly formatted name of the {@link Icon}.
     */
    @Nonnull
    String getProperName();

    /**
     * Gets whether the {@link #getFileAdditive()} is a prefix.
     *
     * @return True if the {@link #getFileAdditive()} is a prefix, false if a suffix.
     */
    default boolean isPrefix() {
        return true;
    }

    /**
     * Gets the {@link Icon} as a file name.
     *
     * @return The {@link Icon} as a file name.
     */
    @Nonnull
    default String asFileName() {
        return (isPrefix() ? (getFileAdditive() + getName()) : (getName() + getFileAdditive())) + ".png";
    }

    /**
     * Gets the subfolder the {@link Icon} is located in.
     * <br />
     * This is based off of already being in the "Icons" folder for DBD.
     *
     * @return The subfolder the {@link Icon} is located in.
     */
    @Nonnull
    String getSubfolderName();

    /**
     * Takes the given folder and gets the file for the relevant icon.
     *
     * @param folder The folder.
     * @return The file for the relevant icon.
     * @throws IllegalArgumentException Thrown if the given folder is not a directory.
     */
    @Nonnull
    default File asFile(@Nonnull File folder) {
        if (!folder.isDirectory())
            throw new IllegalArgumentException(PBD.format("The given file ({}) is not a directory.",
                    folder.getAbsolutePath()));
        String sub = getSubfolderName();
        return new File(folder, (sub.isEmpty() ? "" : sub + File.separator) + asFileName());
    }

    /**
     * Checks if the {@link Icon} is {@link Character}-specific.
     *
     * @return True if the {@link Icon} is {@link Character}-specific.
     */
    default boolean hasCharacter() {
        return getCharacter() != null;
    }

    /**
     * Gets the {@link Character} the {@link Icon} belongs to.
     *
     * @return The {@link Character} the {@link Icon} belongs to or null if nonexistent.
     */
    @Nullable
    Character getCharacter();
}