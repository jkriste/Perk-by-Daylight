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

public class CopyTask extends Task<File> {

    private final File to;
    private final long max;
    private final File from;
    private static final Logger logger = PBD.getLogger();

    public CopyTask(@Nonnull File from, @Nonnull File to) {
        if (!from.exists())
            throw new IllegalArgumentException("Given file/folder '" + from.getName() + "' does not exist.");
        this.from = from;
        this.to = to;
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
        int copied = copyDirectory(from, to, 0);
        logger.info("Copied {} file(s) from '{}' to '{}'.", copied, from.getAbsolutePath(), to.getAbsolutePath());
        return to;
    }

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
}