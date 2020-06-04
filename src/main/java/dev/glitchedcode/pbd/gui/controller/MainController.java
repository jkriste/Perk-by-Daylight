package dev.glitchedcode.pbd.gui.controller;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.dbd.Icon;
import dev.glitchedcode.pbd.file.Files;
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
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
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
    @FXML private MenuBar menuBar;
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
    private static final String NEW_PACK = "+ Add New Icon Pack";
    private static final String INSTALLED = "Currently Installed";
    private static final URL darkTheme = PBD.class.getResource("/theme/dark-theme.css");

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the theme of the UI.
     *
     * @param dark True to set to dark mode.
     */
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
        constructIconPacks();
        // Makes the icon pack list editable.
        packList.setEditable(true);
        packList.setCellFactory(TextFieldListCell.forListView());
    }

    /**
     * Called when the "Check for Update" menu item is clicked.
     *
     * @param event The action event.
     */
    @FXML
    public void onCheckUpdate(ActionEvent event) {
        status.setText("Checking for update...");
        status.setVisible(true);
        if (LatestRelease.checkUpdate()) {
            setStatus("Update available, downloading...", false);
            // TODO
        } else
            setStatus("Latest version is installed.", false);
    }

    /**
     * Called when the "Delete Temp Contents" menu item is clicked.
     *
     * @param event The action event.
     */
    @FXML
    public void onDeleteTempContents(ActionEvent event) {
        int count = Files.getCount(PBD.getTempDir());
        if (count == 0) {
            setStatus("No files were found in the 'temp' folder.", false);
            return;
        }
        try {
            Files.deleteAll(PBD.getTempDir(), false);
            setStatus("Deleted " + count + " file(s) in 'temp' folder.", false);
        } catch (IOException e) {
            logger.warn("Failed to delete contents in 'temp' dir: {}", e.getMessage());
            logger.handleError(Thread.currentThread(), e);
        }
    }

    /**
     * Called when the "Open PBD Folder" menu item is clicked.
     *
     * @param event The action event.
     */
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

    /**
     * Called when the "Donate..." menu item is clicked.
     *
     * @param event The action event.
     */
    @FXML
    public void onDonate(ActionEvent event) {
        // TODO
    }

    /**
     * Called when the "Add New Icon Pack..." menu item is clicked.
     *
     * @param event The action event.
     */
    @FXML
    public void onNewIconPack(ActionEvent event) {
        addNewPack();
    }

    /**
     * Called when an icon pack is being renamed.
     *
     * @param event The edit event.
     */
    @FXML
    public void onPackEdit(ListView.EditEvent<String> event) {
        String old = packList.getItems().get(event.getIndex());
        if (old != null && !old.equalsIgnoreCase(INSTALLED) && !old.equalsIgnoreCase(NEW_PACK)) {
            String newName = event.getNewValue();
            if (newName.equalsIgnoreCase(NEW_PACK)) {
                setStatus("Not acceptable new name.", false);
                return;
            }
            if (!newName.isEmpty()) {
                if (IconPack.hasName(newName)) {
                    setStatus("An icon pack with that name already exists.", false);
                    return;
                }
                String item = packList.getSelectionModel().getSelectedItem();
                if (item != null && !item.isEmpty()) {
                    IconPack pack = IconPack.fromName(item);
                    if (pack != null) {
                        logger.debug("Set new name for Icon Pack to '{}'", newName);
                        pack.setName(newName);
                        perkTree.setRoot(null);
                        constructIconPacks();
                        event.consume();
                    }
                }
            } else
                setStatus("You cannot leave the name empty.", false);
        } else
            setStatus("You cannot rename this item.", false);
    }

    /**
     * Called when the "Refresh Icon Packs" menu item is clicked.
     *
     * @param event The action event.
     */
    @FXML
    public void onRefreshIconPacks(ActionEvent event) {
        logger.debug("Refreshing icon packs...");
        perkTree.setRoot(null);
        constructIconPacks();
        setStatus("Refreshed icon packs list.", false);
    }

    /**
     * Called when the "Install Icon Pack" menu item is clicked.
     *
     * @param event The action event.
     */
    @FXML
    public void onInstallIconPack(ActionEvent event) {
        String name = packList.getSelectionModel().getSelectedItem();
        if (!name.equalsIgnoreCase(NEW_PACK) && !name.equalsIgnoreCase(INSTALLED)) {
            IconPack pack = IconPack.fromName(name);
            if (pack == null) {
                logger.warn("Failed to get icon pack from name with name '{}'", name);
                return;
            }
            setDisabled(true);
            setStatus("Installing icon pack '" + name + "'...", false);
            pack.install(progressBar, () -> {
                setStatus("Icon pack '" + name + "' installed.", false);
                setDisabled(false);
            });
        }
    }

    /**
     * Called when the "Delete Icon Pack" menu item is clicked.
     *
     * @param event The action event.
     */
    @FXML
    public void onDeleteIconPack(ActionEvent event) {
        String name = packList.getSelectionModel().getSelectedItem();
        if (name != null && !name.equalsIgnoreCase(NEW_PACK) && !name.equalsIgnoreCase(INSTALLED)) {
            IconPack pack = IconPack.fromName(name);
            if (pack == null) {
                logger.warn("Failed to get icon pack from name with name '{}'", name);
                return;
            }
            iconDisplay.setImage(null);
            perkTree.setRoot(null);
            iconMenu.setVisible(false);
            installIconPack.setVisible(false);
            deleteIconPack.setVisible(false);
            File folder = pack.getFolder();
            pack.dispose();
            try {
                Files.deleteAll(folder, true);
            } catch (IOException e) {
                logger.warn("Failed to delete icon pack '{}': {}", pack.getMeta().getName(), e.getMessage());
                logger.handleError(Thread.currentThread(), e);
            }
            setStatus("Deleted icon pack '" + name + "'.", false);
            constructIconPacks();
        }
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
        TreeItem<String> selected = perkTree.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;
        TreeItem<String> parent = selected.getParent();
        if (parent == null)
            return;
        if (selected.getChildren().isEmpty()) {
            String name = perkTree.getSelectionModel().getSelectedItem().getValue();
            IconPack pack = IconPack.fromName(iconName);
            if (pack == null) {
                logger.warn("Failed to get icon pack from name '{}'", iconName);
                return;
            }
            Icon icon = PBD.getIcon(name);
            if (icon != null)
                iconDisplay.setImage(toImage(icon.asFile(pack.getFolder())));
            else
                logger.warn("Couldn't find icon with name '{}'", name);
            iconMenu.setVisible(true);
        }
    }

    @FXML
    public void onPackList(MouseEvent event) {
        String name = packList.getSelectionModel().getSelectedItem();
        if (name == null || name.isEmpty()) {
            perkTree.setRoot(null);
            return;
        }
        logger.debug("Clicked on: {}", name);
        if (name.equalsIgnoreCase(NEW_PACK)) {
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
            logger.warn("Failed to get icon pack with name '{}', selected via packList.", name);
    }

    @FXML
    public void onDarkMode(ActionEvent event) {
        setMode(true);
    }

    @FXML
    public void onLightMode(ActionEvent event) {
        setMode(false);
    }

    private void setDisabled(boolean disabled) {
        perkTree.setDisable(disabled);
        packList.setDisable(disabled);
        menuBar.setDisable(disabled);
    }

    private void setStatus(@Nonnull String status, boolean error) {
        if (error)
            this.status.setTextFill(Color.RED);
        else
            this.status.setTextFill(PBD.getConfig().isDarkMode() ? Color.WHITE : Color.BLACK);
        this.status.setText(status);
        this.status.setVisible(true);
    }

    private void constructIconPacks() {
        packList.getItems().clear();
        IconPack installed = IconPack.fromName(INSTALLED);
        if (installed == null) {
            installed = IconPack.of(PBD.getIconsDir());
            if (installed != null)
                installed.setName(INSTALLED);
        }
        IconPack.getPacks().forEach(pack -> packList.getItems().add(pack.getMeta().getName()));
        packList.getItems().add(NEW_PACK);
        packList.refresh();
        deleteIconPack.setVisible(false);
        installIconPack.setVisible(false);
    }

    private void addNewPack() {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home"), "Downloads"));
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("ZIP Files", "*.zip");
        chooser.setSelectedExtensionFilter(filter);
        File file = chooser.showOpenDialog(null);
        if (file == null)
            return;
        processFile(file);
        constructIconPacks();
    }

    private void processFile(@Nonnull File file) {
        IconPack pack = IconPack.newPack(file, progressBar);
        if (pack == null)
            setStatus("Failed to process file, check logs.", true);
    }

    @Nullable
    private Image toImage(@Nonnull File file) {
        try (FileInputStream in = new FileInputStream(file)) {
            return new Image(in);
        } catch (IOException e) {
            logger.warn("Failed to get JavaFX Image from file '{}': {}", file.getAbsolutePath(), e.getMessage());
            logger.handleError(Thread.currentThread(), e);
        }
        return null;
    }
}