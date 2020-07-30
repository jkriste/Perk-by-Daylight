package dev.glitchedcode.pbd.logger;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.json.Config;
import org.fusesource.jansi.Ansi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Logger {

    private final File logFile;
    private final AtomicInteger errCount;
    private final AtomicInteger warnCount;
    private final BetterPrintWriter writer;
    private final SimpleDateFormat logFormat;
    private final SimpleDateFormat timeFormat;

    private static Logger INSTANCE;
    private static final Config CONFIG = PBD.getConfig();

    private Logger() throws IOException {
        this.logFile = new File(PBD.PBD_DIR, "latest.log");
        this.logFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        if (logFile.exists()) {
            if (!logFile.renameTo(new File(PBD.LOGS_DIR, logFormat.format(new Date()) + ".log")))
                throw new IOException("Could not move existing latest.log file to 'logs' dir: " + logFile.getAbsolutePath());
        }
        if (!logFile.createNewFile())
            throw new IOException("Could not create 'latest.log' file: " + logFile.getAbsolutePath());
        this.writer = new BetterPrintWriter(new BufferedWriter(new FileWriter(logFile, false)));
        this.timeFormat = new SimpleDateFormat("HH:mm:ss");
        this.errCount = new AtomicInteger(0);
        this.warnCount = new AtomicInteger(0);
    }

    public void print(LogType type, String message) {
        if (isOpen()) {
            file(type, message);
            console(type, message);
        }
    }

    public void print(LogType type, String message, Object... params) {
        print(type, PBD.format(message, params));
    }

    public void print(LogType type, Ansi.Color color, String message) {
        if (isOpen()) {
            file(type, message);
            console(type, color, message);
        }
    }

    public void file(LogType type, String message) {
        if (isOpen()) {
            if (type == LogType.DEBUG && !CONFIG.debug())
                return;
            this.writer.println(time() + thread() + type.getPrefix() + message);
            save();
        }
    }

    public void console(LogType type, String message) {
        if (isOpen())
            console(type, Ansi.Color.WHITE, message);
    }

    public void console(LogType type, String message, Object... params) {
        console(type, PBD.format(message, params));
    }

    public void console(LogType type, Ansi.Color color, String message) {
        if (isOpen()) {
            if (type == LogType.DEBUG && !CONFIG.debug())
                return;
            if (type == LogType.ERROR)
                errCount.incrementAndGet();
            else if (type == LogType.WARN)
                warnCount.incrementAndGet();
            if (CONFIG.noColor()) {
                System.out.println(time() + thread() + type.getPrefix() + message);
            } else {
                System.out.print(Ansi.ansi().fgBrightBlack().a(time() + thread()));
                System.out.println(type.getColor().fg(color).a(message).reset());
            }
        }
    }

    public void info(String message) {
        file(LogType.INFO, message);
        console(LogType.INFO, message);
    }

    public void info(String message, Object... param) {
        info(PBD.format(message, param));
    }

    public void info(Ansi.Color color, String message) {
        file(LogType.INFO, message);
        console(LogType.INFO, color, message);
    }

    public void info(Ansi.Color color, String message, Object... param) {
        info(color, PBD.format(message, param));
    }

    public void debug(String message) {
        file(LogType.DEBUG, message);
        console(LogType.DEBUG, message);
    }

    public void debug(String message, Object... param) {
        debug(PBD.format(message, param));
    }

    public void debug(Ansi.Color color, String message) {
        file(LogType.DEBUG, message);
        console(LogType.DEBUG, color, message);
    }

    public void debug(Ansi.Color color, String message, Object... param) {
        debug(color, PBD.format(message, param));
    }

    public void warn(String message) {
        file(LogType.WARN, message);
        console(LogType.WARN, message);
    }

    public void warn(String message, Object... param) {
        warn(PBD.format(message, param));
    }

    public void warn(Ansi.Color color, String message) {
        file(LogType.WARN, message);
        console(LogType.WARN, color, message);
    }

    public void warn(Ansi.Color color, String message, Object... param) {
        warn(color, PBD.format(message, param));
    }

    public void handleError(Thread thread, Throwable throwable) {
        new ErrorLog(thread, throwable);
    }

    public void error(String message) {
        file(LogType.ERROR, message);
        console(LogType.ERROR, message);
    }

    public void error(String message, Object... param) {
        error(PBD.format(message, param));
    }

    public void error(Ansi.Color color, String message) {
        file(LogType.ERROR, message);
        console(LogType.ERROR, color, message);
    }

    public void error(Ansi.Color color, String message, Object... param) {
        error(color, PBD.format(message, param));
    }

    public void save() {
        if (isOpen())
            this.writer.flush();
    }

    public void close() throws IOException {
        if (isOpen()) {
            if (errCount.get() > 0 || warnCount.get() > 0) {
                warn(Ansi.Color.YELLOW, "Finished with {} error(s) and {} warning(s).", errCount.get(), warnCount.get());
                info("Report bugs, errors, and issues here: https://github.com/glitchedcoder/Perk-by-Daylight/issues/new/choose");
            } else
                info(Ansi.Color.GREEN, "Finished with {} error(s) and {} warning(s).", errCount.get(), warnCount.get());
            this.writer.flush();
            this.writer.close();
            if (!logFile.renameTo(new File(PBD.LOGS_DIR, logFormat.format(new Date()) + ".log")))
                throw new IOException("Failed to move 'latest.log' file to 'logs' dir.");
        }
    }

    public boolean isOpen() {
        return this.writer.isOpen();
    }

    public File getFile() {
        return logFile;
    }

    public BetterPrintWriter getWriter() {
        return this.writer;
    }

    public int getErrorCount() {
        return errCount.get();
    }

    public int getWarnCount() {
        return warnCount.get();
    }

    public static Logger getInstance() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new Logger();
            } catch (IOException e) {
                System.err.println("Failed to initiate the logger: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    private String time() {
        return "[" + timeFormat.format(new Date()) + "] ";
    }

    private String thread() {
        return "[" + Thread.currentThread().getName() + "] ";
    }
}