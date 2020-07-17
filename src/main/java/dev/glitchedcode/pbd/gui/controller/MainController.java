package dev.glitchedcode.pbd.gui.controller;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.dbd.Icon;
import dev.glitchedcode.pbd.gui.task.CopyTask;
import dev.glitchedcode.pbd.gui.task.DeleteTask;
import dev.glitchedcode.pbd.gui.task.IconInstallTask;
import dev.glitchedcode.pbd.gui.task.MoveTask;
import dev.glitchedcode.pbd.gui.task.UnzipTask;
import dev.glitchedcode.pbd.json.Config;
import dev.glitchedcode.pbd.json.LatestRelease;
import dev.glitchedcode.pbd.logger.Logger;
import dev.glitchedcode.pbd.pack.IconPack;
import dev.glitchedcode.pbd.pack.PackMeta;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
    private ScheduledFuture<?> statusTask;
    private static final Logger logger = PBD.getLogger();
    private static final Config CONFIG = PBD.getConfig();
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
        logger.info("Set theme to {} theme.", (dark ? "dark" : "light"));
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
        CONFIG.setDarkMode(dark);
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
        logger.info("Initialized JavaFX form successfully.");
    }

    @FXML
    public void onPreferences() {
        try {
            FXMLLoader loader = new FXMLLoader(PBD.class.getResource("/form/PreferenceForm.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            PreferenceController controller = loader.getController();
            controller.setStage(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            stage.initStyle(StageStyle.UNIFIED);
            stage.setTitle("Preferences");
            stage.getIcons().add(new Image(PBD.class.getResourceAsStream("/pic/pbd_icon.png")));
            if (CONFIG.isDarkMode())
                stage.getScene().getStylesheets().add(darkTheme.toExternalForm());
            stage.show();
        } catch (IOException e) {
            logger.warn("Failed to open PreferenceForm.fxml: {}", e.getMessage());
            logger.handleError(Thread.currentThread(), e);
        }
    }

    /**
     * Called when the "Check for Update" menu item is clicked.
     */
    @FXML
    public void onCheckUpdate() {
        if (CONFIG.isOfflineMode()) {
            setStatus("Offline mode is enabled, cannot check for update.", Type.WARN);
            return;
        }
        status.setText("Checking for update...");
        status.setVisible(true);
        if (LatestRelease.checkUpdate()) {
            setStatus("Update available, downloading...", Type.NONE);
            new Thread(() -> LatestRelease.downloadUpdate(this, () -> {
                setDisabled(false);
                logger.info("Download completed. Delete the old JAR and rename the new one.");
                PBD.close(0);
            })).start();
        } else
            setStatus("Latest version is installed.", Type.NONE);
    }

    /**
     * Called when the "Delete Temp Contents" menu item is clicked.
     */
    @FXML
    public void onDeleteTempContents() {
        File[] files = PBD.getTempDir().listFiles();
        if (files == null || files.length == 0) {
            setStatus("No files were found in the 'temp' folder.", Type.NONE);
            return;
        }
        setStatus("Deleting contents in 'temp' folder...", Type.NONE);
        progressBar.setVisible(true);
        setDisabled(true);
        DeleteTask task = new DeleteTask(PBD.getTempDir(), false);
        progressBar.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(e -> {
            try {
                setDisabled(false);
                progressBar.setVisible(false);
                setStatus("Deleted " + task.get() + " file(s) in 'temp' folder.", Type.NONE);
            } catch (InterruptedException | ExecutionException ex) {
                logger.warn("Failed to get int from DeleteTask: {}", ex.getMessage());
                logger.handleError(Thread.currentThread(), ex);
            }
        });
        setupTask("Cache Deletion Thread", task);
    }

    /**
     * Called when the "Open PBD Folder" menu item is clicked.
     */
    @FXML
    public void onOpenPBD() {
        File dir = PBD.PBD_DIR;
        try {
            Desktop.getDesktop().browse(dir.toURI());
        } catch (IOException e) {
            logger.warn("Failed to open the local PBD directory '{}': {}", dir.getAbsolutePath(), e.getMessage());
            logger.handleError(Thread.currentThread(), e);
            setStatus("Failed to open PBD directory, check logs.", Type.ERROR);
        }
    }

    /**
     * Called when the "Donate..." menu item is clicked.
     */
    @FXML
    public void onDonate() {
        // TODO
    }

    @FXML
    public void onNewPackZip() {
        setDisabled(true);
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select ZIP File");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("ZIP", "*.zip");
        chooser.getExtensionFilters().add(filter);
        chooser.setInitialDirectory(new File(System.getProperty("user.home"), "Downloads"));
        File file = chooser.showOpenDialog(null);
        if (file == null) {
            setDisabled(false);
            return;
        }
        if (!file.getName().endsWith(".zip")) {
            setDisabled(false);
            setStatus("File '" + file.getName() + "' not supported.", Type.WARN);
            return;
        }
        UnzipTask task = new UnzipTask(file);
        progressBar.progressProperty().bind(task.progressProperty());
        setStatus("Unzipping '" + file.getName() + "'...", Type.NONE);
        setDisabled(true);
        progressBar.setVisible(true);
        task.setOnSucceeded(event -> {
            try {
                File newDir = task.get();
                logger.debug("Unzipped file '{}' successfully: {}", file.getName(), newDir.getAbsolutePath());
                verifyAndInstall(newDir);
            } catch (InterruptedException | ExecutionException e) {
                logger.warn("Failed to get File: {}", e.getMessage());
                logger.handleError(Thread.currentThread(), e);
            }
        });
        setupTask("Icon Pack Unzipper", task);
    }

    @FXML
    public void onNewPackFolder() {
        setDisabled(true);
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Icon Pack Folder");
        chooser.setInitialDirectory(new File(System.getProperty("user.home"), "Downloads"));
        File file = chooser.showDialog(null);
        if (file == null) {
            setDisabled(false);
            return;
        }
        if (!file.isDirectory()) {
            setStatus("Given file '" + file.getName() + "' is not a folder.", Type.WARN);
            setDisabled(false);
            return;
        }
        CopyTask task = new CopyTask(file, new File(PBD.getTempDir(), file.getName()));
        progressBar.progressProperty().bind(task.progressProperty());
        setStatus("Copying '" + file.getName() + "' to 'temp' directory.", Type.NONE);
        setDisabled(true);
        progressBar.setVisible(true);
        task.setOnSucceeded(event -> {
            try {
                File newDir = task.get();
                logger.debug("Moved file '{}' successfully: {}", file.getName(), newDir.getAbsolutePath());
                verifyAndInstall(newDir);
            } catch (InterruptedException | ExecutionException e) {
                logger.warn("Failed to get File: {}", e.getMessage());
                logger.handleError(Thread.currentThread(), e);
            }
        });
        setupTask("Icon Pack File Copier", task);
    }

    /**
     * Called when an icon pack is being renamed.
     *
     * @param event The edit event.
     */
    @FXML
    public void onPackEdit(ListView.EditEvent<String> event) {
        String old = packList.getItems().get(event.getIndex());
        if (old != null && !old.equalsIgnoreCase(INSTALLED)) {
            String newName = event.getNewValue();
            if (!newName.isEmpty()) {
                if (IconPack.hasName(newName)) {
                    setStatus("An icon pack with that name already exists.", Type.WARN);
                    return;
                }
                String item = packList.getSelectionModel().getSelectedItem();
                if (item != null && !item.isEmpty()) {
                    IconPack pack = IconPack.fromName(item);
                    if (pack != null) {
                        logger.debug("Set new name for Icon Pack to '{}'", newName);
                        pack.setName(newName);
                        setPerkTree(null);
                        constructIconPacks();
                        event.consume();
                    }
                }
            } else
                setStatus("You cannot leave the name empty.", Type.WARN);
        } else
            setStatus("You cannot rename this item.", Type.WARN);
    }

    /**
     * Called when the "Refresh Icon Packs" menu item is clicked.
     */
    @FXML
    public void onRefreshIconPacks() {
        logger.debug("Refreshing icon packs...");
        setPerkTree(null);
        constructIconPacks();
        setStatus("Refreshed icon packs list.", Type.NONE);
    }

    /**
     * Called when the "Install Icon Pack" menu item is clicked.
     */
    @FXML
    public void onInstallIconPack() {
        String name = packList.getSelectionModel().getSelectedItem();
        if (!name.equalsIgnoreCase(INSTALLED)) {
            IconPack pack = IconPack.fromName(name);
            if (pack == null) {
                logger.warn("Failed to get icon pack from name with name '{}'", name);
                constructIconPacks();
                return;
            }
            setDisabled(true);
            setStatus("Installing icon pack '" + name + "'...", Type.NONE);
            IconInstallTask task = new IconInstallTask(pack);
            progressBar.progressProperty().bind(task.progressProperty());
            progressBar.setVisible(true);
            task.setOnSucceeded(event -> {
                progressBar.setVisible(false);
                setDisabled(false);
                setStatus("Successfully installed icon pack '" + pack.getMeta().getName() + "'.", Type.NONE);
            });
            setupTask("Icon Pack Installer", task);
        }
    }

    /**
     * Called when the "Delete Icon Pack" menu item is clicked.
     */
    @FXML
    public void onDeleteIconPack() {
        String name = packList.getSelectionModel().getSelectedItem();
        if (name != null && !name.equalsIgnoreCase(INSTALLED)) {
            IconPack pack = IconPack.fromName(name);
            if (pack == null) {
                logger.warn("Failed to get icon pack from name with name '{}'", name);
                constructIconPacks();
                return;
            }
            iconDisplay.setImage(null);
            setPerkTree(null);
            installIconPack.setVisible(false);
            deleteIconPack.setVisible(false);
            setDisabled(true);
            File folder = pack.getFolder();
            pack.dispose();
            DeleteTask task = new DeleteTask(folder, true);
            progressBar.progressProperty().bind(task.progressProperty());
            progressBar.setVisible(true);
            task.setOnSucceeded(e -> {
                setStatus("Deleted icon pack '" + name + "'.", Type.NONE);
                constructIconPacks();
                progressBar.setVisible(false);
                setDisabled(false);
            });
            setupTask("Icon Pack Disposer", task);
        }
    }

    @FXML
    public void onInstallIcon() {
        
    }

    @FXML
    public void onUpdateIcon() {

    }

    @FXML
    public void onPerkTree() {
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
                setStatus("Encountered an error, check logs.", Type.ERROR);
                return;
            }
            Icon icon = PBD.getIcon(name);
            if (icon != null)
                iconDisplay.setImage(toImage(icon, pack));
            else
                logger.warn("Couldn't find icon with name '{}'", name);
            iconMenu.setVisible(true);
        }
    }

    @FXML
    public void onPackList() {
        String name = packList.getSelectionModel().getSelectedItem();
        if (name == null || name.isEmpty()) {
            setPerkTree(null);
            return;
        }
        logger.debug("Clicked on: {}", name);
        IconPack pack = IconPack.fromName(name);
        if (pack != null) {
            installIconPack.setVisible(true);
            deleteIconPack.setVisible(true);
            setPerkTree(pack);
        } else
            logger.warn("Failed to get icon pack with name '{}', selected via packList.", name);
    }

    @FXML
    public void onDarkMode() {
        setMode(true);
    }

    @FXML
    public void onLightMode() {
        setMode(false);
    }

    public void setDisabled(boolean disabled) {
        perkTree.setDisable(disabled);
        packList.setDisable(disabled);
        menuBar.setDisable(disabled);
    }

    public void setStatus(@Nonnull String status, @Nonnull Type type) {
        this.status.setGraphic(type.asImageView());
        this.status.setText(status);
        this.status.setVisible(true);
        // check if the status bar is already displaying something
        // this prevents new messages from disappearing too early
        if (this.statusTask != null && !this.statusTask.isDone())
            this.statusTask.cancel(true);
        this.statusTask = PBD.getService().schedule(() -> this.status.setVisible(false),
                CONFIG.getStatusDuration(), TimeUnit.SECONDS);
    }

    private void verifyAndInstall(@Nonnull File dir) {
        if (!dir.isDirectory())
            throw new IllegalArgumentException("Given file '" + dir.getName() + "' is not directory.");
        if (PackMeta.eval(dir)) {
            MoveTask task = new MoveTask(dir, PBD.getPacksDir());
            progressBar.progressProperty().bind(task.progressProperty());
            task.setOnSucceeded(event -> {
                try {
                    IconPack pack = IconPack.of(task.get());
                    if (pack == null) {
                        setStatus("Failed to add icon pack, check logs for details.", Type.WARN);
                        setDisabled(false);
                        progressBar.setVisible(false);
                        return;
                    }
                    constructIconPacks();
                    setStatus("Successfully added icon pack '" + dir.getName() + "'.", Type.NONE);
                    progressBar.setVisible(false);
                    setDisabled(false);
                } catch (ExecutionException | InterruptedException e) {
                    logger.warn("Failed to move folder: {}", e.getMessage());
                    logger.handleError(Thread.currentThread(), e);
                }
            });
            setStatus("Evaluation complete, moving files...", Type.NONE);
            setupTask("Icon Pack Verifier", task);
        } else {
            setStatus("'" + dir.getName() + "' could not be eval'd as an icon pack.", Type.WARN);
            setDisabled(false);
            progressBar.setVisible(false);
            logger.warn("Given directory '{}' could not be evalutated as a valid icon pack.", dir.getName());
        }
    }

    private void constructIconPacks() {
        packList.getItems().clear();
        IconPack installed = IconPack.fromName(INSTALLED);
        if (installed == null) {
            installed = IconPack.of(PBD.getIconsDir());
            if (installed != null)
                installed.setName(INSTALLED);
        }
        IconPack.getPacks().forEach(pack -> {
            if (pack.refresh())
                packList.getItems().add(pack.getMeta().getName());
        });
        packList.refresh();
        deleteIconPack.setVisible(false);
        installIconPack.setVisible(false);
    }

    private void setPerkTree(@Nullable IconPack pack) {
        if (pack == null) {
            perkTree.setRoot(null);
            iconMenu.setVisible(false);
        } else
            perkTree.setRoot(pack.getTree());
        perkTree.refresh();
    }

    private void setupTask(@Nonnull String name, @Nonnull Task<?> task) {
        task.setOnFailed(event -> {
            Throwable throwable = task.getException();
            if (throwable == null) {
                logger.error("Task '{}' failed but exception thrown is null.", name);
                return;
            }
            logger.warn("Encountered an exception whilst running task '{}': {}", name, throwable.getMessage());
            logger.handleError(Thread.currentThread(), throwable);
        });
        Thread thread = new Thread(task);
        thread.setName(name);
        thread.start();
    }

    @Nullable
    @ParametersAreNonnullByDefault
    private Image toImage(Icon icon, IconPack pack) {
        File folder = pack.getFolder();
        try (FileInputStream in = new FileInputStream(icon.asFile(folder))) {
            return new Image(in);
        } catch (IOException | IllegalArgumentException e) {
            // a fail-safe for packs that have been deleted by the user
            if (pack.refresh()) {
                logger.warn("Failed to get JavaFX Image from icon '{}' in pack '{}': {}",
                        icon.getProperName(), pack.getMeta().getName(), e.getMessage());
                setStatus("Encountered an error, check logs.", Type.ERROR);
                logger.handleError(Thread.currentThread(), e);
            } else {
                setPerkTree(null);
                constructIconPacks();
            }
        }
        return null;
    }

    public enum Type {

        NONE(null),
        WARN(getResource("/pic/warnicon_small.png")),
        ERROR(getResource("/pic/erricon_small.png"));

        @Nullable
        private final Image image;

        Type(@Nullable Image image) {
            this.image = image;
        }

        public ImageView asImageView() {
            if (image == null)
                return null;
            return new ImageView(image);
        }

        private static Image getResource(@Nullable String name) {
            if (name == null)
                return null;
            return new Image(PBD.class.getResource(name).toString());
        }
    }
}