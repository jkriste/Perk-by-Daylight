package dev.glitchedcode.pbd.logger;

import dev.glitchedcode.pbd.PBD;
import org.fusesource.jansi.Ansi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

public class ErrorLog {

    private static final Logger logger = PBD.getLogger();
    private static final File ERROR_DIR = new File(PBD.PBD_DIR, "error logs");

    public ErrorLog(Thread thread, Throwable throwable) {
        String stacktrace = PBD.formatStackTrace(throwable);
        logger.error(Ansi.Color.RED, "Uncaught exception " + throwable.getClass().getName()
                + " in thread \"" + thread.getName() + "\": " + throwable.getMessage());
        logger.debug(Ansi.Color.RED, "Stack trace:\n" + stacktrace);
        if (!ERROR_DIR.exists()) {
            logger.debug("Error logs folder doesn't exist, creating...");
            if (ERROR_DIR.mkdir())
                logger.debug("Created error log folder located at " + ERROR_DIR.getAbsolutePath());
        }
        File log = new File(ERROR_DIR, "error_log_" + timeLog() + ".txt");
        try {
            if (log.createNewFile())
                logger.debug("Created error log \"" + log.getName() + "\".");
            BetterPrintWriter writer = new BetterPrintWriter(new BufferedWriter(new FileWriter(log, false)));
            Stream.of("Error log of Perk by Daylight " + PBD.VERSION,
                    "// Here we go again :(",
                    "",
                    "Please report bugs and issues here: https://github.com/glitchedcoder/Perk-by-Daylight/issues/new/choose",
                    "",
                    "Occurrence: " + (new SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss z")).format(new Date()),
                    "",
                    "Thread: " + thread.getName(),
                    "",
                    "Exception type: " + throwable.getClass().getSimpleName(),
                    "",
                    "Message: " + throwable.getMessage(),
                    "",
                    "------------------------------",
                    "BEGINNING OF STACK TRACE",
                    "------------------------------",
                    stacktrace,
                    "------------------------------",
                    "END OF STACK TRACE",
                    "------------------------------"
            ).forEach(writer::println);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logger.warn(Ansi.Color.RED, "Somehow managed to run into an error while creating the error log.");
            logger.warn(Ansi.Color.RED, "Report the following error to the developer:\n" + PBD.formatStackTrace(e));
        }
    }

    private String timeLog() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");
        return f.format(new Date());
    }
}