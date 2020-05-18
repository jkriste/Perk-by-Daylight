package dev.glitchedcode.pbd.util;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.logger.Logger;
import javafx.scene.image.Image;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class FileUtil {

    private static final Logger logger = PBD.getLogger();

    private FileUtil() {
    }

    @Nullable
    public static File unzipFile(@Nonnull File zip, @Nonnull File to) throws IOException {
        if (!zip.getName().endsWith(".zip")) {
            logger.warn("Tried to unzip a file not ending with .zip, '{}'", zip.getAbsolutePath());
            return null;
        }
        if (!to.exists()) {
            if (to.mkdirs())
                logger.debug("Created dir(s): {}", to.getAbsolutePath());
        }
        ZipInputStream in = new ZipInputStream(new FileInputStream(zip));
        ZipEntry entry;
        while ((entry = in.getNextEntry()) != null) {
            File file = new File(to, entry.getName());
            logger.debug(file.getAbsolutePath());
            if (file.getName().indexOf('.') != -1) {
                if (file.createNewFile())
                    extract(in, file);
            } else {
                if (file.mkdirs())
                    logger.debug("Created dir '{}' whilst extracting zip.", entry.getName());
            }
            in.closeEntry();
        }
        in.close();
        logger.debug(zip.getName());
        File file = new File(to, zip.getName().split("\\.")[0]);
        assert (file.exists() && file.isDirectory());
        return file;
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

    @Nullable
    public static Image toImage(@Nonnull File file) {
        try {
            return new Image(new FileInputStream(file));
        } catch (IOException e) {
            logger.warn("Failed to get JavaFX Image from file '{}': {}", file.getAbsolutePath(), e.getMessage());
            logger.handleError(Thread.currentThread(), e);
        }
        return null;
    }
}