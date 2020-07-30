package dev.glitchedcode.pbd.pack;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.gui.IconPackTreeView;
import dev.glitchedcode.pbd.logger.Logger;
import javafx.scene.control.TreeItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class IconPack {

    private final int id;
    private final File folder;
    private final PackMeta meta;
    private TreeItem<String> icons;
    private TreeItem<String> missingIcons;
    private static final AtomicInteger ID_GEN;
    private static final List<IconPack> packs;
    private static final Logger logger = PBD.getLogger();

    static {
        ID_GEN = new AtomicInteger(0);
        packs = new CopyOnWriteArrayList<>();
    }

    public IconPack(@Nonnull File folder, @Nonnull PackMeta meta) {
        this.id = ID_GEN.getAndIncrement();
        this.folder = folder;
        this.meta = meta;
        this.icons = IconPackTreeView.of(this);
        this.missingIcons = IconPackTreeView.ofMissing(this);
        packs.add(this);
        logger.debug("Icon pack '{}' assigned internal id {}", meta.getName(), id);
    }

    public int getId() {
        return id;
    }

    public File getFolder() {
        return folder;
    }

    public PackMeta getMeta() {
        return meta;
    }

    public TreeItem<String> getIcons() {
        return icons;
    }

    public TreeItem<String> getMissingIcons() {
        return missingIcons;
    }

    public void save() throws IOException {
        Gson gson = PBD.getGson();
        File meta = new File(folder, "packmeta.json");
        logger.debug("packmeta.json for icon pack '{}' existed on save: {}", this.meta.getName(), meta.exists());
        if (!meta.exists()) {
            if (meta.createNewFile())
                logger.info("Recreated packmeta.json for icon pack {}.", this.meta.getName());
        }
        JsonWriter writer = new JsonWriter(new FileWriter(meta));
        gson.toJson(this.meta, PackMeta.class, writer);
        writer.flush();
        writer.close();
    }

    public boolean refresh() {
        logger.debug("Re-evaluating icon pack '{}'", meta.getName());
        if (!folder.exists()) {
            logger.warn("Icon pack '{}' folder no longer exists and will be disposed of.", meta.getName());
            dispose();
            return false;
        }
        this.meta.reeval(folder);
        this.icons = IconPackTreeView.of(this);
        this.missingIcons = IconPackTreeView.ofMissing(this);
        return true;
    }

    public void dispose() {
        logger.debug("Disposing of icon pack '{}'", meta.getName());
        packs.remove(this);
    }

    public void setName(@Nonnull String name) {
        this.meta.setName(name);
        this.icons.setValue(name);
    }

    public static void saveAll() {
        for (IconPack pack : packs) {
            try {
                pack.save();
            } catch (IOException e) {
                logger.warn("Failed to save packmeta.json for icon pack {}.", pack.meta.getName());
                logger.handleError(Thread.currentThread(), e);
            }
        }
    }

    public static List<IconPack> getPacks() {
        return Collections.unmodifiableList(packs);
    }

    @Nullable
    public static IconPack fromId(int id) {
        for (IconPack pack : packs) {
            if (pack.id == id)
                return pack;
        }
        logger.debug("Failed to retrieve icon pack with given id {}", id);
        return null;
    }

    @Nullable
    public static IconPack fromName(@Nonnull String name) {
        for (IconPack pack : packs) {
            if (pack.getMeta().getName().equalsIgnoreCase(name))
                return pack;
        }
        logger.debug("Failed to retrieve icon pack with the given name '{}'", name);
        return null;
    }

    public static boolean hasName(@Nonnull String name) {
        for (IconPack pack : packs) {
            if (pack.getMeta().getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public static boolean isRegistered(@Nonnull File folder) {
        if (!folder.isDirectory())
            throw new IllegalArgumentException("Given folder '" + folder.getName() + "' is not a directory.");
        File parentDir = folder.getParentFile();
        if (!parentDir.equals(PBD.getPacksDir()) && !parentDir.equals(PBD.getIconsDir()))
            throw new IllegalArgumentException("Given folder '" + folder.getName() + "' is not in the 'packs'/DBD folder.");
        for (IconPack pack : packs) {
            if (pack.getFolder().equals(folder))
                return true;
        }
        logger.debug("Directory '{}' is not currently registered as an icon pack.");
        return false;
    }

    @Nullable
    public static IconPack of(@Nonnull File file) {
        if (!file.isDirectory())
            throw new IconPackException("Given file '{}' was not icon pack directory.", file.getName());
        if (file.getParentFile().equals(PBD.getPacksDir()) || file.equals(PBD.getIconsDir())) {
            File packMeta = new File(file, "packmeta.json");
            PackMeta meta;
            if (!packMeta.exists()) {
                meta = PackMeta.of(file);
                if (meta == null)
                    return null;
            } else {
                try (FileReader reader = new FileReader(packMeta)) {
                    meta = PBD.getGson().fromJson(reader, PackMeta.class);
                    meta.reeval(file);
                } catch (IOException e) {
                    logger.warn("Failed to read 'packmeta.json' from dir '{}': {}", file.getName(), e.getMessage());
                    logger.handleError(Thread.currentThread(), e);
                    return null;
                }
            }
            IconPack pack = new IconPack(file, meta);
            try {
                pack.save();
            } catch (IOException e) {
                logger.warn("Failed to save 'packmeta.json' for icon pack '{}': {}", meta.getName(), e.getMessage());
                logger.handleError(Thread.currentThread(), e);
            }
            return pack;
        } else
            logger.warn("Tried to eval existing pack on a file that isn't in the 'packs' dir: {}", file.getAbsolutePath());
        return null;
    }

    @Nullable
    private static PackMeta fromDir(@Nonnull File directory) {
        PackMeta meta = null;
        assert (directory.isDirectory());
        File packMeta = new File(directory, "packmeta.json");
        logger.debug("packmeta.json for directory '{}' exists: {}", directory.getName(), packMeta.exists());
        if (packMeta.exists()) {
            try {
                JsonReader reader = new JsonReader(new FileReader(packMeta));
                meta = PBD.getGson().fromJson(reader, PackMeta.class);
            } catch (FileNotFoundException e) {
                logger.warn("File ({}) somehow disappeared whilst creating JsonReader.", packMeta.getAbsolutePath());
                logger.handleError(Thread.currentThread(), e);
            }
        } else {
            meta = PackMeta.of(directory);
        }
        return meta;
    }
}