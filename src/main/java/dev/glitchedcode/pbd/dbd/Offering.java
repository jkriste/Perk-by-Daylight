package dev.glitchedcode.pbd.dbd;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public enum Offering implements Icon {

    BLOODY_PARTY_STREAMERS("bloodyPartyStreamers", "Bloody Party Streamers", "Anniversary"),
    ESCAPE_CAKE("escapeCake", "Escape Cake", "Anniversary"),
    GRUESOME_GATEAU("gruesomeGateau", "Gruesome Gateau", "Anniversary"),
    SURVIVOR_PUDDING("survivorPudding", "Survivor Pudding", "Anniversary"),
    BONE_SPLINTER("boneSplinter", "Bone Splinter", "Cannibal"),
    BLACK_SPLINTER("blackSplinter", "Black Splinter", "DLC2"),
    DECREPIT_CLAPBOARD("decrepitClapboard", "Decrepit Clapboard", "DLC2"),
    HARVEST_FESTIVAL_LEAFLET("harvestFestivalLeaflet", "Harvest Festival Leaflet", "DLC2"),
    STRODE_REALTY_KEY("strodeRealtyKey", "Strode Realty Key", "DLC2"),
    FUMING_CORDAGE("fumingCordage", "Fuming Cordage", "DLC3"),
    FUMING_WELCOME_SIGN("fumingWelcomeSign", "Fuming Welcome Sign", "DLC3"),
    GRANMAS_COOKBOOK("granmasCookbook", "Granmas Cookbook", "DLC3"),
    MUDDY_SPLINTER("muddySplinter", "Muddy Splinter", "DLC3"),
    EMERGENCY_CERTIFICATE("emergencyCertificate", "Emergency Certificate", "DLC4"),
    PSYCHIATRIC_ASSESSMENT_REPORT("psychiatricAssessmentReport", "Psychiatric Assessment Report", "DLC4"),
    SHATTERED_GLASSES("shatteredGlasses", "Shattered Glasses", "DLC4"),
    SHOCK_SPLINTER("shockSplinter", "Shock Splinter", "DLC4"),
    CHILDRENS_BOOK("childrensBook", "Childrens Book", "DLC5"),
    PAINTED_RIVER_ROCK("paintedRiverRock", "Painted River Rock", "DLC5"),
    THE_LAST_MASK("theLastMask", "The Last Mask", "DLC5"),
    SMOKING_SPLINTER("smokingSplinter", "Smoking Splinter", "England"),
    THE_PIED_PIPER("thePiedPiper", "The Pied Piper", "England"),
    GLASS_SPLINTER("glassSplinter", "Glass Splinter", "Finland"),
    JIGSAW_PIECE("jigsawPiece", "Jigsaw Piece", "Finland"),
    YAMAOKAS_CREST("yamaokasCrest", "Yamaokas Crest", "Haiti"),
    PUSTULA_PETALS("pustulaPetals", "Pustula Petals", "Halloween"),
    ARDENT_RAVEN_WREATH("ardentRavenWreath", "Ardent Raven Wreath"),
    ARDENT_SHRIKE_WREATH("ardentShrikeWreath", "Ardent Shrike Wreath"),
    ARDENT_SPOTTED_OWL_WREATH("ardentSpottedOwlWreath", "Ardent Spotted Owl Wreath"),
    ARDENT_TANAGER_WREATH("ardentTanagerWreath", "Ardent Tanager Wreath"),
    AZAROVS_KEY("azarovsKey", "Azarov's Key"),
    BLACK_SALT_STATUETTE("blackSaltStatuette", "Black Salt Statuette"),
    BOG_LAUREL_SACHET("bogLaurelSachet", "Bog Laurel Sachet"),
    BOUND_ENVELOPE("boundEnvelope", "Bound Envelope"),
    CATTLE_TAG28("cattleTag28", "Cattle Tag 28"),
    CATTLE_TAG81("cattleTag81", "Cattle Tag 81"),
    CHALK_POUCH("chalkPouch", "Chalk Pouch"),
    CHARRED_WEDDING_PHOTOGRAPH("charredWeddingPhotograph", "Charred Wedding Photograph"),
    CLEAR_REAGENT("clearReagent", "Clear Reagent"),
    CREAM_CHALK_POUCH("creamChalkPouch", "Cream Chalk Pouch"),
    CRECENT_MOON_BOUQUET("crecentMoonBouquet", "Crecent Moon Bouquet"),
    CRISPLEAF_AMARANTH_SACHET("crispleafAmaranthSachet", "Crispleaf Amaranth Sachet"),
    CUT_COIN("cutCoin", "Cut Coin"),
    DEVOUT_RAVEN_WREATH("devoutRavenWreath", "Devout Raven Wreath"),
    DEVOUT_SHRIKE_WREATH("devoutShrikeWreath", "Devout Shrike Wreath"),
    DEVOUT_SPOTTED_OWL_WREATH("devoutSpottedOwlWreath", "Devout Spotted Owl Wreath"),
    DEVOUT_TANAGER_WREATH("devoutTanagerWreath", "Devout Tanager Wreath"),
    FAINT_REAGENT("faintReagent", "Faint Reagent"),
    FRAGRANT_BOG_LAUREL("fragrantBogLaurel", "Fragrant Bog Laurel"),
    FRAGRANT_CRISPLEAF_AMARANTH("fragrantCrispleafAmaranth", "Fragrant Crispleaf Amaranth"),
    FRAGRANT_PRIMROSE_BLOSSOM("fragrantPrimroseBlossom", "Fragrant Primrose Blossom"),
    FRAGRANT_SWEET_WILLIAM("fragrantSweetWilliam", "Fragrant Sweet William"),
    FRESH_BOG_LAUREL("freshBogLaurel", "Fresh Bog Laurel"),
    FRESH_CRISPLEAF_AMARANTH("freshCrispleafAmaranth", "Fresh Crispleaf Amaranth"),
    FRESH_PRIMROSE_BLOSSOM("freshPrimroseBlossom", "Fresh Primrose Blossom"),
    FRESH_SWEET_WILLIAM("freshSweetWilliam", "Fresh Sweet William"),
    FULL_MOON_BOUQUET("fullMoonBouquet", "Full Moon Bouquet"),
    HAZY_REAGENT("hazyReagent", "Hazy Reagent"),
    HEART_LOCKET("heartLocket", "Heart Locket"),
    HOLLOW_SHELL("hollowShell", "Hollow Shell"),
    IVORY_CHALK_POUCH("ivoryChalkPouch", "Ivory Chalk Pouch"),
    JAR_OF_SALTY_LIPS("jarOfSaltyLips", "Jar Of Salty Lips"),
    LUNACY_TICKET("lunacyTicket", "Lunacy Ticket"),
    MACMILLIAN_LEDGER_PAGE("macmillianLedgerPage", "Macmillian Ledger Page"),
    MACMILLIANS_PHALANX_BONE("macmilliansPhalanxBone", "Macmillians Phalanx Bone"),
    MOLDY_OAK("moldyOak", "Moldy Oak"),
    MOMENTO_MORI_CYPRESS("momentoMoriCypress", "Momento Mori Cypress"),
    MOMENTO_MORI_EBONY("momentoMoriEbony", "Momento Mori Ebony"),
    MOMENTO_MORI_IVORY("momentoMoriIvory", "Momento Mori Ivory"),
    MURKY_REAGENT("murkyReagent", "Murky Reagent"),
    NEW_MOON_BOUQUET("newMoonBouquet", "New Moon Bouquet"),
    P_ELLIOTT_LUNACY_TICKET("pElliottLunacyTicket", "P. Elliott Lunacy Ticket"),
    PETRIFIED_OAK("petrifiedOak", "Petrified Oak"),
    PLATE_SHREDDED("plateShredded", "Plate Shredded"),
    PLATE_VIRGINIA("plateVirginia", "Plate Virginia"),
    PRIMROSE_BLOSSOM_SACHET("primroseBlossomSachet", "Primrose Blossom Sachet"),
    PUTRID_OAK("putridOak", "Putrid Oak"),
    QUARTER_MOON_BOUQUET("quarterMoonBouquet", "Quarter Moon Bouquet"),
    RAVEN_WREATH("ravenWreath", "Raven Wreath"),
    ROTTEN_OAK("rottenOak", "Rotten Oak"),
    SALT_POUCH("saltPouch", "Salt Pouch"),
    SCRATCHED_COIN("scratchedCoin", "Scratched Coin"),
    SEALED_ENVELOPE("sealedEnvelope", "Sealed Envelope"),
    SHINY_COIN("shinyCoin", "Shiny Coin"),
    SHRIKE_WREATH("shrikeWreath", "Shrike Wreath"),
    SHROUD_OF_BINDING("shroudOfBinding", "Shroud Of Binding"),
    SHROUD_OF_SEPARATION("shroudOfSeparation", "Shroud Of Separation"),
    SHROUD_OF_UNION("shroudOfUnion", "Shroud Of Union"),
    SIGNED_LEDGER_PAGE("signedLedgerPage", "Signed Ledger Page"),
    SPOTTED_OWL_WREATH("spottedOwlWreath", "Spotted Owl Wreath"),
    SWEET_WILLIAM_SACHET("sweetWilliamSachet", "Sweet William Sachet"),
    TANAGER_WREATH("tanagerWreath", "Tanager Wreath"),
    TARNISHED_COIN("tarnishedCoin", "Tarnished Coin"),
    VIGOS_SHROUD("vigosShroud", "Vigos Shroud"),
    WARD_BLACK("wardBlack", "Ward Black"),
    WARD_WHITE("wardWhite", "Ward White"),
    DAMAGED_PHOTO("damagedPhoto", "Damaged Photo", "Kenya"),
    RED_MONEY_PACKET("redMoneyPacket", "Red Money Packet", "LunarNewYear"),
    HAWKINS_NATIONAL_LABORATORY_ID("hawkinsNationalLaboratoryID", "Hawkins National Laboratory ID", "Qatar"),
    BBQ_INVITATION("bbqInvitation", "Bbq Invitation", "Summer"),
    DUSTY_NOOSE("dustyNoose", "Dusty Noose", "Ukraine");

    private final String name;
    private final String subFolder;
    private final String properName;
    public static final Offering[] VALUES = values();

    @ParametersAreNonnullByDefault
    Offering(String name, String properName) {
        this(name, properName, "");
    }

    @ParametersAreNonnullByDefault
    Offering(String name, String properName, String subFolder) {
        this.name = name;
        this.properName = properName;
        this.subFolder = subFolder;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Nonnull
    @Override
    public String getFileAdditive() {
        return "iconFavors_";
    }

    @Nonnull
    @Override
    public String getProperName() {
        return properName;
    }

    @Nonnull
    @Override
    public String getSubfolderName() {
        return "Favors\\" + subFolder;
    }

    @Nullable
    @Override
    public Character getCharacter() {
        return null;
    }

    @Nullable
    public static Offering fromName(@Nonnull String name) {
        for (Offering offering : VALUES) {
            if (offering.getName().equalsIgnoreCase(name))
                return offering;
        }
        return null;
    }

    @Nonnull
    public static Offering fromName(@Nonnull String name, @Nonnull Offering defaultVal) {
        for (Offering offering : VALUES) {
            if (offering.getName().equalsIgnoreCase(name))
                return offering;
        }
        return defaultVal;
    }

    @Nullable
    public static Offering fromProperName(@Nonnull String properName) {
        for (Offering offering : VALUES) {
            if (offering.getProperName().equalsIgnoreCase(properName))
                return offering;
        }
        return null;
    }
}