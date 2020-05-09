package dev.glitchedcode.pbd.gui;

import dev.glitchedcode.pbd.dbd.Addon;
import dev.glitchedcode.pbd.dbd.Perk;
import dev.glitchedcode.pbd.dbd.Portrait;
import dev.glitchedcode.pbd.pack.IconPack;
import dev.glitchedcode.pbd.pack.PackMeta;
import javafx.scene.control.TreeItem;

public class IconPackTreeView extends TreeItem<String> {

    private transient IconPack iconPack;

    @SuppressWarnings("unchecked")
    public IconPackTreeView(IconPack iconPack) {
        super(iconPack.getMeta().getName());
        PackMeta meta = iconPack.getMeta();
        TreeItem<String> perks = new TreeItem<>("Perks");
        for (Perk perk : Perk.VALUES) {
            if (meta.isMissingPerk(perk))
                continue;
            perks.getChildren().add(new TreeItem<>(perk.asProperName()));
        }
        TreeItem<String> addons = new TreeItem<>("Addons");
        for (Addon addon : Addon.VALUES) {
            if (meta.isMissingAddon(addon))
                continue;
            addons.getChildren().add(new TreeItem<>(addon.asProperName()));
        }
        TreeItem<String> portraits = new TreeItem<>("Portraits");
        for (Portrait portrait : Portrait.VALUES) {
            if (meta.isMissingPortrait(portrait))
                continue;
            portraits.getChildren().add(new TreeItem<>(portrait.asProperName()));
        }
        getChildren().addAll(perks, addons, portraits);
    }

    public IconPack getIconPack() {
        return iconPack;
    }
}