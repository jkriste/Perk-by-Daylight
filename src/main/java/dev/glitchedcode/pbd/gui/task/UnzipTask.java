package dev.glitchedcode.pbd.gui.task;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.logger.Logger;
import javafx.concurrent.Task;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class UnzipTask extends Task<File> {

    private final File zipFile;
    private static final Logger logger = PBD.getLogger();

    public UnzipTask(@Nonnull File zipFile) {
        if (!zipFile.exists())
            throw new IllegalArgumentException("Given file '" + zipFile.getName() + "' does not exist.");
        if (!zipFile.getName().endsWith(".zip"))
            throw new IllegalArgumentException("Given file '" + zipFile.getName() + "' is not ZIP.");
        this.zipFile = zipFile;
    }

    @Override
    protected File call() throws IOException {
        logger.info("Unzipping file '{}'.", zipFile.getName());
        long count = getCount(zipFile);
        try (ZipInputStream in = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            int current = 0;
            while ((entry = in.getNextEntry()) != null) {
                File file = new File(PBD.getTempDir(), entry.getName());
                File parent = file.getParentFile();
                if (!parent.exists()) {
                    if (!parent.mkdirs())
                        throw new IOException("Failed to create parent dir '" + parent.getAbsolutePath() + "'");
                }
                if (file.getName().indexOf('.') != -1) {
                    if (file.createNewFile())
                        extract(in, file);
                } else {
                    if (file.mkdirs())
                        logger.debug("Created dir '{}' whilst unzipping.", file.getName());
                }
                current++;
                updateProgress(current, count);
                in.closeEntry();
            }
        }
        File file = new File(PBD.getTempDir(), zipFile.getName().split("\\.")[0]);
        if (file.exists() && file.isDirectory()) {
            return file;
        }
        logger.warn("Failed to unzip file: new dir '{}' does not exist.", file.getAbsolutePath());
        return null;
    }

    private long getCount(@Nonnull File file) {
        logger.debug("Getting file count in file '{}'", file.getName());
        long count = 0;
        try (ZipFile f = new ZipFile(file)) {
            count = f.stream().count();
        } catch (IOException e) {
            logger.warn("Failed to get file count in zip '{}': {}", file.getName(), e.getMessage());
            logger.handleError(Thread.currentThread(), e);
        }
        return count;
    }

    @ParametersAreNonnullByDefault
    private void extract(ZipInputStream in, File file) throws IOException {
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        byte[] buffer = new byte[4096];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        out.flush();
        out.close();
    }
}