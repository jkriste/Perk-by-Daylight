package dev.glitchedcode.pbd.gui.controller;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.dbd.Addon;
import dev.glitchedcode.pbd.dbd.Perk;
import dev.glitchedcode.pbd.dbd.Portrait;
import dev.glitchedcode.pbd.json.LatestRelease;
import dev.glitchedcode.pbd.logger.Logger;
import dev.glitchedcode.pbd.pack.IconPack;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private Label status;
    @FXML private Menu iconMenu;
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
        status.setTextFill(dark ? Color.WHITE : Color.BLACK);
        PBD.getConfig().setDarkMode(dark);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        status.setVisible(false);
        iconMenu.setVisible(false);
        progressBar.setVisible(false);
        deleteIconPack.setVisible(false);
        installIconPack.setVisible(false);
        constructIconPerks();
    }

    @FXML
    public void onCheckUpdate(ActionEvent event) {
        status.setText("Checking for update...");
        status.setVisible(true);
        try {
            LatestRelease latest = PBD.checkUpdate();
            if (latest != null) {
                if (latest.isUpdate()) {
                    setStatus("Update available, downloading...", false);
                } else
                    setStatus("Latest version is installed.", false);
            } else {
                setStatus("Failed to parse JSON, check logs.", true);
            }
        } catch (IOException e) {
            logger.warn("Failed to check for update: {}", e.getMessage());
            logger.handleError(Thread.currentThread(), e);
            setStatus("Failed to get update, check logs.", true);
        }
    }

    @FXML
    public void onOpenPBD(ActionEvent event) {
        File dir = PBD.PBD_DIR;
        try {
            Desktop.getDesktop().browse(dir.toURI());
        } catch (IOException e) {
            logger.warn("Failed to open the local PBD directory '{}': {}", dir.getAbsolutePath(), e.getMessage());
            logger.handleError(Thread.currentThread(), e);
            setStatus("Failed to open PBD directory, check logs.", true);
        }
    }

    @FXML
    public void onDonate(ActionEvent event) {

    }

    @FXML
    public void onNewIconPack(ActionEvent event) {
        addNewPack();
    }

    @FXML
    public void perkEdit(ListView.EditEvent<String> event) {

    }

    @FXML
    public void onRefreshIconPacks(ActionEvent event) {
        logger.debug("Refreshing icon packs...");
        constructIconPerks();
        setStatus("Refreshed icon packs list.", false);
    }

    @FXML
    public void onInstallIconPack(ActionEvent event) {
        String name = packList.getSelectionModel().getSelectedItem();
        if (!name.equalsIgnoreCase("+ Add New Icon Pack")) {
            IconPack pack = IconPack.fromName(name);
            if (pack == null) {
                logger.warn("Failed to get icon pack from name with name '{}'", name);
                return;
            }
            try {
                pack.install(progressBar);
                setStatus("Installed icon pack '" + name + "'.", false);
            } catch (IOException e) {
                logger.warn("Failed to install icon pack '{}': {}", name, e.getMessage());
                logger.handleError(Thread.currentThread(), e);
                setStatus("Failed to install icon pack '" + name + "', check logs.", true);
            }
        }
    }

    @FXML
    public void onDeleteIconPack(ActionEvent event) {

    }

    @FXML
    public void onInstallIcon(ActionEvent event) {

    }

    @FXML
    public void onUpdateIcon(ActionEvent event) {

    }

    @FXML
    public void onPerkTree(MouseEvent event) {
        String iconName = packList.getSelectionModel().getSelectedItem();
        TreeItem<String> parent = perkTree.getSelectionModel().getSelectedItem().getParent();
        String name = perkTree.getSelectionModel().getSelectedItem().getValue();
        if (!name.equalsIgnoreCase(iconName) && !name.equalsIgnoreCase("Perks")
            && !name.equalsIgnoreCase("Addons") && !name.equalsIgnoreCase("Portraits")) {
            IconPack pack = IconPack.fromName(iconName);
            if (pack == null) {
                logger.warn("Failed to get icon pack from name '{}'", iconName);
                return;
            }
            logger.debug(parent.getValue());
            switch (parent.getValue()) {
                case "Perks":
                    Perk perk = Perk.fromProperName(name);
                    if (perk != null)
                        iconDisplay.setImage(fromFile(perk.asFile(pack.getFolder())));
                    break;
                case "Portraits":
                    Portrait portrait = Portrait.fromProperName(name);
                    if (portrait != null)
                        iconDisplay.setImage(fromFile(portrait.asFile(pack.getFolder())));
                    break;
                case "Addons":
                    Addon addon = Addon.fromProperName(name);
                    if (addon != null)
                        iconDisplay.setImage(fromFile(addon.asFile(pack.getFolder())));
                    break;
                default:
                    logger.warn("Couldn't find icon with name '{}'", name);
            }
            iconMenu.setVisible(true);
        }
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

    @Nullable
    private Image fromFile(@Nonnull File file) {
        try {
            return new Image(new FileInputStream(file));
        } catch (IOException e) {
            logger.warn("Failed to get JavaFX Image from file '{}': {}", file.getAbsolutePath(), e.getMessage());
            logger.handleError(Thread.currentThread(), e);
        }
        return null;
    }

    private void setStatus(@Nonnull String status, boolean error) {
        if (error)
            this.status.setTextFill(Color.RED);
        else
            this.status.setTextFill(PBD.getConfig().isDarkMode() ? Color.WHITE : Color.BLACK);
        this.status.setText(status);
        this.status.setVisible(true);
    }

    private void constructIconPerks() {
        packList.getItems().clear();
        IconPack installed = IconPack.fromName("Currently Installed");
        if (installed == null) {
            installed = IconPack.of(PBD.getIconsDir());
            if (installed != null)
                installed.getMeta().setName("Currently Installed");
        }
        IconPack.getPacks().forEach(pack -> packList.getItems().add(pack.getMeta().getName()));
        packList.getItems().add("+ Add New Icon Pack");
        packList.refresh();
    }

    private void addNewPack() {
        logger.debug("Add new icon pack!");
    }
}