package dev.glitchedcode.pbd.gui.controller;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.json.Config;
import dev.glitchedcode.pbd.logger.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.stage.Stage;

import javax.annotation.Nonnull;
import java.net.URL;
import java.util.ResourceBundle;

public class PreferenceController implements Initializable {

    @FXML private CheckBox debugCheck;
    @FXML private CheckBox ignoreCheck;
    @FXML private CheckBox delErrCheck;
    @FXML private CheckBox delLogCheck;
    @FXML private CheckBox noColorCheck;
    @FXML private CheckBox offlineCheck;
    @FXML private Spinner<Integer> statusSpinner;

    private Stage stage;
    private static final Logger logger = PBD.getLogger();
    private static final Config CONFIG = PBD.getConfig();

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
        IntegerSpinnerValueFactory factory = new IntegerSpinnerValueFactory(1, 60, CONFIG.getStatusDuration(), 1);
        statusSpinner.setValueFactory(factory);
        debugCheck.setSelected(CONFIG.debug());
        ignoreCheck.setSelected(CONFIG.doesIgnoreUpdates());
        noColorCheck.setSelected(CONFIG.noColor());
        offlineCheck.setSelected(CONFIG.isOfflineMode());
        delErrCheck.setSelected(CONFIG.deleteErrorLogs());
        delLogCheck.setSelected(CONFIG.deleteLogs());
    }

    /**
     * Called when the "Save" button is pressed.
     */
    @FXML
    public void onSave() {
        CONFIG.setDebug(debugCheck.isSelected());
        CONFIG.setIgnoreUpdates(ignoreCheck.isSelected());
        CONFIG.setNoColor(noColorCheck.isSelected());
        CONFIG.setOfflineMode(offlineCheck.isSelected());
        CONFIG.setStatusDuration(statusSpinner.getValue().shortValue());
        CONFIG.setDeleteErrorLogs(delErrCheck.isSelected());
        CONFIG.setDeleteLogs(delLogCheck.isSelected());
        logger.info("Saved preferences.");
        stage.close();
    }

    /**
     * Called when the "Cancel" button is pressed.
     */
    public void onCancel() {
        stage.close();
    }
}