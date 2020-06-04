package dev.glitchedcode.pbd.logger;

import org.fusesource.jansi.Ansi;

public enum LogType {

    INFO("INFO"),
    DEBUG("DEBUG"),
    WARN("WARN"),
    ERROR("ERROR");

    private final String prefix;

    LogType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return "[" + prefix + "] ";
    }

    public Ansi getColor() {
        Ansi a = Ansi.ansi().fgBrightBlack().a("[");
        switch (this) {
            case INFO:
                a.fgBrightGreen().a(prefix);
                break;
            case DEBUG:
                a.fgCyan().a(prefix);
                break;
            case WARN:
                a.fgBrightRed().a(prefix);
                break;
            case ERROR:
                a.fgRed().a(prefix);
                break;
        }
        return a.fgBrightBlack().a("] ").reset();
    }
}