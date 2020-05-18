package dev.glitchedcode.pbd.gui.controller;

import com.google.common.io.Files;
import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.dbd.Addon;
import dev.glitchedcode.pbd.dbd.Perk;
import dev.glitchedcode.pbd.dbd.Portrait;
import dev.glitchedcode.pbd.json.LatestRelease;
import dev.glitchedcode.pbd.logger.Logger;
import dev.glitchedcode.pbd.pack.IconPack;
import dev.glitchedcode.pbd.util.FileUtil;
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
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.annotation.Nonnull;
import java.awt.Desktop;
import java.io.File;
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
        constructIconPerks();
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
        try {
            LatestRelease latest = PBD.checkUpdate();
            if (latest != null) {
                if (latest.isUpdate()) {
                    setStatus("Update available, downloading...", false);
                    // TODO
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
        try {
            addNewPack();
        } catch (IOException e) {
            logger.warn("Failed to add new icon pack: {}", e.getMessage());
            logger.handleError(Thread.currentThread(), e);
        }
    }

    /**
     * Called when an icon pack is being renamed.
     *
     * @param event The edit event.
     */
    @FXML
    public void onPackEdit(ListView.EditEvent<String> event) {
        String old = packList.getItems().get(event.getIndex());
        if (old != null && !old.equalsIgnoreCase("Currently Installed")
                && !old.equalsIgnoreCase("+ Add New Icon Pack")) {
            String newName = event.getNewValue();
            if (newName != null && !newName.isEmpty()) {
                String item = packList.getSelectionModel().getSelectedItem();
                if (item != null && !item.isEmpty()) {
                    IconPack pack = IconPack.fromName(item);
                    if (pack != null) {
                        logger.debug("Set new name for Icon Pack to '{}'", newName);
                        pack.setName(newName);
                        perkTree.setRoot(null);
                        constructIconPerks();
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
        constructIconPerks();
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
        if (!name.equalsIgnoreCase("+ Add New Icon Pack") && !name.equalsIgnoreCase("Currently Installed")) {
            IconPack pack = IconPack.fromName(name);
            if (pack == null) {
                logger.warn("Failed to get icon pack from name with name '{}'", name);
                return;
            }
            setStatus("Installing icon pack '" + name + "'...", false);
            pack.install(progressBar, () -> setStatus("Icon pack '" + name + "' installed.", false));
        }
    }

    /**
     *
     *
     * @param event
     */
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
        TreeItem<String> selected = perkTree.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;
        TreeItem<String> parent = selected.getParent();
        if (parent == null)
            return;
        String name = perkTree.getSelectionModel().getSelectedItem().getValue();
        if (!name.equalsIgnoreCase("Perks") && !name.equalsIgnoreCase("Addons")
                && !name.equalsIgnoreCase("Portraits")) {
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
                        iconDisplay.setImage(FileUtil.toImage(perk.asFile(pack.getFolder())));
                    break;
                case "Portraits":
                    Portrait portrait = Portrait.fromProperName(name);
                    if (portrait != null)
                        iconDisplay.setImage(FileUtil.toImage(portrait.asFile(pack.getFolder())));
                    break;
                case "Addons":
                    Addon addon = Addon.fromProperName(name);
                    if (addon != null)
                        iconDisplay.setImage(FileUtil.toImage(addon.asFile(pack.getFolder())));
                    break;
                default:
                    logger.warn("Couldn't find icon with name '{}' and parent '{}'", name, parent.getValue());
            }
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
        if (name.equalsIgnoreCase("+ Add New Icon Pack")) {
            installIconPack.setVisible(false);
            deleteIconPack.setVisible(false);
            perkTree.setRoot(null);
            try {
                addNewPack();
            } catch (IOException e) {
                logger.warn("Failed to add new icon pack: {}", e.getMessage());
                logger.handleError(Thread.currentThread(), e);
            }
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
                installed.setName("Currently Installed");
        }
        IconPack.getPacks().forEach(pack -> packList.getItems().add(pack.getMeta().getName()));
        packList.getItems().add("+ Add New Icon Pack");
        packList.refresh();
    }

    private void addNewPack() throws IOException {
//        JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.home"), "Downloads"));
//        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//        fileChooser.setFileFilter(new FileFilter() {
//            @Override
//            public boolean accept(File f) {
//                return f.isDirectory() || f.getName().endsWith(".zip") || f.getName().endsWith(".rar");
//            }
//
//            @Override
//            public String getDescription() {
//                return "Directories, .ZIP, .RAR";
//            }
//        });
//        Action detailsView = fileChooser.getActionMap().get("viewTypeDetails");
//        detailsView.actionPerformed(null);
//        fileChooser.showOpenDialog(null);
//        fileChooser.requestFocus();
//        File file = fileChooser.getSelectedFile();
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home"), "Downloads"));
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("ZIP Files", "*.zip");
        chooser.setSelectedExtensionFilter(filter);
        File file = chooser.showOpenDialog(null);
        if (file == null)
            return;
        processFile(file);
    }

    @SuppressWarnings("UnstableApiUsage")
    private void processFile(@Nonnull File file) throws IOException {
        File newDir;
        if (file.isDirectory()) {
            Files.copy(file, PBD.getPacksDir());
            newDir = new File(PBD.getPacksDir(), file.getName());
        } else if (file.getName().endsWith(".zip")) {
            newDir = FileUtil.unzipFile(file, PBD.getPacksDir());
        } else {
            logger.error("Didn't know how to process file '{}', not dir/zip/rar.", file.getAbsolutePath());
            return;
        }
        if (newDir != null && newDir.exists() && newDir.isDirectory()) {
            IconPack pack = IconPack.of(newDir);
            if (pack != null)
                logger.info("Added new icon pack '{}'.", pack.getMeta().getName());
        } else {
            if (newDir == null) {
                logger.error("newDir is null.");
                return;
            }
            logger.warn("newDir ({}) didn't exist or wasn't a directory.", newDir.toString());
        }
    }
}