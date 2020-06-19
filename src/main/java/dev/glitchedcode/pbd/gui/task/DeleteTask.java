package dev.glitchedcode.pbd.gui.task;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.logger.Logger;
import javafx.concurrent.Task;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DeleteTask extends Task<Integer> {

    private final File dir;
    private final boolean deleteFolder;
    private static final Logger logger = PBD.getLogger();

    public DeleteTask(@Nonnull File dir, boolean deleteFolder) {
        if (!dir.isDirectory())
            throw new IllegalArgumentException("Given file '" + dir.getName() + "' is not directory.");
        this.deleteFolder = deleteFolder;
        this.dir = dir;
    }

    @Override
    protected Integer call() throws IOException {
        File[] files = dir.listFiles();
        if (files != null) {
            int count = getCount(dir);
            for (File file : files) {
                if (file.isDirectory())
                    deleteAll(file);
                else
                    Files.delete(file.toPath());
            }
            if (deleteFolder)
                Files.delete(dir.toPath());
            return count;
        } else
            logger.warn("Directory '{}' #listFiles() returned null.", dir.getName());
        return -1;
    }

    private int getCount(@Nonnull File dir) {
        if (!dir.isDirectory())
            throw new IllegalArgumentException("Given file '" + dir.getName() + "' is not a directory.");
        File[] files = dir.listFiles();
        if (files == null) {
            logger.warn("#listFiles() for dir '{}' returned null, returning -1.", dir.getName());
            return -1;
        }
        int count = 0;
        for (File file : files) {
            if (file.isDirectory())
                count += getCount(file);
            else
                count++;
        }
        logger.debug("File count for '{}': {}", dir.getName(), count);
        return count;
    }

    private void deleteAll(@Nonnull File dir) throws IOException {
        if (!dir.isDirectory())
            throw new IllegalArgumentException("Given file '" + dir.getName() + "' is not a directory.");
        File[] files = dir.listFiles();
        if (files == null) {
            logger.warn("#listFiles() for dir '{}' returned null.", dir.getName());
            return;
        }
        for (File file : files) {
            if (file.isDirectory())
                deleteAll(file);
            else
                Files.delete(file.toPath());
        }
        Files.delete(dir.toPath());
    }
}