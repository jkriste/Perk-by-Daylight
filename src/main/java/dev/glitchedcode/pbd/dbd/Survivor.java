package dev.glitchedcode.pbd.dbd;

import javax.annotation.Nonnull;

/**
 * Used to represent all of the survivors in the game.
 */
public enum Survivor implements Character {

    DWIGHT("Dwight Fairfield"),
    MEG("Meg Thomas"),
    CLAUDETTE("Claudette Morel"),
    JAKE("Jake Park"),
    NEA("Nea Karlsson"), // actually the entity
    LAURIE("Laurie Strode"),
    ACE("Ace Visconti"),
    BILL("Bill Overbeck"),
    FENG("Feng Min"),
    DAVID("David King"),
    QUENTIN("Quentin Smith"),
    TAPP("David Tapp"),
    KATE("Kate Denson"),
    ADAM("Adam Francis"),
    JEFF("Jeff Johansen"),
    JANE("Jane Romero"),
    ASH("Ash Williams"),
    NANCY("Nancy Wheeler"),
    STEVE("Steve Harrington"),
    YUI("Yui Kimura"),
    ZARINA("Zarina Kassir"),
    CHERYL("Cheryl Mason");

    private final String name;

    Survivor(@Nonnull String name) {
        this.name = name;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }
}