package dev.glitchedcode.pbd.dbd;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static dev.glitchedcode.pbd.dbd.Killer.*;
import static dev.glitchedcode.pbd.dbd.Survivor.*;

public enum Portrait implements Icon {

    MS2("MS2", "Ash", ASH),
    CA("CA", "Cannibal", CANNIBAL),
    CM("CM", CLAUDETTE),
    DF("DF", DWIGHT),
    LS("LS", "DLC2", LAURIE),
    SH("SH", "DLC2", SHAPE),
    AV("AV", "DLC3", ACE),
    HA("HA", "DLC3", HAG),
    DO("DO", "DLC4", DOCTOR),
    FM("FM", "DLC4", FENG),
    BE("BE", "DLC5", HUNTRESS),
    DK("DK", "DLC5", DAVID),
    QS("QS", "England", QUENTIN),
    SD("SD", "England", NIGHTMARE),
    FK("FK", "Finland", PIG),
    FS("FS", "Finland", TAPP),
    GK("GK", "Guam", CLOWN),
    GS("GS", "Guam", KATE),
    AF("AF", "Haiti", ADAM),
    HK("HK", "Haiti", SPIRIT),
    HB("HB", HILLBILLY),
    JP("JP", JAKE),
    KK("KK", "Kenya", LEGION),
    KS("KS", "Kenya", JEFF),
    BO("BO", "L4D", BILL),
    MK("MK", "Mali", PLAGUE),
    MS("MS", "Mali", JANE),
    MT("MT", MEG),
    NK("NK", NEA),
    NR("NR", NURSE),
    OK("OK", "Oman", GHOST_FACE),
    QF("QF", "Qatar", NANCY),
    QK("QK", "Qatar", DEMOGORGON),
    QM("QM", "Qatar", STEVE),
    SK("SK", "Sweden", ONI),
    SS("SS", "Sweden", YUI),
    TR("TR", TRAPPER),
    UK("UK", "Ukraine", DEATHSLINGER),
    US("US", "Ukraine", ZARINA),
    WR("WR", WRAITH);

    private final String name;
    private final String subFolder;
    private final Character character;
    public static final Portrait[] VALUES = Portrait.values();

    @ParametersAreNonnullByDefault
    Portrait(String name, Character character) {
        this(name, "", character);
    }

    Portrait(@Nonnull String name, @Nonnull String subFolder, @Nonnull Character character) {
        this.name = name;
        this.subFolder = subFolder;
        this.character = character;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Nonnull
    @Override
    public String getFileAdditive() {
        return "_charSelect_portrait";
    }

    @Nonnull
    @Override
    public String getProperName() {
        return character.getName() + "'s Portrait";
    }

    @Override
    public boolean isPrefix() {
        return false;
    }

    @Nonnull
    @Override
    public String getSubfolderName() {
        return "CharPortraits\\" + subFolder;
    }

    @Nonnull
    @Override
    public Character getCharacter() {
        return character;
    }

    @Nullable
    public static Portrait fromName(@Nonnull String name) {
        for (Portrait portrait : VALUES) {
            if (portrait.name.equalsIgnoreCase(name))
                return portrait;
        }
        return null;
    }

    @Nonnull
    public static Portrait fromName(@Nonnull String name, @Nonnull Portrait defaultVal) {
        for (Portrait portrait : VALUES) {
            if (portrait.name.equalsIgnoreCase(name))
                return portrait;
        }
        return defaultVal;
    }

    @Nullable
    public static Portrait fromProperName(@Nonnull String properName) {
        for (Portrait portrait : VALUES) {
            if (portrait.getProperName().equalsIgnoreCase(properName))
                return portrait;
        }
        return null;
    }

    @Nonnull
    public static Portrait fromCharacter(@Nonnull Character character) {
        for (Portrait portrait : VALUES) {
            if (portrait.getCharacter().getName().equalsIgnoreCase(character.getName()))
                return portrait;
        }
        return MS2; // Return Ash; this should never happen
    }
}