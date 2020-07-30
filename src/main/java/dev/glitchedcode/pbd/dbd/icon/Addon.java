package dev.glitchedcode.pbd.dbd.icon;

import dev.glitchedcode.pbd.dbd.Character;
import dev.glitchedcode.pbd.dbd.Killer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static dev.glitchedcode.pbd.dbd.Killer.*;

public enum Addon implements Icon {

    AWARDWINNING_CHILI("awardwinningChili", "Awardwinning Chili", "Cannibal", CANNIBAL),
    CHILI("chili", "Chili", "Cannibal", CANNIBAL),
    KNIFE_SCRATCHES("knifeScratches", "Knife Scratches", "Cannibal", CANNIBAL),
    THE_BEASTS_MARK("theBeastsMark", "The Beast's Mark", "Cannibal", CANNIBAL),
    THE_GREASE("theGrease", "The Grease", "Cannibal", CANNIBAL),
    BLONDE_HAIR("blondeHair", "Blonde Hair", "DLC2", SHAPE),
    BOYFRIENDS_MEMO("boyfriendsMemo", "Boyfriend's Memo", "DLC2", SHAPE),
    DEAD_RABBIT("deadRabbit", "Dead Rabbit", "DLC2", SHAPE),
    GLASS_FRAGMENT("glassFragment", "Glass Fragment", "DLC2", SHAPE),
    HAIR_BOW("hairBow", "Hair Bow", "DLC2", SHAPE),
    HAIR_BRUSH("hairBrush", "Hair Brush", "DLC2", SHAPE),
    JEWELRY("jewelry", "Jewelry", "DLC2", SHAPE),
    JEWELRY_BOX("jewelryBox", "Jewelry Box", "DLC2", SHAPE),
    J_MYERS_MEMORIAL("jMyersMemorial", "J. Myers' Memorial", "DLC2", SHAPE),
    JUDITHS_JOURNAL("judithsJournal", "Judiths Journal", "DLC2", SHAPE),
    JUDITHS_TOMBSTONE("judithsTombstone", "Judith's Tombstone", "DLC2", SHAPE),
    LOCK_OF_HAIR("lockOfHair", "Lock Of Hair", "DLC2", SHAPE),
    MEMORIAL_FLOWER("memorialFlower", "Memorial Flower", "DLC2", SHAPE),
    MIRROR_SHARD("mirrorShard", "Mirror Shard", "DLC2", SHAPE),
    REFLECTIVE_FRAGMENT("reflectiveFragment", "Reflective Fragment", "DLC2", SHAPE),
    SCRATCHED_MIRROR("scratchedMirror", "Scratched Mirror", "DLC2", SHAPE),
    TACKY_EARRINGS("tackyEarrings", "Tacky Earrings", "DLC2", SHAPE),
    TOMBSTONE_PIECE("tombstonePiece", "Tombstone Piece", "DLC2", SHAPE),
    TUFT_OF_HAIR("tuftOfHair", "Tuft Of Hair", "DLC2", SHAPE),
    VANITY_MIRROR("vanityMirror", "Vanity Mirror", "DLC2", SHAPE),
    BLOODIED_MUD("bloodiedMud", "Bloodied Mud", "DLC3", HAG),
    BLOODIED_WATER("bloodiedWater", "Bloodied Water", "DLC3", HAG),
    BOG_WATER("bogWater", "Bog Water", "DLC3", HAG),
    CRACKED_TURTLE_EGG("crackedTurtleEgg", "Cracked Turtle Egg", "DLC3", HAG),
    CYPRESS_NECKLET("cypressNecklet", "Cypress Necklet", "DLC3", HAG),
    DEAD_FLY_MUD("deadFlyMud", "Dead Fly Mud", "DLC3", HAG),
    DISFIGURED_EAR("disfiguredEar", "Disfigured Ear", "DLC3", HAG),
    DRAGONFLY_WINGS("dragonflyWings", "Dragonfly Wings", "DLC3", HAG),
    DRIED_CICADA("driedCicada", "Dried Cicada", "DLC3", HAG),
    GRANMAS_HEART("granmasHeart", "Granmas Heart", "DLC3", HAG),
    HALF_EGGSHELL("halfEggshell", "Half Eggshell", "DLC3", HAG),
    MINT_RAG("mintRag", "Mint Rag", "DLC3", HAG),
    POWDERED_EGGSHELL("powderedEggshell", "Powdered Eggshell", "DLC3", HAG),
    PUSSY_WILLOW_CATKINS("pussyWillowCatkins", "Pussy Willow Catkins", "DLC3", HAG),
    ROPE_NECKLET("ropeNecklet", "Rope Necklet", "DLC3", HAG),
    RUSTY_SHACKLES("rustyShackles", "Rusty Shackles", "DLC3", HAG),
    SCARRED_HAND("scarredHand", "Scarred Hand", "DLC3", HAG),
    SWAMP_ORCHID_NECKLET("swampOrchidNecklet", "Swamp Orchid Necklet", "DLC3", HAG),
    WATERLOGGED_SHOE("waterloggedShoe", "Waterlogged Shoe", "DLC3", HAG),
    WILLOW_WREATH("willowWreath", "Willow Wreath", "DLC3", HAG),
    CALM_CARTERS_NOTES("calmCartersNotes", "Calm Carter's Notes", "DLC4", DOCTOR),
    CALM_CLASS_I("calmClassI", "Calm Class I", "DLC4", DOCTOR),
    CALM_CLASS_II("calmClassII", "Calm Class II", "DLC4", DOCTOR),
    DICIPLINE_CARTERS_NOTES("diciplineCartersNotes", "Dicipline Carter's Notes", "DLC4", DOCTOR),
    DICIPLINE_CLASS_II("diciplineClassII", "Dicipline Class II", "DLC4", DOCTOR),
    DICIPLINE_CLASS_III("diciplineClassIII", "Dicipline Class III", "DLC4", DOCTOR),
    HIGH_STIMULUS_ELECTRODE("highStimulusElectrode", "High Stimulus Electrode", "DLC4", DOCTOR),
    INTERVIEW_TAPE("interviewTape", "Interview Tape", "DLC4", DOCTOR),
    IRIDESCENT_KING("iridescentKing", "Iridescent King", "DLC4", DOCTOR),
    IRIDESCENT_QUEEN("iridescentQueen", "Iridescent Queen", "DLC4", DOCTOR),
    MAPLE_KNIGHT("mapleKnight", "Maple Knight", "DLC4", DOCTOR),
    MOLDY_ELECTRODE("moldyElectrode", "Moldy Electrode", "DLC4", DOCTOR),
    OBEDIENCE_CARTERS_NOTES("obedienceCartersNotes", "Obedience Carter's Notes", "DLC4", DOCTOR),
    ORDER_CARTERS_NOTES("orderCartersNotes", "Order Carter's Notes", "DLC4", DOCTOR),
    ORDER_CLASS_I("orderClassI", "Order Class I", "DLC4", DOCTOR),
    ORDER_CLASS_II("orderClassII", "Order Class II", "DLC4", DOCTOR),
    POLISHED_ELECTRODE("polishedElectrode", "Polished Electrode", "DLC4", DOCTOR),
    RESTRAINT_CARTERS_NOTES("restraintCartersNotes", "Restraint Carters Notes", "DLC4", DOCTOR),
    RESTRAINT_CLASS_II("restraintClassII", "Restraint Class II", "DLC4", DOCTOR),
    RESTRAINT_CLASS_III("restraintClassIII", "Restraint Class III", "DLC4", DOCTOR),
    SCRAPPED_TAPE("scrappedTape", "Scrapped Tape", "DLC4", DOCTOR),
    AMANITA_TOXIN("amanitaToxin", "Amanita Toxin", "DLC5", HUNTRESS),
    BANDAGED_HAFT("bandagedHaft", "Bandaged Haft", "DLC5", HUNTRESS),
    BEGRIMED_HEAD("begrimedHead", "Begrimed Head", "DLC5", HUNTRESS),
    BERUS_TOXIN("berusToxin", "Berus Toxin", "DLC5", HUNTRESS),
    COARSE_STONE("coarseStone", "Coarse Stone", "DLC5", HUNTRESS),
    DEERSKIN_GLOVES("deerskinGloves", "Deerskin Gloves", "DLC5", HUNTRESS),
    FINE_STONE("fineStone", "Fine Stone", "DLC5", HUNTRESS),
    FLOWER_BABUSHKA("flowerBabushka", "Flower Babushka", "DLC5", HUNTRESS),
    GLOWING_CONCOCTION("glowingConcoction", "Glowing Concoction", "DLC5", HUNTRESS),
    INFANTRY_BELT("infantryBelt", "Infantry Belt", "DLC5", HUNTRESS),
    IRIDESCENT_HEAD("iridescentHead", "Iridescent Head", "DLC5", HUNTRESS),
    LEATHER_LOOP("leatherLoop", "Leather Loop", "DLC5", HUNTRESS),
    MANNA_GRASS_BRAID("mannaGrassBraid", "Manna Grass Braid", "DLC5", HUNTRESS),
    OAK_HAFT("oakHaft", "Oak Haft", "DLC5", HUNTRESS),
    PUNGENT_FIALE("pungentFiale", "Pungent Fiale", "DLC5", HUNTRESS),
    RUSTY_HEAD("rustyHead", "Rusty Head", "DLC5", HUNTRESS),
    SHINY_PIN("shinyPin", "Shiny Pin", "DLC5", HUNTRESS),
    VENOMOUS_CONCOCTION("venomousConcoction", "Venomous Concoction", "DLC5", HUNTRESS),
    YEW_SEED_BREW("yewSeedBrew", "Yew Seed Brew", "DLC5", HUNTRESS),
    YEW_SEED_CONCOCTION("yewSeedConcoction", "Yew Seed Concoction", "DLC5", HUNTRESS),
    BLACK_BOX("blackBox", "Black Box", "England", NIGHTMARE),
    BLUE_DRESS("blueDress", "Blue Dress", "England", NIGHTMARE),
    CAT_BLOCK("catBlock", "Cat Block", "England", NIGHTMARE),
    CLASS_PHOTO("classPhoto", "Class Photo", "England", NIGHTMARE),
    GARDEN_RAKE("gardenRake", "Garden Rake", "England", NIGHTMARE),
    GREEN_DRESS("greenDress", "Green Dress", "England", NIGHTMARE),
    JUMP_ROPE("jumpRope", "Jump Rope", "England", NIGHTMARE),
    KIDS_DRAWING("kidsDrawing", "Kid's Drawing", "England", NIGHTMARE),
    NANCYS_MASTERPIECE("nancysMasterpiece", "Nancy's Masterpiece", "England", NIGHTMARE),
    NANCYS_SKETCH("nancysSketch", "Nancy's Sketch", "England", NIGHTMARE),
    OUTDOOR_ROPE("outdoorRope", "Outdoor Rope", "England", NIGHTMARE),
    PAINT_THINNER("paintThinner", "Paint Thinner", "England", NIGHTMARE),
    PILL_BOTTLE("pillBottle", "Pill Bottle", "England", NIGHTMARE),
    PROTOTYPE_CLAW("prototypeClaw", "Prototype Claw", "England", NIGHTMARE),
    RED_PAINT_BRUSH("redPaintBrush", "Red Paint Brush", "England", NIGHTMARE),
    SHEEP_BLOCK("sheepBlock", "Sheep Block", "England", NIGHTMARE),
    SWING_CHAINS("swingChains", "Swing Chains", "England", NIGHTMARE),
    UNICORN_BLOCK("unicornBlock", "Unicorn Block", "England", NIGHTMARE),
    WOOL_SHIRT("woolShirt", "Wool Shirt", "England", NIGHTMARE),
    Z_BLOCK("zBlock", "Z Block", "England", NIGHTMARE),
    AMANDAS_LETTER("amandasLetter", "Amanda's Letter", "Finland", PIG),
    AMANDAS_SECRET("amandasSecret", "Amanda's Secret", "Finland", PIG),
    BAG_OF_GEARS("bagOfGears", "Bag Of Gears", "Finland", PIG),
    COMBAT_STRAPS("combatStraps", "Combat Straps", "Finland", PIG),
    CRATE_OF_GEARS("crateOfGears", "Crate Of Gears", "Finland", PIG),
    FACE_MASK("faceMask", "Face Mask", "Finland", PIG),
    INTERLOCKING_RAZOR("interlockingRazor", "Interlocking Razor", "Finland", PIG),
    JIGSAWS_ANNOTATED_PLAN("jigsawsAnnotatedPlan", "Jigsaw's Annotated Plan", "Finland", PIG),
    JIGSAWS_SKETCH("jigsawsSketch", "Jigsaw's Sketch", "Finland", PIG),
    JOHNS_MEDICAL_FILE("johnsMedicalFile", "John's Medical File", "Finland", PIG),
    LAST_WILL("lastWill", "Last Will", "Finland", PIG),
    RAZER_WIRE("razerWire", "Razer Wire", "Finland", PIG),
    RULES_SET_N2("rulesSetN2", "Rules Set N2", "Finland", PIG),
    RUSTY_ATTACHMENTS("rustyAttachments", "Rusty Attachments", "Finland", PIG),
    SHATTERED_SYRINGE("shatteredSyringe", "Shattered Syringe", "Finland", PIG),
    SLOW_RELEASE_TOXIN("slowReleaseToxin", "Slow Release Toxin", "Finland", PIG),
    TAMPERED_TIMER("tamperedTimer", "Tampered Timer", "Finland", PIG),
    UTILITY_BLADES("utilityBlades", "Utility Blades", "Finland", PIG),
    VIDEO_TAPE("videoTape", "Video Tape", "Finland", PIG),
    WORKSHOP_GREASE("workshopGrease", "Workshop Grease", "Finland", PIG),
    BOTTLE_OF_CHLOROFORM("bottleOfChloroform", "Bottle Of Chloroform", "Guam", CLOWN),
    CHEAP_GIN_BOTTLE("cheapGinBottle", "Cheap Gin Bottle", "Guam", CLOWN),
    CIGAR_BOX("cigarBox", "Cigar Box", "Guam", CLOWN),
    ETHER10("ether10", "Ether10", "Guam", CLOWN),
    ETHER15("ether15", "Ether15", "Guam", CLOWN),
    ETHER5("ether5", "Ether5", "Guam", CLOWN),
    FINGERLESS_PARADE_GLOVES("fingerlessParadeGloves", "Fingerless Parade Gloves", "Guam", CLOWN),
    FLASK_OF_BLEACH("flaskOfBleach", "Flask Of Bleach", "Guam", CLOWN),
    GARISH_MAKEUP_KIT("garishMakeupKit", "Garish Makeup Kit", "Guam", CLOWN),
    KEROSENE_CAN("keroseneCan", "Kerosene Can", "Guam", CLOWN),
    REDHEADS_PINKY_FINGER("redheadsPinkyFinger", "Redhead's Pinky Finger", "Guam", CLOWN),
    ROBIN_FEATHER("robinFeather", "Robin Feather", "Guam", CLOWN),
    SMELLY_INNER_SOLES("smellyInnerSoles", "Smelly Inner Soles", "Guam", CLOWN),
    SOLVENT_JUG("solventJug", "Solvent Jug", "Guam", CLOWN),
    STARLING_FEATHER("starlingFeather", "Starling Feather", "Guam", CLOWN),
    STICKY_SODA_BOTTLE("stickySodaBottle", "Sticky Soda Bottle", "Guam", CLOWN),
    SULFURIC_ACID_VIAL("sulfuricAcidVial", "Sulfuric Acid Vial", "Guam", CLOWN),
    TATTOOS_MIDDLE_FINGER("tattoosMiddleFinger", "Tattoos Middle Finger", "Guam", CLOWN),
    THICK_CORK_STOPPER("thickCorkStopper", "Thick Cork Stopper", "Guam", CLOWN),
    VHS_PORN("vhsPorn", "VHS Porn", "Guam", CLOWN),
    BLOODY_HAIR_BROOCHI("bloodyHairBroochi", "Bloody Hair Broochi", "Haiti", SPIRIT),
    DIRTY_UWABAKI("dirtyUwabaki", "Dirty Uwabaki", "Haiti", SPIRIT),
    DRIED_CHERRY_BLOSSOM("driedCherryBlossom", "Dried Cherry Blossom", "Haiti", SPIRIT),
    FATHERS_GLASSES("fathersGlasses", "Father's Glasses", "Haiti", SPIRIT),
    GIFTED_BAMBOO_COMB("giftedBambooComb", "Gifted Bamboo Comb", "Haiti", SPIRIT),
    JUNIPER_BONZAI("juniperBonzai", "Juniper Bonzai", "Haiti", SPIRIT),
    KAIUN_TALISMAN("kaiunTalisman", "Kaiun Talisman", "Haiti", SPIRIT),
    KATANA_TSUBA("katanaTsuba", "Katana Tsuba", "Haiti", SPIRIT),
    KATSUMORI_TALISMAN("katsumoriTalisman", "Katsumori Talisman", "Haiti", SPIRIT),
    MOTHER_DAUGHTER_RING("motherDaughterRing", "Mother Daughter Ring", "Haiti", SPIRIT),
    MUDDY_SPORT_CAP("muddySportCap", "Muddy Sport Cap", "Haiti", SPIRIT),
    ORIGAMI_CRANE("origamiCrane", "Origami Crane", "Haiti", SPIRIT),
    PRAYERS_BEADS("prayersBeads", "Prayer's Beads", "Haiti", SPIRIT),
    RINS_BROKEN_WATCH("rinsBrokenWatch", "Rins Broken Watch", "Haiti", SPIRIT),
    RUSTY_FLUTE("rustyFlute", "Rusty Flute", "Haiti", SPIRIT),
    SHIAWASE_AMULET("ShiawaseAmulet", "Shiawase Amulet", "Haiti", SPIRIT),
    WAKIZASHI_SAYA("wakizashiSaya", "Wakizashi Saya", "Haiti", SPIRIT),
    WHITE_HAIR_RIBBON("whiteHairRibbon", "White Hair Ribbon", "Haiti", SPIRIT),
    YAKUYOKE_AMULET("yakuyokeAmulet", "Yakuyoke Amulet", "Haiti", SPIRIT),
    ZORI("zori", "Zori", "Haiti", SPIRIT),
    ABDOMINAL_DRESSING("abdominalDressing", "Abdominal Dressing"),
    ANXIOUS_GASP("anxiousGasp", "Anxious Gasp", NURSE),
    ATAXIC_RESPIRATION("ataxicRespiration", "Ataxic Respiration", NURSE),
    BAD_MAN_KEEPSAKE("badManKeepsake", "Bad Man Keepsake", NURSE),
    BAD_MANS_LAST_BREATH("badMansLastBreath", "Bad Mans Last Breath", NURSE),
    BANDAGES("bandages", "Bandages"),
    BATTERY("battery", "Battery"),
    BEAD_CRYSTAL("beadCrystal", "Bead Crystal"),
    BEAD_GLASS("beadGlass", "Bead Glass"),
    BLOOD_AMBER("bloodAmber", "Blood Amber"),
    BLOOD_KRA_FABAI("bloodKraFabai", "Blood Kra Fabai", WRAITH),
    BLOOD_SHADOW_DANCE("bloodShadowDance", "Blood Shadow Dance", WRAITH),
    BLOOD_SWIFT_HUNT("bloodSwiftHunt", "Blood Swift Hunt", WRAITH),
    BLOOD_WINDSTORM("bloodWindstorm", "Blood Windstorm", WRAITH),
    BLOODY_COIL("bloodyCoil", "Bloody Coil", TRAPPER),
    BONE_CLAPPER("boneClapper", "Bone Clapper", WRAITH),
    BRAND_NEW_PART("brandNewPart", "Brand New Part"),
    BUTTERFLY_TAPE("butterflyTape", "Butterfly Tape"),
    CAMPBELLS_LAST_BREATH("campbellsLastBreath", "Campbell's Last Breath", NURSE),
    CARBURETOR_TUNING_GUIDE("carburetorTuningGuide", "Carburetor Tuning Guide", CANNIBAL),
    CATATONIC_TREASURE("catatonicTreasure", "Catatonic Treasure", NURSE),
    CHAINSAW_FILE("chainsawFile", "Chainsaw File", CANNIBAL),
    CHAINS_BLOODY("chainsBloody", "Chains Bloody", CANNIBAL),
    CHAINS_GRISLY("chainsGrisly", "Chains Grisly", CANNIBAL),
    CHAINS_RUSTED("chainsRusted", "Chains Rusted", CANNIBAL),
    CLEAN_RAG("cleanRag", "Clean Rag"),
    COILS_KIT4("coilsKit4", "Coils Kit4", TRAPPER),
    COIL_SPRING("coilSpring", "Coil Spring", TRAPPER),
    COXCOMBED_CLAPPER("coxcombedClapper", "Coxcombed Clapper", WRAITH),
    CUTTING_WIRE("cuttingWire", "Cutting Wire"),
    DARK_CINCTURE("darkCincture", "Dark Cincture", NURSE),
    DEATH_ENGRAVINGS("deathEngravings", "Death Engravings", CANNIBAL),
    DEPTH_GAUGE_RAKE("depthGaugeRake", "Depth Gauge Rake", CANNIBAL),
    DIAMOND_STONE("diamondStone", "Diamond Stone", TRAPPER),
    DOOM_ENGRAVINGS("doomEngravings", "Doom Engravings", CANNIBAL),
    DULL_BRACELET("dullBracelet", "Dull Bracelet", NURSE),
    FASTENING_TOOLS("fasteningTools", "Fastening Tools", TRAPPER),
    FOCUS_LENS("focusLens", "Focus Lens"),
    FRAGILE_WHEEZE("fragileWheeze", "Fragile Wheeze", NURSE),
    GAUSE_ROLL("gauseRoll", "Gause Roll"),
    GEL_DRESSINGS("gelDressings", "Gel Dressings"),
    GLOVES("gloves", "Gloves"),
    GRIP_WRENCH("gripWrench", "Grip Wrench"),
    HEAVY_DUTY_BATTERY("heavyDutyBattery", "Heavy Duty Battery"),
    HEAVY_PANTING("heavyPanting", "Heavy Panting", NURSE),
    HIGH_END_SAPPHIRE_LENS("highEndSapphireLens", "High End Sapphire Lens"),
    HOMEMADE_MUFFLER("homemadeMuffler", "Homemade Muffler", CANNIBAL),
    HONING_STONE("honingStone", "Honing Stone", TRAPPER),
    INSTRUCTIONS("instructions", "Instructions"),
    INTENSE_HALOGEN("intenseHalogen", "Intense Halogen"),
    JENNERS_LAST_BREATH("jennersLastBreath", "Jenners Last Breath", NURSE),
    KAVANAGHS_LAST_BREATH("kavanaghsLastBreath", "Kavanaghs Last Breath", NURSE),
    LEATHER_GRIP("leatherGrip", "Leather Grip"),
    LIGHT_CHASSIS("lightChassis", "Light Chassis", CANNIBAL),
    LOGWOOD_DYE("logwoodDye", "Logwood Dye", TRAPPER),
    LONG_GUIDE_BAR("longGuideBar", "Long Guide Bar", CANNIBAL),
    LONG_LIFE_BATTERY("longLifeBattery", "Long Life Battery"),
    MAP_ADDENDUM("mapAddendum", "Map Addendum"),
    MATCH_BOX("matchBox", "Match Box", NURSE),
    METAL_SAW("metalSaw", "Metal Saw", NURSE),
    METAL_SPOON("metalSpoon", "Metal Spoon", NURSE),
    MILKY_GLASS("milkyGlass", "Milky Glass"),
    MUD_BAIKRA_KAEUG("mudBaikraKaeug", "Mud Baikra Kaeug", WRAITH),
    MUD_BLINK("mudBlink", "Mud Blink", WRAITH),
    MUD_SWIFT_HUNT("mudSwiftHunt", "Mud Swift Hunt", WRAITH),
    MUD_WINDSTORM("mudWindstorm", "Mud Windstorm", WRAITH),
    NEED_AND_THREAD("needAndThread", "Need And Thread"),
    ODD_BULB("oddBulb", "Odd Bulb"),
    OILY_COIL("oilyCoil", "Oily Coil", TRAPPER),
    PADDED_JAWS("paddedJaws", "Padded Jaws", TRAPPER),
    PLAID_FLANNEL("plaidFlannel", "Plaid Flannel", NURSE),
    POCKET_WATCH("pocketWatch", "Pocket Watch", NURSE),
    POWER_BULB("powerBulb", "Power Bulb"),
    PRAYER_BEADS("prayerBeads", "Prayer Beads"),
    PRAYER_ROPE("prayerRope", "Prayer Rope"),
    PRIMER_BULB("primerBulb", "Primer Bulb", CANNIBAL),
    PROTECTIVE_GLOVES("protectiveGloves", "Protective Gloves"),
    RETARDANT_JELLY("retardantJelly", "Retardant Jelly"),
    ROPE_BLACK("ropeBlack", "Rope Black"),
    ROPE_RED("ropeRed", "Rope Red"),
    ROPE_YELLOW("ropeYellow", "Rope Yellow"),
    RUBBER_GRIP("rubberGrip", "Rubber Grip"),
    RUSTED_JAWS("rustedJaws", "Rusted Jaws", TRAPPER),
    SCISSORS("scissors", "Scissors"),
    SCRAPS("scraps", "Scraps"),
    SCRATCHED_PEARL("scratchedPearl", "Scratched Pearl"),
    SECONDARY_COIL("secondaryCoil", "Secondary Coil", TRAPPER),
    SELF_ADHERENT_WRAP("selfAdherentWrap", "Self Adherent Wrap"),
    SERRATED_JAWS("serratedJaws", "Serrated Jaws", TRAPPER),
    SETTING_TOOLS("settingTools", "Setting Tools", TRAPPER),
    SHOP_LUBRICANT("shopLubricant", "Shop Lubricant", CANNIBAL),
    SOCKET_SWIVELS("socketSwivels", "Socket Swivels"),
    SOOT_SHADOW_DANCE("sootShadowDance", "Soot Shadow Dance", WRAITH),
    SOOT_THE_BEAST("sootTheBeast", "Soot The Beast", WRAITH),
    SOOT_THE_GHOST("sootTheGhost", "Soot The Ghost", WRAITH),
    SOOT_THE_HOUND("sootTheHound", "Soot The Hound", WRAITH),
    SOOT_THE_SERPENT("sootTheSerpent", "Soot The Serpent", WRAITH),
    SPARK_PLUG("sparkPlug", "Spark Plug", CANNIBAL),
    SPASMODIC_BREATH("spasmodicBreath", "Spasmodic Breath", NURSE),
    SPEED_LIMITER("speedLimiter", "Speed Limiter", CANNIBAL),
    SPIKED_BOOTS("spikedBoots", "Spiked Boots", TRAPPER),
    SPIRIT_ALL_SEEING("spiritAllSeeing", "Spirit All Seeing", WRAITH),
    SPONGE("sponge", "Sponge"),
    SPOOL_OF_WIRE("spoolOfWire", "Spool Of Wire"),
    SPRING_CLAMP("springClamp", "Spring Clamp"),
    STAMP_ODD("stampOdd", "Stamp Odd"),
    STAMP_UNUSUAL("stampUnusual", "Stamp Unusual"),
    STICHED_BAG("stichedBag", "Stiched Bag", TRAPPER),
    STYPTIC_AGENT("stypticAgent", "Styptic Agent"),
    SURGICAL_SUTURE("surgicalSuture", "Surgical Suture"),
    SYRINGE("syringe", "Syringe"),
    TAP_SETTERS("tapSetters", "Trap Setters", TRAPPER),
    TAR_BOTTLE("tarBottle", "Tar Bottle", TRAPPER),
    THE_THOMPSONS_MIX("theThompsonsMix", "The Thompson's Mix", CANNIBAL),
    THOMPSONS_MOONSHINE("thompsonsMoonshine", "Thompson's Moonshine", CANNIBAL),
    THREADED_FILAMENT("threadedFilament", "Threaded Filament"),
    TIR_OPTIC("tirOptic", "Tir Optic"),
    TOKEN_ERRODED("tokenErroded", "Token Erroded"),
    TOKEN_GOLD("tokenGold", "Token Gold"),
    TORN_BOOKMARK("tornBookmark", "Torn Bookmark", NURSE),
    TRAPPER_BAG("trapperBag", "Trapper Bag", TRAPPER),
    TRAPPER_GLOVES("trapperGloves", "Trapper Gloves", TRAPPER),
    TRAPPER_SACK("trapperSack", "Trapper Sack", TRAPPER),
    UNIQUE_RING("uniqueRing", "Unique Ring"),
    VEGETABLE_OIL("vegetableOil", "Vegetable Oil", CANNIBAL),
    WAX_BRICK("waxBrick", "Wax Brick", TRAPPER),
    WEAVED_RING("weavedRing", "Weaved Ring"),
    WHITE_BLIND_WARRIOR("whiteBlindWarrior", "White Blind Warrior", WRAITH),
    WHITE_BLINK("whiteBlink", "White Blink", WRAITH),
    WHITE_KUNTIN_TAKKHO("whiteKuntinTakkho", "White Kuntin Takkho", WRAITH),
    WHITE_NIT_COMB("whiteNitComb", "White Nit Comb", NURSE),
    WHITE_SHADOW_DANCE("whiteShadowDance", "White Shadow Dance", WRAITH),
    WHITE_WINDSTORM("whiteWindstorm", "White Windstorm", WRAITH),
    WIDE_LENS("wideLens", "Wide Lens"),
    WOODEN_HORSE("woodenHorse", "Wooden Horse", NURSE),
    COLD_DIRT("coldDirt", "Cold Dirt", "Kenya", LEGION),
    DEFACED_SMILEY_BUTTON("defacedSmileyButton", "Defaced Smiley Button", "Kenya", LEGION),
    ETCHED_RULER("etchedRuler", "Etched Ruler", "Kenya", LEGION),
    FILTHY_BLADE("filthyBlade", "Filthy Blade", "Kenya", LEGION),
    FRANKS_MIXTAPE("franksMixtape", "Franks Mixtape", "Kenya", LEGION),
    FRIENDSHIP_BRACELET("friendshipBracelet", "Friendship Bracelet", "Kenya", LEGION),
    FUMING_MIXTAPE("fumingMixtape", "Fuming Mixtape", "Kenya", LEGION),
    IRIDESCENT_BUTTON("iridescentButton", "Iridescent Button", "Kenya", LEGION),
    JOEYS_MIXTAPE("joeysMixtape", "Joey's Mixtape", "Kenya", LEGION),
    JULIES_MIXTAPE("juliesMixtape", "Julie's Mixtape", "Kenya", LEGION),
    MISCHIEF_LIST("mischiefList", "Mischief List", "Kenya", LEGION),
    MURAL_SKETCH("muralSketch", "Mural Sketch", "Kenya", LEGION),
    NASTY_BLADE("nastyBlade", "Nasty Blade", "Kenya", LEGION),
    NEVER_SLEEP_PILLS("neverSleepPills", "Never Sleep Pills", "Kenya", LEGION),
    SCRATCHED_RULER("scratchedRuler", "Scratched Ruler", "Kenya", LEGION),
    SMILEY_FACE_BUTTON("smileyFaceButton", "Smiley Face Button", "Kenya", LEGION),
    STAB_WOUNDS_STUDY("stabWoundsStudy", "Stab Wounds Study", "Kenya", LEGION),
    STOLEN_SKETCHBOOK("stolenSketchbook", "Stolen Sketchbook", "Kenya", LEGION),
    SUZIES_MIXTAPE("suziesMixtape", "Suzies Mixtape", "Kenya", LEGION),
    THE_LEGION_BUTTON("theLegionButton", "The Legion Button", "Kenya", LEGION),
    ASHEN_APPLE("ashenApple", "Ashen Apple", "Mali", PLAGUE),
    BLACK_INCENSE("blackIncense", "Black Incense", "Mali", PLAGUE),
    DEVOTEES_AMULET("devoteesAmulet", "Devotee's Amulet", "Mali", PLAGUE),
    EMETIC_POTION("emeticPotion", "Emetic Potion", "Mali", PLAGUE),
    EXORCISM_AMULET("exorcismAmulet", "Exorcism Amulet", "Mali", PLAGUE),
    HEALING_SALVE("healingSalve", "Healing Salve", "Mali", PLAGUE),
    HEMATITE_SEAL("hematiteSeal", "Hematite Seal", "Mali", PLAGUE),
    INCENSED_OINTMENT("incensedOintment", "Incensed Ointment", "Mali", PLAGUE),
    INFECTED_EMETIC("infectedEmetic", "Infected Emetic", "Mali", PLAGUE),
    IRIDESCENT_SEAL("IridescentSeal", "Iridescent Seal", "Mali", PLAGUE),
    LIMESTONE_SEAL("limestoneSeal", "Limestone Seal", "Mali", PLAGUE),
    OLIBANUM_INCENSE("olibanumIncense", "Olibanum Incense", "Mali", PLAGUE),
    POTENT_TINCTURE("potentTincture", "Potent Tincture", "Mali", PLAGUE),
    PRAYER_APPLE("prayerApple", "Prayer Apple", "Mali", PLAGUE),
    PRAYER_TABLET_FRAGMENT("prayerTabletFragment", "Prayer Tablet Fragment", "Mali", PLAGUE),
    PROPHYLACTIC_AMULET("prophylacticAmulet", "Prophylactic Amulet", "Mali", PLAGUE),
    RUBBING_OIL("rubbingOil", "Rubbing Oil", "Mali", PLAGUE),
    SEVERED_TOE("severedToe", "Severed Toe", "Mali", PLAGUE),
    VILE_EMETIC("vileEmetic", "Vile Emetic", "Mali", PLAGUE),
    WORSHIP_TABLET("worshipTablet", "Worship Tablet", "Mali", PLAGUE),
    CAUGHT_ON_TAPE("caughtOnTape", "Caught On Tape", "Oman", GHOST_FACE),
    CHEAP_COLOGNE("cheapCologne", "Cheap Cologne", "Oman", GHOST_FACE),
    CHEWED_PEN("chewedPen", "Chewed Pen", "Oman", GHOST_FACE),
    DRIVERS_LICENSE("driversLicense", "Drivers License", "Oman", GHOST_FACE),
    DROP_LEG_KNIFE_SHEATH("dropLegKnifeSheath", "Drop Leg Knife Sheath", "Oman", GHOST_FACE),
    HEADLINES_CUTOUTS("headlinesCutouts", "Headlines Cutouts", "Oman", GHOST_FACE),
    KNIFE_BELT_CLIP("knifeBeltClip", "Knife Belt Clip", "Oman", GHOST_FACE),
    LASTING_PERFUME("lastingPerfume", "Lasting Perfume", "Oman", GHOST_FACE),
    LEATHER_KNIFE_SHEATH("leatherKnifeSheath", "Leather Knife Sheath", "Oman", GHOST_FACE),
    MARKED_MAP("markedMap", "Marked Map", "Oman", GHOST_FACE),
    NIGHTVISION_MONCULAR("nightvisionMoncular", "Nightvision Moncular", "Oman", GHOST_FACE),
    OLSENS_ADDRESS_BOOK("olsensAddressBook", "Olsen's Address Book", "Oman", GHOST_FACE),
    OLSENS_JOURNAL("olsensJournal", "Olsen's Journal", "Oman", GHOST_FACE),
    OLSENS_WALLET("olsensWallet", "Olsen's Wallet", "Oman", GHOST_FACE),
    OUTDOOR_SECURITY_CAMERA("outdoorSecurityCamera", "Outdoor Security Camera", "Oman", GHOST_FACE),
    PHILLY("philly", "Philly", "Oman", GHOST_FACE),
    REUSUABLE_CINCH_STRAPS("reusuableCinchStraps", "Reusuable Cinch Straps", "Oman", GHOST_FACE),
    TELEPHOTO_LENS("telephotoLens", "Telephoto Lens", "Oman", GHOST_FACE),
    VICTIMS_DETAILED_ROUTINE("victimsDetailedRoutine", "Victim's Detailed Routine", "Oman", GHOST_FACE),
    WALLEYES_MATCHBOOK("walleyesMatchbook", "Walleyes Matchbook", "Oman", GHOST_FACE),
    BARBS_GLASSES("barbsGlasses", "Barb's Glasses", "Qatar", DEMOGORGON),
    BLACK_HEART("blackHeart", "Black Heart", "Qatar", DEMOGORGON),
    BRASS_CASE_LIGHTER("brassCaseLighter", "Brass Case Lighter", "Qatar", DEMOGORGON),
    DEER_LUNG("deerLung", "Deer Lung", "Qatar", DEMOGORGON),
    ELEVENS_SODA("elevensSoda", "Eleven's Soda", "Qatar", DEMOGORGON),
    LEPROSE_LICHEN("leproseLichen", "Leprose Lichen", "Qatar", DEMOGORGON),
    LIFEGUARD_WHISTLE("lifeguardWhistle", "Lifeguard Whistle", "Qatar", DEMOGORGON),
    MEWS_GUTS("mewsGuts", "Mews Guts", "Qatar", DEMOGORGON),
    RAT_LIVER("ratLiver", "Rat Liver", "Qatar", DEMOGORGON),
    RAT_TAIL("ratTail", "Rat Tail", "Qatar", DEMOGORGON),
    RED_MOSS("redMoss", "Red Moss", "Qatar", DEMOGORGON),
    ROTTEN_GREEN_TRIPE("rottenGreenTripe", "Rotten Green Tripe", "Qatar", DEMOGORGON),
    ROTTEN_PUMPKIN("rottenPumpkin", "Rotten Pumpkin", "Qatar", DEMOGORGON),
    STICKY_LINING("stickyLining", "Sticky Lining", "Qatar", DEMOGORGON),
    THORNY_VINES("thornyVines", "Thorny Vines", "Qatar", DEMOGORGON),
    UNKNOWN_EGG("unknownEgg", "Unknown Egg", "Qatar", DEMOGORGON),
    UPSIDEDOWN_RESIN("upsidedownResin", "Upsidedown Resin", "Qatar", DEMOGORGON),
    VERMILLION_WEBCAP("vermillionWebcap", "Vermillion Webcap", "Qatar", DEMOGORGON),
    VIOLET_WAXCAP("violetWaxcap", "Violet Waxcap", "Qatar", DEMOGORGON),
    VISCOUS_WEBBING("viscousWebbing", "Viscous Webbing", "Qatar", DEMOGORGON),
    AKITOS_CRUTCH("akitosCrutch", "Akitos Crutch", "Sweden", ONI),
    BLACKENED_TOENAIL("blackenedToenail", "Blackened Toenail", "Sweden", ONI),
    BLOODY_SASH("bloodySash", "Bloody Sash", "Sweden", ONI),
    CHILDS_WOODEN_SWORD("childsWoodenSword", "Child's Wooden Sword", "Sweden", ONI),
    CHIPPED_SAIHAI("chippedSaihai", "Chipped Saihai", "Sweden", ONI),
    CRACKED_SAKAZUKI("crackedSakazuki", "Cracked Sakazuki", "Sweden", ONI),
    INK_LION("inkLion", "Ink Lion", "Sweden", ONI),
    IRIDESCENT_FAMILY_CREST("IridescentFamilyCrest", "Iridescent Family Crest", "Sweden", ONI),
    KANAIANZEN_TALISMAN("kanaianzenTalisman", "Kanaianzen Talisman", "Sweden", ONI),
    LION_FANG("lionFang", "Lion Fang", "Sweden", ONI),
    PAPER_LANTERN("paperLantern", "Paper Lantern", "Sweden", ONI),
    POLISHED_MAEDATE("polishedMaedate", "Polished Maedate", "Sweden", ONI),
    RENIROS_BLOODY_GLOVE("renirosBloodyGlove", "Reniros Bloody Glove", "Sweden", ONI),
    ROTTING_ROPE("rottingRope", "Rotting Rope", "Sweden", ONI),
    SCALPED_TOPKNOT("scalpedTopknot", "Scalped Topknot", "Sweden", ONI),
    SHATTERED_WAKIZASHI("shatteredWakizashi", "Shattered Wakizashi", "Sweden", ONI),
    SPLINTERED_HULL("splinteredHull", "Splintered Hull", "Sweden", ONI),
    TEAR_SOAKED_TENUGUI("tearSoakedTenugui", "Tear Soaked Tenugui", "Sweden", ONI),
    WOODEN_ONI_MASK("woodenOniMask", "Wooden Oni Mask", "Sweden", ONI),
    YAMAOKA_SASHIMONO("yamaokaSashimono", "Yamaoka Sashimono", "Sweden", ONI),
    BARBED_WIRE("barbedWire", "Barbed Wire", "Ukraine", DEATHSLINGER),
    BAYSHORES_CIGAR("bayshoresCigar", "Bayshore's Cigar", "Ukraine", DEATHSLINGER),
    BAYSHORES_GOLD_TOOTH("bayshoresGoldTooth", "Bayshore's Gold Tooth", "Ukraine", DEATHSLINGER),
    CHEWING_TOBACCO("chewingTobacco", "Chewing Tobacco", "Ukraine", DEATHSLINGER),
    CLEAR_CREEK_WHISKEY("clearCreekWhiskey", "Clear Creek Whiskey", "Ukraine", DEATHSLINGER),
    HELLSHIRE_IRON("hellshireIron", "Hellshire Iron", "Ukraine", DEATHSLINGER),
    HONEY_LOCUST_THORNS("honeyLocustThorns", "Honey Locust Thorns", "Ukraine", DEATHSLINGER),
    IRIDESCENT_COIN("iridescentCoin", "Iridescent Coin", "Ukraine", DEATHSLINGER),
    JAW_SMASHER("jawSmasher", "Jaw Smasher", "Ukraine", DEATHSLINGER),
    MARSHALS_BADGE("marshalsBadge", "Marshal's Badge", "Ukraine", DEATHSLINGER),
    MODIFIED_AMMO_BELT("modifiedAmmoBelt", "Modified Ammo Belt", "Ukraine", DEATHSLINGER),
    POISON_OAK_LEAVES("poisonOakLeaves", "Poison Oak Leaves", "Ukraine", DEATHSLINGER),
    PRISON_CHAIN("prisonChain", "Prison Chain", "Ukraine", DEATHSLINGER),
    RICKETY_CHAIN("ricketyChain", "Rickety Chain", "Ukraine", DEATHSLINGER),
    RUSTED_SPIKE("rustedSpike", "Rusted Spike", "Ukraine", DEATHSLINGER),
    SNAKE_OIL("snakeOil", "Snake Oil", "Ukraine", DEATHSLINGER),
    SPIT_POLISH_RAG("spitPolishRag", "Spit Polish Rag", "Ukraine", DEATHSLINGER),
    TIN_OIL_CAN("tinOilCan", "Tin Oil Can", "Ukraine", DEATHSLINGER),
    WANTED_POSTER("wantedPoster", "Wanted Poster", "Ukraine", DEATHSLINGER),
    WARDENS_KEYS("wardensKeys", "Warden's Keys", "Ukraine", DEATHSLINGER),
    BLACK_STRAP("blackStrap", "Black Strap", "Wales", EXECUTIONER),
    BURNING_MAN_PAINTING("burningManPainting", "Burning Man Painting", "Wales", EXECUTIONER),
    CINDERELLA_MUSIC_BOX("cinderellaMusicBox", "Cinderella Music Box", "Wales", EXECUTIONER),
    COPPER_RING("copperRing", "Copper Ring", "Wales", EXECUTIONER),
    CRIMSON_CEREMONY_BOOK("crimsonCeremonyBook", "Crimson Ceremony Book", "Wales", EXECUTIONER),
    DEAD_BUTTERFLY("deadButterfly", "Dead Butterfly", "Wales", EXECUTIONER),
    FORGOTTEN_VIDEO_TAPE("forgottenVideoTape", "Forgotten Video Tape", "Wales", EXECUTIONER),
    IRIDESCENT_SEAL_2("iridescentSeal", "Iridescent Seal", "Wales", EXECUTIONER),
    LEAD_RING("leadRing", "Lead Ring", "Wales", EXECUTIONER),
    LEOPARD_PRINT_FABRIC("leopardPrintFabric", "Leopard Print Frabric", "Wales", EXECUTIONER),
    LOST_MEMORIES_BOOK("lostMemoriesBook", "Lost Memories Book", "Wales", EXECUTIONER),
    MANNEQUIN_FOOT("mannequinFoot", "Mannequin Foot", "Wales", EXECUTIONER),
    MISTY_DAY("mistyDay", "Misty Day", "Wales", EXECUTIONER),
    OBSIDIAN_GOBLET("obsidianGoblet", "Obsidian Goblet", "Wales", EXECUTIONER),
    RUST_COLORED_EGG("rustColoredEgg", "Rust Colored Egg", "Wales", EXECUTIONER),
    SCARLET_EGG("scarletEgg", "Scarlet Egg", "Wales", EXECUTIONER),
    SPEARHEAD("spearhead", "Spearhead", "Wales", EXECUTIONER),
    TABLET_OF_THE_OPPRESSOR("tabletOfTheOppressor", "Tablet of the Oppressor", "Wales", EXECUTIONER),
    VALTIEL_SECT_PHOTOGRAPH("valtielSectPhotograph", "Valtiel Sect. Photograph", "Wales", EXECUTIONER),
    WAX_DOLL("waxDoll", "Wax Doll", "Wales", EXECUTIONER);

    private final String name;
    private final String subFolder;
    private final String properName;
    private final Character character;
    public static final Set<Addon> GENERIC_ADDONS;
    public static final Map<Killer, Set<Addon>> KILLER_LOOKUP;

    static {
        Set<Addon> generics = new HashSet<>();
        Map<Killer, Set<Addon>> lookup = new EnumMap<>(Killer.class);
        for (Killer killer : Killer.values()) {
            Set<Addon> addons = new HashSet<>();
            for (Addon addon : values()) {
                Character c = addon.getCharacter();
                if (c != null) {
                    if (c.getName().equalsIgnoreCase(killer.getName()))
                        addons.add(addon);
                } else
                    generics.add(addon);
            }
            lookup.put(killer, addons);
        }
        KILLER_LOOKUP = Collections.unmodifiableMap(lookup);
        GENERIC_ADDONS = Collections.unmodifiableSet(generics);
    }

    @ParametersAreNonnullByDefault
    Addon(String name, String properName) {
        this(name, properName, "");
    }

    @ParametersAreNonnullByDefault
    Addon(String name, String properName, Character character) {
        this(name, properName, "", character);
    }

    Addon(@Nonnull String name, @Nonnull String properName, @Nonnull String subFolder) {
        this(name, properName, subFolder, null);
    }

    Addon(@Nonnull String name, @Nonnull String properName, @Nonnull String subFolder, @Nullable Character character) {
        this.name = name;
        this.properName = properName;
        this.subFolder = subFolder;
        this.character = character;
    }

    @Nonnull
    @Override
    public IconCategory getCategory() {
        return IconCategory.ADDON;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Nonnull
    @Override
    public String getFileAdditive() {
        return "iconAddon_";
    }

    @Nonnull
    @Override
    public String getProperName() {
        return properName;
    }

    @Nonnull
    @Override
    public String getSubfolderName() {
        return "ItemAddons\\" + subFolder;
    }

    @Nullable
    @Override
    public Character getCharacter() {
        return character;
    }

    @Nonnull
    public static Set<Addon> getAddons(@Nonnull Killer killer) {
        return KILLER_LOOKUP.get(killer);
    }
}