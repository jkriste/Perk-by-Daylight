package dev.glitchedcode.pbd.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.logger.Logger;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public final class LatestRelease {

    private static transient JsonObject object;
    private static final transient Logger logger = PBD.getLogger();
    private static final String LINK = "https://api.github.com/repos/glitchedcoder/Perk-by-Daylight/releases/latest";

    private LatestRelease() {
    }

    /**
     * Translates the JSON information from the link into a readable {@link JsonObject}.
     *
     * @return The information as a readable {@link JsonObject}.
     */
    @Nullable
    public static JsonObject getObject() {
        return object;
    }

    /**
     * Refreshes the {@link JsonObject}.
     * <br />
     * Calls to GitHub to receive the latest release in JSON form.
     */
    public static void refresh() {
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
            if (response.code() == 404) {
                logger.warn("The request returned an error 404 code. Not connected to the internet?");
                return;
            }
            logger.debug("Response to call for link '{}' returned code {}", LINK, response.code());
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body == null) {
                    logger.warn("Response#body() returned null with response code {}", response.code());
                    response.close();
                    return;
                }
                JsonElement element = JsonParser.parseString(body.string());
                response.close();
                if (element != null && element.isJsonObject())
                    object = element.getAsJsonObject();
            } else
                logger.warn("Http Request unsuccessful: code {}", response.code());
        } catch (IOException e) {
            logger.warn("Failed to get JSON from link '{}': {}", LINK, e.getMessage());
            logger.handleError(Thread.currentThread(), e);
        }
    }
}