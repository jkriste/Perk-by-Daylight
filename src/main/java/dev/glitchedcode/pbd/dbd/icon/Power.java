package dev.glitchedcode.pbd.dbd.icon;

import dev.glitchedcode.pbd.dbd.Character;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static dev.glitchedcode.pbd.dbd.Killer.*;

public enum Power implements Icon {

    BUBBAS_CHAINSAW("bubbasChainsaw", "Bubbas Chainsaw", "Cannibal", CANNIBAL),
    STALKER1("stalker1", "Stalker Tier 1", "DLC2", SHAPE),
    STALKER2("stalker2", "Stalker Tier 2", "DLC2", SHAPE),
    STALKER3("stalker3", "Stalker Tier 3", "DLC2", SHAPE),
    STALKER3A("stalker3a", "Stalker Tier 3A", "DLC2", SHAPE),
    BLACKENED_CATALYST("blackenedCatalyst", "Blackened Catalyst", "DLC3", HAG),
    CARTERS_SPARK("cartersSpark", "Carters Spark", "DLC4", DOCTOR),
    HUNTING_HATCHETS("huntingHatchets", "Hunting Hatchets", "DLC5", HUNTRESS),
    DREAM_MASTER("dreamMaster", "Dream Master", "England", NIGHTMARE),
    REVERSE_BEAR_TRAP("reverseBearTrap", "Reverse Bear Trap", "Finland", PIG),
    GAS_BOMB("gasBomb", "Gas Bomb", "Guam", CLOWN),
    YAMAOKAS_HAUNTING("yamaokasHaunting", "Yamaokas Haunting", "Haiti", SPIRIT),
    BELL("bell", "Bell", WRAITH),
    BREATH("breath", "Breath", NURSE),
    CHAINSAW("chainsaw", "Chainsaw", HILLBILLY),
    TRAP("trap", "Trap", TRAPPER),
    FERAL_FRENZY("feralFrenzy", "Feral Frenzy", "Kenya", LEGION),
    VILE_PURGE("vilePurge", "Vile Purge", "Mali", PLAGUE),
    GHOST_POWER_ACTIVATED("ghostPower_activated", "Ghost Power Activated", "Oman", GHOST_FACE),
    GHOST_POWER_AVAILABLE("ghostPower_available", "Ghost Power Available", "Oman", GHOST_FACE),
    OF_THE_ABYSS("ofTheAbyss", "Of The Abyss", "Qatar", DEMOGORGON),
    YAMAOKAS_WRATH("yamaokasWrath", "Yamaoka's Wrath", "Sweden", ONI),
    YAMAOKAS_WRATH_DEMON("yamaokasWrath_demon", "Yamaoka's Wrath Demon", "Sweden", ONI),
    U_K("UK", "The Redeemer", "Ukraine", DEATHSLINGER),
    U_K_CHAIN_BREAK("UK_chainBreak", "The Redeemer Chain Break", "Ukraine", DEATHSLINGER),
    WALES_RITES_OF_JUDGEMENT("Wales_ritesOfJudgement", "Rites Of Judgement", "Wales", EXECUTIONER);

    private final String name;
    private final String subFolder;
    private final String properName;
    private final Character character;

    @ParametersAreNonnullByDefault
    Power(String name, String properName) {
        this(name, properName, "", null);
    }

    @ParametersAreNonnullByDefault
    Power(String name, String properName, @Nullable Character character) {
        this(name, properName, "", character);
    }

    @ParametersAreNonnullByDefault
    Power(String name, String properName, String subFolder, @Nullable Character character) {
        this.name = name;
        this.properName = properName;
        this.subFolder = subFolder;
        this.character = character;
    }

    @Nonnull
    @Override
    public IconCategory getCategory() {
        return IconCategory.POWER;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Nonnull
    @Override
    public String getFileAdditive() {
        return "iconPowers_";
    }

    @Nonnull
    @Override
    public String getProperName() {
        return properName;
    }

    @Nonnull
    @Override
    public String getSubfolderName() {
        return "Powers\\" + subFolder;
    }

    @Nullable
    @Override
    public Character getCharacter() {
        return character;
    }
}
