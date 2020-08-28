package dev.glitchedcode.pbd.gui.controller;

import com.google.gson.JsonObject;
import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.json.Config;
import dev.glitchedcode.pbd.json.LatestRelease;
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

public class UpdateController implements Initializable {

    private Stage stage;
    private JsonObject jsonObject;
    private MainController instance;

    private static final Logger logger = PBD.getLogger();
    private static final Config config = PBD.getConfig();

    /**
     * Sets the stage and the main UI instance.
     *
     * @param stage The stage.
     * @param instance The main UI instance.
     */
    void setStage(@Nonnull Stage stage, @Nonnull MainController instance) {
        this.stage = stage;
        this.instance = instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.jsonObject = LatestRelease.getObject();
    }

    /**
     * Called when the "View on GitHub" button is pressed.
     */
    @FXML
    public void onGitHub() {
        try {
            if (!jsonObject.has("html_url")) {
                logger.warn("JsonObject does not contain URL object.");
                return;
            }
            String url = jsonObject.get("html_url").getAsString();
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            logger.warn("Failed to open update link: {}", e.getMessage());
            logger.handleError(Thread.currentThread(), e);
        }
    }

    /**
     * Called when the "Cancel" button is pressed.
     */
    @FXML
    public void onCancel() {
        instance.setDisabled(false);
        stage.close();
    }

    /**
     * Called when the "Ignore" button option is pressed.
     */
    @FXML
    public void onIgnore() {
        config.setIgnoreUpdates(true);
        instance.setDisabled(false);
        instance.setStatus("Future updates ignored.", MainController.Type.NONE);
        stage.close();
    }

    /**
     * Called when the "Update" button is pressed.
     * <br />
     * Makes a call back to the main UI to start the update.
     */
    @FXML
    public void onUpdate() {
        stage.close();
        instance.startDownload();
    }
}