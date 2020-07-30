package dev.glitchedcode.pbd.gui.task;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.dbd.icon.Icon;
import dev.glitchedcode.pbd.logger.Logger;
import dev.glitchedcode.pbd.pack.IconPack;
import dev.glitchedcode.pbd.pack.PackMeta;
import dev.glitchedcode.pbd.util.Files;
import javafx.concurrent.Task;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

public class IconInstallTask extends Task<Integer> {

    private final Icon icon;
    private final IconPack pack;
    private static final Logger logger = PBD.getLogger();

    public IconInstallTask(@Nonnull IconPack pack) {
        this(pack, null);
    }

    public IconInstallTask(@Nonnull IconPack pack, @Nullable Icon icon) {
        this.pack = pack;
        this.icon = icon;
    }

    @Override
    protected Integer call() throws IOException {
        PackMeta meta = pack.getMeta();
        if (icon != null) {
            if (meta.isMissingIcon(icon)) {
                logger.warn("Tried to install missing icon '{}' from pack '{}'.", icon.getProperName(), meta.getName());
                return -1;
            }
            Files.copy(icon.asFile(pack.getFolder()), icon.asFile(PBD.getIconsDir()));
            return 1;
        }
        int current = 0;
        int max = PBD.getIcons().size() - meta.getMissingIcons().size();
        for (Icon icon : PBD.getIcons()) {
            if (meta.isMissingIcon(icon))
                continue;
            Files.copy(icon.asFile(pack.getFolder()), icon.asFile(PBD.getIconsDir()));
            current++;
            updateProgress(current, max);
        }
        return max;
    }
}