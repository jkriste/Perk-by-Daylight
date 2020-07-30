package dev.glitchedcode.pbd.gui.task;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.json.Config;
import dev.glitchedcode.pbd.util.Files;
import javafx.concurrent.Task;

import java.io.IOException;

public class CacheDeletionTask extends Task<Long> {

    private static final Config CONFIG = PBD.getConfig();

    @Override
    protected Long call() throws IOException {
        long total = 0L;
        total += Files.getFileCount(PBD.TEMP_DIR);
        Files.deleteAll(PBD.TEMP_DIR, false);
        if (CONFIG.deleteLogs()) {
            total += Files.getFileCount(PBD.LOGS_DIR);
            Files.deleteAll(PBD.LOGS_DIR, false);
        }
        if (CONFIG.deleteErrorLogs()) {
            total += Files.getFileCount(PBD.ERROR_LOGS_DIR);
            Files.deleteAll(PBD.ERROR_LOGS_DIR, false);
        }
        return total;
    }
}