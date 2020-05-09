package dev.glitchedcode.pbd.pack;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.dbd.Addon;
import dev.glitchedcode.pbd.dbd.Character;
import dev.glitchedcode.pbd.dbd.Icon;
import dev.glitchedcode.pbd.dbd.Killer;
import dev.glitchedcode.pbd.dbd.Perk;
import dev.glitchedcode.pbd.dbd.Portrait;
import dev.glitchedcode.pbd.dbd.Survivor;
import dev.glitchedcode.pbd.logger.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Used to represent the 'packmeta.json' file stored in all cached icon packs.
 * Saves time & processing instead of having to re-eval each icon pack at runtime.
 */
public class PackMeta {

    private String name;
    private final String[] missingPerks;
    private final String[] missingAddons;
    private final String[] missingPortraits;
    private transient Set<Perk> missingPerksEnum;
    private transient Set<Addon> missingAddonsEnum;
    private transient Set<Portrait> missingPortraitsEnum;
    private static final Logger logger = PBD.getLogger();

    @ParametersAreNonnullByDefault
    public PackMeta(String name, String[] missingPerks, String[] missingAddons, String[] missingPortraits) {
        this.name = name;
        this.missingPerks = missingPerks;
        this.missingAddons = missingAddons;
        this.missingPortraits = missingPortraits;
    }

    public boolean isUpToDate() {
        return !getIcons(Survivor.ZARINA).isEmpty()
                && !getIcons(Killer.DEATHSLINGER).isEmpty();
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public String[] getMissingPerks() {
        return missingPerks;
    }

    @Nonnull
    public String[] getMissingAddons() {
        return missingAddons;
    }

    public String[] getMissingPortraits() {
        return missingPortraits;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public Set<Perk> getMissingPerksEnum() {
        if (missingPerksEnum != null)
            return missingPerksEnum;
        Set<Perk> perks = new HashSet<>();
        for (String n : getMissingPerks()) {
            Perk perk = Perk.fromName(n);
            if (perk != null)
                perks.add(perk);
            else
                logger.warn("Missing perks array item '{}' returned null.", n);
        }
        this.missingPerksEnum = perks;
        return perks;
    }

    public Set<Addon> getMissingAddonsEnum() {
        if (missingAddonsEnum != null)
            return missingAddonsEnum;
        Set<Addon> addons = new HashSet<>();
        for (String s : getMissingAddons()) {
            Addon addon = Addon.fromName(s);
            if (addon != null)
                addons.add(addon);
            else
                logger.warn("Missing addons array item '{}' returned null.", s);
        }
        this.missingAddonsEnum = addons;
        return addons;
    }

    public Set<Portrait> getMissingPortraitsEnum() {
        if (missingPortraitsEnum != null)
            return missingPortraitsEnum;
        Set<Portrait> portraits = new HashSet<>();
        for (String s : getMissingPortraits()) {
            Portrait portrait = Portrait.fromName(s);
            if (portrait != null)
                portraits.add(portrait);
            else
                logger.warn("Missing portraits array item '{}' returned null.", s);
        }
        this.missingPortraitsEnum = portraits;
        return portraits;
    }

    public boolean isMissingPerk(@Nonnull Perk perk) {
        for (Perk p : getMissingPerksEnum()) {
            if (p == perk)
                return true;
        }
        return false;
    }

    public boolean isMissingAddon(@Nonnull Addon addon) {
        for (Addon a : getMissingAddonsEnum()) {
            if (a == addon)
                return true;
        }
        return false;
    }

    public boolean isMissingPortrait(@Nonnull Portrait portrait) {
        for (Portrait p : getMissingPortraitsEnum()) {
            if (p == portrait)
                return true;
        }
        return false;
    }

    public Set<Icon> getIcons(@Nonnull Character character) {
        Set<Icon> icons = new HashSet<>();
        for (Perk perk : Perk.VALUES) {
            Character c = perk.getCharacter();
            if (c != null && c.getName().equalsIgnoreCase(character.getName()))
                icons.add(perk);
        }
        for (Addon addon : Addon.VALUES) {
            Character c = addon.getCharacter();
            if (c != null && c.getName().equalsIgnoreCase(character.getName()))
                icons.add(addon);
        }
        for (Portrait portrait : Portrait.VALUES) {
            Character c = portrait.getCharacter();
            if (c.getName().equalsIgnoreCase(character.getName()))
                icons.add(portrait);
        }
        logger.debug("Retrieved {} icons for character '{}' in icon pack '{}'.",
                icons.size(), character.getName(), getName());
        return icons;
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
        List<String> missingPerks = new ArrayList<>();
        for (Perk perk : Perk.VALUES) {
            if (perk.asFile(folder).exists())
                i++;
            else
                missingPerks.add(perk.getName());
        }
        List<String> missingAddons = new ArrayList<>();
        for (Addon addon : Addon.VALUES) {
            if (addon.asFile(folder).exists())
                i++;
            else
                missingAddons.add(addon.getName());
        }
        List<String> missingPortraits = new ArrayList<>();
        for (Portrait portrait : Portrait.VALUES) {
            if (portrait.asFile(folder).exists())
                i++;
            else
                missingPortraits.add(portrait.getName());
        }
        if (i != 0)
            return new PackMeta(folder.getName(), missingPerks.toArray(new String[] {}),
                    missingAddons.toArray(new String[] {}), missingPortraits.toArray(new String[] {}));
        logger.debug("Directory '{}' contained 0 recognizable icons w/ formatted directories and file names.",
                folder.getName());
        return null;
    }
}