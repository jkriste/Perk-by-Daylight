package dev.glitchedcode.pbd.file;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.logger.Logger;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Used for copying and moving files.
 * A better solution than using Google's Files class because pain in the ass.
 */
public final class Files {

    private static final Logger logger = PBD.getLogger();

    private Files() {
    }

    /**
     *
     *
     * @param file
     * @return
     */
    public static int getCount(@Nonnull File file) {
        logger.debug("Getting file count for file '{}'", file.getName());
        if (!file.exists())
            return 0;
        if (!file.isDirectory())
            return 1;
        File[] files = file.listFiles();
        if (files == null)
            return 1;
        int count = 0;
        for (File f : files) {
            if (f.isDirectory())
                count += getCount(f);
            else
                count++;
        }
        return count;
    }

    /**
     * Deletes all files inside of the directory.
     *
     * @param directory The directory.
     * @param deleteDir True if the given directory should be deleted as well.
     */
    public static void deleteAll(@Nonnull File directory, boolean deleteDir) throws IOException {
        if (!directory.isDirectory()) {
            logger.warn("Given file '{}' was not a directory, but will be deleted.", directory.getName());
            java.nio.file.Files.delete(directory.toPath());
            return;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory())
                    deleteAll(file, true);
                else
                    java.nio.file.Files.delete(file.toPath());
            }
        } else
            logger.warn("#listFiles() returned null when trying to delete dir '{}'", directory.getName());
        if (deleteDir)
            java.nio.file.Files.delete(directory.toPath());
    }

    /**
     * Copies a file from an existing location to another location, existing or not.
     *
     * @param from The original file.
     * @param to The file/directory to copy it to.
     * @return The copied file (to).
     * @throws IOException Thrown for various reasons.
     */
    @ParametersAreNonnullByDefault
    public static File copy(File from, File to) throws IOException {
        if (!from.exists())
            throw new FileNotFoundException("Given file '" + from.getAbsolutePath() + "' does not exist.");
        if (to.isDirectory()) {
            if (!to.exists()) {
                if (!to.mkdirs())
                    throw new IOException("Could not create directory '" + to.getAbsolutePath() + "'.");
                to = new File(to, from.getName());
            }
        }
        if (!to.exists()) {
            if (!to.createNewFile())
                throw new IOException("");
        }
        logger.debug("Copying from '{}' to '{}'", from.getAbsolutePath(), to.getAbsolutePath());
        try (FileInputStream in = new FileInputStream(from);
             FileOutputStream out = new FileOutputStream(to)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
        }
        return to;
    }

    /**
     * Copys one directory to the inside of the other.
     *
     * @param dir1 The directory to be copied.
     * @param dir2 The directory to put the copied directory inside of.
     * @return The new directory, essentially dir1 inside of dir2.
     * @throws IOException Thrown for various reasons.
     */
    @ParametersAreNonnullByDefault
    @SuppressWarnings("UnusedReturnValue")
    public static File copyDirectory(File dir1, File dir2) throws IOException {
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

        logger.debug("Copying from '{}' to '{}'", dir1.getAbsolutePath(), dir2.getAbsolutePath());
        File[] files = dir1.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory())
                    copyDirectory(file, new File(dir2, file.getName()));
                else
                    copy(file, new File(dir2, file.getName()));
            }
        } else
            throw new IOException("#listFiles() in directory '" + dir1.getAbsolutePath() + "' returned null.");
        return dir2;
    }

    @ParametersAreNonnullByDefault
    public static File moveToDirectory(File from, File directory) throws IOException {
        File file = copy(from, directory);
        java.nio.file.Files.delete(from.toPath());
        return file;
    }
}