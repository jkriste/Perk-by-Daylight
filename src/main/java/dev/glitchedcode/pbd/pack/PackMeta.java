package dev.glitchedcode.pbd.pack;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.dbd.Icon;
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
 * Saves time & processing instead of having to re-eval each icon pack at runtime.
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

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public boolean isMissingIcon(@Nonnull Icon icon) {
        for (String s : missingIcons) {
            if (icon.getName().equalsIgnoreCase(s))
                return true;
        }
        return false;
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
    static PackMeta eval(@Nonnull File folder) {
        int i = 0;
        Set<String> missingIcons = new HashSet<>();
        for (Icon icon : PBD.getIcons()) {
            if (icon.asFile(folder).exists())
                i++;
            else
                missingIcons.add(icon.getName());
        }
        if (i != 0)
            return new PackMeta(folder.getName(), missingIcons);
        logger.debug("Directory '{}' contained 0 recognizable icons w/ formatted directories and file names.",
                folder.getName());
        return null;
    }
}