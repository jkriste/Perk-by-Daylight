package dev.glitchedcode.pbd.json;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.logger.Logger;

import javax.annotation.Nonnull;

public class Config {

    private boolean debug;
    private boolean noColor;
    private String dbdFolder;
    private boolean darkMode;
    private boolean offlineMode;
    private short statusDuration;
    private boolean ignoreUpdates;

    private static final transient Logger logger = PBD.getLogger();

    public Config() {
        this(false);
    }

    public Config(boolean defaults) {
        if (defaults) {
            this.dbdFolder = "";
            this.statusDuration = 10;
            this.darkMode = true;
        }
    }

    public void validate() {
        if (dbdFolder == null)
            dbdFolder = "";
        if (statusDuration < 1)
            statusDuration = 10;
    }

    public boolean debug() {
        return debug;
    }

    public boolean noColor() {
        return noColor;
    }

    public short getStatusDuration() {
        return statusDuration;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public boolean isOfflineMode() {
        return offlineMode;
    }

    public boolean doesIgnoreUpdates() {
        return ignoreUpdates;
    }

    public String getDbdFolder() {
        return dbdFolder;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setNoColor(boolean noColor) {
        this.noColor = noColor;
    }

    public void setStatusDuration(short statusDuration) {
        if (statusDuration < 1) {
            logger.warn("Tried setting status duration to '{}', must be 1 or higher.", statusDuration);
            return;
        }
        this.statusDuration = statusDuration;
    }

    public void setOfflineMode(boolean offlineMode) {
        this.offlineMode = offlineMode;
    }

    public void setDbdFolder(@Nonnull String dbdFolder) {
        this.dbdFolder = dbdFolder;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public void setIgnoreUpdates(boolean ignoreUpdates) {
        this.ignoreUpdates = ignoreUpdates;
    }
}