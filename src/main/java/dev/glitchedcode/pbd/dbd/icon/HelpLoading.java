package dev.glitchedcode.pbd.dbd.icon;

import dev.glitchedcode.pbd.dbd.Character;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Used to represent all icons in the "HelpLoading" category.
 */
public enum HelpLoading implements Icon {

    ADDONS("addons", "Addons"),
    BLOODPOINTS("bloodpoints", "Bloodpoints"),
    CANNIBAL("cannibal", "Cannibal"),
    CLOWN("clown", "Clown"),
    CROWS("crows", "Crows"),
    DOCTOR("doctor", "Doctor"),
    GENERATORS("generators", "Generators"),
    GHOST("ghost", "Ghost"),
    HAG("hag", "Hag"),
    HATCH("hatch", "Hatch"),
    HEALTH("health", "Health"),
    HILLBILLY("hillbilly", "Hillbilly"),
    HOOK("hook", "Hook"),
    HUNTRESS("huntress", "Huntress"),
    INFO("info", "Info"),
    ITEMS("items", "Items"),
    KILLER("killer", "Killer"),
    KILLER_VISION("killerVision", "Killer Vision"),
    LEGION("legion", "Legion"),
    LORE("lore", "Lore"),
    NIGHTMARE("nightmare", "Nightmare"),
    NURSE("nurse", "Nurse"),
    OFFERING("offering", "Offering"),
    PIG("pig", "Pig"),
    PLAGUE("plague", "Plague"),
    PLAYERS("players", "Players"),
    SETTINGS("settings", "Settings"),
    SHAPE("shape", "Shape"),
    SKILL_CHECK("skillCheck", "Skill Check"),
    SOUND("sound", "Sound"),
    SPIRIT("spirit", "Spirit"),
    SURVIVOR("survivor", "Survivor"),
    TOTEM("totem", "Totem"),
    TRAPPER("trapper", "Trapper"),
    WRAITH("wraith", "Wraith"),
    DEMOGORGON("demogorgon", "Demogorgon", "Qatar"),
    SK("sk", "Oni", "Sweden"),
    UK("uk", "Deathslinger", "Ukraine"),
    WALES("wales", "Executioner", "Wales");

    private final String name;
    private final String subFolder;
    private final String properName;

    @ParametersAreNonnullByDefault
    HelpLoading(String name, String properName) {
        this(name, properName, "");
    }

    @ParametersAreNonnullByDefault
    HelpLoading(String name, String properName, String subFolder) {
        this.name = name;
        this.subFolder = subFolder;
        this.properName = properName;
    }

    @Nonnull
    @Override
    public IconCategory getCategory() {
        return IconCategory.HELP_LOADING;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Nonnull
    @Override
    public String getFileAdditive() {
        return "iconHelpLoading_";
    }

    @Nonnull
    @Override
    public String getProperName() {
        return properName;
    }

    @Nonnull
    @Override
    public String getSubfolderName() {
        return "HelpLoading\\" + subFolder;
    }

    @Nullable
    @Override
    public Character getCharacter() {
        return null;
    }
}