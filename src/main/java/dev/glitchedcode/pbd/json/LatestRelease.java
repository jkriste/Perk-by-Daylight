package dev.glitchedcode.pbd.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.logger.Logger;
import javafx.scene.control.ProgressBar;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
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
        if (object.has("message")) {
            String message = object.get("message").getAsString();
            if (!message.equalsIgnoreCase("Not Found"))
                logger.warn("Could not get latest release: {}", message);
            return false;
        }
        if (object.has("tag_name")) {
            String tag = object.get("tag_name").getAsString();
            logger.debug("tag_name JSON returned {}", tag);
            return !tag.equalsIgnoreCase(PBD.VERSION);
        }
        return false;
    }

    public static void downloadUpdate(@Nonnull ProgressBar bar, Runnable after) {

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
                    return null;
                }
                JsonElement element = JsonParser.parseString(body.string());
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