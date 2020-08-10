package dev.glitchedcode.pbd.gui.controller;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.logger.Logger;
import dev.glitchedcode.pbd.pack.IconPack;
import dev.glitchedcode.pbd.pack.PackMeta;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import javax.annotation.Nonnull;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IconPackInfoController implements Initializable {

    @FXML private Label locLabel;
    @FXML private Label nameLabel;
    @FXML private Label iconsLabel;
    @FXML private TreeView<String> missingTree;

    private Stage stage;
    private IconPack iconPack;
    private static final Logger logger = PBD.getLogger();

    void setStage(@Nonnull Stage stage) {
        this.stage = stage;
    }

    void setIconPack(@Nonnull IconPack iconPack) {
        this.iconPack = iconPack;
        PackMeta meta = iconPack.getMeta();
        missingTree.setRoot(iconPack.getMissingIcons());
        locLabel.setText(iconPack.getFolder().getAbsolutePath());
        nameLabel.setText(meta.getName());
        int missing = meta.getMissingIcons().size();
        int icons = PBD.getIcons().size() - missing;
        iconsLabel.setText(icons + " (" + missing + " missing)");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // not used
    }

    /**
     * Called when "Open Folder" button is clicked.
     */
    @FXML
    public void onOpenLoc() {
        File dir = iconPack.getFolder();
        try {
            Desktop.getDesktop().browse(dir.toURI());
        } catch (IOException e) {
            logger.warn("Failed to open icon pack directory '{}': {}", dir.getAbsolutePath(), e.getMessage());
            logger.handleError(Thread.currentThread(), e);
        }
    }

    /**
     * Called when "Close" button is clicked.
     */
    @FXML
    public void onClose() {
        stage.close();
    }
}