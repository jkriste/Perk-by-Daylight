package dev.glitchedcode.pbd.gui.controller;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.dbd.icon.Icon;
import dev.glitchedcode.pbd.gui.task.CacheDeletionTask;
import dev.glitchedcode.pbd.gui.task.CopyTask;
import dev.glitchedcode.pbd.gui.task.DeleteTask;
import dev.glitchedcode.pbd.gui.task.DownloadTask;
import dev.glitchedcode.pbd.gui.task.IconInstallTask;
import dev.glitchedcode.pbd.gui.task.UnzipTask;
import dev.glitchedcode.pbd.gui.task.UpdateTask;
import dev.glitchedcode.pbd.json.Config;
import dev.glitchedcode.pbd.json.LatestRelease;
import dev.glitchedcode.pbd.logger.Logger;
import dev.glitchedcode.pbd.pack.IconPack;
import dev.glitchedcode.pbd.pack.PackMeta;
import javafx.application.Platform;
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
    @FXML private MenuItem packInfoMenu;
    @FXML private ImageView iconDisplay;
    @FXML private CheckMenuItem darkMode;
    @FXML private ProgressBar progressBar;
    @FXML private MenuItem deleteIconPack;
    @FXML private CheckMenuItem lightMode;
    @FXML private MenuItem installIconPack;
    @FXML private TreeView<String> perkTree;
    @FXML private ListView<String> packList;

    private Stage stage;
    private ScheduledFuture<?> updateTask;
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
        packInfoMenu.setVisible(false);
        constructIconPacks();
        // Makes the icon pack list editable.
        packList.setEditable(true);
        packList.setCellFactory(TextFieldListCell.forListView());
        logger.info("Initialized JavaFX form successfully.");
        this.updateTask = PBD.getService().schedule(() -> {
            if (CONFIG.isOfflineMode() || CONFIG.doesIgnoreUpdates())
                return;
            LatestRelease.refresh();
            Platform.runLater(this::onCheckUpdate);
        }, 2, TimeUnit.SECONDS);
    }

    /**
     * Called when the "Preferences..." menu item is clicked.
     */
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
            stage.setResizable(false);
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
        if (!updateTask.isDone())
            return;
        setStatus("Checking for update...", Type.NONE);
        UpdateTask task = new UpdateTask();
        task.setOnSucceeded(e -> {
            try {
                boolean success = task.get();
                if (success) {
                    FXMLLoader loader = new FXMLLoader(PBD.class.getResource("/form/UpdateForm.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    UpdateController controller = loader.getController();
                    controller.setStage(stage, this);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setAlwaysOnTop(true);
                    stage.initStyle(StageStyle.UNIFIED);
                    stage.setTitle("Update Available");
                    stage.setResizable(false);
                    stage.getIcons().add(new Image(PBD.class.getResourceAsStream("/pic/pbd_icon.png")));
                    if (CONFIG.isDarkMode())
                        stage.getScene().getStylesheets().add(darkTheme.toExternalForm());
                    stage.show();
                } else {
                    setStatus("Latest version " + PBD.VERSION.toString() + " installed.", Type.NONE);
                    setDisabled(false);
                }
            } catch (InterruptedException | ExecutionException | IOException ex) {
                handleError("Update Task", ex);
            }
        });
        setupTask("Update Task", task, false);
    }

    /**
     * Called when the "Delete Cache" menu item is clicked.
     */
    @FXML
    public void onDeleteCache() {
        setStatus("Deleting cached files...", Type.NONE);
        CacheDeletionTask task = new CacheDeletionTask();
        task.setOnSucceeded(e -> {
            try {
                setDisabled(false);
                progressBar.setVisible(false);
                setStatus("Deleted " + task.get() + " cached file(s).", Type.NONE);
            } catch (InterruptedException | ExecutionException ex) {
                handleError("Cache Deletion Thread", ex);
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
        try {
            FXMLLoader loader = new FXMLLoader(PBD.class.getResource("/form/DonationForm.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            DonationController controller = loader.getController();
            controller.setStage(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            stage.initStyle(StageStyle.UNIFIED);
            stage.setTitle("Donate");
            stage.setResizable(false);
            stage.getIcons().add(new Image(PBD.class.getResourceAsStream("/pic/pbd_icon.png")));
            if (CONFIG.isDarkMode())
                stage.getScene().getStylesheets().add(darkTheme.toExternalForm());
            stage.show();
        } catch (IOException e) {
            logger.warn("Failed to open DonationForm.fxml: {}", e.getMessage());
            logger.handleError(Thread.currentThread(), e);
        }
    }

    /**
     * Called when the "From ZIP..." menu item is clicked.
     */
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
        setStatus("Unzipping '" + file.getName() + "'...", Type.NONE);
        task.setOnSucceeded(event -> {
            try {
                File newDir = task.get();
                logger.debug("Unzipped file '{}' successfully: {}", file.getName(), newDir.getAbsolutePath());
                verifyAndInstall(newDir);
            } catch (InterruptedException | ExecutionException e) {
                handleError("Unzipper Thread", e);
            }
        });
        setupTask("Unzipper Thread", task);
    }

    /**
     * Called when the "From Folder..." menu item is clicked.
     */
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
        CopyTask task = new CopyTask(file, new File(PBD.getTempDir(), file.getName()), false);
        setStatus("Copying '" + file.getName() + "' to 'temp' directory.", Type.NONE);
        task.setOnSucceeded(event -> {
            try {
                File newDir = task.get();
                logger.debug("Moved file '{}' successfully: {}", file.getName(), newDir.getAbsolutePath());
                verifyAndInstall(newDir);
            } catch (InterruptedException | ExecutionException e) {
                handleError("File Copier Thread", e);
            }
        });
        setupTask("File Copier Thread", task);
    }

    /**
     * Called when the "Icon Pack Info..." menu option is clicked.
     */
    @FXML
    public void onPackInfo() {
        String name = packList.getSelectionModel().getSelectedItem();
        IconPack pack = IconPack.fromName(name);
        if (pack == null)
            return;
        try {
            FXMLLoader loader = new FXMLLoader(PBD.class.getResource("/form/IconPackInfoForm.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            IconPackInfoController controller = loader.getController();
            controller.setStage(stage);
            controller.setIconPack(pack);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            stage.initStyle(StageStyle.UNIFIED);
            stage.setTitle("'" + name + "' Pack Info");
            stage.setResizable(false);
            stage.getIcons().add(new Image(PBD.class.getResourceAsStream("/pic/pbd_icon.png")));
            if (CONFIG.isDarkMode())
                stage.getScene().getStylesheets().add(darkTheme.toExternalForm());
            stage.show();
        } catch (IOException e) {
            logger.warn("Failed to open IconPackInfoForm.fxml: {}", e.getMessage());
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
            setStatus("Installing icon pack '" + name + "'...", Type.NONE);
            IconInstallTask task = new IconInstallTask(pack);
            task.setOnSucceeded(event -> {
                try {
                    logger.info("Successfully installed {} icon(s) from icon pack '{}'.",
                            task.get(), name);
                    progressBar.setVisible(false);
                    setDisabled(false);
                    setStatus("Successfully installed icon pack '" + pack.getMeta().getName() + "'.", Type.NONE);
                } catch (InterruptedException | ExecutionException e) {
                    handleError("Installer Thread", e);
                }
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
            File folder = pack.getFolder();
            pack.dispose();
            DeleteTask task = new DeleteTask(folder, true);
            task.setOnSucceeded(e -> {
                setStatus("Deleted icon pack '" + name + "'.", Type.NONE);
                constructIconPacks();
                progressBar.setVisible(false);
                setDisabled(false);
            });
            setupTask("Pack Deletion Thread", task);
        }
    }

    /**
     * Called when the "Install Icon" menu option is clicked.
     */
    @FXML
    public void onInstallIcon() {
        String name = packList.getSelectionModel().getSelectedItem();
        if (name == null || name.equalsIgnoreCase(INSTALLED))
            return;
        IconPack pack = IconPack.fromName(name);
        if (pack == null) {
            logger.warn("Failed to get icon pack from name '{}'", name);
            constructIconPacks();
            return;
        }
        String iconName = perkTree.getSelectionModel().getSelectedItem().getValue();
        Icon icon = PBD.getIconProper(iconName);
        if (icon == null || pack.getMeta().isMissingIcon(icon)) {
            logger.warn("Failed to get an icon with the name '{}'/icon is missing.", iconName);
            constructIconPacks();
            return;
        }
        IconInstallTask task = new IconInstallTask(pack, icon);
        task.setOnSucceeded(e -> {
            setStatus("Successfully installed icon '" + iconName + "' from icon pack '" + name + "'.", Type.NONE);
            progressBar.setVisible(false);
            setDisabled(false);
        });
        setupTask("Icon Pack Installer", task);
    }

    /**
     * Called when the "Update Icon..." menu option is clicked.
     */
    @FXML
    public void onUpdateIcon() {

    }

    /**
     * Called when the icon perk tree is clicked on.
     */
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
            Icon icon = PBD.getIconProper(name);
            if (icon != null)
                iconDisplay.setImage(toImage(icon, pack));
            else
                logger.warn("Couldn't find icon with name '{}'", name);
            iconMenu.setVisible(true);
        } else
            iconMenu.setVisible(false);
    }

    /**
     * Called when the icon pack list is clicked on.
     */
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
            installIconPack.setVisible(!name.equalsIgnoreCase(INSTALLED));
            deleteIconPack.setVisible(!name.equalsIgnoreCase(INSTALLED));
            setPerkTree(pack);
        } else
            logger.warn("Failed to get icon pack with name '{}', selected via packList.", name);
    }

    /**
     * Called when the "Dark Theme" menu option is clicked.
     */
    @FXML
    public void onDarkMode() {
        setMode(true);
    }

    /**
     * Called when the "Light Theme" menu option is clicked.
     */
    @FXML
    public void onLightMode() {
        setMode(false);
    }

    /**
     * Disables the UI.
     *
     * @param disabled True to disable, false to enable.
     */
    public void setDisabled(boolean disabled) {
        perkTree.setDisable(disabled);
        packList.setDisable(disabled);
        menuBar.setDisable(disabled);
    }

    /**
     * Sets the status bar with the given status and icon type.
     *
     * @param status The status.
     * @param type The type of icon to display with the message.
     * @see Type
     */
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

    /**
     * Verifies and installs the icon pack.
     * <br />
     * The given directory should be in the 'temp' folder.
     *
     * @param dir The directory.
     */
    private void verifyAndInstall(@Nonnull File dir) {
        if (!dir.isDirectory())
            throw new IllegalArgumentException("Given file '" + dir.getName() + "' is not directory.");
        if (PackMeta.eval(dir)) {
            CopyTask task = new CopyTask(dir, PBD.getPacksDir(), true);
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
                    handleError("Verifier Thread", e);
                }
            });
            setStatus("Evaluation complete, moving files...", Type.NONE);
            setupTask("Verifier Thread", task);
        } else {
            setStatus("'" + dir.getName() + "' could not be eval'd as an icon pack.", Type.WARN);
            setDisabled(false);
            progressBar.setVisible(false);
            logger.warn("Given directory '{}' could not be evalutated as a valid icon pack.", dir.getName());
        }
    }

    /**
     * Constructs the icon packs for the packs list.
     */
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

    /**
     * Sets the perk tree item to the given {@link IconPack}'s tree item.
     *
     * @param pack The selected icon pack.
     */
    private void setPerkTree(@Nullable IconPack pack) {
        if (pack == null) {
            perkTree.setRoot(null);
            iconMenu.setVisible(false);
            packInfoMenu.setVisible(false);
        } else {
            packInfoMenu.setVisible(true);
            perkTree.setRoot(pack.getIcons());
        }
        perkTree.refresh();
    }

    /**
     * Does {@link #setupTask(String, Task, boolean)} but sets the progress bar visible.
     *
     * @param name The name of the task.
     * @param task The task instance.
     * @see #setupTask(String, Task, boolean)
     */
    private void setupTask(@Nonnull String name, @Nonnull Task<?> task) {
        setupTask(name, task, true);
    }

    /**
     * Does boilerplate code for tasks:
     * <ul>
     *     <li>Handles errors on task failure.</li>
     *     <li>Binds the progress property of the task to the progress bar.</li>
     *     <li>Sets the progress bar visible.</li>
     *     <li>Disables the main UI.</li>
     *     <li>Creates a thread, sets the name, and starts it.</li>
     * </ul>
     *
     * @param name The name of the task.
     * @param task The task instance.
     * @param barVisible True if the progress bar should be visible.
     */
    private void setupTask(@Nonnull String name, @Nonnull Task<?> task, boolean barVisible) {
        task.setOnFailed(event -> handleError(name, task.getException()));
        progressBar.progressProperty().bind(task.progressProperty());
        progressBar.setVisible(barVisible);
        setDisabled(true);
        Thread thread = new Thread(task);
        thread.setName(name);
        thread.start();
    }

    /**
     * Handles errors for tasks when they fail.
     *
     * @param name The name of the task.
     * @param e The exception thrown.
     */
    private void handleError(@Nonnull String name, @Nullable Throwable e) {
        setStatus("An error occurred during task '" + name + "', check logs.", Type.ERROR);
        progressBar.setVisible(false);
        setDisabled(false);
        if (e == null)
            logger.error("Task '{}' failed but exception thrown is null.", name);
        else {
            logger.warn("An error occurred during task '{}': {}", name, e.getMessage());
            logger.handleError(Thread.currentThread(), e);
        }
    }

    /**
     * Gets the image for the given {@link Icon} and {@link IconPack}.
     *
     * @param icon The type of icon to display.
     * @param pack The icon pack to get the image from.
     * @return The image for the given {@link Icon} and {@link IconPack}, or null.
     */
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
                setStatus("Encountered an error fetching image, check logs.", Type.ERROR);
                logger.handleError(Thread.currentThread(), e);
            } else {
                setPerkTree(null);
                constructIconPacks();
            }
        }
        return null;
    }

    /**
     * Called when the "Update" button is clicked on the Update form.
     */
    void startDownload() {
        DownloadTask task = new DownloadTask();
        task.setOnSucceeded(e -> {
            try {
                boolean success = task.get();
                if (!success)
                    setStatus("Download failed, please check logs.", Type.ERROR);
                else
                    PBD.close(1);
                setDisabled(false);
                progressBar.setVisible(false);
            } catch (ExecutionException | InterruptedException ex) {
                handleError("Download Thread", ex);
            }
        });
        setStatus("Downloading new update...", Type.NONE);
        setupTask("Download Thread", task);
    }

    /**
     * Distinguishes the different icons that can be shown on the status bar.
     */
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