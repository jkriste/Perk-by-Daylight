package dev.glitchedcode.pbd.gui;

import javax.annotation.Nonnull;
import javax.swing.JMenuBar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * By default, {@link JMenuBar} does not allow coloring.
 * This class allows the menu bar to be colored so it's not so ugly.
 */
public class MenuBar extends JMenuBar {

    private Color color;

    public MenuBar() {
        this(Color.WHITE);
    }

    public MenuBar(@Nonnull Color color) {
        this.color = color;
    }

    public void setColor(@Nonnull Color color) {
        this.color = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
    }
}