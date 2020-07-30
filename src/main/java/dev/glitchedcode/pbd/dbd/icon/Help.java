package dev.glitchedcode.pbd.dbd.icon;

import dev.glitchedcode.pbd.dbd.Character;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public enum Help implements Icon {

    ADDONS("addons", "Addons"),
    ALTRUISM("altruism", "Altruism"),
    ARCHIVES_GENERAL("archivesGeneral", "Archives General"),
    ARCHIVES_QUESTS("archivesQuests", "Archives Quests"),
    ARCHIVES_REWARDS("archivesRewards", "Archives Rewards"),
    AURAS("auras", "Auras"),
    BASEMENT("basement", "Basement"),
    BLOODLUST("bloodlust", "Bloodlust"),
    BLOODPOINTS("bloodpoints", "Bloodpoints"),
    BLOOD_STAINS("bloodStains", "Blood Stains"),
    BLOODWEB("bloodweb", "Bloodweb"),
    BREAKING_GENERATORS("breakingGenerators", "Breaking Generators"),
    CARRY_SURVIVOR("carrySurvivor", "Carry Survivor"),
    CHASE("chase", "Chase"),
    CHESTS("chests", "Chests"),
    COOPERATION("cooperation", "Cooperation"),
    CROWS("crows", "Crows"),
    DAILY_RITUALS("dailyRituals", "Daily Rituals"),
    DBD_LOGO("DBDlogo", "DBD Logo"),
    DYING("dying", "Dying"),
    END_GAME("endGame", "End Game"),
    ENTITY("entity", "Entity"),
    EXIT_GATES("exitGates", "Exit Gates"),
    FEAR("fear", "Fear"),
    GENERATORS("generators", "Generators"),
    HATCH("hatch", "Hatch"),
    HEARING("hearing", "Hearing"),
    HIDDEN_KILLER("hiddenKiller", "Hidden Killer"),
    HOOK_STRUGGLE("hookStruggle", "Hook Struggle"),
    HOW_TO_WIN_KILLERS("HowToWin_killers", "How To Win - Killers"),
    HOW_TO_WIN_SURVIVORS("HowToWin_survivors", "How To Win - Survivors"),
    INJURED("injured", "Injured"),
    INTERRUPT("interrupt", "Interrupt"),
    ITEMS("items", "Items"),
    KILLER_VISION("killerVision", "Killer Vision"),
    LOADOUT("loadout", "Loadout"),
    LOCKERS("lockers", "Lockers"),
    MEAT_HOOKS("meatHooks", "Meat Hooks"),
    MOMENTO_MORI("momentoMori", "Momento Mori"),
    NOTE("note", "Note"),
    OBSESSION("obsession", "Obsession"),
    OFFERINGS("offerings", "Offerings"),
    PERKS("perks", "Perks"),
    PROCEDURAL("procedural", "Procedural"),
    PROFICIENCY("proficiency", "Proficiency"),
    PROFICIENCY_LIST("proficiencyList", "Proficiency List"),
    SABOTAGE("sabotage", "Sabotage"),
    SCRATCH_MARKS("scratchMarks", "Scratch Marks"),
    SHRINE_OF_SECRETS("shrineOfSecrets", "Shrine Of Secrets"),
    SKILL_CHECKS("skillChecks", "Skill Checks"),
    SLASHING("slashing", "Slashing"),
    SOUNDS("sounds", "Sounds"),
    STATUS_EFFECT_LIST("statusEffectList", "Status Effect List"),
    STATUS_EFFECTS("statusEffects", "Status Effects"),
    STEALTH("stealth", "Stealth"),
    TEACHABLES("teachables", "Teachables"),
    TOTEMS("totems", "Totems"),
    VAULT_PALETTES("vaultPalettes", "Vault Palettes");

    private final String name;
    private final String properName;

    @ParametersAreNonnullByDefault
    Help(String name, String properName) {
        this.name = name;
        this.properName = properName;
    }

    @Nonnull
    @Override
    public IconCategory getCategory() {
        return IconCategory.HELP;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Nonnull
    @Override
    public String getFileAdditive() {
        return "iconHelp_";
    }

    @Nonnull
    @Override
    public String getProperName() {
        return properName;
    }

    @Nonnull
    @Override
    public String getSubfolderName() {
        return "Help";
    }

    @Nullable
    @Override
    public Character getCharacter() {
        return null;
    }
}