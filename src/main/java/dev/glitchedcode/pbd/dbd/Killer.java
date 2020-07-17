package dev.glitchedcode.pbd.dbd;

import javax.annotation.Nonnull;

public enum Killer implements Character {

    TRAPPER("Trapper"),
    WRAITH("Wraith"),
    HILLBILLY("Hillbilly"),
    NURSE("Nurse"),
    SHAPE("Shape"),
    HAG("Hag"),
    DOCTOR("Doctor"),
    HUNTRESS("Huntress"),
    CANNIBAL("Cannibal"),
    NIGHTMARE("Nightmare"),
    PIG("Pig"),
    CLOWN("Clown"),
    SPIRIT("Spirit"),
    LEGION("Legion"),
    PLAGUE("Plague"),
    GHOST_FACE("Ghost Face"),
    DEMOGORGON("Demogorgon"),
    ONI("Oni"),
    DEATHSLINGER("Deathslinger"),
    EXECUTIONER("Executioner"); // conehead

    private final String name;

    Killer(@Nonnull String name) {
        this.name = "The " + name;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }
}