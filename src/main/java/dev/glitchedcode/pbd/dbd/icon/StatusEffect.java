package dev.glitchedcode.pbd.dbd.icon;

import dev.glitchedcode.pbd.dbd.Character;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Used to represent all of the icons in the "StatusEffects" category.
 */
public enum StatusEffect implements Icon {

    BLEEDING("bleeding", "Bleeding"),
    BLOOD_LUST("bloodLust", "Blood Lust"),
    BROKEN("broken", "Broken"),
    CLEANSING("cleansing", "Cleansing"),
    DEEP_WOUND("deepWound", "Deep Wound"),
    ENDURANCE("endurance", "Endurance"),
    EXHAUSTED("exhausted", "Exhausted"),
    EXPERTISE("expertise", "Expertise"),
    EXPOSED("exposed", "Exposed"),
    HEALING("healing", "Healing"),
    HEARING("hearing", "Hearing"),
    HINDERED("hindered", "Hindered"),
    LUCK("luck", "Luck"),
    MADNESS("madness", "Madness"),
    MANGLED("mangled", "Mangled"),
    OBLIVIOUS("oblivious", "Oblivious"),
    PROGRESSION_SPEED("progressionSpeed", "Progression Speed"),
    REPAIRING("repairing", "Repairing"),
    SABOTAGING("sabotaging", "Sabotaging"),
    SKILL_CHECK_DIFFICULTY("skillCheckDifficulty", "Skill Check Difficulty"),
    SKILL_CHECK_PROBABILITY("skillCheckProbability", "Skill Check Probability"),
    SKILLS("skills", "Skills"),
    SLEEP_PENALTY("sleepPenalty", "Sleep Penalty"),
    SPEED("speed", "Speed"),
    UNDETECTABLE("undetectable", "Undetectable"),
    VISION("vision", "Vision");

    private final String name;
    private final String properName;

    @ParametersAreNonnullByDefault
    StatusEffect(String name, String properName) {
        this.name = name;
        this.properName = properName;
    }

    @Nonnull
    @Override
    public IconCategory getCategory() {
        return IconCategory.STATUS_EFFECT;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Nonnull
    @Override
    public String getFileAdditive() {
        return "iconStatusEffects_";
    }

    @Nonnull
    @Override
    public String getProperName() {
        return properName;
    }

    @Nonnull
    @Override
    public String getSubfolderName() {
        return "StatusEffects";
    }

    @Nullable
    @Override
    public Character getCharacter() {
        return null;
    }
}
