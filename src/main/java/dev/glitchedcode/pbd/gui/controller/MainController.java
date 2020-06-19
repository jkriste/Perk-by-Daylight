package dev.glitchedcode.pbd.gui.controller;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.dbd.Icon;
import dev.glitchedcode.pbd.gui.task.DeleteTask;
import dev.glitchedcode.pbd.gui.task.ExampleTask;
import dev.glitchedcode.pbd.json.LatestRelease;
import dev.glitchedcode.pbd.logger.Logger;
import dev.glitchedcode.pbd.pack.IconPack;
import javafx.event.ActionEvent;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
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
        ExampleTask task = new ExampleTask(() -> {
            logger.info("poopy butthole");
            progressBar.setVisible(false);
        });
        progressBar.progressProperty().bind(task.progressProperty());
        constructIconPacks();
        // Makes the icon pack list editable.
        packList.setEditable(true);
        packList.setCellFactory(TextFieldListCell.forListView());
    }

    @FXML
    public void onPreferences(ActionEvent event) {
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
            if (PBD.getConfig().isDarkMode())
                stage.getScene().getStylesheets().add(darkTheme.toExternalForm());
            stage.show();
        } catch (IOException e) {
            logger.warn("Failed to open PreferenceForm.fxml: {}", e.getMessage());
            logger.handleError(Thread.currentThread(), e);
        }
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
     *
     * @param event The action event.
     */
    @FXML
    public void onDeleteTempContents(ActionEvent event) {
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
        new Thread(task).start();
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
            setStatus("Failed to open PBD directory, check logs.", Type.ERROR);
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
                setStatus("Not acceptable new name.", Type.WARN);
                return;
            }
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
     *
     * @param event The action event.
     */
    @FXML
    public void onRefreshIconPacks(ActionEvent event) {
        logger.debug("Refreshing icon packs...");
        setPerkTree(null);
        constructIconPacks();
        setStatus("Refreshed icon packs list.", Type.NONE);
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
            setStatus("Installing icon pack '" + name + "'...", Type.NONE);
            pack.install(progressBar, () -> {
                setStatus("Icon pack '" + name + "' installed.", Type.NONE);
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
            new Thread(task).start();
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
    public void onPackList(MouseEvent event) {
        String name = packList.getSelectionModel().getSelectedItem();
        if (name == null || name.isEmpty()) {
            setPerkTree(null);
            return;
        }
        logger.debug("Clicked on: {}", name);
        if (name.equalsIgnoreCase(NEW_PACK)) {
            installIconPack.setVisible(false);
            deleteIconPack.setVisible(false);
            setPerkTree(null);
            addNewPack();
            return;
        }
        IconPack pack = IconPack.fromName(name);
        if (pack != null) {
            installIconPack.setVisible(true);
            deleteIconPack.setVisible(true);
            setPerkTree(pack);
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

    public void update(double progress) {
        if (progress == -1D) {
            this.progressBar.setVisible(false);
            return;
        }
        if (!this.progressBar.isVisible())
            this.progressBar.setVisible(true);
        this.progressBar.setProgress(progress);
    }

    public void setDisabled(boolean disabled) {
        perkTree.setDisable(disabled);
        packList.setDisable(disabled);
        menuBar.setDisable(disabled);
    }

    public void setStatus(@Nonnull String status, @Nonnull Type type) {
        this.status.setGraphic(type.asImageView());
        this.status.setTextFill(PBD.getConfig().isDarkMode() ? Color.WHITE : Color.BLACK);
        this.status.setText(status);
        this.status.setVisible(true);
        PBD.getService().schedule(() -> this.status.setVisible(false),
                PBD.getConfig().getStatusDuration(), TimeUnit.SECONDS);
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
        IconPack pack = IconPack.newPack(file, progressBar);
        if (pack == null)
            setStatus("Failed to process file, check logs.", Type.ERROR);
        constructIconPacks();
    }

    private void setPerkTree(@Nullable IconPack pack) {
        if (pack == null) {
            perkTree.setRoot(null);
            iconMenu.setVisible(false);
        } else
            perkTree.setRoot(pack.getTree());
        perkTree.refresh();
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

    /**
     * Gets the file count of a directory.
     * <br />
     * Will return 0 if the file doesn't exist, or {@link File#listFiles()} is null.
     * <br />
     * Will return 1 if the file is not a directory.
     *
     * @param file The directory to search.
     * @return The file count of a directory.
     */
    private int getCount(@Nonnull File file) {
        logger.debug("Getting file count for file '{}'", file.getName());
        if (!file.exists())
            return 0;
        if (!file.isDirectory())
            return 1;
        File[] files = file.listFiles();
        if (files == null)
            return 0;
        int count = 0;
        for (File f : files) {
            if (f.isDirectory())
                count += getCount(f);
            else
                count++;
        }
        return count;
    }

//    /**
//     * Deletes all files inside of the directory.
//     *
//     * @param directory The directory.
//     * @param deleteDir True if the given directory should be deleted as well.
//     */
//    @ParametersAreNonnullByDefault
//    private void deleteAll(File directory, boolean deleteDir) throws IOException {
//        if (!directory.isDirectory()) {
//            logger.warn("Given file '{}' was not a directory, but will be deleted.", directory.getName());
//            java.nio.file.Files.delete(directory.toPath());
//            return;
//        }
//        int current = 0;
//        File[] files = directory.listFiles();
//        if (files != null) {
//            for (File file : files) {
//                if (file.isDirectory())
//                    deleteAll(file, progressBar, true);
//                else {
//                    java.nio.file.Files.delete(file.toPath());
//                    current++;
//                    bar.setProgress((double) current / (double) files.length);
//                }
//            }
//        } else
//            logger.warn("#listFiles() returned null when trying to delete dir '{}'", directory.getName());
//        if (deleteDir)
//            java.nio.file.Files.delete(directory.toPath());
//    }

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