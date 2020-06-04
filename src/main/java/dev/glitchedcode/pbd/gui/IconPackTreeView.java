package dev.glitchedcode.pbd.gui;

import dev.glitchedcode.pbd.PBD;
import dev.glitchedcode.pbd.dbd.Action;
import dev.glitchedcode.pbd.dbd.Addon;
import dev.glitchedcode.pbd.dbd.Item;
import dev.glitchedcode.pbd.dbd.Offering;
import dev.glitchedcode.pbd.dbd.Perk;
import dev.glitchedcode.pbd.dbd.Portrait;
import dev.glitchedcode.pbd.logger.Logger;
import dev.glitchedcode.pbd.pack.IconPack;
import dev.glitchedcode.pbd.pack.PackMeta;
import javafx.scene.control.TreeItem;

import javax.annotation.Nonnull;

public class IconPackTreeView extends TreeItem<String> {

    private transient IconPack iconPack;
    private static final transient Logger logger = PBD.getLogger();

    @SuppressWarnings("unchecked")
    public IconPackTreeView(@Nonnull IconPack iconPack) {
        super(iconPack.getMeta().getName());
        this.iconPack = iconPack;
        PackMeta meta = iconPack.getMeta();
        TreeItem<String> perks = new TreeItem<>("Perks");
        for (Perk perk : Perk.VALUES) {
            if (meta.isMissingIcon(perk))
                continue;
            perks.getChildren().add(new TreeItem<>(perk.getProperName()));
        }
        TreeItem<String> addons = new TreeItem<>("Addons");
        for (Addon addon : Addon.VALUES) {
            if (meta.isMissingIcon(addon))
                continue;
            addons.getChildren().add(new TreeItem<>(addon.getProperName()));
        }
        TreeItem<String> portraits = new TreeItem<>("Portraits");
        for (Portrait portrait : Portrait.VALUES) {
            if (meta.isMissingIcon(portrait))
                continue;
            portraits.getChildren().add(new TreeItem<>(portrait.getProperName()));
        }
        TreeItem<String> offerings = new TreeItem<>("Offerings");
        for (Offering offering : Offering.VALUES) {
            if (meta.isMissingIcon(offering))
                continue;
            offerings.getChildren().add(new TreeItem<>(offering.getProperName()));
        }
        TreeItem<String> actions = new TreeItem<>("Actions");
        for (Action action : Action.VALUES) {
            if (meta.isMissingIcon(action))
                continue;
            actions.getChildren().add(new TreeItem<>(action.getProperName()));
        }
        TreeItem<String> items = new TreeItem<>("Items");
        for (Item item : Item.VALUES) {
            if (meta.isMissingIcon(item))
                continue;
            items.getChildren().add(new TreeItem<>(item.getProperName()));
        }
        getChildren().addAll(perks, addons, portraits, offerings, actions, items);
    }

    @Nonnull
    public IconPack getIconPack() {
        return iconPack;
    }
}