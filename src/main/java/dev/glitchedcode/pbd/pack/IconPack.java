package dev.glitchedcode.pbd.pack;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.dbd.Icon;
import dev.glitchedcode.pbd.gui.IconPackTreeView;
import dev.glitchedcode.pbd.logger.Logger;
import javafx.scene.control.ProgressBar;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class IconPack {

    private final int id;
    private final File folder;
    private final PackMeta meta;
    private final IconPackTreeView tree;
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

    public void dispose() {
        logger.debug("Disposing of icon pack '{}'", meta.getName());
        packs.remove(this);
    }

    public void install(@Nonnull ProgressBar progressBar) throws IOException {
        int current = 0;
        int max = PBD.getIcons().size() - meta.getMissingIcons().size();
        progressBar.setVisible(true);
        for (Icon icon : PBD.getIcons()) {
            if (meta.isMissingIcon(icon))
                continue;
            copy(icon.asFile(folder), icon.asFile(PBD.getIconsDir()));
            current++;
            progressBar.setProgress((double) current / (double) max);
        }
        progressBar.setVisible(false);
    }

    public void install(@Nonnull Icon icon) throws IOException {
        if (meta.isMissingIcon(icon))
            throw new IconPackException("Given icon '" + icon.getName()+ "' is missing from this pack.");
        copy(icon.asFile(folder), icon.asFile(PBD.getIconsDir()));
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

    @Nullable
    public static IconPack of(@Nonnull File directory) {
        if (!directory.isDirectory()) {
            logger.warn("Given file '{}' is not a directory, will be ignored.", directory.getName());
            return null;
        }
        PackMeta meta = fromDir(directory);
        if (meta != null)
            return new IconPack(directory, meta);
        return null;
    }

    private void copy(@Nonnull File from, @Nonnull File to) throws IOException {
        try (InputStream in = new FileInputStream(from);
             OutputStream out = new FileOutputStream(to)) {
            int read;
            byte[] buffer = new byte[1024];
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
        }
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
            meta = PackMeta.eval(directory);
        }
        return meta;
    }
}