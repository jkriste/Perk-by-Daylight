package dev.glitchedcode.pbd.pack;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.dbd.Icon;
import dev.glitchedcode.pbd.file.Files;
import dev.glitchedcode.pbd.file.ZipEvaluator;
import dev.glitchedcode.pbd.gui.IconPackTreeView;
import dev.glitchedcode.pbd.logger.Logger;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class IconPack {

    private final int id;
    private final File folder;
    private final PackMeta meta;
    private IconPackTreeView tree;
    private static final AtomicInteger ID_GEN;
    private static final List<IconPack> packs;
    private static final Logger logger = PBD.getLogger();
    private static final ScheduledExecutorService SERVICE = PBD.getService();

    static {
        ID_GEN = new AtomicInteger(0);
        packs = new CopyOnWriteArrayList<>();
    }

    public IconPack(@Nonnull File folder, @Nonnull PackMeta meta) {
        this.id = ID_GEN.getAndIncrement();
        this.folder = folder;
        this.meta = meta;
        this.tree = new IconPackTreeView(this);
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

    public IconPackTreeView getTree() {
        return tree;
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
        if (!folder.exists()) {
            dispose();
            return false;
        }
        this.meta.reeval(folder);
        this.tree = new IconPackTreeView(this);
        return true;
    }

    public void dispose() {
        logger.debug("Disposing of icon pack '{}'", meta.getName());
        packs.remove(this);
    }

    public void install(@Nonnull ProgressBar progressBar, Runnable after) {
        int max = PBD.getIcons().size() - meta.getMissingIcons().size();
        progressBar.setVisible(true);
        SERVICE.execute(() -> {
            int current = 0;
            for (Icon icon : PBD.getIcons()) {
                if (meta.isMissingIcon(icon))
                    continue;
                try {
                    Files.copy(icon.asFile(folder), icon.asFile(PBD.getIconsDir()));
                } catch (IOException e) {
                    logger.warn("Could not copy file '{}' to icons directory.", icon.asFileName());
                }
                current++;
                progressBar.setProgress((double) current / (double) max);
            }
            progressBar.setVisible(false);
            Platform.runLater(after);
        });
    }

    public void install(@Nonnull Icon icon) throws IOException {
        if (meta.isMissingIcon(icon))
            throw new IconPackException("Given icon '" + icon.getName()+ "' is missing from this pack.");
        Files.copy(icon.asFile(folder), icon.asFile(PBD.getIconsDir()));
    }

    public void setName(@Nonnull String name) {
        this.meta.setName(name);
        this.tree.setValue(name);
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
                logger.warn("Failed to save 'packmeta.json': {}", e.getMessage());
                logger.handleError(Thread.currentThread(), e);
            }
            return pack;
        } else
            logger.warn("Tried to eval existing pack on a file that isn't in the 'packs' dir: {}", file.getAbsolutePath());
        return null;
    }

    @Nullable
    @ParametersAreNonnullByDefault
    public static IconPack newPack(File file, ProgressBar bar) {
        // check if file is dir/zip, as those are the only supported types.
        // TODO: Clean up - duplicate lines.
        if (file.isDirectory()) {
            try {
                File tempDir = Files.copyDirectory(file, new File(PBD.getTempDir(), file.getName()));
                if (!tempDir.exists())
                    throw new IOException("New dir in 'temp' file does not exist: " + tempDir.getAbsolutePath());
                if (PackMeta.eval(tempDir)) {
                    File newDir = Files.copyDirectory(tempDir, new File(PBD.getPacksDir(), tempDir.getName()));
                    if (!newDir.exists())
                        throw new IOException("New dir in 'packs' file does not exist: " + newDir.getAbsolutePath());
                    PackMeta meta = PackMeta.of(newDir);
                    if (meta == null)
                        throw new IconPackException("Icon pack '{}' was eval'd but #of returned null.", file.getName());
                    return new IconPack(newDir, meta);
                } else
                    logger.warn("The file/directory '{}' could not be evaluated as a proper icon pack.", file.getName());
            } catch (IOException e) {
                logger.warn("Failed to create a new icon pack '{}': {}", file.getName(), e.getMessage());
                logger.handleError(Thread.currentThread(), e);
            }
        } else if (file.getName().endsWith(".zip")) {
            ZipEvaluator evaluator = new ZipEvaluator(file, PBD.getTempDir(), bar);
            try {
                File tempDir = SERVICE.schedule(evaluator, 0, TimeUnit.MILLISECONDS).get();
                if (PackMeta.eval(tempDir)) {
                    File newDir = Files.copyDirectory(tempDir, new File(PBD.getPacksDir(), tempDir.getName()));
                    if (!newDir.exists())
                        throw new IOException("New dir in 'packs' file does not exist: " + newDir.getAbsolutePath());
                    PackMeta meta = PackMeta.of(newDir);
                    if (meta == null)
                        throw new IconPackException("Icon pack '{}' was eval'd but #of returned null.", file.getName());
                    return new IconPack(newDir, meta);
                } else
                    return null;
            } catch (Exception e) {
                logger.warn("Failed to unzip file '{}': {}", file.getName(), e.getMessage());
                logger.handleError(Thread.currentThread(), e);
            }
        } else
            logger.warn("Unsupported file '{}', not directory or .zip.", file.getName());
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