package dev.glitchedcode.pbd.pack;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.dbd.icon.Icon;
import dev.glitchedcode.pbd.logger.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Used to represent the 'packmeta.json' file stored in all cached icon packs.
 */
public class PackMeta {

    private String name;
    private final Set<String> missingIcons;
    private static final Logger logger = PBD.getLogger();

    @ParametersAreNonnullByDefault
    public PackMeta(String name, Set<String> missingIcons) {
        this.name = name;
        this.missingIcons = missingIcons;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public Set<String> getMissingIcons() {
        return Collections.unmodifiableSet(missingIcons);
    }

    public boolean isMissingIcon(@Nonnull Icon icon) {
        for (String s : missingIcons) {
            if (icon.asFileName().equals(s))
                return true;
        }
        return false;
    }

    void setName(@Nonnull String name) {
        this.name = name;
    }

    void reeval(@Nonnull File folder) {
        assert (folder.isDirectory());
        missingIcons.clear();
        for (Icon icon : PBD.getIcons()) {
            if (icon.asFile(folder).exists())
                continue;
            missingIcons.add(icon.asFileName());
        }
        logger.debug("Re-evaluation of '{}' found {} missing icon(s).", name, missingIcons.size());
    }

    /**
     * Evaluates the given folder and creates
     * a new instance of {@link PackMeta}.
     * <br />
     * Only to be used when a {@code packmeta.json} file does not exist
     * <i>or</i> an icon pack is being updated and re-evaluated.
     *
     * @param folder The folder to evaluate.
     * @return A new instance of {@link PackMeta}
     * or null if an icon pack could not be detected.
     */
    @Nullable
    static PackMeta of(@Nonnull File folder) {
        int i = 0;
        Set<String> missingIcons = new HashSet<>();
        for (Icon icon : PBD.getIcons()) {
            if (icon.asFile(folder).exists())
                i++;
            else
                missingIcons.add(icon.asFileName());
        }
        if (i != 0)
            return new PackMeta(folder.getName(), missingIcons);
        logger.warn("Directory '{}' contained 0 recognizable icons w/ formatted directories and file names.",
                folder.getName());
        logger.info("Tip: Your main folder should contain sub-folders such as: Actions, Perks, Items, etc.");
        return null;
    }

    public static boolean eval(@Nonnull File folder) {
        logger.debug("Evaluating folder '{}' as an icon pack...", folder.getName());
        int i = 0;
        for (Icon icon : PBD.getIcons()) {
            if (icon.asFile(folder).exists())
                i++;
        }
        logger.debug("Folder '{}' is an icon pack: {}, {} icons found.", folder.getName(), i > 0, i);
        return i > 0;
    }
}