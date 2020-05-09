package dev.glitchedcode.pbd.json;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.logger.Logger;

import java.net.URI;
import java.net.URISyntaxException;

public class LatestRelease {

    private String version;
    private String download;
    private String[] patchNotes;
    private static transient Logger logger = PBD.getLogger();

    public URI asURI() {
        try {
            return new URI(getUrl());
        } catch (URISyntaxException e) {
            logger.warn("Failed to translate url ({}) to URI.", download);
            logger.handleError(Thread.currentThread(), e);
        }
        return null;
    }

    public String getUrl() {
        return download;
    }

    public String getVersion() {
        return version;
    }

    public String[] getUpdates() {
        return patchNotes;
    }

    public boolean isUpdate() {
        return !getVersion().equalsIgnoreCase(PBD.VERSION);
    }
}