package dev.glitchedcode.pbd.pack;

import dev.glitchedcode.pbd.PBD;

public class IconPackException extends RuntimeException {

    public IconPackException(String msg) {
        super(msg);
    }

    public IconPackException(Throwable t) {
        super(t);
    }

    public IconPackException(String msg, Object... param) {
        super(PBD.format(msg, param));
    }

    public IconPackException(String msg, Throwable t) {
        super(msg, t);
    }

    public IconPackException(String msg, Throwable t, Object... param) {
        super(PBD.format(msg, param), t);
    }
}