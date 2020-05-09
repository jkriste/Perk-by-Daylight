package dev.glitchedcode.pbd.gui;

/**
 * Wrapper for {@link javax.swing.JOptionPane} magic icon constants.
 */
public enum Icon {

    NONE(-1),
    ERROR(0),
    INFO(1),
    WARNING(2),
    QUESTION(3);

    private final int id;

    Icon(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}