package dev.glitchedcode.pbd.dbd;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static dev.glitchedcode.pbd.dbd.Survivor.*;
import static dev.glitchedcode.pbd.dbd.Killer.*;

public enum Perk implements Icon {

    BUCKLE_UP("buckleUp", "Ash", ASH),
    FLIP_FLOP("flipFlop", "Ash", ASH),
    METTLE_OF_MAN("mettleOfMan", "Ash", ASH),
    BBQ_AND_CHILI("BBQAndChili", "Cannibal", CANNIBAL),
    FRANKLINS_DEMISE("franklinsLoss", "Cannibal", CANNIBAL),
    KNOCK_OUT("knockOut", "Cannibal", CANNIBAL),
    DECISIVE_STRIKE("decisiveStrike", "DLC2", LAURIE),
    DYING_LIGHT("dyingLight", "DLC2", SHAPE),
    OBJECT_OF_OBSESSION("objectOfObsession", "DLC2", LAURIE),
    PLAY_WITH_YOUR_FOOD("playWithYourFood", "DLC2", SHAPE),
    SAVE_THE_BEST_FOR_LAST("saveTheBestForLast", "DLC2", SHAPE),
    SOLE_SURVIVOR("soleSurvivor", "DLC2", LAURIE),
    ACE_IN_THE_HOLE("aceInTheHole", "DLC3", ACE),
    DEVOUR_HOPE("devourHope", "DLC3", HAG),
    OPEN_HANDED("openHanded", "DLC3", ACE),
    RUIN("ruin", "DLC3", HAG),
    THE_THIRD_SEAL("theThirdSeal", "DLC3", HAG),
    THRILL_OF_THE_HUNT("thrillOfTheHunt", "DLC3"),
    UP_THE_ANTE("upTheAnte", "DLC3", ACE),
    ALERT("alert", "DLC4", FENG),
    OVERCHARGE("generatorOvercharge", "DLC4", DOCTOR),
    LITHE("lithe", "DLC4", FENG),
    MONITOR_AND_ABUSE("monitorAndAbuse", "DLC4", DOCTOR),
    OVERWHELMING_PRESENCE("overwhelmingPresence", "DLC4", DOCTOR),
    TECHNICIAN("technician", "DLC4", FENG),
    BEAST_OF_PREY("BeastOfPrey", "DLC5", HUNTRESS),
    DEAD_HARD("DeadHard", "DLC5", DAVID),
    HUNTRESS_LULLABY("HuntressLullaby", "DLC5", HUNTRESS),
    NO_MITHER("NoMither", "DLC5", DAVID),
    TERRITORIAL_IMPERATIVE("TerritorialImperative", "DLC5", HUNTRESS),
    WERE_GONNA_LIVE_FOREVER("WereGonnaLiveForever", "DLC5", DAVID),
    BLOOD_WARDEN("bloodWarden", "England", NIGHTMARE),
    FIRE_UP("fireUp", "England", NIGHTMARE),
    PHARMACY("pharmacy", "England", QUENTIN),
    REMEMBER_ME("rememberMe", "England", NIGHTMARE),
    VIGIL("vigil", "England", QUENTIN),
    WAKE_UP("wakeUp", "England", QUENTIN),
    DETECTIVES_HUNCH("detectivesHunch", "Finland", TAPP),
    HANGMANS_TRICK("hangmansTrick", "Finland", PIG),
    MAKE_YOUR_CHOICE("makeYourChoice", "Finland", PIG),
    STAKE_OUT("stakeOut", "Finland", TAPP),
    SURVEILLANCE("surveillance", "Finland", PIG),
    TENACITY("tenacity", "Finland", TAPP),
    BAMBOOZLE("bamboozle", "Guam", CLOWN),
    COULROPHOBIA("coulrophobia", "Guam", CLOWN),
    POP_GOES_THE_WEASEL("popGoesTheWeasel", "Guam", CLOWN),
    AUTODIDACT("autodidact", "Haiti", ADAM),
    DELIVERANCE("deliverance", "Haiti", ADAM),
    DIVERSION("diversion", "Haiti", ADAM),
    RANCOR("hatred", "Haiti", SPIRIT),
    HAUNTED_GROUND("hauntedGround", "Haiti", SPIRIT),
    SPIRIT_FURY("spiritFury", "Haiti", SPIRIT),
    BOIL_OVER("boilOver", "Kate", KATE),
    DANCE_WITH_ME("danceWithMe", "Kate", KATE),
    WINDOWS_OF_OPPORTUNITY("windowsOfOpportunity", "Kate", KATE),
    AFTERCARE("aftercare", "Kenya", JEFF),
    BREAKDOWN("breakdown", "Kenya", JEFF),
    DISCORDANCE("discordance", "Kenya", LEGION),
    DISTORTION("distortion", "Kenya", JEFF),
    IRON_MAIDEN("ironMaiden", "Kenya", LEGION),
    MAD_GRIT("madGrit", "Kenya", LEGION),
    BORROWED_TIME("borrowedTime", "L4D", BILL),
    LEFT_BEHIND("leftBehind", "L4D", BILL),
    UNBREAKABLE("unbreakable", "L4D", BILL),
    CORRUPT_INTERVENTION("corruptIntervention", "Mali", PLAGUE),
    DARK_DEVOTION("darkDevotion", "Mali", PLAGUE),
    HEAD_ON("headOn", "Mali", JANE),
    INFECTIOUS_FRIGHT("infectiousFright", "Mali", PLAGUE),
    POISED("poised", "Mali", JANE),
    SOLIDARITY("solidarity", "Mali", JANE),
    FURTIVE_CHASE("furtiveChase", "Oman", GHOST_FACE),
    IM_ALL_EARS("imAllEars", "Oman", GHOST_FACE),
    THRILLING_TREMORS("thrillingTremors", "Oman", GHOST_FACE),
    BABY_SITTER("babySitter", "Qatar", STEVE),
    BETTER_TOGETHER("betterTogether", "Qatar", NANCY),
    CAMARADERIE("camaraderie", "Qatar", STEVE),
    CRUEL_CONFINEMENT("cruelConfinement", "Qatar", DEMOGORGON),
    FIXATED("fixated", "Qatar", NANCY),
    INNER_STRENGTH("innerStrength", "Qatar", NANCY),
    MIND_BREAKER("mindBreaker", "Qatar", DEMOGORGON),
    SECOND_WIND("secondWind", "Qatar", STEVE),
    SURGE("surge", "Qatar", DEMOGORGON),
    ANY_MEANS_NECESSARY("anyMeansNecessary", "Sweden", YUI),
    BLOOD_ECHO("bloodEcho", "Sweden", ONI),
    BREAKOUT("breakout", "Sweden", YUI),
    LUCKY_BREAK("luckyBreak", "Sweden", YUI),
    NEMESIS("nemesis", "Sweden", ONI),
    ZANSHIN_TACTICS("zanshinTactics", "Sweden", ONI),
    DEAD_MAN_SWITCH("deadManSwitch", "Ukraine", DEATHSLINGER),
    FOR_THE_PEOPLE("forThePeople", "Ukraine", ZARINA),
    GEAR_HEAD("gearHead", "Ukraine", DEATHSLINGER),
    RETRIBUTION("hexRetribution", "Ukraine", DEATHSLINGER),
    OFF_THE_RECORD("offTheRecord", "Ukraine", ZARINA),
    RED_HERRING("redHerring", "Ukraine", ZARINA),
    ADRENALINE("adrenaline", MEG),
    AGITATION("agitation", TRAPPER),
    A_NURSES_CALLING("aNursesCalling", NURSE),
    BALANCED_LANDING("balancedLanding", NEA),
    BITTER_MURMUR("bitterMurmur"),
    BLOODHOUND("bloodhound", WRAITH),
    BOND("bond", DWIGHT),
    BOTANY_KNOWLEDGE("botanyKnowledge", CLAUDETTE),
    BRUTAL_STRENGTH("brutalStrength", TRAPPER),
    CALM_SPIRIT("calmSpirit", JAKE),
    DARK_SENSE("darkSense"),
    DEERSTALKER("deerstalker"),
    DEJA_VU("dejaVu"),
    DISTRESSING("distressing"),
    EMPATHY("empathy", CLAUDETTE),
    ENDURING("enduring", HILLBILLY),
    HOPE("hope"),
    INSIDIOUS("insidious"),
    IRON_GRASP("ironGrasp"),
    IRON_WILL("ironWill", JAKE),
    KINDRED("kindred"),
    LEADER("leader", DWIGHT),
    LIGHTBORN("lightborn", HILLBILLY),
    LIGHTWEIGHT("lightweight"),
    MONSTROUS_SHRINE("monstrousShrine"),
    NO_ONE_ESCAPES_DEATH("noOneEscapesDeath"),
    NO_ONE_LEFT_BEHIND("noOneLeftBehind"),
    PLUNDERERS_INSTINCT("plunderersInstinct"),
    PREDATOR("predator", WRAITH),
    PREMONITION("premonition"),
    PROVE_THYSELF("proveThyself", DWIGHT),
    QUICK_AND_QUIET("quickAndQuiet", NEA),
    RESILIENCE("resilience"),
    SABOTEUR("saboteur", JAKE),
    SELF_CARE("selfCare", CLAUDETTE),
    SHADOWBORN("shadowborn", WRAITH),
    SLIPPERY_MEAT("slipperyMeat"),
    SLOPPY_BUTCHER("sloppyButcher"),
    SMALL_GAME("smallGame"),
    SPIES_FROM_THE_SHADOWS("spiesFromTheShadows"),
    SPINE_CHILL("spineChill"),
    SPRINT_BURST("sprintBurst", MEG),
    STREETWISE("streetwise", NEA),
    STRIDOR("stridor", NURSE),
    THANATOPHOBIA("thatanophobia", NURSE),
    THIS_IS_NOT_HAPPENING("thisIsNotHappening"),
    TINKERER("tinkerer", HILLBILLY),
    UNNERVING_PRESENCE("unnervingPresence", TRAPPER),
    UNRELENTING("unrelenting", WRAITH),
    URBAN_EVASION("urbanEvasion", NEA),
    WELL_MAKE_IT("wellMakeIt"),
    WHISPERS("whispers");

    private final String name;
    private final String subFolder;
    private final Character character;
    public static final Perk[] VALUES = values();

    Perk(@Nonnull String name) {
        this(name, "", null);
    }

    @ParametersAreNonnullByDefault
    Perk(String name, String subFolder) {
        this(name, subFolder, null);
    }

    Perk(@Nonnull String name, @Nullable Character character) {
        this(name, "", character);
    }

    Perk(@Nonnull String name, @Nonnull String subFolder, @Nullable Character character) {
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
        return "iconPerks_";
    }

    @Nonnull
    public String getPerkName() {
        return getName().toLowerCase().replace('_', ' ');
    }

    @Nonnull
    @Override
    public String getSubfolderName() {
        return "Perks\\" + subFolder;
    }

    @Nullable
    @Override
    public Character getCharacter() {
        return character;
    }

    @Nullable
    @ParametersAreNonnullByDefault
    public static Perk fromName(String name) {
        for (Perk perk : VALUES) {
            if (perk.name.equalsIgnoreCase(name))
                return perk;
        }
        return null;
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public static Perk fromName(String name, Perk defaultVal) {
        for (Perk perk : VALUES) {
            if (perk.name.equalsIgnoreCase(name))
                return perk;
        }
        return defaultVal;
    }
}