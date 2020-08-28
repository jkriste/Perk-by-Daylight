package dev.glitchedcode.pbd.gui.task;

import com.google.gson.JsonObject;
import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.json.LatestRelease;
import dev.glitchedcode.pbd.logger.Logger;
import javafx.concurrent.Task;

/**
 * Used to check for an available update.
 */
public class UpdateTask extends Task<Boolean> {

    private final JsonObject object;
    private static final Logger logger = PBD.getLogger();

    public UpdateTask() {
        LatestRelease.refresh();
        this.object = LatestRelease.getObject();
    }

    @Override
    protected Boolean call() {
        if (object == null) {
            logger.warn("JsonObject is null.");
            return false;
        }
        if (object.has("tag_name")) {
            String tag = object.get("tag_name").getAsString();
            logger.debug("tag_name JSON returned {}", tag);
            return !tag.equalsIgnoreCase(PBD.VERSION.toString());
        }
        return false;
    }
}