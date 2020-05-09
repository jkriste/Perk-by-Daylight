package dev.glitchedcode.pbd.logger;

import org.fusesource.jansi.Ansi;

public enum LogType {

    INFO("INFO"),
    DEBUG("DEBUG"),
    COMMAND("COMMAND"),
    WARN("WARN"),
    ERROR("ERROR"),
    FATAL("FATAL");

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
            case COMMAND:
                a.fgCyan().a(prefix);
                break;
            case DEBUG:
                a.fgYellow().a(prefix);
                break;
            case WARN:
                a.fgBrightMagenta().a(prefix);
                break;
            case ERROR:
                a.fgBrightRed().a(prefix);
                break;
            case FATAL:
                a.fgRed().a(prefix);
        }
        return a.fgBrightBlack().a("] ").reset();
    }
}