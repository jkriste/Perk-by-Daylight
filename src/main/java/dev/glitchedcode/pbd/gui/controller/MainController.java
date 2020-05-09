package dev.glitchedcode.pbd.gui.controller;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.json.LatestRelease;
import dev.glitchedcode.pbd.logger.Logger;
import dev.glitchedcode.pbd.pack.IconPack;
import dev.glitchedcode.pbd.pack.PackMeta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private Label status;
    @FXML private ImageView iconDisplay;
    @FXML private CheckMenuItem darkMode;
    @FXML private ProgressBar progressBar;
    @FXML private MenuItem deleteIconPack;
    @FXML private CheckMenuItem lightMode;
    @FXML private MenuItem installIconPack;
    @FXML private TreeView<String> perkTree;
    @FXML private ListView<String> packList;

    private Stage stage;
    private static final Logger logger = PBD.getLogger();
    private static final URL darkTheme = PBD.class.getResource("/theme/dark-theme.css");

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMode(boolean dark) {
        logger.debug("Set theme to {} theme.", (dark ? "dark" : "light"));
        if (dark) {
            stage.getScene().getStylesheets().add(darkTheme.toExternalForm());
            darkMode.setSelected(true);
            lightMode.setSelected(false);
        } else {
            stage.getScene().getStylesheets().clear();
            lightMode.setSelected(true);
            darkMode.setSelected(false);
        }
        PBD.getConfig().setDarkMode(dark);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        status.setVisible(false);
        progressBar.setVisible(false);
        deleteIconPack.setVisible(false);
        installIconPack.setVisible(false);
        List<IconPack> packs = IconPack.getPacks();
        new IconPack(PBD.getIconsDir(), new PackMeta("Default", new String[0], new String[0], new String[0]));
        if (packs.isEmpty())
            packList.getItems().add("+ Add New Icon Pack");
        packs.forEach(pack -> packList.getItems().add(pack.getMeta().getName()));
    }

    @FXML
    public void onCheckUpdate(ActionEvent event) {
        status.setText("Checking for update...");
        status.setVisible(true);
        try {
            LatestRelease latest = PBD.checkUpdate();
            if (latest != null) {
                if (latest.isUpdate()) {
                    status.setText("Update available, downloading...");
                } else
                    status.setText("Latest version is installed.");
            } else {
                status.setText("Failed to parse JSON, check console.");
                status.setTextFill(Color.RED);
            }
        } catch (IOException e) {
            logger.warn("Failed to check for update: {}", e.getMessage());
            logger.handleError(Thread.currentThread(), e);
            status.setText("Failed to get update, check console.");
            status.setTextFill(Color.RED);
        }
    }

    @FXML
    public void onOpenConsole(ActionEvent event) {

    }

    @FXML
    public void onDonate(ActionEvent event) {

    }

    @FXML
    public void onNewIconPack(ActionEvent event) {
        addNewPack();
    }

    @FXML
    public void onRefreshIconPacks(ActionEvent event) {
        logger.debug("Refreshing icon packs...");
        packList.getItems().clear();
        IconPack.getPacks().forEach(pack -> packList.getItems().add(pack.getMeta().getName()));
        packList.refresh();
    }

    @FXML
    public void onInstallIconPack(ActionEvent event) {

    }

    @FXML
    public void onDeleteIconPack(ActionEvent event) {

    }

    @FXML
    public void onPerkTree(MouseEvent event) {

    }

    @FXML
    public void onPackList(MouseEvent event) {
        String name = packList.getSelectionModel().getSelectedItem();
        logger.debug("Clicked on: {}", name);
        if (name.equalsIgnoreCase("+ Add New Icon Pack")) {
            installIconPack.setVisible(false);
            deleteIconPack.setVisible(false);
            perkTree.setRoot(null);
            addNewPack();
            return;
        }
        IconPack pack = IconPack.fromName(name);
        if (pack != null) {
            installIconPack.setVisible(true);
            deleteIconPack.setVisible(true);
            perkTree.setRoot(pack.getTree());
            perkTree.refresh();
        } else
            logger.warn("Failed to get icon pack with name '{}', selected via packList.");
    }

    @FXML
    public void onDarkMode(ActionEvent event) {
        setMode(true);
    }

    @FXML
    public void onLightMode(ActionEvent event) {
        setMode(false);
    }

    private void addNewPack() {
        logger.debug("Add new icon pack!");
    }
}