package dev.glitchedcode.pbd.gui;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.dbd.icon.Icon;
import dev.glitchedcode.pbd.dbd.icon.IconCategory;
import dev.glitchedcode.pbd.logger.Logger;
import dev.glitchedcode.pbd.pack.IconPack;
import dev.glitchedcode.pbd.pack.PackMeta;
import javafx.scene.control.TreeItem;

import javax.annotation.Nonnull;
import java.util.EnumMap;

public class IconPackTreeView {

    private static final Logger logger = PBD.getLogger();

    private IconPackTreeView() {
    }

    public static TreeItem<String> of(@Nonnull IconPack iconPack) {
        PackMeta meta = iconPack.getMeta();
        TreeItem<String> root = new TreeItem<>(meta.getName());
        EnumMap<IconCategory, TreeItem<String>> categories = new EnumMap<>(IconCategory.class);
        for (IconCategory ic : IconCategory.values()) {
            categories.put(ic, new TreeItem<>(ic.getName()));
        }
        for (Icon icon : PBD.getIcons()) {
            if (meta.isMissingIcon(icon))
                continue;
            TreeItem<String> category = categories.get(icon.getCategory());
            category.getChildren().add(new TreeItem<>(icon.getProperName()));
        }
        for (IconCategory ic : IconCategory.values()) {
            if (categories.get(ic).getChildren().isEmpty())
                categories.remove(ic);
        }
        root.getChildren().addAll(categories.values());
        return root;
    }

    public static TreeItem<String> ofMissing(@Nonnull IconPack iconPack) {
        PackMeta meta = iconPack.getMeta();
        TreeItem<String> root = new TreeItem<>("Missing");
        EnumMap<IconCategory, TreeItem<String>> categories = new EnumMap<>(IconCategory.class);
        for (IconCategory ic : IconCategory.values()) {
            categories.put(ic, new TreeItem<>(ic.getName()));
        }
        for (String name : meta.getMissingIcons()) {
            Icon icon = PBD.getIcon(name);
            if (icon == null) {
                logger.warn("Failed to get icon with name '{}' while " +
                        "getting missing tree for icon pack '{}'", name, meta.getName());
                continue;
            }
            TreeItem<String> category = categories.get(icon.getCategory());
            category.getChildren().add(new TreeItem<>(icon.getProperName()));
        }
        for (IconCategory ic : IconCategory.values()) {
            if (categories.get(ic).getChildren().isEmpty())
                categories.remove(ic);
        }
        root.getChildren().addAll(categories.values());
        return root;
    }
}