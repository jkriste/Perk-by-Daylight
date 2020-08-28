package dev.glitchedcode.pbd.gui.controller;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.logger.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import javax.annotation.Nonnull;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class DonationController implements Initializable {

    private Stage stage;
    private static final Logger logger = PBD.getLogger();
    private static final String LINK = "https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=kristensenjl" +
            "%40my.gvltec.edu&item_name=Making+free+software+for+all.&currency_code=USD&source=url";

    /**
     * Sets the stage of the form.
     *
     * @param stage The stage.
     */
    void setStage(@Nonnull Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // ignore
    }

    /**
     * Called when the "Donate" button is clicked.
     */
    @FXML
    public void onDonate() {
        try {
            Desktop.getDesktop().browse(new URI(LINK));
            stage.close();
        } catch (IOException | URISyntaxException e) {
            logger.warn("Failed to open link '{}': {}", LINK, e.getMessage());
            logger.handleError(Thread.currentThread(), e);
        }
    }

    /**
     * Called when the "Close" button is clicked.
     */
    @FXML
    public void onClose() {
        stage.close();
    }
}