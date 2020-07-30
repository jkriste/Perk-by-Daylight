package dev.glitchedcode.pbd.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.gui.controller.MainController;
import dev.glitchedcode.pbd.logger.Logger;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public final class LatestRelease {

    private static final Logger logger = PBD.getLogger();
    private static final String LINK = "https://api.github.com/repos/glitchedcoder/Perk-by-Daylight/releases/latest";

    private LatestRelease() {
    }

    public static boolean checkUpdate() {
        JsonObject object = toObject();
        if (object == null)
            return false;
        if (object.has("tag_name")) {
            String tag = object.get("tag_name").getAsString();
            logger.debug("tag_name JSON returned {}", tag);
            return !tag.equalsIgnoreCase(PBD.VERSION.toString());
        }
        return false;
    }

    public static void downloadUpdate(@Nonnull MainController ui, @Nullable Runnable after) {
        ui.setDisabled(true);
        URL url;
        try {
            url = getDownloadURL();
        } catch (MalformedURLException e) {
            logger.warn("Malformed download url: {}", e.getMessage());
            return;
        }
        if (url == null) {
            logger.warn("Download URL returned null when attempting to download update.");
            return;
        }
        long size = getSize();
        logger.debug("Download size: {} bytes", size);
        try {
            File jar = PBD.getJar();
            File newJar = new File(jar.getParent(), "Rename Me.jar");
            if (newJar.exists()) {
                logger.warn("\"Rename Me.jar\" currently exists, please rename this before updating.");
                ui.setDisabled(false);
                return;
            }
            if (!newJar.createNewFile()) {
                logger.warn("Could not create new JAR file '{}'", newJar.getAbsolutePath());
                ui.setDisabled(false);
                return;
            }
            try (BufferedInputStream in = new BufferedInputStream(url.openStream());
                 FileOutputStream out = new FileOutputStream(newJar)) {
                int total = 0;
                int length;
                byte[] buffer = new byte[16384];
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                    total += length;
                    logger.debug("Length read: {} / {}", total, size);
                }
                out.flush();
            }
        } catch (IOException e) {
            logger.warn("Failed to download new JAR: {}", e.getMessage());
            logger.handleError(Thread.currentThread(), e);
        } catch (URISyntaxException e) {
            logger.warn("Could not get local JAR file location: {}", e.getMessage());
            logger.handleError(Thread.currentThread(), e);
        }
        if (after != null)
            Platform.runLater(after);
        ui.setDisabled(false);
    }

    @Nullable
    public static URL getDownloadURL() throws MalformedURLException {
        JsonObject object = toObject();
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
            logger.warn("JSON from link '{}' did not contain an 'assets' array object.", LINK);
        return null;
    }

    private static long getSize() {
        JsonObject object = toObject();
        if (object == null)
            return -1L;
        if (object.has("message")) {
            String message = object.get("message").getAsString();
            if (!message.equalsIgnoreCase("Not Found"))
                logger.warn("Could not get latest release: {}", message);
            return -1L;
        }
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
            logger.warn("JSON from link '{}' did not contain an 'assets' array object.", LINK);
        return -1L;
    }

    @Nullable
    private static JsonObject toObject() {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .callTimeout(5, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .addHeader("Content-Type", "application/json")
                    .url(LINK)
                    .build();
            Call call = client.newCall(request);
            Response response = call.execute();
            logger.debug("Response to call for link '{}' returned code {}", LINK, response.code());
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body == null) {
                    logger.warn("Response#body() returned null with response code {}", response.code());
                    response.close();
                    return null;
                }
                JsonElement element = JsonParser.parseString(body.string());
                response.close();
                if (element != null && element.isJsonObject())
                    return element.getAsJsonObject();
            } else
                logger.warn("Http Request unsuccessful: code {}", response.code());
        } catch (IOException e) {
            logger.warn("Failed to get JSON from link '{}': {}", LINK, e.getMessage());
            logger.handleError(Thread.currentThread(), e);
        }
        return null;
    }
}