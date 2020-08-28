package dev.glitchedcode.pbd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import dev.glitchedcode.pbd.dbd.icon.Action;
import dev.glitchedcode.pbd.dbd.icon.Addon;
import dev.glitchedcode.pbd.dbd.icon.Archive;
import dev.glitchedcode.pbd.dbd.icon.Help;
import dev.glitchedcode.pbd.dbd.icon.HelpLoading;
import dev.glitchedcode.pbd.dbd.icon.Icon;
import dev.glitchedcode.pbd.dbd.icon.IconCategory;
import dev.glitchedcode.pbd.dbd.icon.Item;
import dev.glitchedcode.pbd.dbd.icon.Offering;
import dev.glitchedcode.pbd.dbd.icon.Perk;
import dev.glitchedcode.pbd.dbd.icon.Portrait;
import dev.glitchedcode.pbd.dbd.icon.Power;
import dev.glitchedcode.pbd.dbd.icon.StatusEffect;
import dev.glitchedcode.pbd.gui.controller.MainController;
import dev.glitchedcode.pbd.json.Config;
import dev.glitchedcode.pbd.json.Latest;
import dev.glitchedcode.pbd.logger.Logger;
import dev.glitchedcode.pbd.pack.IconPack;
import dev.glitchedcode.pbd.util.Files;
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
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
     * The 'Perk by Daylight' folder located in the %appdata% folder.
     */
    public static final File PBD_DIR;
    /**
     * The location of the "logs" directory in the 'Perk by Daylight' folder.
     */
    public static final File LOGS_DIR;
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
     * The location of the "error logs" directory in the 'Perk by Daylight' folder.
     */
    public static final File ERROR_LOGS_DIR;
    /**
     * A convenient way to access all icons.
     */
    private static final Collection<Icon> ICONS;
    /**
     * The location of the "Program Files (x86)" folder.
     */
    public static final File PROGRAM_FILES_86_DIR;
    /**
     * A convenient way to access all icons, sorted by category.
     */
    private static final Map<IconCategory, List<Icon>> ICONS_SORTED;
    /**
     * The current version of this program.
     */
    public static final Latest.Version VERSION = new Latest.Version(0, 2, 0);
    /**
     * Used to schedule async tasks.
     */
    private static final ScheduledExecutorService SERVICE = Executors.newScheduledThreadPool(1);

    // initialize crucial files and directories
    static {
        APP_DATA_DIR = new File(System.getenv("APPDATA"));
        PROGRAM_FILES_86_DIR = new File(System.getenv("ProgramFiles(X86)"));
        DBD_DIR = new File(PROGRAM_FILES_86_DIR, "Steam\\steamapps\\common\\Dead by Daylight");
        PBD_DIR = new File(APP_DATA_DIR, "Perk by Daylight");
        PACKS_DIR = new File(PBD_DIR, "packs");
        TEMP_DIR = new File(PBD_DIR, "temp");
        LOGS_DIR = new File(PBD_DIR, "logs");
        ERROR_LOGS_DIR = new File(PBD_DIR, "error logs");
        CONFIG_FILE = new File(PBD_DIR, "config.json");
    }

    // initialize icons
    static {
        ICONS_SORTED = new EnumMap<>(IconCategory.class);
        ICONS_SORTED.put(IconCategory.ACTION, asList(Action.values()));
        ICONS_SORTED.put(IconCategory.ADDON, asList(Addon.values()));
        ICONS_SORTED.put(IconCategory.ARCHIVE, asList(Archive.values()));
        ICONS_SORTED.put(IconCategory.FAVOR, asList(Offering.values()));
        ICONS_SORTED.put(IconCategory.HELP, asList(Help.values()));
        ICONS_SORTED.put(IconCategory.HELP_LOADING, asList(HelpLoading.values()));
        ICONS_SORTED.put(IconCategory.ITEM, asList(Item.values()));
        ICONS_SORTED.put(IconCategory.PERK, asList(Perk.values()));
        ICONS_SORTED.put(IconCategory.PORTRAIT, asList(Portrait.values()));
        ICONS_SORTED.put(IconCategory.POWER, asList(Power.values()));
        ICONS_SORTED.put(IconCategory.STATUS_EFFECT, asList(StatusEffect.values()));
        ICONS = new HashSet<>();
        for (List<Icon> icons : ICONS_SORTED.values())
            ICONS.addAll(icons);
    }

    public static void main(String[] args) {
        // Check if the system we're running on is Windows
        if (!isWindows()) {
            System.err.println("Invalid operating system. This software is for Windows 7/8/8.1/10.");
            System.err.println("Your operating system: " + System.getProperty("os.name"));
            System.exit(-1);
        }
        // Check if PDB folder exists -> if not, create it
        if (!PBD_DIR.exists()) {
            if (!PBD_DIR.mkdirs()) {
                System.err.println("Failed to create directory: " + PBD_DIR.getAbsolutePath());
                System.err.println("Please create an issue here: https://github.com/glitchedcoder/Perk-by-Daylight");
            }
        }
        // Builds our Gson object used to read & write to/from JSON files.
        GSON = new GsonBuilder()
                .setPrettyPrinting()
                .setLenient()
                .create();
        // load config file
        CONFIG = loadConfig();
        if (CONFIG == null) {
            System.err.println("'config.json' was not saved properly last run and is empty/corrupt.");
            System.err.println("The file will be deleted on exit and regenerated on restart.");
            CONFIG_FILE.deleteOnExit();
            System.exit(0);
        }
        // Install Jansi
        AnsiConsole.systemInstall();
        // Create out LOGGER for info & error reporting
        LOGGER = Logger.getInstance();
        LOGGER.debug("Debug mode has been enabled. Expect excessive spam."); // this won't run unless debug is enabled
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
        SERVICE.shutdown();
        try {
            LOGGER.close();
            if (!SERVICE.awaitTermination(500, TimeUnit.MILLISECONDS))
                SERVICE.shutdownNow();
        } catch (InterruptedException e) {
            SERVICE.shutdownNow();
        } catch (IOException e) {
            System.err.println("Failed to close logger: " + e.getMessage());
            e.printStackTrace();
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
     * A useful method that replaces any `{}` with the given params.
     * e.g. {@code format("Hello, {}! My name is {}.", "world", name);}
     * will output {@code "Hello, world! My name is Justin."}
     *
     * @param str    The string to be formatted.
     * @param params The objects to insert into the string.
     * @return The formatted string.
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
        boolean first = true;
        do {
            builder.append(first ? "" : "Caused by: \n");
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
            first = false;
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
            LOGGER.info("Config saved successfully.");
        } catch (IOException e) {
            LOGGER.warn("Failed to save config.json: {}", e.getMessage());
            LOGGER.handleError(Thread.currentThread(), e);
        }
    }

    /**
     * Gets a {@link Collection} of all existing, registered icons.
     * <br />
     * This {@link Collection} is immutable.
     *
     * @return A {@link Collection} of all existing, registered icons.
     */
    public static Collection<Icon> getIcons() {
        return Collections.unmodifiableCollection(ICONS);
    }

    /**
     * Gets a {@link Map} of all icons, sorted by {@link IconCategory}.
     * <br />
     * This {@link Map} is immutable.
     *
     * @return A {@link Map} of all icons, sorted by {@link IconCategory}.
     */
    public static Map<IconCategory, List<Icon>> getIconsSorted() {
        return Collections.unmodifiableMap(ICONS_SORTED);
    }

    /**
     * Gets an {@link Icon} from the given name. The name should be non-proper.
     *
     * @param name The name of the icon.
     * @return An {@link Icon} from the given name.
     * @see #getIconProper(String)
     */
    @Nullable
    public static Icon getIcon(@Nonnull String name) {
        for (Icon icon : getIcons()) {
            if (icon.asFileName().equals(name))
                return icon;
        }
        LOGGER.warn("Could not find icon with name '{}', might not be registered?", name);
        return null;
    }

    /**
     * Gets an {@link Icon} from the given proper name.
     *
     * @param properName The proper name of the icon.
     * @return An {@link Icon} from the given proper name.
     * @see #getIcon(String)
     */
    @Nullable
    public static Icon getIconProper(@Nonnull String properName) {
        LOGGER.debug("Getting icon with proper name '{}'", properName);
        for (Icon icon : getIcons()) {
            if (icon.getProperName().equals(properName))
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
     * Turns the given array into a {@link Set}.
     * <br />
     * Note: the returned {@link Set} is immutable.
     *
     * @param array The array to turn into a set.
     * @param <T>   Sub-class of {@link Icon}.
     * @return A {@link Set}.
     */
    @Nonnull
    private static <T extends Icon> List<Icon> asList(T[] array) {
        List<Icon> icons = new ArrayList<>();
        Collections.addAll(icons, array);
        return Collections.unmodifiableList(icons);
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
                System.err.println("Failed to create/save config file: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
                return GSON.fromJson(reader, Config.class);
            } catch (IOException e) {
                System.err.println("Failed to read from config file: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Handles multiple things:
     * <ul>
     *     <li>Makes sure the Roaming appdata folder exists.</li>
     *     <li>Initializes and caches all valid icon packs in the 'packs' folder.</li>
     *     <li>Deletes any files or folders that do not represent an icon pack in the 'packs' folder.</li>
     *     <li>Makes sure the 'temp', 'logs', 'error logs', and 'packs' folders exist/is created.</li>
     *     <li>Calls {@link #getDbdDir()} and {@link #saveConfig()}</li>
     * </ul>
     */
    private static void setupFiles() {
        // Make sure local %appdata% folder exists.
        LOGGER.debug("%appdata% directory path: {}", APP_DATA_DIR.getAbsolutePath());
        if (!APP_DATA_DIR.exists()) {
            LOGGER.warn("Your local %appdata% directory seems to be missing: {}", APP_DATA_DIR.getAbsolutePath());
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
                            " to be an icon pack, deleting...", file.getName());
                    try {
                        Files.deleteAll(file, true);
                    } catch (IOException e) {
                        LOGGER.warn("Failed to delete file '{}': {}", file.getAbsolutePath(), e.getMessage());
                        LOGGER.handleError(Thread.currentThread(), e);
                    }
                }
            }
        }
        if (!TEMP_DIR.exists()) {
            if (!TEMP_DIR.mkdir())
                LOGGER.warn("Could not create 'temp' dir in PBD folder: {}", TEMP_DIR.getAbsolutePath());
        }
        if (!LOGS_DIR.exists()) {
            if (!LOGS_DIR.mkdir())
                LOGGER.warn("Could not create 'logs' dir in PBD folder: {}", LOGS_DIR.getAbsolutePath());
        }
        if (!ERROR_LOGS_DIR.exists()) {
            if (!ERROR_LOGS_DIR.mkdir())
                LOGGER.warn("Could not create 'error logs' dir in PBD folder: {}", ERROR_LOGS_DIR.getAbsolutePath());
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
     * Called every time on start up but only used for its
     * full potential when the config was just created/the
     * default Dead by Daylight folder could not be located.
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
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        do {
            int r = fileChooser.showOpenDialog(null);
            if (r == JFileChooser.CANCEL_OPTION)
                System.exit(0);
            folder = fileChooser.getSelectedFile();
            LOGGER.debug("Selected folder: {}", folder.getAbsolutePath());
            if (!folder.getName().equalsIgnoreCase("Dead by Daylight")) {
                LOGGER.info("Please locate and select the \"Dead by Daylight\" folder.");
                continue;
            }
            if (!isDBD(folder))
                LOGGER.warn("Could not detect the given folder as the DBD folder.");
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
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/pic/pbd_icon.png")));
        controller.setMode(CONFIG.isDarkMode());
        primaryStage.show();
    }
}