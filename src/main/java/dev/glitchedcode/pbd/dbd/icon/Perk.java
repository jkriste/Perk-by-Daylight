package dev.glitchedcode.pbd.dbd.icon;

import dev.glitchedcode.pbd.dbd.Character;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static dev.glitchedcode.pbd.dbd.Survivor.*;
import static dev.glitchedcode.pbd.dbd.Killer.*;

/**
 * Used to represent all of the icons in the "Perks" category.
 */
public enum Perk implements Icon {

    BUCKLE_UP("buckleUp", "Buckle Up", "Ash", ASH),
    FLIP_FLOP("flipFlop", "Flip-Flop", "Ash", ASH),
    METTLE_OF_MAN("mettleOfMan", "Mettle of Man", "Ash", ASH),
    BBQ_AND_CHILI("BBQAndChili", "Barbecue and Chili", "Cannibal", CANNIBAL),
    FRANKLINS_DEMISE("franklinsLoss", "Franklin's Demise", "Cannibal", CANNIBAL),
    KNOCK_OUT("knockOut", "Knock Out", "Cannibal", CANNIBAL),
    DECISIVE_STRIKE("decisiveStrike", "Decisive Strike", "DLC2", LAURIE),
    DYING_LIGHT("dyingLight", "Dying Light", "DLC2", SHAPE),
    OBJECT_OF_OBSESSION("objectOfObsession", "Object of Obsession", "DLC2", LAURIE),
    PLAY_WITH_YOUR_FOOD("playWithYourFood", "Play With Your Food", "DLC2", SHAPE),
    SAVE_THE_BEST_FOR_LAST("saveTheBestForLast", "Save The Best For Last", "DLC2", SHAPE),
    SOLE_SURVIVOR("soleSurvivor", "Sole Survivor", "DLC2", LAURIE),
    ACE_IN_THE_HOLE("aceInTheHole", "Ace In The Hole", "DLC3", ACE),
    DEVOUR_HOPE("devourHope", "Hex: Devour Hope", "DLC3", HAG),
    OPEN_HANDED("openHanded", "Open Handed", "DLC3", ACE),
    RUIN("ruin", "Hex: Ruin", "DLC3", HAG),
    THE_THIRD_SEAL("theThirdSeal", "Hex: The Third Seal", "DLC3", HAG),
    THRILL_OF_THE_HUNT("thrillOfTheHunt", "Hex: Thrill Of The Hunt", "DLC3"),
    UP_THE_ANTE("upTheAnte", "Up The Ante", "DLC3", ACE),
    ALERT("alert", "Alert", "DLC4", FENG),
    OVERCHARGE("generatorOvercharge", "Overcharge", "DLC4", DOCTOR),
    LITHE("lithe", "Lithe", "DLC4", FENG),
    MONITOR_AND_ABUSE("monitorAndAbuse", "Monitor And Abuse", "DLC4", DOCTOR),
    OVERWHELMING_PRESENCE("overwhelmingPresence", "Overwhelming Presence", "DLC4", DOCTOR),
    TECHNICIAN("technician", "Technician", "DLC4", FENG),
    BEAST_OF_PREY("BeastOfPrey", "Beast Of Prey", "DLC5", HUNTRESS),
    DEAD_HARD("DeadHard", "Dead Hard", "DLC5", DAVID),
    HUNTRESS_LULLABY("HuntressLullaby", "Hex: Huntress Lullaby", "DLC5", HUNTRESS),
    NO_MITHER("NoMither", "No Mither", "DLC5", DAVID),
    TERRITORIAL_IMPERATIVE("TerritorialImperative", "Territorial Imperative", "DLC5", HUNTRESS),
    WERE_GONNA_LIVE_FOREVER("WereGonnaLiveForever", "We're Gonna Live Forever", "DLC5", DAVID),
    BLOOD_WARDEN("bloodWarden", "Blood Warden", "England", NIGHTMARE),
    FIRE_UP("fireUp", "Fire Up", "England", NIGHTMARE),
    PHARMACY("pharmacy", "Pharmacy", "England", QUENTIN),
    REMEMBER_ME("rememberMe", "Remember Me", "England", NIGHTMARE),
    VIGIL("vigil", "Vigil", "England", QUENTIN),
    WAKE_UP("wakeUp", "Wake Up", "England", QUENTIN),
    DETECTIVES_HUNCH("detectivesHunch", "Detective's Hunch", "Finland", TAPP),
    HANGMANS_TRICK("hangmansTrick", "Hangman's Trick", "Finland", PIG),
    MAKE_YOUR_CHOICE("makeYourChoice", "Make Your Choice", "Finland", PIG),
    STAKE_OUT("stakeOut", "Stake Out", "Finland", TAPP),
    SURVEILLANCE("surveillance", "Surveillance", "Finland", PIG),
    TENACITY("tenacity", "Tenacity", "Finland", TAPP),
    BAMBOOZLE("bamboozle", "Bamboozle", "Guam", CLOWN),
    COULROPHOBIA("coulrophobia", "Coulrophobia", "Guam", CLOWN),
    POP_GOES_THE_WEASEL("popGoesTheWeasel", "Pop Goes The Weasel", "Guam", CLOWN),
    AUTODIDACT("autodidact", "Autodidact", "Haiti", ADAM),
    DELIVERANCE("deliverance", "Deliverance", "Haiti", ADAM),
    DIVERSION("diversion", "Diversion", "Haiti", ADAM),
    RANCOR("hatred", "Hatred", "Haiti", SPIRIT),
    HAUNTED_GROUND("hauntedGround", "Hex: Haunted Ground", "Haiti", SPIRIT),
    SPIRIT_FURY("spiritFury", "Spirit Fury", "Haiti", SPIRIT),
    BOIL_OVER("boilOver", "Boil Over", "Kate", KATE),
    DANCE_WITH_ME("danceWithMe", "Dance With Me", "Kate", KATE),
    WINDOWS_OF_OPPORTUNITY("windowsOfOpportunity", "Windows Of Opportunity", "Kate", KATE),
    AFTERCARE("aftercare", "Aftercare", "Kenya", JEFF),
    BREAKDOWN("breakdown", "Breakdown", "Kenya", JEFF),
    DISCORDANCE("discordance", "Discordance", "Kenya", LEGION),
    DISTORTION("distortion", "Distortion", "Kenya", JEFF),
    IRON_MAIDEN("ironMaiden", "Iron Maiden", "Kenya", LEGION),
    MAD_GRIT("madGrit", "Mad Grit", "Kenya", LEGION),
    BORROWED_TIME("borrowedTime", "Borrowed Time", "L4D", BILL),
    LEFT_BEHIND("leftBehind", "Left Behind", "L4D", BILL),
    UNBREAKABLE("unbreakable", "Unbreakable", "L4D", BILL),
    CORRUPT_INTERVENTION("corruptIntervention", "Corrupt Intervention", "Mali", PLAGUE),
    DARK_DEVOTION("darkDevotion", "Dark Devotion", "Mali", PLAGUE),
    HEAD_ON("headOn", "Head On", "Mali", JANE),
    INFECTIOUS_FRIGHT("infectiousFright", "Infectious Fright", "Mali", PLAGUE),
    POISED("poised", "Poised", "Mali", JANE),
    SOLIDARITY("solidarity", "Solidarity", "Mali", JANE),
    FURTIVE_CHASE("furtiveChase", "Furtive Chase", "Oman", GHOST_FACE),
    IM_ALL_EARS("imAllEars", "I'm All Ears", "Oman", GHOST_FACE),
    THRILLING_TREMORS("thrillingTremors", "Thrilling Tremors", "Oman", GHOST_FACE),
    BABY_SITTER("babySitter", "Baby Sitter", "Qatar", STEVE),
    BETTER_TOGETHER("betterTogether", "Better Together", "Qatar", NANCY),
    CAMARADERIE("camaraderie", "Camaraderie", "Qatar", STEVE),
    CRUEL_CONFINEMENT("cruelConfinement", "Cruel Confinement", "Qatar", DEMOGORGON),
    FIXATED("fixated", "Fixated", "Qatar", NANCY),
    INNER_STRENGTH("innerStrength", "Inner Strength", "Qatar", NANCY),
    MIND_BREAKER("mindBreaker", "Mind Breaker", "Qatar", DEMOGORGON),
    SECOND_WIND("secondWind", "Second Wind", "Qatar", STEVE),
    SURGE("surge", "Surge", "Qatar", DEMOGORGON),
    ANY_MEANS_NECESSARY("anyMeansNecessary", "Any Means Necessary", "Sweden", YUI),
    BLOOD_ECHO("bloodEcho", "Blood Echo", "Sweden", ONI),
    BREAKOUT("breakout", "Breakout", "Sweden", YUI),
    LUCKY_BREAK("luckyBreak",  "Lucky Break", "Sweden", YUI),
    NEMESIS("nemesis", "Nemesis", "Sweden", ONI),
    ZANSHIN_TACTICS("zanshinTactics", "Zanshin Tactics", "Sweden", ONI),
    DEAD_MAN_SWITCH("deadManSwitch", "Dead Man Switch", "Ukraine", DEATHSLINGER),
    FOR_THE_PEOPLE("forThePeople", "For The People", "Ukraine", ZARINA),
    GEAR_HEAD("gearHead", "Gear Head", "Ukraine", DEATHSLINGER),
    RETRIBUTION("hexRetribution", "Hex: Retribution", "Ukraine", DEATHSLINGER),
    OFF_THE_RECORD("offTheRecord", "Off The Record", "Ukraine", ZARINA),
    RED_HERRING("redHerring", "Red Herring", "Ukraine", ZARINA),
    ADRENALINE("adrenaline", "Adrenaline", MEG),
    AGITATION("agitation", "Agitation", TRAPPER),
    A_NURSES_CALLING("aNursesCalling", "A Nurse's Calling", NURSE),
    BALANCED_LANDING("balancedLanding", "Balanced Landing", NEA),
    BITTER_MURMUR("bitterMurmur", "Bitter Murmur"),
    BLOODHOUND("bloodhound", "Bloodhound", WRAITH),
    BOND("bond", "Bond", DWIGHT),
    BOTANY_KNOWLEDGE("botanyKnowledge", "Botany Knowledge", CLAUDETTE),
    BRUTAL_STRENGTH("brutalStrength", "Brutal Strength", TRAPPER),
    CALM_SPIRIT("calmSpirit", "Calm Spirit", JAKE),
    DARK_SENSE("darkSense", "Dark Sense"),
    DEERSTALKER("deerstalker", "Deer Stalker"),
    DEJA_VU("dejaVu", "Deja Vu"),
    DISTRESSING("distressing", "Distressing"),
    EMPATHY("empathy", "Empathy", CLAUDETTE),
    ENDURING("enduring", "Enduring", HILLBILLY),
    HOPE("hope", "Hope"),
    INSIDIOUS("insidious", "Insidious"),
    IRON_GRASP("ironGrasp", "Iron Grasp"),
    IRON_WILL("ironWill", "Iron Will", JAKE),
    KINDRED("kindred", "Kindred"),
    LEADER("leader", "Leader", DWIGHT),
    LIGHTBORN("lightborn", "Lightborn", HILLBILLY),
    LIGHTWEIGHT("lightweight", "Lightweight"),
    MONSTROUS_SHRINE("monstrousShrine", "Monstrous Shrine"),
    NO_ONE_ESCAPES_DEATH("noOneEscapesDeath", "Hex: No One Escapes Death"),
    NO_ONE_LEFT_BEHIND("noOneLeftBehind", "No One Left Behind"),
    PLUNDERERS_INSTINCT("plunderersInstinct", "Plunderer's Instinct"),
    PREDATOR("predator", "Predator", WRAITH),
    PREMONITION("premonition", "Premonition"),
    PROVE_THYSELF("proveThyself", "Prove Thyself", DWIGHT),
    QUICK_AND_QUIET("quickAndQuiet", "Quick And Quiet", NEA),
    RESILIENCE("resilience", "Resilience"),
    SABOTEUR("saboteur", "Saboteur", JAKE),
    SELF_CARE("selfCare", "Self-Care", CLAUDETTE),
    SHADOWBORN("shadowborn", "Shadowborn", WRAITH),
    SLIPPERY_MEAT("slipperyMeat", "Slippery Meat"),
    SLOPPY_BUTCHER("sloppyButcher", "Sloppy Butcher"),
    SMALL_GAME("smallGame", "Small Game"),
    SPIES_FROM_THE_SHADOWS("spiesFromTheShadows", "Spies From The Shadows"),
    SPINE_CHILL("spineChill", "Spine Chill"),
    SPRINT_BURST("sprintBurst", "Sprint Burst", MEG),
    STREETWISE("streetwise", "Streetwise", NEA),
    STRIDOR("stridor", "Stridor", NURSE),
    THANATOPHOBIA("thatanophobia", "Thanatophobia", NURSE),
    THIS_IS_NOT_HAPPENING("thisIsNotHappening", "This Is Not Happening"),
    TINKERER("tinkerer", "Tinkerer", HILLBILLY),
    UNNERVING_PRESENCE("unnervingPresence", "Unnerving Presence", TRAPPER),
    UNRELENTING("unrelenting", "Unrelenting", WRAITH),
    URBAN_EVASION("urbanEvasion", "Urban Evasion", NEA),
    WELL_MAKE_IT("wellMakeIt", "We'll Make It"),
    WHISPERS("whispers", "Whispers"),
    BLOOD_PACT("bloodPact", "Blood Pact", "Wales", CHERYL),
    REPRESSED_ALLIANCE("repressedAlliance", "Repressed Alliance", "Wales", CHERYL),
    SOUL_GUARD("soulGuard", "Soul Guard", "Wales", CHERYL),
    DEATHBOUND("deathbound", "Deathbound", "Wales", EXECUTIONER),
    FORCED_PENANCE("forcedPenance", "Forced Penance", "Wales", EXECUTIONER),
    TRAIL_OF_TORMENT("trailOfTorment", "Trail of Torment", "Wales", EXECUTIONER);

    private final String name;
    private final String subFolder;
    private final String properName;
    private final Character character;

    Perk(@Nonnull String name, @Nonnull String properName) {
        this(name, properName, "", null);
    }

    @ParametersAreNonnullByDefault
    Perk(String name, String properName, String subFolder) {
        this(name, properName, subFolder, null);
    }

    Perk(@Nonnull String name, @Nonnull String properName, @Nullable Character character) {
        this(name, properName, "", character);
    }

    Perk(@Nonnull String name, @Nonnull String properName, @Nonnull String subFolder, @Nullable Character character) {
        this.name = name;
        this.properName = properName;
        this.subFolder = subFolder;
        this.character = character;
    }

    @Nonnull
    @Override
    public IconCategory getCategory() {
        return IconCategory.PERK;
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
    @Override
    public String getProperName() {
        return properName;
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
}