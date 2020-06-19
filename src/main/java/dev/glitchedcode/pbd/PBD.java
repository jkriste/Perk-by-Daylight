package dev.glitchedcode.pbd;

import com.google.common.base.CaseFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import dev.glitchedcode.pbd.dbd.Addon;
import dev.glitchedcode.pbd.dbd.Item;
import dev.glitchedcode.pbd.dbd.Offering;
import dev.glitchedcode.pbd.dbd.Perk;
import dev.glitchedcode.pbd.dbd.Portrait;
import dev.glitchedcode.pbd.gui.Icon;
import dev.glitchedcode.pbd.gui.controller.MainController;
import dev.glitchedcode.pbd.json.Config;
import dev.glitchedcode.pbd.logger.Logger;
import dev.glitchedcode.pbd.pack.IconPack;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.fusesource.jansi.AnsiConsole;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class PBD extends Application {

    /**
     * Handles the reading & writing of configs in JSON.
     */
    private static Gson GSON;
    /**
     * The location of the Dead by Daylight folder used by Steam.
     */
    private static File DBD_DIR;
    /**
     * Handles the logging of info into a text file located in the 'Perk by Daylight' folder.
     */
    private static Logger LOGGER;
    /**
     * The config file instance, located in the 'Perk by Daylight' folder.
     */
    private static Config CONFIG;
    /**
     * The location of the "Icons" folder in DBD.
     */
    private static File ICONS_DIR;
    /**
     * The PerkByDaylight folder located in the %appdata% folder.
     */
    public static final File PBD_DIR;
    /**
     * Temporary folder for holding packs that need verification.
     */
    public static final File TEMP_DIR;
    /**
     * The location all cached icon packs are stored.
     */
    public static final File PACKS_DIR;
    /**
     * The location of the config file, in the 'Perk by Daylight' folder.
     */
    public static final File CONFIG_FILE;
    /**
     * The location of the "Roaming" folder in %appdata%.
     */
    public static final File APP_DATA_DIR;
    /**
     * The location of the "Program Files (x86)" folder.
     */
    public static final File PROGRAM_FILES_86_DIR;
    /**
     * The current version of this program.
     */
    public static final String VERSION = "v0.0.1-BETA";
    /**
     * A convenient way to access all icons.
     */
    private static Set<dev.glitchedcode.pbd.dbd.Icon> ICONS;
    /**
     * Used to schedule async tasks.
     */
    private static final ScheduledExecutorService SERVICE = Executors.newScheduledThreadPool(1);

    static {
        APP_DATA_DIR = new File(System.getenv("APPDATA"));
        PROGRAM_FILES_86_DIR = new File(System.getenv("ProgramFiles(X86)"));
        DBD_DIR = new File(PROGRAM_FILES_86_DIR, "Steam\\steamapps\\common\\Dead by Daylight");
        PBD_DIR = new File(APP_DATA_DIR, "Perk by Daylight");
        PACKS_DIR = new File(PBD_DIR, "packs");
        TEMP_DIR = new File(PBD_DIR, "temp");
        CONFIG_FILE = new File(PBD_DIR, "config.json");
    }

    public static void main(String[] args) {
        // Check if the system we're running on is Windows
        if (!isWindows()) {
            popup("Invalid Operating System:\nThis software is only supported on Windows.", Icon.ERROR);
            System.exit(-1);
        }
        // Check if PDB folder exists -> if not, create it
        if (!PBD_DIR.exists()) {
            if (PBD_DIR.mkdir())
                System.out.println("Created 'Perk by Daylight' folder.");
            else
                popup("Failed to create folder:\n{}", Icon.ERROR, PBD_DIR.getAbsolutePath());
        }
        // Builds our Gson object used to read & write to/from JSON files.
        GSON = new GsonBuilder()
                .setPrettyPrinting()
                .setLenient()
                .create();
        // load config file
        CONFIG = loadConfig();
        if (CONFIG == null) {
            System.out.println("'config.json' was not saved properly last run and is empty/corrupt.");
            System.out.println("The file will be deleted on exit and regenerated on restart.");
            CONFIG_FILE.deleteOnExit();
            System.exit(0);
        }
        // Install Jansi
        AnsiConsole.systemInstall();
        // Create out LOGGER for info & error reporting
        LOGGER = new Logger(PBD_DIR);
        if (CONFIG.debug())
            LOGGER.debug("Debug mode has been enabled. Expect excessive spam.");
        // Letting the LOGGER handle errors.
        Thread.setDefaultUncaughtExceptionHandler(getExceptionHandler());
        // Set up all additional files & folders.
        setupFiles();
        // Launch the application
        launch(args);
    }

    /**
     * Closes the application.
     *
     * @param code The exit code.
     */
    public static void close(int code) {
        LOGGER.debug("Closing application with code {}", code);
        Platform.exit();
        LOGGER.debug("Closed application with Platform#exit");
        IconPack.saveAll();
        LOGGER.debug("Saved all icon packs.");
        saveConfig();
        LOGGER.debug("Saved config...closing logger.");
        LOGGER.close();
        SERVICE.shutdown();
        try {
            if (!SERVICE.awaitTermination(500, TimeUnit.MILLISECONDS))
                SERVICE.shutdownNow();
        } catch (InterruptedException e) {
            SERVICE.shutdownNow();
        }
        AnsiConsole.systemUninstall();
        System.exit(code);
    }

    /**
     * Gets the LOGGER instance.
     *
     * @return The LOGGER instance.
     */
    public static Logger getLogger() {
        return LOGGER;
    }

    /**
     * Gets the Dead by Daylight folder.
     *
     * @return The Dead by Daylight folder.
     */
    public static File getDbdDir() {
        return DBD_DIR;
    }

    /**
     * Gets the "temp" folder in the Perk by Daylight folder.
     * <br />
     * Used as a temporary place to identify what we are
     * adding to the software is a valid icon pack.
     *
     * @return The "temp" folder in the Perk by Daylight folder.
     */
    public static File getTempDir() {
        return TEMP_DIR;
    }

    /**
     * Gets the "Icons" folder in the Dead by Daylight folder.
     *
     * @return The "Icons" folder in the Dead by Daylight folder.
     */
    public static File getIconsDir() {
        return ICONS_DIR;
    }

    /**
     * Gets the "packs" folder in the Perk by Daylight folder.
     *
     * @return The "packs" folder in the Perk by Daylight folder.
     */
    public static File getPacksDir() {
        return PACKS_DIR;
    }

    /**
     * Gets the {@link Config} instance.
     *
     * @return The {@link Config} instance.
     */
    public static Config getConfig() {
        return CONFIG;
    }

    /**
     * Gets the {@link File} location of the JAR.
     * <br />
     * Used to replace the current JAR file with an updated one with ease.
     *
     * @return The {@link File} location of the JAR.
     * @throws URISyntaxException Thrown if something goes horribly wrong.
     */
    public static File getJar() throws URISyntaxException {
        return new File(PBD.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI());
    }

    /**
     * Gets the {@link Gson} object built by the {@link GsonBuilder}.
     *
     * @return The {@link Gson} object.
     */
    public static Gson getGson() {
        return GSON;
    }

    /**
     * Gets the {@link ScheduledExecutorService} used to schedule async tasks.
     *
     * @return The {@link ScheduledExecutorService}.
     */
    public static ScheduledExecutorService getService() {
        return SERVICE;
    }

    /**
     * Gets the default {@link java.lang.Thread.UncaughtExceptionHandler}.
     *
     * @return The default {@link java.lang.Thread.UncaughtExceptionHandler}.
     */
    public static Thread.UncaughtExceptionHandler getExceptionHandler() {
        return LOGGER::handleError;
    }

    /**
     * Creates a {@link JOptionPane} message dialog with the given message and {@link Icon}.
     *
     * @param message The message to put.
     * @param icon The icon to put.
     */
    @SuppressWarnings("MagicConstant")
    public static void popup(@Nonnull String message, @Nonnull Icon icon) {
        // TODO: Unimplement Swing's JOptionPane
        JOptionPane.showMessageDialog(null, message, "Perk by Daylight", icon.getId());
    }

    /**
     * Creates a {@link JOptionPane} message dialog with the given message
     * formatted using {@link #format(String, Object...)} and the given {@link Icon}.
     *
     * @param message The message to put.
     * @param icon The icon to put.
     * @param params The objects to insert into the message.
     */
    public static void popup(@Nonnull String message, @Nonnull Icon icon, Object... params) {
        popup(format(message, params), icon);
    }

    /**
     * A useful method that replaces any `{}` with the given params.
     * e.g. {@code format("Hello, {}! My name is {}.", "world", name);}
     * will output {@code "Hello, world! My name is Justin."}
     *
     * @param str The string to be formatted.
     * @param params The objects to insert into the string.
     * @return The formatted string.
     *
     * @apiNote Credit to Walshy for writing this one lmao.
     */
    public static String format(String str, Object... params) {
        boolean b = false;
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (char c : str.toCharArray()) {
            if (c == '{') {
                b = true;
            } else if (c == '}' && b) {
                sb.append(params[i++].toString());
                b = false;
            } else
                sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Formats the given {@link Throwable} into a string.
     * <br />
     * Just my special format, nothing too special about it.
     *
     * @param throwable The given throwable to format.
     * @return The given {@link Throwable} into a formatted string.
     */
    public static String formatStackTrace(@Nonnull Throwable throwable) {
        StringBuilder builder = new StringBuilder();
        do {
            builder.append(throwable.getClass().getName());
            builder.append(": ");
            builder.append(throwable.getMessage());
            builder.append("\n");
            for (StackTraceElement element : throwable.getStackTrace()) {
                builder.append("\t");
                builder.append(element.getClassName());
                builder.append("#");
                builder.append(element.getMethodName());
                builder.append(":");
                builder.append(element.getLineNumber());
                builder.append(element.isNativeMethod() ? " (native method)\n" : "\n");
            }
            builder.append("\n");
        } while ((throwable = throwable.getCause()) != null);
        return builder.toString();
    }

    /**
     * Saves the {@link Config} instance to 'config.json'
     */
    public static void saveConfig() {
        LOGGER.debug("Trying to save config.json file...");
        if (CONFIG == null) {
            LOGGER.warn("#saveConfig() called whilst Config instance is null.");
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            GSON.toJson(CONFIG, Config.class, writer);
            writer.flush();
            LOGGER.debug("Config saved successfully.");
        } catch (IOException e) {
            LOGGER.warn("Failed to save config.json: {}", e.getMessage());
            LOGGER.handleError(Thread.currentThread(), e);
        }
    }

    public static Set<dev.glitchedcode.pbd.dbd.Icon> getIcons() {
        if (ICONS != null)
            return ICONS;
        Set<dev.glitchedcode.pbd.dbd.Icon> icons = new HashSet<>();
        Collections.addAll(icons, Addon.VALUES);
        Collections.addAll(icons, Perk.VALUES);
        Collections.addAll(icons, Portrait.VALUES);
        Collections.addAll(icons, Offering.VALUES);
        Collections.addAll(icons, dev.glitchedcode.pbd.dbd.Action.VALUES);
        Collections.addAll(icons, Item.VALUES);
        ICONS = icons;
        return icons;
    }

    @Nullable
    public static dev.glitchedcode.pbd.dbd.Icon getIcon(@Nonnull String properName) {
        LOGGER.debug("Getting icon with proper name '{}'", properName);
        for (dev.glitchedcode.pbd.dbd.Icon icon : getIcons()) {
            if (icon.getProperName().equalsIgnoreCase(properName))
                return icon;
        }
        LOGGER.warn("Could not find icon with proper name '{}', might not be registered?", properName);
        return null;
    }

    /**
     * Checks if the OS is Windows by checking the OS name.
     *
     * @return True if the OS is Windows.
     */
    private static boolean isWindows() {
        return System.getProperty("os.name").contains("Win");
    }

    /**
     * Loads the 'config.json' file and returns a {@link Config} instance.
     *
     * @return A {@link Config} instance.
     */
    @Nullable
    private static Config loadConfig() {
        if (!CONFIG_FILE.exists()) {
            try {
                if (CONFIG_FILE.createNewFile()) {
                    System.out.println("Created config file at: " + CONFIG_FILE.getAbsolutePath());
                    Config config = new Config(true);
                    // save to the config since it's first-time
                    JsonWriter writer = new JsonWriter(new FileWriter(CONFIG_FILE, false));
                    GSON.toJson(config, Config.class, writer);
                    writer.flush();
                    writer.close();
                    return config;
                }
            } catch (IOException e) {
                System.out.println("Failed to create/save config file: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
                return GSON.fromJson(reader, Config.class);
            } catch (IOException e) {
                System.out.println("Failed to read from config file: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    private static void setupFiles() {
        // Make sure local %appdata% folder exists.
        LOGGER.debug("%appdata% directory path: {}", APP_DATA_DIR.getAbsolutePath());
        if (!APP_DATA_DIR.exists()) {
            popup("Missing app data directory:\n{}", Icon.ERROR, APP_DATA_DIR.getAbsolutePath());
            close(0);
        }
        LOGGER.debug("Icon pack cache directory path: {}", PACKS_DIR.getAbsolutePath());
        if (!PACKS_DIR.exists()) {
            if (PACKS_DIR.mkdir())
                LOGGER.debug("Created 'packs' folder in 'Perk by Daylight' folder.");
        } else {
            File[] files = PACKS_DIR.listFiles();
            assert (PACKS_DIR.isDirectory() && files != null);
            LOGGER.debug("{} files/directories found in '{}'", files.length, PACKS_DIR.getAbsolutePath());
            for (File file : files) {
                if (IconPack.of(file) == null) {
                    LOGGER.warn("File/directory '{}' could not be determined" +
                            " as an icon pack, deleting on exit.", file.getName());
                    file.deleteOnExit();
                }
            }
        }
        if (!TEMP_DIR.exists()) {
            if (TEMP_DIR.mkdir())
                LOGGER.debug("Created 'temp' folder in 'Perk by Daylight' folder.");
        }
        // Checks if the default DBD folder exists, if not let the user get the folder.
        DBD_DIR = getDBDFolder();
        CONFIG.setDbdFolder(DBD_DIR.getAbsolutePath());
        saveConfig();
        ICONS_DIR = new File(DBD_DIR, "DeadByDaylight\\Content\\UI\\Icons");
    }

    /**
     * Gets the Dead by Daylight folder.
     * <br />
     * Only to be used when the {@link #CONFIG_FILE} does not exist.
     *
     * @return The Dead by Daylight folder.
     */
    private static File getDBDFolder() {
        if (!CONFIG.getDbdFolder().isEmpty())
            DBD_DIR = new File(CONFIG.getDbdFolder());
        // Check if the default DBD folder exists & verify it's the DBD folder.
        if (DBD_DIR.exists() && isDBD(DBD_DIR))
            return DBD_DIR;
        LOGGER.debug("Could not locate default DBD dir '{}'", DBD_DIR.getAbsolutePath());
        int result = JOptionPane.showConfirmDialog(null,
                "Could not locate the default Dead by Daylight folder.\n"
                + "Locate the folder manually?", "Perk by Daylight", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.NO_OPTION)
            close(0);
        File folder;
        JFileChooser fileChooser = new JFileChooser(PROGRAM_FILES_86_DIR);
        Action detailsView = fileChooser.getActionMap().get("viewTypeDetails");
        detailsView.actionPerformed(null);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        do {
            int r = fileChooser.showOpenDialog(null);
            if (r == JFileChooser.CANCEL_OPTION)
                System.exit(0);
            folder = fileChooser.getSelectedFile();
            LOGGER.debug("Selected folder: {}", folder.getAbsolutePath());
            if (!folder.getName().equalsIgnoreCase("Dead by Daylight")) {
                popup("Select the \"Dead by Daylight\" folder.", Icon.INFO);
                continue;
            }
            if (!isDBD(folder))
                popup("Could not detect the given folder as the DBD folder.", Icon.WARNING);
        } while (!isDBD(folder));
        CONFIG.setDbdFolder(folder.getAbsolutePath());
        return folder;
    }

    /**
     * Verifies if the given {@link File folder} is the Dead by Daylight folder.
     *
     * @param folder The folder to check.
     * @return True if the given {@link File folder} is the Dead by Daylight folder.
     * @throws IllegalArgumentException Thrown if the given file is not a directory.
     */
    private static boolean isDBD(@Nonnull File folder) {
        if (!folder.isDirectory())
            throw new IllegalArgumentException(format("Given file ({}) is not a directory.",
                    folder.getAbsolutePath()));
        File exe = new File(folder, "DeadByDaylight.exe");
        return exe.exists();
    }

    /**
     * Starts the {@link Application}.
     *
     * @param primaryStage The stage.
     * @throws IOException Thrown if the resource cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/MainForm.fxml"));
        Parent parent = loader.load();
        MainController controller = loader.getController();
        controller.setStage(primaryStage);
        primaryStage.setTitle("Perk by Daylight " + PBD.VERSION);
        primaryStage.setOnHidden(event -> close(0));
        primaryStage.setScene(new Scene(parent));
        primaryStage.setAlwaysOnTop(true);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/pic/pbd_icon.png")));
        controller.setMode(CONFIG.isDarkMode());
        primaryStage.show();
    }

    private static String buildOfferingsEnum(@Nonnull File[] files, @Nullable String dirName) {
        StringBuilder builder = new StringBuilder();
        for (File file : files) {
            if (file.isDirectory()) {
                File[] files1 = file.listFiles();
                assert (files1 != null);
                builder.append(buildOfferingsEnum(files1, file.getName()));
                continue;
            }
            System.out.println(file.getName());
            String name = file.getName().replaceFirst("iconItems_", "").replaceFirst(".png", "");
            String enumName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, name);
            builder.append(enumName)
                    .append("(\"")
                    .append(name)
                    .append("\", ")
                    .append("\"")
                    .append(toTitleCase(enumName.replace('_', ' ')))
                    .append(dirName != null ? ("\", \"" + dirName + "\"") : "\"")
                    .append("),\n");
        }
        builder.append(";");
        return builder.toString();
    }

    private static String toTitleCase(@Nonnull String s) {
        if (s.isEmpty()) {
            return "";
        }

        if (s.length() == 1) {
            return s.toUpperCase();
        }

        StringBuilder resultPlaceHolder = new StringBuilder(s.length());

        Stream.of(s.split(" ")).forEach(stringPart -> {
            char[] charArray = stringPart.toLowerCase().toCharArray();
            charArray[0] = Character.toUpperCase(charArray[0]);
            resultPlaceHolder.append(new String(charArray)).append(" ");
        });


        return resultPlaceHolder.toString().trim();
    }
}