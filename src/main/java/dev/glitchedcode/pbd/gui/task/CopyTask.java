package dev.glitchedcode.pbd.gui.task;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.logger.Logger;
import dev.glitchedcode.pbd.util.Files;
import javafx.concurrent.Task;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Used to copy/move files from one directory to another.
 */
public class CopyTask extends Task<File> {

    private final File to;
    private final long max;
    private final File from;
    private final boolean delete;
    private static final Logger logger = PBD.getLogger();

    public CopyTask(@Nonnull File from, @Nonnull File to, boolean delete) {
        if (!from.exists())
            throw new IllegalArgumentException("Given file/folder '" + from.getName() + "' does not exist.");
        this.from = from;
        this.to = to;
        this.delete = delete;
        this.max = Files.getFileCount(from);
    }

    @Override
    protected File call() throws IOException {
        if (!from.isDirectory()) {
            File toFile = new File(to, from.getName());
            if (!toFile.exists()) {
                if (!toFile.createNewFile())
                    throw new IOException("Could not create new file '" + toFile.getAbsolutePath() + ".");
            }
            copy(from, toFile);
            return toFile;
        }
        File[] files = from.listFiles();
        if (files == null) {
            logger.warn("#listFiles() for dir '{}' returned null, returning null.", from.getName());
            return null;
        }
        int copied = copyDirectory(from, to, 0);
        if (delete) {
            logger.debug("Cleaning up...deleting copied files.");
            for (File f : files) {
                if (f.isDirectory())
                    deleteAll(f);
                else
                    java.nio.file.Files.delete(f.toPath());
            }
            java.nio.file.Files.delete(from.toPath());
        }
        logger.info("{} {} file(s) from '{}' to '{}'.", (delete ? "Moved" : "Copied"), copied, from.getAbsolutePath(), to.getAbsolutePath());
        return to;
    }

    /**
     * Copys one directory to the inside of the other.
     *
     * @param dir1 The directory to be copied.
     * @param dir2 The directory to put the copied directory inside of.
     * @param current The current file count.
     * @return The new directory, essentially dir1 inside of dir2.
     * @throws IOException Thrown for various reasons.
     */
    @ParametersAreNonnullByDefault
    public int copyDirectory(File dir1, File dir2, int current) throws IOException {
        if (!dir1.exists())
            throw new IOException("Given directory to copy '" + dir1.getAbsolutePath() + "' does not exist.");
        if (!dir1.isDirectory())
            throw new IOException("Given file '" + dir1.getAbsolutePath() + "' is not a directory.");
        if (!dir2.exists()) {
            if (!dir2.mkdirs())
                throw new IOException("Could not create directory '" + dir2.getAbsolutePath() + "'.");
        }
        if (!dir2.isDirectory())
            throw new IOException("Given file '" + dir2.getAbsolutePath() + "' is not a directory.");
        File[] files = dir1.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    current = copyDirectory(file, new File(dir2, file.getName()), current);
                } else {
                    copy(file, new File(dir2, file.getName()));
                    current++;
                }
                updateProgress(current, max);
            }
        } else
            throw new IOException("#listFiles() in directory '" + dir1.getAbsolutePath() + "' returned null.");
        return current;
    }

    private void copy(@Nonnull File from, @Nonnull File to) throws IOException {
        if (!from.exists())
            throw new IOException("Given file '" + from.getAbsolutePath() + "' does not exist.");
        if (from.isDirectory())
            throw new IOException("Given file '" + from.getAbsolutePath() + "' is a directory, not file.");
        if (!to.exists()) {
            if (!to.createNewFile())
                throw new IOException("Could not create file '" + to.getAbsolutePath() + "'.");
        }
        logger.debug("Copying from '{}' to '{}'", from.getAbsolutePath(), to.getAbsolutePath());
        try (FileInputStream in = new FileInputStream(from);
             FileOutputStream out = new FileOutputStream(to)) {
            int length;
            byte[] buffer = new byte[4096];
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (IOException e) {
            logger.warn("Failed to copy '{}': {}", from.getAbsolutePath(), e.getMessage());
            logger.handleError(Thread.currentThread(), e);
        }
    }

    /**
     * Deletes all files in the given dir.
     * <br />
     * Similar to {@link Files#deleteAll(File, boolean)} but kind of not.
     *
     * @param dir The directory to delete.
     * @throws IOException Thrown if any files could not be deleted.
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
                java.nio.file.Files.delete(file.toPath());
        }
        java.nio.file.Files.delete(dir.toPath());
    }
}