package dev.glitchedcode.pbd.file;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.logger.Logger;
import dev.glitchedcode.pbd.pack.IconPackException;
import javafx.scene.control.ProgressBar;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public final class ZipEvaluator implements Callable<File> {

    private final File to;
    private final File zip;
    private final ProgressBar bar;
    private static final Logger logger = PBD.getLogger();

    @ParametersAreNonnullByDefault
    public ZipEvaluator(File zip, File to, ProgressBar bar) {
        if (!zip.getName().endsWith(".zip"))
            throw new IconPackException("Given file '{}' is not a zip file.", zip.getName());
        if (!to.isDirectory())
            throw new IconPackException("Given directory '{}' to unzip file to is not directory.", to.getName());
        this.to = to;
        this.zip = zip;
        this.bar = bar;
    }

    @Override
    public File call() throws IOException {
        logger.debug("Unzipping file '{}'", zip.getName());
        if (!to.exists()) {
            if (to.mkdirs())
                logger.debug("Created dir(s) whilst unzipping: {}", to.getAbsolutePath());
        }
        int current = 0;
        long max = getCount(zip);
        assert (max > 0);
        logger.debug("File count for zip '{}': {}", zip.getName(), max);
        bar.setVisible(true);
        bar.setProgress(0D);
        logger.debug("a");
        try (ZipInputStream in = new ZipInputStream(new FileInputStream(zip))) {
            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                File file = new File(to, entry.getName());
                logger.debug(file.getAbsolutePath());
                if (file.getName().indexOf('.') != -1) { // file.isDirectory() does not work in this instance
                    if (file.createNewFile())
                        extract(in, file);
                } else {
                    if (file.mkdirs())
                        logger.debug("Created dir '{}' whilst unzipping.", file.getName());
                }
                current++;
                bar.setProgress((double) current / (double) max);
                in.closeEntry();
            }
        }
        logger.debug("b");
        bar.setVisible(false);
        File file = new File(to, zip.getName().split("\\.")[0]);
        if (file.exists() && file.isDirectory()) {
            logger.debug("c");
            return file;
        }
        logger.error("Expected unzipped file '{}' does not exist.", file.getAbsolutePath());
        return null;
    }

    private static long getCount(@Nonnull File file) {
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
    private static void extract(ZipInputStream in, File file) throws IOException {
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