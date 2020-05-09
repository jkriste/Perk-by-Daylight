package dev.glitchedcode.pbd.json;

public class Config {

    private String dbdFolder;
    private boolean darkMode;
    private boolean ignoreUpdates;

    public boolean isDarkMode() {
        return darkMode;
    }

    public boolean doesIgnoreUpdates() {
        return ignoreUpdates;
    }

    public String getDbdFolder() {
        return dbdFolder;
    }

    public void setDbdFolder(String dbdFolder) {
        this.dbdFolder = dbdFolder;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public void setIgnoreUpdates(boolean ignoreUpdates) {
        this.ignoreUpdates = ignoreUpdates;
    }
}
