package dev.glitchedcode.pbd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import dev.glitchedcode.pbd.gui.Icon;
import dev.glitchedcode.pbd.gui.controller.MainController;
import dev.glitchedcode.pbd.json.Config;
import dev.glitchedcode.pbd.json.LatestRelease;
import dev.glitchedcode.pbd.logger.Logger;
import dev.glitchedcode.pbd.pack.IconPack;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
     * True if debug messages will be displayed to the console/logger.
     */
    private static boolean DEBUG;
    /**
     * Handles the logging of info into a text file located in the PerkByDaylight folder.
     */
    private static Logger logger;
    /**
     * The config file instance, located in the 'Perk by Daylight' folder.
     */
    private static Config CONFIG;
    /**
     * The location of the "Icons" folder in DBD.
     */
    private static File ICONS_DIR;
    /**
     * True if the logger will not output color to the console.
     */
    private static boolean NO_COLOR;
    /**
     * The PerkByDaylight folder located in the %appdata% folder.
     */
    public static final File PBD_DIR;
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
    public static final String VERSION = "0.0.1-BETA";

    static {
        APP_DATA_DIR = new File(System.getenv("APPDATA"));
        PROGRAM_FILES_86_DIR = new File(System.getenv("ProgramFiles(X86)"));
        DBD_DIR = new File(PROGRAM_FILES_86_DIR, "Steam\\steamapps\\common\\Dead by Daylight");
        PBD_DIR = new File(APP_DATA_DIR, "Perk by Daylight");
        PACKS_DIR = new File(PBD_DIR, "packs");
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
        // get runtime args
        DEBUG = hasArg("-debug", args);
        NO_COLOR = hasArg("-nocolor", args);
        // Create out logger for info & error reporting
        logger = new Logger(PBD_DIR);
        // Builds our Gson object used to read & write to/from JSON files.
        GSON = new GsonBuilder()
                .setPrettyPrinting()
                .setLenient()
                .create();
        // Load config file
        CONFIG = loadConfig();
        if (CONFIG == null) {
            logger.error("config.json was not saved properly last run and is empty/corrupt.");
            logger.error("Delete '{}' and restart.", CONFIG_FILE.getAbsolutePath());
            System.exit(0);
        }
        // Set up all additional files & folders.
        setupFiles();
        // Let the Logger class handle errors.
        Thread.currentThread().setUncaughtExceptionHandler(getExceptionHandler());
        // Create the main form
        launch(args);
    }

    /**
     * Closes the application.
     *
     * @param code The exit code.
     */
    public static void close(int code) {
        Platform.exit();
        IconPack.saveAll();
        saveConfig();
        logger.close();
        System.exit(code);
    }

    /**
     * Gets the logger instance.
     *
     * @return The logger instance.
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * Gets whether debug mode is enabled.
     *
     * @return True if debug mode is enabled.
     */
    public static boolean debug() {
        return DEBUG;
    }

    /**
     * Gets whether "no color" mode is enabled.
     *
     * @return True if "no color" mode is enabled.
     */
    public static boolean noColor() {
        return NO_COLOR;
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
     * Gets the default {@link java.lang.Thread.UncaughtExceptionHandler}.
     *
     * @return The default {@link java.lang.Thread.UncaughtExceptionHandler}.
     */
    public static Thread.UncaughtExceptionHandler getExceptionHandler() {
        return logger::handleError;
    }

    /**
     * Creates a {@link JOptionPane} message dialog with the given message and {@link Icon}.
     *
     * @param message The message to put.
     * @param icon The icon to put.
     */
    @SuppressWarnings("MagicConstant")
    public static void popup(@Nonnull String message, @Nonnull Icon icon) {
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
    public static String formatStackTrace(Throwable throwable) {
        StringBuilder builder = new StringBuilder();
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
        return builder.toString();
    }

    /**
     * Pulls the update.json file and packs it into a {@link LatestRelease} class.
     *
     * @return The update.json file as a {@link LatestRelease} instance.
     * @throws IOException Thrown for bad reasons.
     */
    @Nullable
    public static LatestRelease checkUpdate() throws IOException {
        logger.debug("Checking for update...");
        LatestRelease release;
        URL url = new URL("https://raw.githubusercontent.com/glitchedcoder/glitchedcode.dev/master/update.json");
        logger.debug("Trying to access JSON file @ {}", url.toString());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            logger.debug("Fetched the following JSON from raw file:\n{}", buffer.toString());
            release = GSON.fromJson(buffer.toString(), LatestRelease.class);
        }
        return release;
    }

    /**
     * Saves the {@link Config} instance to 'config.json'
     */
    public static void saveConfig() {
        logger.debug("Trying to save config.json file...");
        if (CONFIG == null) {
            logger.warn("#saveConfig() called whilst Config instance is null.");
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            GSON.toJson(CONFIG, Config.class, writer);
            writer.flush();
            logger.debug("Config saved successfully.");
        } catch (IOException e) {
            logger.warn("Failed to save config.json: {}", e.getMessage());
            logger.handleError(Thread.currentThread(), e);
        }
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
                    logger.debug("Successfully created new config file at '{}'", CONFIG_FILE.getAbsolutePath());
                    Config config = new Config();
                    config.setDbdFolder(""); // this is meant to be empty
                    // save to the config since it's first-time
                    logger.debug("JSON:\n{}", GSON.toJson(config, Config.class));
                    JsonWriter writer = new JsonWriter(new FileWriter(CONFIG_FILE, false));
                    GSON.toJson(config, Config.class, writer);
                    writer.flush();
                    writer.close();
                    return config;
                }
            } catch (IOException e) {
                logger.warn("Failed to create/save config file @ '{}'", CONFIG_FILE.getAbsolutePath());
                logger.handleError(Thread.currentThread(), e);
            }
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
                return GSON.fromJson(reader, Config.class);
            } catch (IOException e) {
                logger.warn("Failed to read from config file '{}': {}", CONFIG_FILE.getAbsolutePath(), e.getMessage());
                logger.handleError(Thread.currentThread(), e);
            }
        }
        return null;
    }

    private static void setupFiles() {
        // Make sure local %appdata% folder exists.
        logger.debug("%appdata% directory path: {}", APP_DATA_DIR.getAbsolutePath());
        if (!APP_DATA_DIR.exists()) {
            popup("Missing app data directory:\n{}", Icon.ERROR, APP_DATA_DIR.getAbsolutePath());
            System.exit(0);
        }
        logger.debug("Icon pack cache directory path: {}", PACKS_DIR.getAbsolutePath());
        if (!PACKS_DIR.exists()) {
            if (PACKS_DIR.mkdir())
                logger.debug("Created 'packs' folder in 'Perk by Daylight' folder.");
        } else {
            File[] files = PACKS_DIR.listFiles();
            assert (PACKS_DIR.isDirectory() && files != null);
            logger.debug("{} files/directories found in '{}'", files.length, PACKS_DIR.getAbsolutePath());
            for (File file : files) {
                if (IconPack.of(file) == null)
                    logger.warn("File/directory '{}' could not be determined as an icon pack, ignoring.", file.getName());
            }
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
        logger.debug("Could not locate default DBD dir '{}'", DBD_DIR.getAbsolutePath());
        int result = JOptionPane.showConfirmDialog(null,
                "Could not locate the default Dead by Daylight folder.\n"
                + "Locate the folder manually?", "Perk by Daylight", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.NO_OPTION)
            System.exit(0);
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
            logger.debug("Selected folder: {}", folder.getAbsolutePath());
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
     * Checks if there's a given argument in the array of args.
     * <br />
     * Used to check for any runtime args such as {@code -debug} or {@code -nocolor}.
     *
     * @param arg The argument to check for.
     * @param args The array of args given in {@code main(String[])}
     * @return True if the given arg is in the array of args.
     */
    private static boolean hasArg(String arg, String[] args) {
        for (String s : args) {
            if (s.equalsIgnoreCase(arg))
                return true;
        }
        return false;
    }

    /**
     * Gets input from runtime args provided by {@code main(String[])}.
     * <br />
     * e.g. If I ran {@code java -jar someProgram.jar -color red -date july 24}
     * the following would return "red" and "july 24" as String arrays:
     * <br />
     * {@code {"red"} = getInput('-', "color", args);}
     * <br />
     * {@code {"july", "24"} = getInput('-', "date", args);}
     *
     * @param c The prefix for args.
     * @param arg The arg to get input for.
     * @param args The runtime args.
     * @return The input from runtime args or an empty array if arg is not in args.
     */
    private static String[] getInput(char c, String arg, String[] args) {
        if (hasArg(c + arg, args)) {
            List<String> list = new ArrayList<>();
            int i = getIndex(c + arg, args);
            if (i != -1) {
                int k = 0;
                for (int j = i + 1; j < args.length && !args[j].startsWith(c + ""); j++) {
                    list.add(args[j]);
                    k++;
                }
                return list.toArray(new String[k]);
            }
        }
        return new String[0];
    }

    /**
     * Gets the index of the given arg in the args array.
     *
     * @param arg The arg to check for.
     * @param args The array of args.
     * @return The index of the given arg or -1 if arg is not in args.
     */
    private static int getIndex(String arg, String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase(arg))
                return i;
        }
        return -1;
    }

    /**
     * Starts the {@link Application}.
     *
     * @param primaryStage The stage.
     * @throws Exception Thrown for various reasons, but (usually) shouldn't happen.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(getExceptionHandler());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/MainForm.fxml"));
        Parent parent = loader.load();
        MainController controller = loader.getController();
        controller.setStage(primaryStage);
        primaryStage.setTitle("Perk by Daylight v" + PBD.VERSION);
        primaryStage.setOnHidden(event -> close(0));
        primaryStage.setScene(new Scene(parent));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/pic/pbd_icon.png")));
        controller.setMode(CONFIG.isDarkMode());
        primaryStage.show();
    }
}