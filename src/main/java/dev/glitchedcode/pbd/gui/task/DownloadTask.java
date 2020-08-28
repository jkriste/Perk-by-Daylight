package dev.glitchedcode.pbd.gui.task;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.json.LatestRelease;
import dev.glitchedcode.pbd.logger.Logger;
import javafx.concurrent.Task;

import javax.annotation.Nullable;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;

/**
 * Used to download and replace the JAR when an update occurrs.
 */
public class DownloadTask extends Task<Boolean> {

    private final JsonObject object;
    private static final Logger logger = PBD.getLogger();

    public DownloadTask() {
        this.object = LatestRelease.getObject();
    }

    @Override
    protected Boolean call() throws IOException, URISyntaxException {
        logger.info("Starting download...");
        URL url = getDownloadURL();
        if (url == null) {
            logger.warn("Failed to download update: download url returned null.");
            return false;
        }
        long size = getSize();
        logger.info("Size of download: {} mb", String.format("%.2f", ((double) size / (1024 * 1024))));
        File jarLoc = PBD.getJar();
        UUID id = UUID.randomUUID();
        File newJar = new File(jarLoc.getParent(), id.toString() + ".jar");
        if (!newJar.createNewFile()) {
            logger.warn("Failed to create JAR file '{}'", newJar.getName());
            return false;
        }
        try (BufferedInputStream in = new BufferedInputStream(url.openStream());
             FileOutputStream out = new FileOutputStream(newJar)) {
            int total = 0;
            int length;
            byte[] buffer = new byte[1024];
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
                total += length;
                updateProgress(total, size);
            }
            out.flush();
        }
        Runtime.getRuntime().exec("cmd /c ping localhost -n 4 > nul && del " + jarLoc.getAbsolutePath());
        Runtime.getRuntime().exec("cmd /c ping localhost -n 5 > nul && ren " + newJar.getAbsolutePath() + " PerkByDaylight.jar");
        return true;
    }

    /**
     * Gets the URL of the update.
     *
     * @return The URL of the update.
     * @throws MalformedURLException Thrown if the URL is malformed.
     */
    @Nullable
    public URL getDownloadURL() throws MalformedURLException {
        if (object == null)
            return null;
        if (object.has("assets")) {
            JsonArray array = object.getAsJsonArray("assets");
            for (JsonElement jsonElement : array) {
                JsonObject obj = jsonElement.getAsJsonObject();
                if (obj.has("name")) {
                    String name = obj.get("name").getAsString();
                    if (name.contains(".jar")) {
                        String url = obj.get("browser_download_url").getAsString();
                        return new URL(url);
                    }
                }
            }
        } else
            logger.warn("JSON object did not contain an 'assets' array object.");
        return null;
    }

    /**
     * Gets the size of the download in bytes from the {@link JsonObject}.
     *
     * @return The size of the download in bytes.
     */
    private long getSize() {
        if (object == null)
            return -1L;
        if (object.has("assets")) {
            JsonArray array = object.getAsJsonArray("assets");
            for (JsonElement jsonElement : array) {
                JsonObject obj = jsonElement.getAsJsonObject();
                if (obj.has("name")) {
                    String name = obj.get("name").getAsString();
                    if (name.contains(".jar"))
                        return obj.get("size").getAsLong();
                }
            }
        } else
            logger.warn("JSON object did not contain an 'assets' array object.");
        return -1L;
    }
}