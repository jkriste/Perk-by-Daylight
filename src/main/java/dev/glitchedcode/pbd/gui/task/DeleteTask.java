package dev.glitchedcode.pbd.gui.task;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.logger.Logger;
import javafx.concurrent.Task;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Used to delete a directory/files in the given directory.
 * <br />
 * Mainly used for {@link dev.glitchedcode.pbd.pack.IconPack}s.
 */
public class DeleteTask extends Task<Long> {

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
    protected Long call() throws IOException {
        File[] files = dir.listFiles();
        if (files != null) {
            long count = dev.glitchedcode.pbd.util.Files.getFileCount(dir);
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
        return -1L;
    }

    /**
     * Deletes the given directory and its subfolders and subfiles.
     *
     * @param dir The directory to delete.
     * @throws IOException Thrown if the folder/file could not be deleted.
     * @throws IllegalArgumentException Thrown if the given file is not a directory.
     */
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