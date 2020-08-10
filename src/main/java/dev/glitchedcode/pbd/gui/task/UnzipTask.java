package dev.glitchedcode.pbd.gui.task;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.dbd.icon.IconCategory;
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
        long count = getCount();
        File main;
        String container = getContainer();
        if (!container.isEmpty())
            main = new File(PBD.getTempDir(), container);
        else {
            // assuming the zip file has no container
            int index = zipFile.getName().lastIndexOf('.');
            String zipName = zipFile.getName().substring(0, index);
            main = new File(PBD.getTempDir(), zipName);
            if (!main.mkdirs())
                logger.warn("Failed to create parent directory '{}'", main.getAbsolutePath());
        }
        try (ZipInputStream in = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            int current = 0;
            while ((entry = in.getNextEntry()) != null) {
                File file = new File(container.isEmpty() ? main : PBD.getTempDir(), entry.getName());
                File parent = file.getParentFile();
                if (!parent.exists()) {
                    if (!parent.mkdirs())
                        logger.warn("Failed to create parent file whilst extracting '{}'", file.getAbsolutePath());
                }
                if (file.getName().indexOf('.') != -1) {
                    if (file.createNewFile())
                        extract(in, file);
                } else {
                    if (!file.mkdirs())
                        logger.warn("Failed to create directory '{}'", file.getAbsolutePath());
                }
                current++;
                updateProgress(current, count);
                in.closeEntry();
            }
        }
        if (main.exists() && main.isDirectory())
            return main;
        logger.warn("Failed to unzip file: expected directory" +
                " '{}' does not exist.", main.getAbsolutePath());
        return null;
    }

    /**
     * Inspects the ZIP file to check if there is a folder containing
     * the core folders such as Actions, Perks, CharPortraits, etc...
     *
     * @return The container folder name, or an empty string if no container exists.
     * @throws IOException Thrown if the file could not be read by an File/ZipInputStream.
     */
    private String getContainer() throws IOException {
        try (ZipInputStream in = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry = in.getNextEntry(); // gives us the first entry
            String n = entry.getName();
            String name;
            // gets rid of the last '/' in the entry name if it ends with '/'
            name = n.endsWith("/") ? n.substring(0, n.lastIndexOf('/') - 1) : n;
            logger.debug("First entry name: {}", name);
            String[] o = name.split("/");
            if (o.length == 1) {
                // not likely to happen, but just checking
                for (String req : IconCategory.FOLDER_NAMES) {
                    if (req.equalsIgnoreCase(o[0])) {
                        logger.debug("Not contained - found match - {}: {}", o[0], req);
                        return "";
                    }
                }
            } else if (o.length > 1) {
                for (String req : IconCategory.FOLDER_NAMES) {
                    if (o[0].equalsIgnoreCase(req)) {
                        logger.debug("Not contained - found match - {}: {}", o[0], req);
                        return "";
                    }
                    if (o[1].equalsIgnoreCase(req)) {
                        logger.debug("Contained - found folder name '{}'", o[0]);
                        return o[0];
                    }
                }
                return o[0]; // just in case
            } else
                logger.warn("Could not evaluate first entry '{}': no directories found.", n);
        }
        return "";
    }

    /**
     * Gets the total file count inside of the file.
     *
     * @return The total file count inside of the file.
     */
    private long getCount() {
        logger.debug("Getting file count in file '{}'", zipFile.getName());
        long count = 0;
        try (ZipFile f = new ZipFile(zipFile)) {
            count = f.stream().count();
        } catch (IOException e) {
            logger.warn("Failed to get file count in zip '{}': {}", zipFile.getName(), e.getMessage());
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