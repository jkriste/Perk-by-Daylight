package dev.glitchedcode.pbd.pack;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.gui.IconPackTreeView;
import dev.glitchedcode.pbd.logger.Logger;
import javafx.scene.control.TreeItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
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

    /**
     * Gets the id of the icon pack.
     *
     * @return The id of the icon pack.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the icon pack folder.
     *
     * @return The icon pack folder.
     */
    public File getFolder() {
        return folder;
    }

    /**
     * Gets the pack meta.
     *
     * @return The pack meta.
     */
    public PackMeta getMeta() {
        return meta;
    }

    /**
     * Gets the existing icons tree view.
     *
     * @return The existing icons tree view.
     */
    public TreeItem<String> getIcons() {
        return icons;
    }

    /**
     * Gets the missing icons tree view.
     *
     * @return The missing icons tree view.
     */
    public TreeItem<String> getMissingIcons() {
        return missingIcons;
    }

    /**
     * Saves the icon pack.
     *
     * @throws IOException Thrown if the icon pack could not be saved.
     */
    public void save() throws IOException {
        Gson gson = PBD.getGson();
        File meta = new File(folder, "packmeta.json");
        logger.debug("packmeta.json for icon pack '{}' existed on save: {}", this.meta.getName(), meta.exists());
        if (!meta.exists()) {
            if (meta.createNewFile())
                logger.info("Recreated packmeta.json for icon pack {}.", this.meta.getName());
        }
        JsonWriter writer = new JsonWriter(new FileWriter(meta));
        writer.setLenient(true);
        gson.toJson(this.meta, PackMeta.class, writer);
        writer.flush();
        writer.close();
    }

    /**
     * Re-evaluates the icon pack.
     * <br />
     * Does the following:
     * <ul>
     *     <li>Checks if the icon pack folder exists.</li>
     *     <li>Re-evaluates the icon pack for missing items.</li>
     *     <li>Constructs an {@link IconPackTreeView} of existing icons.</li>
     *     <li>Constructs an {@link IconPackTreeView} of missing icons.</li>
     * </ul>
     *
     * @return True if the icon pack was re-evaluated successfully.
     */
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

    /**
     * Disposes of the icon pack.
     * <br />
     * This does not delete the icon pack, only removes it from the registered list.
     *
     * @see #getPacks()
     */
    public void dispose() {
        logger.debug("Disposing of icon pack '{}'", meta.getName());
        packs.remove(this);
    }

    /**
     * Sets the name of the icon pack as well as updating the tree item.
     *
     * @param name The new name of the icon pack.
     */
    public void setName(@Nonnull String name) {
        this.meta.setName(name);
        this.icons.setValue(name);
    }

    /**
     * Saves all icon packs.
     */
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

    /**
     * Gets a {@link List} of registered icon packs.
     *
     * @return A {@link List} of registered icon packs.
     */
    public static List<IconPack> getPacks() {
        return Collections.unmodifiableList(packs);
    }

    /**
     * Gets an icon pack from the given id.
     *
     * @param id The id of the icon pack.
     * @return An icon pack with the given id or null if non-existent.
     */
    @Nullable
    public static IconPack fromId(int id) {
        for (IconPack pack : packs) {
            if (pack.id == id)
                return pack;
        }
        logger.debug("Failed to retrieve icon pack with given id {}", id);
        return null;
    }

    /**
     * Gets an icon pack from the given name.
     *
     * @param name The name of an icon pack.
     * @return An icon pack with the given name or null if non-existent.
     */
    @Nullable
    public static IconPack fromName(@Nonnull String name) {
        for (IconPack pack : packs) {
            if (pack.getMeta().getName().equalsIgnoreCase(name))
                return pack;
        }
        logger.debug("Failed to retrieve icon pack with the given name '{}'", name);
        return null;
    }

    /**
     * Checks if an icon pack with the given name exists.
     *
     * @param name The name to check.
     * @return True if there is an existing icon pack with the given name.
     */
    public static boolean hasName(@Nonnull String name) {
        for (IconPack pack : packs) {
            if (pack.getMeta().getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    /**
     * Checks if the given folder is registered as an icon pack.
     *
     * @param folder The file to check.
     * @return True if the given folder is registered as an icon pack.
     * @throws IllegalArgumentException Thrown if the given file is not a directory.
     * @throws IllegalArgumentException Thrown if the given file is not located in the 'packs' folder.
     */
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

    /**
     * Creates or loads an icon pack instance from the given file.
     * <br />
     * Will return null/throw error if:
     * <ul>
     *     <li>The given file is not a directory.</li>
     *     <li>{@link PackMeta#of(File)} returns null.</li>
     *     <li>{@link Gson#fromJson(Reader, Class)} throws an IOException.</li>
     *     <li>Saving the packmeta.json file throws an IOException.</li>
     * </ul>
     *
     * @param file The folder of an icon pack.
     * @return An instance of {@link IconPack} or null.
     */
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
}