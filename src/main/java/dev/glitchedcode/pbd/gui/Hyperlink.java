package dev.glitchedcode.pbd.gui;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.logger.Logger;

import javax.annotation.Nonnull;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * By default, {@link JLabel} does not support hyperlinks.
 * So this class is meant to act as one.
 */
public class Hyperlink extends JLabel {

    private static final Logger logger = PBD.getLogger();

    public Hyperlink(@Nonnull String url) throws URISyntaxException {
        this(new URI(url));
    }

    public Hyperlink(@Nonnull URI url) {
        super.setForeground(Color.BLUE);
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                try {
                    Desktop.getDesktop().browse(url);
                } catch (IOException e) {
                    logger.warn("Failed to open URL ({}).", url);
                    logger.handleError(Thread.currentThread(), e);
                }
            }
        });
    }
}