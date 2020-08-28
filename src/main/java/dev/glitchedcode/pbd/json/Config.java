package dev.glitchedcode.pbd.json;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.logger.Logger;

import javax.annotation.Nonnull;

public class Config {

    private boolean debug;
    private boolean noColor;
    private String dbdFolder;
    private boolean darkMode;
    private boolean deleteLogs;
    private boolean offlineMode;
    private short statusDuration;
    private boolean ignoreUpdates;
    private boolean deleteErrorLogs;

    private static final transient Logger logger = PBD.getLogger();

    public Config() {
        this(false);
    }

    public Config(boolean defaults) {
        if (defaults) {
            this.dbdFolder = "";
            this.statusDuration = 10;
            this.darkMode = true; // dark mode is superior
        }
    }

    /**
     * Checks if the program allows debug messages to console.
     *
     * @return True if the program allows debug messages to console.
     */
    public boolean debug() {
        return debug;
    }

    /**
     * Checks if the program does not output color to the console.
     *
     * @return True if the program does not output color to the console.
     */
    public boolean noColor() {
        return noColor;
    }

    /**
     * Gets the duration that the status bar will show, in seconds.
     *
     * @return The duration that the status bar will show, in seconds.
     */
    public short getStatusDuration() {
        return statusDuration;
    }

    /**
     * Checks if the program has the dark theme applied to the UI.
     *
     * @return True if the program has the dark theme applied to the UI.
     */
    public boolean isDarkMode() {
        return darkMode;
    }

    /**
     * Checks if the program is running in offline mode.
     * <br />
     * Offline mode ensures that the program will not check for updates.
     * Clicking on {@code File -> Check For Update} will give you a warning in the status bar.
     *
     * @return True if the program is running in offline mode.
     */
    public boolean isOfflineMode() {
        return offlineMode;
    }

    /**
     * Checks if the program is configured to ignore updates.
     * <br />
     * Ignoring updates does not mean you cannot check for updates,
     * but updates will not be presented when launching the program.
     *
     * @return True if the program is configured to ignore updates.
     */
    public boolean doesIgnoreUpdates() {
        return ignoreUpdates;
    }

    /**
     * Gets the full path of the local Dead by Daylight folder.
     * Usually, this is located at {@code .../Program Files (x86)/Steam/steamapps/common/Dead by Daylight}
     *
     * @return The full path of the local Dead by Daylight folder.
     */
    public String getDbdFolder() {
        return dbdFolder;
    }

    /**
     * Checks if the program is configured to delete logs when clearing cache.
     * <br />
     * You can delete cache by going to {@code File -> Delete Cache}.
     *
     * @return True if the program is configured to delete logs when clearing cache.
     */
    public boolean deleteLogs() {
        return deleteLogs;
    }

    /**
     * Checks if the program is configured to delete error logs when clearing cache.
     * <br />
     * You can delete cache by going to {@code File -> Delete Cache}.
     *
     * @return True if the program is configured to delete error logs when clearing cache.
     */
    public boolean deleteErrorLogs() {
        return deleteErrorLogs;
    }

    /**
     * Sets if the program allows debug messages to be printed to console.
     *
     * @param debug True to allow debug messages to be printed to console.
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * Sets if the program does not output color to the console.
     *
     * @param noColor True if the program does not output color to the console.
     */
    public void setNoColor(boolean noColor) {
        this.noColor = noColor;
    }

    /**
     * Sets the duration that the status bar will show, in seconds.
     * <br />
     * Ranges from 1 to 60.
     *
     * @param statusDuration The duration that the status bar will show, in seconds.
     */
    public void setStatusDuration(short statusDuration) {
        if (statusDuration < 1 || statusDuration > 60) {
            logger.warn("Tried setting status duration to '{}', must be between 1 and 60.", statusDuration);
            return;
        }
        this.statusDuration = statusDuration;
    }

    /**
     * Sets if the program is running in offline mode.
     *
     * @param offlineMode True if the program should run in offline mode.
     */
    public void setOfflineMode(boolean offlineMode) {
        this.offlineMode = offlineMode;
    }

    /**
     * Sets the full path of the local Dead by Daylight folder.
     *
     * @param dbdFolder The full path of the local Dead by Daylight folder.
     */
    public void setDbdFolder(@Nonnull String dbdFolder) {
        this.dbdFolder = dbdFolder;
    }

    /**
     * Sets if the program has the dark theme applied to the UI.
     * <br />
     * Note: Setting this to {@code true} will not automatically change the UI.
     *
     * @param darkMode True if the program has the dark theme applied to the UI.
     */
    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    /**
     * Sets whether the program is configured to ignore updates.
     *
     * @param ignoreUpdates True if the program is configured to ignore updates.
     */
    public void setIgnoreUpdates(boolean ignoreUpdates) {
        this.ignoreUpdates = ignoreUpdates;
    }

    /**
     * Sets whether the program is configured to delete error logs when clearing cache.
     *
     * @param deleteErrorLogs True if the program is configured to delete error logs when clearing cache.
     */
    public void setDeleteErrorLogs(boolean deleteErrorLogs) {
        this.deleteErrorLogs = deleteErrorLogs;
    }

    /**
     * Sets whether the program is configured to delete logs when clearing cache.
     *
     * @param deleteLogs True if the program is configured to delete logs when clearing cache.
     */
    public void setDeleteLogs(boolean deleteLogs) {
        this.deleteLogs = deleteLogs;
    }
}