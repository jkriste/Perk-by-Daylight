package dev.glitchedcode.pbd.file;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.gui.controller.MainController;
import dev.glitchedcode.pbd.logger.Logger;
import dev.glitchedcode.pbd.pack.IconPackException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

/**
 * @deprecated To be removed entirely by next pre-release in favor of async tasks.
 */
@Deprecated
public final class ZipEvaluator implements Callable<File> {

    private final File to;
    private final File zip;
    private final Runnable after;
    private final MainController ui;
    private static final Logger logger = PBD.getLogger();

    public ZipEvaluator(@Nonnull File zip, @Nonnull File to, @Nonnull MainController ui, @Nullable Runnable after) {
        if (!zip.getName().endsWith(".zip"))
            throw new IconPackException("Given file '{}' is not a zip file.", zip.getName());
        if (!to.isDirectory())
            throw new IconPackException("Given directory '{}' to unzip file to is not directory.", to.getName());
        this.to = to;
        this.zip = zip;
        this.ui = ui;
        this.after = after;
    }

    @Override
    public File call() throws IOException {
        ui.setDisabled(true);
        logger.debug("Unzipping file '{}'", zip.getName());
        if (!to.exists()) {
            if (to.mkdirs())
                logger.debug("Created dir(s) whilst unzipping: {}", to.getAbsolutePath());
        }
        int current = 0;
        long max = getCount(zip);
        assert (max > 0);
        logger.debug("File count for zip '{}': {}", zip.getName(), max);
        try (ZipInputStream in = new ZipInputStream(new FileInputStream(zip))) {
            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                File file = new File(to, entry.getName());
                if (file.getName().indexOf('.') != -1) { // file.isDirectory() does not work in this instance
                    if (file.createNewFile())
                        extract(in, file);
                } else {
                    if (file.mkdirs())
                        logger.debug("Created dir '{}' whilst unzipping.", file.getName());
                }
                current++;
                ui.update((double) current / (double) max);
                in.closeEntry();
            }
        }
        logger.debug("b");
        ui.update(-1D);
        ui.setDisabled(false);
        File file = new File(to, zip.getName().split("\\.")[0]);
        if (file.exists() && file.isDirectory()) {
            if (after != null)
                after.run();
            ui.setStatus("Unzipped file, authenticating...", MainController.Type.NONE);
            return file;
        }
        logger.error("Expected unzipped file '{}' does not exist.", file.getAbsolutePath());
        ui.setStatus("Encountered an error, check logs.", MainController.Type.ERROR);
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